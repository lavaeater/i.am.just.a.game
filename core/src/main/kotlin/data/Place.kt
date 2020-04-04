package data

import com.badlogic.gdx.math.Rectangle
import ktx.math.ImmutableVector2

data class Place(val type: PlaceType = PlaceType.Workplace, val x : Float = 0f, val y : Float  = 0f, val width : Float  = 10f, val height : Float  = 10f) {
    val box = Rectangle(x, y, width, height)
    val center = ImmutableVector2(x + width / 2, y + height / 2)
}