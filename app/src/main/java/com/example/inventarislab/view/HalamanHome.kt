package com.example.inventarislab.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventarislab.R
import com.example.inventarislab.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    viewModel: LoginViewModel,
    onBahanClick: (Int) -> Unit,
    onPeralatanClick: (Int) -> Unit,
    onLogoutClick: () -> Unit,
    onKelolaPenggunaClick: (Int) -> Unit
) {
    val currentUser = viewModel.currentUser.collectAsState()
    val userName = currentUser.value?.nama ?: "User"
    val userRole = currentUser.value?.role ?: "user"
    val institusi = currentUser.value?.institusi ?: "Instansi"
    val namaLab = currentUser.value?.nama_lab ?: "Laboratorium"
    val labId = currentUser.value?.lab_id?.takeIf { it > 0 } ?: 1

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                ),
                title = {
                    Column {
                        Text(
                            text = "Dashboard",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$userName",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Anda " +
                                    (if (userRole == "admin") "Pengelola" else "Pengguna") +
                                    " $namaLab",
                            fontSize = 16.sp,
                            color = Color(0xFFBDBDBD)
                        )
                        Text(
                            text = institusi,
                            fontSize = 14.sp,
                            color = Color(0xFFBDBDBD)
                        )
                    }
                },
                actions = {}
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // CARD BAHAN
            Card(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3)),
                onClick = { onBahanClick(labId) } // ✅ Kirim labId
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bahan",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Image(
                        painter = painterResource(id = R.drawable.bahan),
                        contentDescription = "Bahan",
                        modifier = Modifier.size(70.dp)
                    )
                }
            }

            // CARD PERALATAN
            Card(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3)),
                onClick = { onPeralatanClick(labId) } // ✅ Kirim labId
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Peralatan",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Image(
                        painter = painterResource(id = R.drawable.alat),
                        contentDescription = "Peralatan",
                        modifier = Modifier.size(70.dp)
                    )
                }
            }

            // CARD KELOLA PENGGUNA (HANYA UNTUK ADMIN)
            if (userRole == "admin") {
                Card(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                    onClick = { onKelolaPenggunaClick(labId) } // ✅ Kirim labId
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kelola Pengguna",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Kelola Pengguna",
                            modifier = Modifier.size(70.dp)
                        )
                    }
                }
            }

            // CARD LOGOUT
            Card(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE57373)),
                onClick = onLogoutClick
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Logout",
                        color = Color.Black,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Logout",
                        modifier = Modifier.size(70.dp)
                    )
                }
            }
        }
    }
}