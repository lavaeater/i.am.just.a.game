package screens

import data.*
import ktx.math.ImmutableVector2
import ktx.math.amid

class Mgo {


    companion object {
        val npcs = mutableListOf<Npc>()
        val areas = mutableListOf<Area>()

        private const val homeWidth = 5f
        private const val homeHeight = 2.5f

        private const val commercialWidth = 10f
        private const val commercialHeight = 5f

        val allPlaces by lazy { areas.flatMap { it.places } }

        val workPlaces by lazy { allPlaces.filter { it.type == PlaceType.Workplace } }

        val restaurants by lazy { allPlaces.filter { it.type == PlaceType.Restaurant } }

        val homes by lazy { allPlaces.filter { it.type == PlaceType.Home } }

        val travelHubs by lazy { allPlaces.filter { it.type == PlaceType.TravelHub } }

        fun setupAreas2() {
            /*
            Goal: Non-overlapping, not so boring structure
            Areas are city blocks. They start with a travel hub

            So the residential / commercial aspect of a block could be ignored and we could just handle
            overlapping instead. Which we can handle easily by making everything the same goddamned size.

            So, all blocks contain width / size items and we just need to determine proportions for them.
            Oh la la.
             */

            val areaCols = 1
            val areaRows = 1
            val placeWidth = 10f
            val placeHeight = 10f
            val placeClearance = 10f //Distance between places
            val areaWidth = 100f
            val areaHeight = 100f
            val areaPositionThingie = 200f //This is to make walking between areas impractical
            val placeCols = (areaWidth / (placeWidth + placeClearance)).toInt()
            val placeRows = (areaHeight / (placeHeight + placeClearance)).toInt()


            val restaurantRange = 85..95
            val workPlaceRange = 96..100

            val totalRange = 0..workPlaceRange.last

            for (cArea in 0 amid areaCols)
                for (rArea in 0 amid areaRows) {
                    var travelHubRange = 0..5
                    val area = Area(AreaType.MultiType, ImmutableVector2(cArea * areaPositionThingie, rArea * areaPositionThingie))

                    for (cPlace in 0..placeCols)
                        for (rPlace in 0..placeRows) {
                            //Now, add some places to it, but at least ONE travelHub... how do we accomplish this?
                            /*
                            For every place added, we raise the possibility of a travel hub being placed, all the way
                            up to 100 - after it is placed, it returns to 0
                             */
                            when (totalRange.random()) {
                                in travelHubRange -> area.addChild(PlaceType.TravelHub, cPlace * (placeWidth + placeClearance), rPlace * (placeHeight + placeClearance),placeWidth, placeHeight)
                                in restaurantRange -> area.addChild(PlaceType.Restaurant, cPlace * (placeWidth + placeClearance), rPlace * (placeHeight + placeClearance),placeWidth, placeHeight)
                                in workPlaceRange -> area.addChild(PlaceType.Workplace, cPlace * (placeWidth + placeClearance), rPlace * (placeHeight + placeClearance),placeWidth, placeHeight)
                                else -> area.addChild(PlaceType.Home, cPlace * (placeWidth + placeClearance), rPlace * (placeHeight + placeClearance),placeWidth, placeHeight) //This is actually homeRange
                            }
                            if(travelHubRange.last != 0 && !area.places.any { it.type == PlaceType.TravelHub }) {
                                travelHubRange = travelHubRange.first..travelHubRange.last + 1
                            } else {
                                travelHubRange = 0..0
                            }
                        }
                    areas.add(area)

                }
        }
    }
}