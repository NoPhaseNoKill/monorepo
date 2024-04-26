package com.nophasenokill;

import org.slf4j.LoggerFactory

object Service {

    val LOGGER = LoggerFactory.getLogger(Service::class.java)

    fun printMessage(messageModel: MessageModel): String {
        val messageToPrint: String = messageModel.message
        LOGGER.info("Message printed")
        return messageToPrint
    }
}
