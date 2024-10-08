package com.nophasenokill

@InstrumentedAnnotation
open class ClassToBeInstrumentedAtRuntime {
    override fun toString(): String {
        return "Uninstrumented"
    }
}
