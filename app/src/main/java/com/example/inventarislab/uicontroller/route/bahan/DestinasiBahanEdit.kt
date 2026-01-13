package com.example.inventarislab.view.route

import com.example.inventarislab.R
import com.example.inventarislab.uicontroller.route.DestinasiNavigasi

object DestinasiBahanEdit : DestinasiNavigasi {
    override val route = "edit_bahan"
    override val titleRes = R.string.edit_bahan
    const val bahanIdArg = "idBahan"
    val routeWithArgs = "$route/{$bahanIdArg}"
}