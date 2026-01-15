// view/HalamanRegister.kt
package com.example.inventarislab.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.inventarislab.R
import com.example.inventarislab.modeldata.Lab
import com.example.inventarislab.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanRegister(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    val context = LocalContext.current
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

    LaunchedEffect(Unit) {
        if (!isRoleAdmin) {
            viewModel.loadLabs()
        }
    }

    LaunchedEffect(isRoleAdmin) {
        if (isRoleAdmin) {
            selectedLab = null
        }
    }

    LaunchedEffect(registerResult) {
        val result = registerResult
        if (result != null) {
            isRegistering = false
            if (result.status == "success") {
                Toast.makeText(context, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                navController.navigate("login")
            } else {
                // ✅ HANYA TAMPILKAN PESAN DARI BACKEND
                Toast.makeText(context, result.message ?: "Registrasi gagal", Toast.LENGTH_SHORT).show()
            }
            viewModel.resetRegisterResult()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.kimia1),
                contentDescription = "Logo Kimia",
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Buat Akun Baru",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF485C91),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Silakan isi data untuk mendaftar",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                RoleChip(
                    text = "Admin",
                    isSelected = isRoleAdmin,
                    onClick = { isRoleAdmin = true },
                    selectedColor = Color(0xFF485C91),
                    unselectedColor = Color(0xFFF0F4F8)
                )
                Spacer(modifier = Modifier.width(12.dp))
                RoleChip(
                    text = "Pengguna",
                    isSelected = !isRoleAdmin,
                    onClick = { isRoleAdmin = false },
                    selectedColor = Color(0xFF485C91),
                    unselectedColor = Color(0xFFF0F4F8)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Lengkap") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            if (isRoleAdmin) {
                OutlinedTextField(
                    value = institusi,
                    onValueChange = { institusi = it },
                    label = { Text("Nama Instansi/Sekolah") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = namaLab,
                    onValueChange = { namaLab = it },
                    label = { Text("Nama Laboratorium") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
            } else {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedLab?.let { "${it.institusi} - ${it.nama_lab}" } ?: "",
                        onValueChange = {},
                        label = { Text("Pilih Laboratorium") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .padding(vertical = 4.dp)
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isRegistering) return@Button

                    // ✅ HANYA CEK APAKAH SEMUA FIELD SUDAH DIISI (TIDAK VALIDASI FORMAT)
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

                    if (!isValidUsername(username)) {
                        Toast.makeText(
                            context,
                            "Username minimal 6 karakter dan hanya boleh huruf, angka, - atau _",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (!isValidPassword(password)) {
                        Toast.makeText(
                            context,
                            "Password minimal 6 karakter dan hanya boleh huruf dan angka",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }


                    isRegistering = true
                    if (isRoleAdmin) {
                        viewModel.register(nama, username, password, institusi, namaLab)
                    } else {
                        // ✅ Simpan ke variabel lokal untuk hindari smart cast error
                        val lab = selectedLab
                        viewModel.register(nama, username, password, labId = lab!!.id)
                    }
                },
                enabled = !isRegistering,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF485C91))
            ) {
                if (isRegistering) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Daftar", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Text("Sudah punya akun? ", color = Color.Gray)
                Text("Login", color = Color(0xFF485C91), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RoleChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color,
    unselectedColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) selectedColor else unselectedColor)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color(0xFF485C91),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}
fun isValidUsername(username: String): Boolean {
    val regex = Regex("^[a-zA-Z0-9_-]{6,}$")
    return regex.matches(username)
}

fun isValidPassword(password: String): Boolean {
    val regex = Regex("^[a-zA-Z0-9]{6,}$")
    return regex.matches(password)
}
