package graph

import ktx.math.ImmutableVector2

sealed class Property<T> {
    abstract val name: String
    abstract var value: T

    sealed class GenericTypedProperty<T>(override val name: String, override var value:T):Property<T>()
    sealed class StringProperty(override val name: String, override var value: String) : Property<String>()
    sealed class IntProperty(override val name: String, override var value: Int) : Property<Int>()
    sealed class VectorProperty(override val name: String, override var value: ImmutableVector2) : Property<ImmutableVector2>()
    sealed class BoolProperty(override val name: String, override var value: Boolean) : Property<Boolean>()
    sealed class Coordinate(val x: Int, val y: Int, var type: Int = 0)
}