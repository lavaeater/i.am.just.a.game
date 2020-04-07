package screens

import data.*
import graph.Graph
import graph.Node
import ktx.math.ImmutableVector2
import ktx.math.amid
import ktx.math.random

class Mgo {


    companion object {
        val npcs = mutableListOf<Npc>()
        val allPlaces = mutableListOf<Place>()
        val graphOfItAll = Graph<ImmutableVector2>()

        private const val homeWidth = 5f
        private const val homeHeight = 2.5f

        private const val commercialWidth = 10f
        private const val commercialHeight = 5f

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

            val placeWidth = 10f
            val placeHeight = 10f
            val placeClearance = 0f //Distance between places

            var travelHubRange = 0..5
            val restaurantRange = 85..95
            val workPlaceRange = 96..100

            val totalRange = 0..workPlaceRange.last
            //What about some streets? Imagine a graph. Nodes are crossroads. And stops, of course.

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

            val nodeCols = 40
            val nodeRows = 40


            /*
            We must traverse a graph, using some kind of insanity-based way of doing it. Do we need directions for the
            relations as I did it before? Maybe not. If they have coordinates everything should work anyways?

            Maybe we should create the graph using a weird recursive algorithm where we create nodes as we go and connect
            them as we go... aah.


            Nah, the easiest way to do it is probably a very large matrix of nodes (first)

             */
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
                        graphOfItAll.addLabelToNode("Road", n)
                        n
                    } else {
                        val n = Node(ImmutableVector2(c * colSize, r * rowSize)) //Is this the center? Yes, yes it is.
                        graphOfItAll.addNode(n)
                        graphOfItAll.addLabelToNode("Place", n)
                        val p = when (totalRange.random()) {
                            in travelHubRange -> Place(PlaceType.TravelHub, n, placeWidth, placeHeight)
                            in restaurantRange -> Place(PlaceType.Restaurant, n, placeWidth, placeHeight)
                            in workPlaceRange -> Place(PlaceType.Workplace, n, placeWidth, placeHeight)
                            else -> Place(PlaceType.Home, n, placeWidth, placeHeight)
                        }
                        travelHubRange = if(p.type != PlaceType.TravelHub) {
                            0..travelHubRange.last + 5
                        } else {
                            0..5
                        }
                        allPlaces.add(p)
                        n
                    }
                }
            }

            /*
            So, how do we set up the relations? Well, we loop over the array of arrays, of course.

            Then we keep track of above, below, side to side, for instance...
             */
            for ((x, c) in giantMatrixOfNodes.withIndex()) {
                for ((y, currentNode) in c.withIndex()) {
                    //Ah, how easy things are!
                    for (i in 0 amid 1) {
                        for (j in 0 amid 1) {
                            if (i != 0 || j != 0) {
                                val cX = x + i
                                val cY = y + j
                                if (cX in giantMatrixOfNodes.indices && cY in c.indices) {
                                    val neighbourNode = giantMatrixOfNodes[cX][cY]
                                    currentNode.addRelation("Neighbour", neighbourNode)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}