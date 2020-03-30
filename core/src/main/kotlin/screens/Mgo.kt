package screens

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import data.Npc
import ktx.math.*

class Mgo {
    companion object {
        val npcs = mutableListOf<Npc>()

        /*

        Lets do random angles and random lengths from a center!
         */
        val homeXRange = -100f amid 200f
        val homeYRange = -100f amid 200f
        val numberOfNpcs = 50
        val numberOfWorkPlaces = numberOfNpcs / 10
        val numberOfRestaurants = numberOfNpcs / 5


        val restaurantRadius = 0f..50f
        val workplaceRadius = 51f..150f
        val homeRadius = 151f..250f

        val angleRange =  0f..359f



        val homeWidth = 10f
        val homeHeight = 5f

        val minDistance = 10f

        val workPlaceWidth = 30f
        val workPlaceHeight = 15f

        val travelHubSize = 15f

        val restaurantWidth = 20f
        val restaurantHeight = 10f



        val workPlaces = (1..numberOfWorkPlaces).map {
            val randomplace = getRandomPlace(workplaceRadius)
            Place(box = Rectangle(randomplace.x,randomplace.y, workPlaceHeight, workPlaceWidth))
        }

        fun getRandomPlace(floatingPointRange: ClosedRange<Float>): ImmutableVector2 {
            val randomAngle = angleRange.random()
            val magnitude = floatingPointRange.random()
            return ImmutableVector2.X.withRotationDeg(randomAngle) * magnitude
        }

        fun getRandomRectangle() : Rectangle {
            val randomPlace = getRandomPlaceForNpc()
            return Rectangle(randomPlace.x, randomPlace.y, homeWidth, homeHeight)
        }

        fun getRandomPlaceForNpc() : ImmutableVector2 {
            return getRandomPlace(homeRadius)
        }

        val restaurants = (0..numberOfRestaurants).map {
            val randomAngle = angleRange.random()
            val magnitude = restaurantRadius.random()
            val randomplace =  getRandomPlace(restaurantRadius)

            Place(type = PlaceType.Restaurant, box = Rectangle(randomplace.x, randomplace.y, restaurantWidth, restaurantHeight))
        }

        val homeAreas = mutableListOf<Place>()

        val allPlaces get() = workPlaces + restaurants + homeAreas
    }
}