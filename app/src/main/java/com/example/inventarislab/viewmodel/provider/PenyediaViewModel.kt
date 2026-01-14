package com.example.inventarislab.viewmodel.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventarislab.repositori.AplikasiInventarisLab
import com.example.inventarislab.viewmodel.AlatViewModel
import com.example.inventarislab.viewmodel.BahanViewModel
import com.example.inventarislab.viewmodel.LoginViewModel
import com.example.inventarislab.viewmodel.RegisterViewModel

fun CreationExtras.aplikasiInventarisLab(): AplikasiInventarisLab =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.example.inventarislab.repositori.AplikasiInventarisLab

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            BahanViewModel(
                aplikasiInventarisLab().container.repositoryInventaris
            )
        }
        initializer {
            AlatViewModel(
                aplikasiInventarisLab().container.repositoryInventaris
            )
        }
        initializer {
            RegisterViewModel(aplikasiInventarisLab().container.repositoryInventaris)
        }
        initializer {
            LoginViewModel(aplikasiInventarisLab().container.repositoryInventaris)
        }
    }
}