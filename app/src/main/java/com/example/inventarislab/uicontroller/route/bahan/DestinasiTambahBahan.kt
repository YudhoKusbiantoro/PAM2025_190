package com.example.inventarislab.view.route

import com.example.inventarislab.R
import com.example.inventarislab.uicontroller.route.DestinasiNavigasi

object DestinasiTambahBahan : DestinasiNavigasi {
    override val route = "tambah_bahan"
    override val titleRes = R.string.tambah_bahan
    const val labIdArg = "labId"
    val routeWithArgs = "$route/{$labIdArg}"
}