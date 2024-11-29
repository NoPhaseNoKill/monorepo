package com.nophasenokill


sealed class DefaultProjectType(override val path: String) : ProjectType {
    object BuildLogic : DefaultProjectType("build-logic")
    object RootOne : DefaultProjectType("root-one")
    object RootTwo : DefaultProjectType("root-two")
    companion object {
        fun values(): Array<DefaultProjectType> {
            return arrayOf(BuildLogic, RootOne, RootTwo)
        }

        fun valueOf(value: String): DefaultProjectType {
            return when (value) {
                "BUILD_LOGIC" -> BuildLogic
                "ROOT_ONE" -> RootOne
                "ROOT_TWO" -> RootTwo
                else -> throw IllegalArgumentException("No object com.nophasenokill.DefaultProjectType.$value")
            }
        }
    }
}
