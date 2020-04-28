package screens

import com.badlogic.gdx.math.Rectangle
import data.MapNode
import data.Place
import data.PlaceType
import graph.Graph
import graph.connect
import ktx.math.ImmutableVector2
import ktx.math.random
import nodeFromString

class Mgo {
    companion object {
        val relationsToDraw = mutableSetOf<DrawableRelation>()
        val allPlaces = mutableListOf<Place>()
        val graph = Graph<ImmutableVector2>()
        val workPlaces get() = allPlaces.filter { it.type == PlaceType.Workplace }
        val restaurants get() = allPlaces.filter { it.type == PlaceType.Restaurant }
        val travelHubs get() = allPlaces.filter { it.type == PlaceType.TravelHub }.toMutableList()

        const val Neighbour = "Neighbour"

        private fun findRandomPosition(iteration: Int = 0): ImmutableVector2 {
            if (graph.nodes.isEmpty()) return ImmutableVector2.ZERO
            val vectors = graph.nodes.map { it.data }

            val minX = vectors.map { it.x }.min()!! - iteration * 10f
            val minY = vectors.map { it.y }.min()!! - iteration * 10f
            val maxX = vectors.map { it.x }.max()!! + iteration * 10f
            val maxY = vectors.map { it.y }.max()!! + iteration * 10f

            return ImmutableVector2((minX..maxX).random(), (minY..maxY).random())
        }

        private fun evaluateArea(area: Rectangle): Boolean {
            return !graph.nodes.map { it.data }.any { area.contains(it.x, it.y) }
        }

        private fun validatePlaceAdd(newNodes: List<MapNode>) {
            val vectors = newNodes.map { it.data }
            val x = vectors.map { it.x }.min()!!
            val y = vectors.map { it.y }.min()!!
            val width = vectors.map { it.x }.max()!! - x
            val height = vectors.map { it.y }.max()!! - y
            var area = Rectangle(x, y, width, height)
            var areaOk = false
            val maxTries = 100
            var tries = 0

            while (!areaOk && tries < maxTries) {
                tries++
                areaOk = evaluateArea(area)


                if (!areaOk) {
                    val newPos = findRandomPosition(tries)
                    area = area.set(newPos.x, newPos.y, width, height)
                }
            }

            /*
            if area has changed (x or y are different, we must adjust position of EVERY node

             */
            if (area.x != x || area.y != y) {
                val diffX = area.x - x
                val diffY = area.y - y
                for (node in newNodes) {
                    node.move(diffX, diffY)
                }
            }

            for (node in newNodes)
                graph.addNode(node)
        }


        fun newCityBuilder() {
            val randomThingie = 0..4
            buildCityCenterBlock()

            for (i in 1..100) {
                if (randomThingie.random() > 3) {
                    //Build some offices
                    buildCityCenterBlock()
                } else {
                    buildResidentialBlock()
                }
            }

            val travelHubs = graph.withLabels("TravelHub").toList()
            for ((i, f) in travelHubs.withIndex()) {
                if (i + 1 == travelHubs.count()) {
                    graph.connect(f, travelHubs[0])
                } else {
                    graph.connect(f, travelHubs[i + 1])
                }
            }

            updateDrawableRelations()
        }

        private fun buildCityCenterBlock() {
            val blockDescription ="""
sssssssss
swsssssts
sssssssss
srsssssrs
sssssssss
sssswssss
sssssssss

""".trimIndent()
            val newNodes = createFromString(blockDescription)
            validatePlaceAdd(newNodes)
        }

        private fun buildResidentialBlock() {
            /*
            I want the travel Hub in the middle of the block.
            I want the top left corner to be a home and so on.

            I want this to be built using some super simple code structure.

            Perhaps a string we input?
             */
            val blockDescription ="""
                hshshshshshshshshsh
                sssssssssssssssssss
                hshshshshshshshshsh
                ssstssssstssssstsss
                hshshshshshshshshsh
                sssssssssssssssssss
                hshshshshshshshshsh
                hshshshshshshshshsh
                sssssssssssssssssss
                hshshshshshshshshsh
                ssstssssstssssstsss
                hshshshshshshshshsh
                sssssssssssssssssss
                hshshshshshshshshsh
            """.trimIndent()
            val newNodes = createFromString(blockDescription)
            /*
            This is kinda nice, because we can generate the string above and easily
            construct our city from it instead of working with code. Also, the relations are
            easily figured out, all columns are same size, all are related the same way.
             */
            validatePlaceAdd(newNodes)
        }

        private fun createFromString(blockDescription: String) : List<MapNode> {
            val rows = blockDescription.split('\n')
            var previousRow = arrayOf<MapNode>()
            val allNodes = mutableListOf<MapNode>()
            for ((r, row) in rows.withIndex()) {
                val cols = row.toCharArray()
                val newNodes = Array(cols.size) { c ->
                    nodeFromString(cols[c], ImmutableVector2(c * 20f, r * 20f))
                }
                for ((i, node) in newNodes.withIndex()) {
                    if (i + 1 in newNodes.indices) {
                        node.connect(newNodes[i + 1])
                    }
                    if (r > 0) {
                        node.connect(previousRow[i])
                    }
                }
                allNodes.addAll(newNodes)
                previousRow = newNodes
            }
            return allNodes
        }

        private fun updateDrawableRelations() {
            for (node in graph.nodes) {
                for (related in node.allNeighbours) {
                    relationsToDraw.add(DrawableRelation(node.data, related.data))
                }
            }
        }
    }
}


