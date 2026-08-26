package com.example.obd2diagnostic.data.obd

class EchoOffCommand : ObdCommand("ATE0") {
    override fun getFormattedResult(): String = formattedResponse
}

class LineFeedOffCommand : ObdCommand("ATL0") {
    override fun getFormattedResult(): String = formattedResponse
}

class ResetCommand : ObdCommand("ATZ") {
    override fun getFormattedResult(): String = formattedResponse
}

class SelectProtocolAutoCommand : ObdCommand("ATSP0") {
    override fun getFormattedResult(): String = formattedResponse
}
