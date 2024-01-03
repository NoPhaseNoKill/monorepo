package integraboost

class SomeClass(overrideInitialValue: Int) {
    var initialValue = 0

    init {
        initialValue = overrideInitialValue
    }
}