package com.example.admin

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminAuthManager private constructor() {

    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _adminName = MutableStateFlow("")
    val adminName: StateFlow<String> = _adminName.asStateFlow()

    // Configurable authorized passcodes - In production, this can connect to Firebase Auth / Custom Claims
    private val authorizedPins = setOf("7777", "1234", "Ghirni@2026", "Gajanan@77")

    fun authenticate(inputPin: String): Boolean {
        return if (authorizedPins.contains(inputPin.trim())) {
            _isAdminLoggedIn.value = true
            _adminName.value = "मंदिर विश्वस्त / व्यवस्थापक"
            true
        } else {
            false
        }
    }

    fun logout() {
        _isAdminLoggedIn.value = false
        _adminName.value = ""
    }

    companion object {
        @Volatile
        private var instance: AdminAuthManager? = null

        fun getInstance(): AdminAuthManager {
            return instance ?: synchronized(this) {
                instance ?: AdminAuthManager().also { instance = it }
            }
        }
    }
}
