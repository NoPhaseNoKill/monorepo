package com.nophasenokill;

import org.slf4j.LoggerFactory;

object Service {
    fun printMessage(messageModel: MessageModel): String {
        val messageToPrint: String = messageModel.message
        println(messageToPrint)
        LoggerFactory.getLogger(Service::class.java).info("Message printed")
        return messageToPrint
    }
}
