package com.example.inventarislab.uicontroller.route.bahan

import com.example.inventarislab.R
import com.example.inventarislab.uicontroller.route.DestinasiNavigasi

object DestinasiBahan : DestinasiNavigasi {
    override val route = "bahan_list/{labId}" // ✅ Route utama
    override val titleRes = R.string.bahan
}