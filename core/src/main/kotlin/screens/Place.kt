package screens

import com.badlogic.gdx.math.Rectangle
import ktx.math.ImmutableVector2

data class Place(val name: String = "Don't matter", val type: PlaceType = PlaceType.Workplace, val box: Rectangle = Rectangle(0f, 0f, 10f, 10f)) {
    val center = ImmutableVector2(box.x + box.width / 2, box.y + box.height / 2)
}