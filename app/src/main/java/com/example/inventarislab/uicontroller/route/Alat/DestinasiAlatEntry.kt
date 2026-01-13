// view/route/DestinasiAlatEntry.kt
package com.example.inventarislab.view.route

import com.example.inventarislab.R
import com.example.inventarislab.uicontroller.route.DestinasiNavigasi

object DestinasiAlatEntry : DestinasiNavigasi {
    override val route = "alat_list"
    override val titleRes = R.string.alat_list
    const val labIdArg = "labId"
    val routeWithArgs = "$route/{$labIdArg}"
}