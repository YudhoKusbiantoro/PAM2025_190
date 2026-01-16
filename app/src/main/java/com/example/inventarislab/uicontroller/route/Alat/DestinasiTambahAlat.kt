package com.example.inventarislab.view.route

import com.example.inventarislab.R
import com.example.inventarislab.uicontroller.route.DestinasiNavigasi

object DestinasiTambahAlat : DestinasiNavigasi {
    override val route = "tambah_alat"
    override val titleRes = R.string.tambah_alat
    const val labIdArg = "labId"
    val routeWithArgs = "$route/{$labIdArg}"
}