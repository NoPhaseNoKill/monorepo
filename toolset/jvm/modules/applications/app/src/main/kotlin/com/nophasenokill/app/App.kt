package com.nophasenokill.app

import com.nophasenokill.utils.StringUtils
import org.apache.commons.text.WordUtils

fun main() {
    val tokens = StringUtils.split(MessageUtils.getMessage())
    StringUtils.join(tokens)
    // println(WordUtils.capitalize(result))
}
