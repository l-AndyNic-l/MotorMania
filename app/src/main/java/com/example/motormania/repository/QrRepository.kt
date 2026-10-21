package com.example.motormania.repository

import com.example.motormania.model.QrResult

class QrRepository {
    fun processQrContent(content: String): QrResult {
        return QrResult(content)
    }
}