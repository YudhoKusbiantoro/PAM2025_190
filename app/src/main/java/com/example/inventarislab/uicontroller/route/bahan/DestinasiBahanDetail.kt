package com.example.inventarislab.view.route

import com.example.inventarislab.R
import com.example.inventarislab.uicontroller.route.DestinasiNavigasi

object DestinasiBahanDetail : DestinasiNavigasi {
    override val route = "detail_bahan"
    override val titleRes = R.string.detail_bahan
    const val bahanIdArg = "idBahan"
    val routeWithArgs = "$route/{$bahanIdArg}"
}