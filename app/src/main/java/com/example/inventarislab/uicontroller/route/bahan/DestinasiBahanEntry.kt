package com.example.inventarislab.view.route

import com.example.inventarislab.R
import com.example.inventarislab.uicontroller.route.DestinasiNavigasi

object DestinasiBahanEntry : DestinasiNavigasi {
    override val route = "bahan_list"
    override val titleRes = R.string.bahan_list
    const val labIdArg = "labId"
    val routeWithArgs = "$route/{$labIdArg}"
}