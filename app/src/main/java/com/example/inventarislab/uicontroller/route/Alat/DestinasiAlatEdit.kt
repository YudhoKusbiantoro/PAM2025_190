package com.example.inventarislab.view.route

import com.example.inventarislab.R
import com.example.inventarislab.uicontroller.route.DestinasiNavigasi

object DestinasiAlatEdit : DestinasiNavigasi {
    override val route = "edit_alat"
    override val titleRes = R.string.edit_alat
    const val alatIdArg = "idAlat"
    val routeWithArgs = "$route/{$alatIdArg}"
}