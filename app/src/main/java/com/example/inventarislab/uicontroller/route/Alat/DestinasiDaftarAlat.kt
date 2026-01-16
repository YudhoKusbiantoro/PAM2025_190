package com.example.inventarislab.view.route

import com.example.inventarislab.R
import com.example.inventarislab.uicontroller.route.DestinasiNavigasi

object DestinasiDaftarAlat : DestinasiNavigasi {
    override val route = "alat_list"
    override val titleRes = R.string.peralatan
    const val labIdArg = "labId"
    val routeWithArgs = "$route/{$labIdArg}"
}