package com.nophasenokill.app

import org.apache.commons.lang3.StringUtils
import com.nophasenokill.lib.MessageModel

fun main() {
  println(MessageModel(StringUtils.trim("  Hello World!  ")))
}
