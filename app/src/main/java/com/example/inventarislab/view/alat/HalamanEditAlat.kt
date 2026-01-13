// view/HalamanEditAlat.kt
package com.example.inventarislab.view

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.inventarislab.viewmodel.AlatViewModel
import com.example.inventarislab.viewmodel.provider.PenyediaViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEditAlat(
    alatId: Int,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    val viewModel: AlatViewModel = viewModel(factory = PenyediaViewModel.Factory)
    val alatState by viewModel.alatDetail.collectAsState()

    var nama by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var terakhirKalibrasi by remember { mutableStateOf("") }
    var intervalKalibrasi by remember { mutableStateOf("") }
    var satuanInterval by remember { mutableStateOf("Bulan") }
    var kondisi by remember { mutableStateOf("Baik") }

    var satuanExpanded by remember { mutableStateOf(false) }
    var kondisiExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    LaunchedEffect(alatId) {
        viewModel.loadAlatById(alatId)
    }

    LaunchedEffect(alatState) {
        alatState?.let { alat ->
            nama = alat.nama
            jumlah = alat.jumlah.toString()
            terakhirKalibrasi = alat.terakhir_kalibrasi ?: ""
            intervalKalibrasi = alat.interval_kalibrasi.toString()
            satuanInterval = alat.satuan_interval
            kondisi = alat.kondisi
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Alat") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Alat") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = jumlah,
                onValueChange = { jumlah = it },
                label = { Text("Jumlah") },
                modifier = Modifier.fillMaxWidth()
            )

            // ✅ TERAKHIR KALIBRASI → DATEPICKER
            OutlinedTextField(
                value = terakhirKalibrasi,
                onValueChange = { /* readOnly */ },
                label = { Text("Terakhir Kalibrasi (YYYY-MM-DD)") },
                trailingIcon = {
                    IconButton(onClick = {
                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                terakhirKalibrasi = "%04d-%02d-%02d".format(y, m + 1, d)
                            },
                            year, month, day
                        ).show()
                    }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Pilih Tanggal")
                    }
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = intervalKalibrasi,
                onValueChange = { intervalKalibrasi = it },
                label = { Text("Interval Kalibrasi") },
                modifier = Modifier.fillMaxWidth()
            )

            // ✅ SATUAN INTERVAL → DROPDOWN
            ExposedDropdownMenuBox(
                expanded = satuanExpanded,
                onExpandedChange = { satuanExpanded = it }
            ) {
                OutlinedTextField(
                    value = satuanInterval,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Satuan Interval") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = satuanExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = satuanExpanded,
                    onDismissRequest = { satuanExpanded = false }
                ) {
                    listOf("Hari", "Minggu", "Bulan", "Tahun").forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                satuanInterval = item
                                satuanExpanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // ✅ KONDISI → DROPDOWN
            ExposedDropdownMenuBox(
                expanded = kondisiExpanded,
                onExpandedChange = { kondisiExpanded = it }
            ) {
                OutlinedTextField(
                    value = kondisi,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kondisi") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = kondisiExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = kondisiExpanded,
                    onDismissRequest = { kondisiExpanded = false }
                ) {
                    listOf("Baik", "Rusak").forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                kondisi = item
                                kondisiExpanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (nama.isNotEmpty() && jumlah.isNotEmpty() && terakhirKalibrasi.isNotEmpty() && intervalKalibrasi.isNotEmpty()) {
                        alatState?.let { alat ->
                            viewModel.updateAlat(
                                id = alatId,
                                nama = nama,
                                jumlah = jumlah,
                                terakhirKalibrasi = terakhirKalibrasi,
                                intervalKalibrasi = intervalKalibrasi,
                                satuanInterval = satuanInterval,
                                kondisi = kondisi,
                                labId = alat.lab_id
                            )
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nama.isNotEmpty() && jumlah.isNotEmpty() && terakhirKalibrasi.isNotEmpty() && intervalKalibrasi.isNotEmpty()
            ) {
                Text("Simpan Perubahan")
            }
        }
    }
}