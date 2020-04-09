package screens

import ai.pathfinding.BreadthFirst
import ai.pathfinding.StarIsBorn
import data.*
import graph.Graph
import graph.Node
import ktx.math.ImmutableVector2
import ktx.math.amid

class Mgo {


    companion object {
        val npcs = mutableListOf<Npc>()
        val allPlaces = mutableListOf<Place>()
        val graphOfItAll = Graph<ImmutableVector2>()

        val workPlaces by lazy { allPlaces.filter { it.type == PlaceType.Workplace } }

        val restaurants by lazy { allPlaces.filter { it.type == PlaceType.Restaurant } }

        val homes by lazy { allPlaces.filter { it.type == PlaceType.Home } }

        val travelHubs by lazy { allPlaces.filter { it.type == PlaceType.TravelHub } }

        fun setupAreas2() {
            val placeWidth = 8f
            val placeHeight = 8f
            val placeClearance = 0f //Distance between places

            var travelHubRange = 0..5
            val restaurantRange = 85..95
            val workPlaceRange = 96..100

            val totalRange = 0..workPlaceRange.last

            val nbList = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))

            val nodeCols = 25
            val nodeRows = 25

            val columnRange = 0 amid nodeCols
            val rowRange = 0 amid nodeRows

            val numberOfCols = columnRange.count()
            val numberOfRows = rowRange.count()

            val colSize = placeWidth + placeClearance
            val rowSize = placeHeight + placeClearance

            val giantMatrixOfNodes = Array<Array<Node<ImmutableVector2>>>(numberOfCols) { c ->
                Array(numberOfRows) { r ->
                    if (c == 0 || r == 0 || c == numberOfCols - 1 || r == numberOfRows - 1 || c % 2 == 0 || r % 2 == 0) {
                        val n = Node(ImmutableVector2(c * colSize, r * colSize))
                        graphOfItAll.addNode(n)
                        n.addLabel("Road")
                        n
                    } else {
                        val n = Node(ImmutableVector2(c * colSize, r * rowSize)) //Is this the center? Yes, yes it is.
                        graphOfItAll.addNode(n)
                        n.addLabel("Place")
                        val p = when (totalRange.random()) {
                            in travelHubRange -> Place(PlaceType.TravelHub, n, placeWidth, placeHeight)
                            in restaurantRange -> Place(PlaceType.Restaurant, n, placeWidth, placeHeight)
                            in workPlaceRange -> Place(PlaceType.Workplace, n, placeWidth, placeHeight)
                            else -> Place(PlaceType.Home, n, placeWidth, placeHeight)
                        }
                        travelHubRange = if (p.type != PlaceType.TravelHub) {
                            0..travelHubRange.last + 1
                        } else {
                            0..5
                        }
                        allPlaces.add(p)
                        n
                    }
                }
            }
            for ((x, c) in giantMatrixOfNodes.withIndex()) {
                for ((y, currentNode) in c.withIndex()) {
                    //Ah, how easy things are!

                    for ((i, j) in nbList) {
                        val cX = x + i
                        val cY = y + j
                        if (cX in giantMatrixOfNodes.indices && cY in c.indices) {
                            val neighbourNode = giantMatrixOfNodes[cX][cY]
                            currentNode.addRelation(Neighbour, neighbourNode)
                        }
                    }
                }
            }
        }

        const val Neighbour = "Neighbour"
    }
}