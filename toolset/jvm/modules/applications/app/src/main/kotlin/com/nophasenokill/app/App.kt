package com.nophasenokill.app

import com.nophasenokill.utils.StringUtils


fun main() {
    val tokens = StringUtils.split(MessageUtils.getMessage())
    StringUtils.join(tokens)
}