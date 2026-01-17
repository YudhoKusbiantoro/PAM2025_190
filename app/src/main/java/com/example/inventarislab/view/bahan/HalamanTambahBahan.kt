package com.example.inventarislab.view.bahan

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.inventarislab.R
import com.example.inventarislab.viewmodel.bahan.BahanCreateViewModel
import com.example.inventarislab.viewmodel.provider.PenyediaViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanTambahBahan(
    labId: Int,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    val viewModel: BahanCreateViewModel =
        viewModel(factory = PenyediaViewModel.Factory)

    var nama by remember { mutableStateOf("") }
    var volume by remember { mutableStateOf("") }
    var expired by remember { mutableStateOf("") }
    var kondisi by remember { mutableStateOf("") }
    var kondisiExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val createResult by viewModel.createResult.collectAsState()

    LaunchedEffect(createResult) {
        val result = createResult
        if (result != null) {
            if (result.status == "success") {
                onBackClick()
            } else {
                Toast.makeText(
                    context,
                    result.message ?: "Bahan sudah ada di laboratorium ini",
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.resetCreateResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.tambah_bahan))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.kembali)
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text(stringResource(R.string.nama_bahan)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = volume,
                onValueChange = { volume = it },
                label = { Text(stringResource(R.string.volume)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = expired,
                onValueChange = {},
                label = { Text(stringResource(R.string.expired)) },
                trailingIcon = {
                    IconButton(onClick = {
                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                expired = "%04d-%02d-%02d".format(y, m + 1, d)
                            },
                            year, month, day
                        ).show()
                    }) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = stringResource(R.string.pilih_tanggal)
                        )
                    }
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = kondisiExpanded,
                onExpandedChange = { kondisiExpanded = it }
            ) {
                OutlinedTextField(
                    value = if (kondisi.isEmpty())
                        stringResource(R.string.pilih_kondisi)
                    else kondisi,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.kondisi)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = kondisiExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = kondisiExpanded,
                    onDismissRequest = { kondisiExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Baik") },
                        onClick = {
                            kondisi = "Baik"
                            kondisiExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Rusak") },
                        onClick = {
                            kondisi = "Rusak"
                            kondisiExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Expired") },
                        onClick = {
                            kondisi = "Expired"
                            kondisiExpanded = false
                        }
                    )
                }

            }

            Button(
                onClick = {
                    viewModel.createBahan(
                        nama,
                        volume,
                        expired,
                        kondisi,
                        labId
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nama.isNotBlank()
                        && volume.isNotBlank()
                        && expired.isNotBlank()
                        && kondisi.isNotBlank()
            ) {
                Text(stringResource(R.string.simpan))
            }
        }
    }
}
