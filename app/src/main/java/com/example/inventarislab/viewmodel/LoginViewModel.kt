package com.example.inventarislab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarislab.modeldata.ResponseData
import com.example.inventarislab.modeldata.User
import com.example.inventarislab.repositori.RepositoryInventaris
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: RepositoryInventaris
) : ViewModel() {

    private val _loginResult = MutableStateFlow<ResponseData<User>?>(null)
    val loginResult: StateFlow<ResponseData<User>?> = _loginResult

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val response = repository.login(username, password)
            _loginResult.value = response
            if (response.status == "success" && response.data != null) {
                _currentUser.value = response.data
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _loginResult.value = null
    }
}