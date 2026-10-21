package com.example.motormania.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.motormania.model.QrResult
import com.example.motormania.repository.QrRepository

class QrViewModel : ViewModel() {

    private val _qrResult = MutableLiveData<String?>()
    val qrResult: LiveData<String?> = _qrResult

    fun onQrDetected(content: String) {
        _qrResult.value = content
    }

    fun clearResult() {
        _qrResult.value = null
    }
}