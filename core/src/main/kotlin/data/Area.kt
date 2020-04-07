package data

import com.badlogic.gdx.math.Rectangle
import ktx.math.ImmutableVector2

class Area(val type: AreaType,  val center: ImmutableVector2 = ImmutableVector2(0f, 0f), size: ImmutableVector2 = ImmutableVector2(100f, 100f)) {
    private val children: MutableList<Place> = mutableListOf()
    val places get() = children.toList()
    fun addChild(placeType: PlaceType, relativeX: Float, relativeY: Float, width: Float, height: Float) : Place {

        /*
        All our places are, for now, immutable.

        So since they are placed RELATIVE to the area that contains them, we must create them at the correct
        location on instantiation, for now
         */
        val place = Place(placeType, x + relativeX, y + relativeY, width, height)
        children.add(place)
        return place
    }

    private val box = Rectangle(center.x - size.x / 2, center.y - size.y / 2, size.x, size.y )
    val x = box.x
    val y = box.y
    val centerX = center.x
    val centerY = center.y
    val width = size.x
    val height = size.y
}