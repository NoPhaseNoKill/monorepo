package com.nophasenokill.app

import com.nophasenokill.utils.StringUtils
import org.apache.commons.text.WordUtils

object App {
    fun main(): String {
        val tokens = StringUtils.split(MessageUtils.getMessage())
        val result = StringUtils.join(tokens)
        return WordUtils.capitalize(result)
    }
}
