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
        val numberOfNpcs = 5000
        val numberOfWorkPlaces = numberOfNpcs / 100 +1
        val numberOfRestaurants = numberOfNpcs / 50 +1


        val restaurantRadius = 0f..(numberOfRestaurants.toFloat()*1.5f)
        val workplaceRadius = restaurantRadius.endInclusive + 25f..(restaurantRadius.endInclusive + numberOfRestaurants / 2)
        val homeRadius = workplaceRadius.endInclusive+50f..workplaceRadius.endInclusive + numberOfNpcs / 15f

        val angleRange =  0f..359f



        val homeWidth = 5f
        val homeHeight = 2.5f

        val minDistance = 10f

        val workPlaceWidth = 15f
        val workPlaceHeight = 5f

        val travelHubSize = 15f

        val restaurantWidth = 10f
        val restaurantHeight = 5f



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