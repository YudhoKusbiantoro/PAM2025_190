// view/route/DestinasiAlatDetail.kt
package com.example.inventarislab.view.route

import com.example.inventarislab.R
import com.example.inventarislab.uicontroller.route.DestinasiNavigasi

object DestinasiAlatDetail : DestinasiNavigasi {
    override val route = "detail_alat"
    override val titleRes = R.string.detail_alat
    const val alatIdArg = "idAlat"
    val routeWithArgs = "$route/{$alatIdArg}"
}