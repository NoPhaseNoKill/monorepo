package com.nophasenokill;

import org.slf4j.LoggerFactory

object Service {
    fun printMessage(messageModel: MessageModel): String {
        val messageToPrint: String = messageModel.message
        LoggerFactory.getLogger(Service::class.java).info("Message printed")
        return messageToPrint
    }
}
