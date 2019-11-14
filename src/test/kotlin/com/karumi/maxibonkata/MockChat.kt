package com.karumi.maxibonkata

class MockChat : Chat {
    var messageSent: String? = null

    override fun sendMessage(message: String) {
        messageSent = message
    }
}