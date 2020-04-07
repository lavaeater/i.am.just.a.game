package screens

import data.*
import graph.Graph
import graph.Node
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
            val placeClearance = 5f //Distance between places
            val areaWidth = 100f
            val areaHeight = 100f
            val areaClearance = 20f //This is to make walking between areas impractical
            val placeCols = (areaWidth / (placeWidth + placeClearance)).toInt()
            val placeRows = (areaHeight / (placeHeight + placeClearance)).toInt()


            val restaurantRange = 85..95
            val workPlaceRange = 96..100

            val totalRange = 0..workPlaceRange.last
            //What about some streets? Imagine a graph. Nodes are crossroads. And stops, of course.
            val g = Graph<ImmutableVector2>(mapOf()) //We provide an empty map for now.

            /*
            Aaah, graphs of places and stuff. Let's do it. This will be more organic. So we have nodes of...
            something.

            Nodes will have, for insanity's sake, two types of relations... no. That's dull.

            The graph is a pure and beautiful thing. It will describe a grid. Some nodes will have
            places attached to them. So, these nodes will be in the middle of a "grid" of several other nodes:

            n n n n n
            n p n p n
            n n n n p

            We don't even need areas anymore. Even rows will not have places, only odd, same for columns


             */

            val nodeCols = 5
            val nodeRows = 5

            for (nodeCol in 0 amid nodeCols) {
                for (nodeRow in 0 amid nodeRows) {
                    if(nodeRow % 2 != 0) {
                        if(nodeCol % 2 != 0) {

                        }
                    }

                }
            }


            for (cArea in 0 amid areaCols)
                for (rArea in 0 amid areaRows) {
                    var travelHubRange = 0..5
                    val area = Area(AreaType.MultiType, ImmutableVector2(cArea * areaWidth + areaClearance + placeClearance, rArea * areaHeight + areaClearance + placeClearance))

                    for (cPlace in 0..placeCols)
                        for (rPlace in 0..placeRows) {
                            /*
                            Skip conditions for now, create cool nodes first, fix EVERYTHING later

                            But make sure we only add ONE node for every crossroad

                             */

                            //Now, add some places to it, but at least ONE travelHub... how do we accomplish this?
                            /*
                            For every place added, we raise the possibility of a travel hub being placed, all the way
                            up to 100 - after it is placed, it returns to 0
                             */
                            val nP = when (totalRange.random()) {
                                in travelHubRange -> area.addChild(PlaceType.TravelHub, cPlace * (placeWidth + placeClearance), rPlace * (placeHeight + placeClearance), placeWidth, placeHeight)
                                in restaurantRange -> area.addChild(PlaceType.Restaurant, cPlace * (placeWidth + placeClearance), rPlace * (placeHeight + placeClearance), placeWidth, placeHeight)
                                in workPlaceRange -> area.addChild(PlaceType.Workplace, cPlace * (placeWidth + placeClearance), rPlace * (placeHeight + placeClearance), placeWidth, placeHeight)
                                else -> area.addChild(PlaceType.Home, cPlace * (placeWidth + placeClearance), rPlace * (placeHeight + placeClearance), placeWidth, placeHeight) //This is actually homeRange
                            }

                            for (n in 0..1) {
                                for (m in 0..1) {
                                    //OK, calculate like a corner or something.
                                    var relCornerX = placeWidth / 2 + placeClearance / 2
                                    var relCornerY = placeHeight / 2 + placeClearance / 2
                                    relCornerX = if (n % 2 == 0) relCornerX else -relCornerX
                                    relCornerY = if (m % 2 == 0) relCornerY else -relCornerY
                                    val corner = nP.center + ImmutableVector2(relCornerX, relCornerY)
                                    if (!g.nodes.any { it.data == corner}) {
                                        //There is no node with this coordinate, so we add this particular coord
                                        g.addNode(Node(corner))
                                    }
                                }
                            }

                            //But what about RELATIONS for the nodes? Ehrmagerd

                            travelHubRange = if (travelHubRange.last != 0 && !area.places.any { it.type == PlaceType.TravelHub }) {
                                travelHubRange.first..travelHubRange.last + 1
                            } else {
                                0..0
                            }
                        }
                    areas.add(area)

                }


            /*
            So, for every place, we can check if it has a neighbour, right? Since everything is squared, the distance
            between all nodes is 1, for now. It could be actual distance, why not, I mean? So if we were to add a node
            for every row of areas... how do we keep track? we do it in the loop above, of course.
             */

        }
    }
}