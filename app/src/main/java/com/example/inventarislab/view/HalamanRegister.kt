// view/HalamanRegister.kt
package com.example.inventarislab.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventarislab.modeldata.Lab
import com.example.inventarislab.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanRegister(navController: NavController) {
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel()

    var nama by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var institusi by remember { mutableStateOf("") }
    var namaLab by remember { mutableStateOf("") }
    var isRoleAdmin by remember { mutableStateOf(false) }

    val labs by viewModel.labs.collectAsState()
    var selectedLab by remember { mutableStateOf<Lab?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val registerResult by viewModel.registerResult.collectAsState()
    var isRegistering by remember { mutableStateOf(false) }

    // ✅ LOAD LABS SAAT HALAMAN DIBUKA (JIKA ROLE USER)
    LaunchedEffect(Unit) {
        if (!isRoleAdmin) {
            viewModel.loadLabs()
        }
    }

    // ✅ SET DEFAULT LAB SAAT DATA DIMUAT
    LaunchedEffect(labs) {
        if (!isRoleAdmin && labs.isNotEmpty()) {
            // Jangan set default lab — biarkan kosong sampai user klik
            if (selectedLab == null) {
                // ✅ Biarkan kosong — tidak set apapun
            }
        }
    }

    // ✅ RESET SELECTED LAB SAAT GANTI KE ADMIN
    LaunchedEffect(isRoleAdmin) {
        if (isRoleAdmin) {
            selectedLab = null
        }
    }

    LaunchedEffect(registerResult) {
        val result = registerResult // ✅ Simpan ke variabel lokal
        if (result != null) {
            isRegistering = false

            if (result.status == "success") { // ✅ Sekarang aman
                Toast.makeText(context, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                navController.navigate("login")
            } else {
                Toast.makeText(context, result.message ?: "Registrasi gagal", Toast.LENGTH_SHORT).show()
            }
            viewModel.resetRegisterResult()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pendaftaran Akun",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { isRoleAdmin = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRoleAdmin) Color(0xFF485C91) else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Admin", color = Color.White)
            }
            Button(
                onClick = { isRoleAdmin = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isRoleAdmin) Color(0xFF485C91) else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text("Pengguna", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        if (isRoleAdmin) {
            OutlinedTextField(
                value = institusi,
                onValueChange = { institusi = it },
                label = { Text("Nama Instansi/Sekolah") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = namaLab,
                onValueChange = { namaLab = it },
                label = { Text("Nama Laboratorium") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        } else {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedLab?.let { "${it.institusi} - ${it.nama_lab}" } ?: "Pilih Laboratorium",
                    onValueChange = {},
                    label = { Text("Laboratorium") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .padding(bottom = 8.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    labs.forEach { lab ->
                        DropdownMenuItem(
                            text = { Text("${lab.institusi} - ${lab.nama_lab}") },
                            onClick = {
                                selectedLab = lab
                                expanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                if (isRegistering) return@Button

                // ✅ VALIDASI SEDERHANA: SEMUA DATA HARUS DIISI
                val isValid = if (isRoleAdmin) {
                    nama.isNotBlank() && username.isNotBlank() && password.isNotBlank() &&
                            institusi.isNotBlank() && namaLab.isNotBlank()
                } else {
                    nama.isNotBlank() && username.isNotBlank() && password.isNotBlank() &&
                            selectedLab != null
                }

                if (!isValid) {
                    Toast.makeText(context, "Semua data harus diisi.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isRegistering = true
                if (isRoleAdmin) {
                    viewModel.register(nama, username, password, institusi, namaLab)
                } else {
                    viewModel.register(nama, username, password, labId = selectedLab!!.id)
                }
            },
            enabled = !isRegistering,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isRegistering) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text("Daftar sebagai ${if (isRoleAdmin) "ADMIN" else "USER"}")
            }
        }

        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Sudah punya akun? Login")
        }
    }
}