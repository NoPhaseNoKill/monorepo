package com.nophasenokill

@InstrumentedAnnotation
class ClassToBeInstrumentedAtRuntime {
    override fun toString(): String {
        return "Uninstrumented"
    }
}
