package screens

import com.badlogic.gdx.math.Rectangle
import data.Npc
import ktx.math.amid
import ktx.math.random
import ktx.math.vec2

class Mgo {
    companion object {
        val npcs = mutableListOf<Npc>()
        val workPlaceRange = 100f amid 200f
        val homeXRange = -100f amid 200f
        val homeYRange = -100f amid 200f

        val sizeRange = 5f..10f

        val workPlaces = (1..50).map {
            Place(box = Rectangle(workPlaceRange.random(), workPlaceRange.random(), sizeRange.random(), sizeRange.random()))
        }

        val restaurants = (1..25).map {
            Place(type = PlaceType.Restaurant, box = Rectangle(workPlaceRange.random(), workPlaceRange.random(), sizeRange.random(), sizeRange.random()))
        }

        val homeAreas = (1..100).map {
            Place(type = PlaceType.Home, box = Rectangle(homeXRange.random(), homeYRange.random(), sizeRange.random(), sizeRange.random()))
        }

        val allPlaces = workPlaces + restaurants + homeAreas
    }
}