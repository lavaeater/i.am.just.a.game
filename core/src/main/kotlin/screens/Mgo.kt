package screens

import data.*
import ktx.math.ImmutableVector2

class Mgo {


    companion object {
        val npcs = mutableListOf<Npc>()
        val areas = mutableListOf<Area>()

        const val homeWidth = 5f
        const val homeHeight = 2.5f

        const val commercialWidth = 10f
        const val commercialHeight = 5f

        val allPlaces get() = areas.flatMap { it.places }

        val workPlaces: List<Place> get() {
            return allPlaces.filter { it.type == PlaceType.Workplace }
        }

        val restaurants : List<Place> get() {
            return allPlaces.filter { it.type == PlaceType.Restaurant }
        }

        val homes : List<Place> get() {
            return allPlaces.filter { it.type == PlaceType.Home }
        }

        val travelHubs: List<Place> get() {
            return allPlaces.filter { it.type == PlaceType.TravelHub }
        }


        fun setupAreas() {
            /*
            We need 12 areas.

            Rows of 4. Npcs are place at their home places, so we can start at zero for
            simplicity's sake
             */
            val homeClearance = 5
            val otherClearance = 5
            var i = 0

            val r = 0..100

            for (row in 0..4) {
                for(col in 0..4) {
                    var randomNumber = r.random()
                    if(randomNumber < 85) {
                        val area = Area(AreaType.Residential, ImmutableVector2( col * 200f, row * 200f))

                        //number of rCols
                        val rCols = (area.width / ( homeWidth * homeClearance)).toInt()
                        val rRows = (area.height / (homeHeight * homeClearance)).toInt()
                        for(c in 0..rCols)
                            for(r in 0..rRows) {
                                area.addChild(PlaceType.Home, c * homeWidth * homeClearance, r * homeHeight * homeClearance, homeWidth, homeHeight)
                            }
                        area.addChild(PlaceType.TravelHub, area.width / 2, area.height / 2, 5f, 5f) //Should place it in the middle, no?
                        areas.add(area)
                    } else {
                        val area = Area(AreaType.Commercial, ImmutableVector2( col * 200f, row * 200f))
                        /*
                        A third of places in commercial areas are restaurants. All will now have same size for simplicity
                         */
                        randomNumber = r.random()
                        var comercial = 0
                        val rCols = (area.width / (commercialWidth * 3)).toInt()
                        val rRows = (area.height / (commercialHeight * 3)).toInt()
                        for(c in 0..rCols)
                            for(r in 0..rRows) {
                                if(randomNumber < 65) {
                                    area.addChild(PlaceType.Restaurant, c * commercialWidth * otherClearance, r * commercialHeight * otherClearance, commercialWidth, commercialHeight)
                                } else{
                                    area.addChild(PlaceType.Workplace,c * commercialWidth * otherClearance, r * commercialHeight * otherClearance, commercialWidth, commercialHeight)
                                }
                                comercial++
                            }
                        area.addChild(PlaceType.TravelHub, area.width / 2, area.height / 2, 5f, 5f) //Should place it in the middle, no?
                        areas.add(area)
                    }
                    i++
                }
            }

        }
    }
}