package com.nophasenokill.functionalTest

import org.junit.platform.commons.logging.Logger
import org.junit.platform.commons.logging.LoggerFactory

/*
    Allows logging through stdout of info logs, due to weirdnesses'
    in how gradle and junit manage their stdouts.

    1. It prevents duplicate classloader warnings by delgating
    to JUL
    2. It modifies logs to higher severity, which mean
    they're often flagged/in red in rich consoles - which
    IMO is behaviour we want when putting logs in tests (visiblity
    is good).

    There may be a better fix for this later, but for such a basic
    thing - it already took much longer to find a fix for this
    'trivial' feature
 */
object TestLogger {
    /*
        You can't seem to do lambdas and just expose TestLogger.info(message),
        otherwise the resulting log will be an anonymous function
        name which isn't useful for logging.
     */
    val LOGGER: Logger = lazy { LoggerFactory.getLogger(this::class.java) }.value
}