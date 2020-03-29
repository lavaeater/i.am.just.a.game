package screens

import com.badlogic.gdx.math.Rectangle

data class Place(val name: String = "Don't matter", val type: PlaceType = PlaceType.Workplace, val box: Rectangle = Rectangle(0f, 0f, 10f, 10f))