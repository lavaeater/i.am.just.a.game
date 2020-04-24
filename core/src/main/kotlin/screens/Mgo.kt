package screens

import com.badlogic.gdx.math.Rectangle
import data.*
import factory.ActorFactory
import graph.Graph
import graph.Node
import injection.Injector
import ktx.math.*

class MapNode(data: ImmutableVector2) : Node<ImmutableVector2>(data) {
    fun move(diffX: Float, diffY: Float) {
        data = ImmutableVector2(data.x + diffX, data.y + diffY)
    }
}

class Mgo {


    companion object {
        val npcs = mutableListOf<Npc>()
        val allPlaces = mutableListOf<Place>()
        val graph = Graph<ImmutableVector2>()

        val workPlaces get() = allPlaces.filter { it.type == PlaceType.Workplace }

        val restaurants get() = allPlaces.filter { it.type == PlaceType.Restaurant }

        val travelHubs get() = allPlaces.filter { it.type == PlaceType.TravelHub }.toMutableList()
        private val rotationDirection = -1..1
        private var currentDirection = ImmutableVector2.X
        private var countX = 0
        private var countY = 0

        private fun findRandomPosition(iteration: Int = 0) : ImmutableVector2 {
            if (graph.nodes.isEmpty()) return ImmutableVector2.ZERO
            val vectors = graph.nodes.map { it.data }

            val minX = vectors.map { it.x }.min()!! - iteration * 10f
            val minY = vectors.map { it.y }.min()!! - iteration * 10f
            val maxX = vectors.map { it.x }.max()!! + iteration * 10f
            val maxY = vectors.map { it.y }.max()!! + iteration * 10f

            return ImmutableVector2((minX..maxX).random(), (minY..maxY).random())
        }

        private fun findSuitableBuildArea(): ImmutableVector2 {
            /*
            Find a suitable area and start building. How?
             */
            if (graph.nodes.isEmpty()) return ImmutableVector2.ZERO
            val vectors = graph.nodes.map { it.data }

            var minX = vectors.map { it.x }.min()!! - 50f
            var minY = vectors.map { it.y }.min()!! - 50f
            var maxX = vectors.map { it.x }.max()!! + 50f
            var maxY = vectors.map { it.y }.max()!! + 50f

            /*
            The min-max of node coordinates represent the four courners of the map:
            minX,maxY   maxX,maxY
            minX,minY   maxX,minY

            Just put the new travelHub at one of those four and work out from there.
             */
            val xCoords = listOf(minX, maxX)
            val yCoords = listOf(minY, maxY)

            /*
            Evaluate coordinates. if x is low, go left else go right.

            if Y is low, go down, else go up
             */
            var xCoord = 0f
            var yCoord = 0f
            if (countY % 2 == 0 && countX % 2 == 0) {
                xCoord = xCoords.first()
                yCoord = yCoords.first()
                countX++
            } else if (countY % 2 != 0 && countX % 2 != 0) {
                xCoord = xCoords.last()
                yCoord = yCoords.last()
                countX++
            } else if (countY % 2 == 0) {
                xCoord = xCoords.first()
                yCoord = yCoords.last()
                countY++
            } else if (countX % 2 == 0) {
                xCoord = xCoords.last()
                yCoord = yCoords.first()
                countY++
            }

            return ImmutableVector2(xCoord, yCoord)

            /*
            The coolest way to do this would be to build a mini-graph FIRST and
            then test if we can fit it somewhere on the map. But the above will do for now.
             */

//            var foundAnArea = false
//            val maxTries = 100
//            var tries =0
//            var centerX = 0f
//            var centerY = 0f
//            while(!foundAnArea && tries < maxTries) {
//                tries++
//                centerX = (minX..maxX).random()
//                centerY = (minY..maxY).random()
//                val potentialArea = Rectangle(centerX - 100f, centerY - 100f, 200f, 200f)
//                foundAnArea = evaluateArea(potentialArea)
//                minX-=50f
//                minY-=50f
//                maxX+=50f
//                maxY+=50f
//            }

//            if(foundAnArea) {
//                return ImmutableVector2(centerX, centerY)
//            }
//            return ImmutableVector2(0f,0f)
        }


        private fun evaluateArea(area: Rectangle): Boolean {
            return !graph.nodes.map { it.data }.any { area.contains(it.x, it.y) }
        }

        val randomRange = 3..12

        private fun buildOfficesAndRestaurants(pos: ImmutableVector2) {
            val newNodes = mutableListOf<MapNode>()
            travelHub(pos, false) {
                street(randomRange.random(), 40f, currentDirection, addNodeToGraph = false) {
                    var sideStreetDirection = currentDirection.withRotation90(rotationDirection.random())
                    street(randomRange.random(), 15f, sideStreetDirection, addNodeToGraph = false) {
                        restaurant(direction = sideStreetDirection.withRotation90(-1), distanceFromStreet = 10f, addNodeToGraph = false) {
                            newNodes.add(this)
                        }
                        workPlace(direction = sideStreetDirection.withRotation90(1), distanceFromStreet = 10f, addNodeToGraph = false) {
                            newNodes.add(this)
                        }
                        newNodes.add(this)
                    }
                    newNodes.add(this)
                }
                newNodes.add(this)
            }
            validatePlaceAdd(newNodes)
        }

        private fun buildHomes(pos: ImmutableVector2) {
            val newNodes = mutableListOf<MapNode>()
            travelHub(pos, false) {
                street(randomRange.random(), 40f, currentDirection, addNodeToGraph = false) {
                    var sideStreetDirection = currentDirection.withRotation90(rotationDirection.random())
                    street(randomRange.random(), 15f, sideStreetDirection, addNodeToGraph = false) {
                        home(direction = sideStreetDirection.withRotation90(rotationDirection.random()), distanceFromStreet = 10f, addNodeToGraph = false) {
                            newNodes.add(this)
                        }
                        newNodes.add(this)
                    }
                    newNodes.add(this)
                }
                newNodes.add(this)
            }
            validatePlaceAdd(newNodes)
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


                if(!areaOk) {
                    val newPos = findRandomPosition(tries)
                    area = area.set(newPos.x, newPos.y, width, height)
                }
            }

            /*
            if area has changed (x or y are different, we must adjust position of EVERY node

             */
            if(area.x != x || area.y != y) {
                val diffX = area.x - x
                val diffY = area.y - y
                for (node in newNodes) {
                    node.move(diffX, diffY)
                }
            }

            for(node in newNodes)
                graph.addNode(node)
        }

        val directions = setOf(ImmutableVector2.X, -ImmutableVector2.X, ImmutableVector2.Y, -ImmutableVector2.Y)

        fun buildUsingCityBlocks() {
            val randomThingie = 0..4
            currentDirection = directions.random()
            buildOfficesAndRestaurants(findRandomPosition())

            for (i in 1..25) {
                if (randomThingie.random() > 3) {
                    //Build some offices
                    buildOfficesAndRestaurants(findRandomPosition())
                } else {
                    buildHomes(findRandomPosition())
                }
            }
            /*
            Just to see what the city looks like, I will connect every travelhub
            to every other travelhub
             */
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


        fun buildWithBuilder() {
            var degreesPerHub = 90f
            node {
                displacedChild(ImmutableVector2(-500f, -1000f)) {
                    addLabel("TravelHub")
                    var degreesPerHub = 360f / 4
                    for (hubIndex in 0 until 2) {
                        travelHub((ImmutableVector2.X * 100f).withAngleDeg(hubIndex * degreesPerHub)) {
                            street(5, 100f) {
                                street(5, 50f, ImmutableVector2.Y) {
                                    workPlace(20f, ImmutableVector2.X)
                                }
                            }
                            street(4, 50f, -ImmutableVector2.X) {
                                street(5, 50f, ImmutableVector2.Y) {
                                    restaurant(15f, ImmutableVector2.X)
                                }
                            }
                        }
                    }
                }
                displacedChild(ImmutableVector2(1000f, 500f)) {
                    addLabel("TravelHub")
                    val numberOfHubs = 2//(5..10).random()
                    degreesPerHub = 360f / numberOfHubs
                    for (hub in 0 until numberOfHubs) {
                        travelHub((ImmutableVector2.X * 500f).withAngleDeg(hub * degreesPerHub)) {
                            val numberOfStreets = (5..10).random()
                            val degreesPerStreet = 360f / numberOfStreets
                            for (i in 0 until numberOfStreets)
                                displacedChild((ImmutableVector2.X * 30f).withAngleDeg(i * degreesPerStreet)) {
                                    addLabel("Street")
                                    street((5..10).random(), direction = ImmutableVector2.X.withAngleDeg(i * degreesPerStreet)) {
                                        home(direction = ImmutableVector2.Y.withAngleDeg(i * degreesPerStreet).withRotation90(1), distanceFromStreet = 10f)
                                        home(direction = -ImmutableVector2.Y.withAngleDeg(i * degreesPerStreet).withRotation90(1), distanceFromStreet = 10f)
                                    }
                                }
                        }
                    }
                }
            }
            updateDrawableRelations()
        }

        fun oneHubStarter() {
            node {
                addLabel("Street")
                travelHub(ImmutableVector2(5f, 5f)) {
                    street(4, direction = -ImmutableVector2.Y) {
                        workPlace { }
                    }
                    street(10, direction = ImmutableVector2.X) {
                        home { }
                    }
                    street(4, direction = -ImmutableVector2.X) {
                        restaurant { }
                    }

                }
            }
        }

        fun updateDrawableRelations() {
            for (node in graph.nodes) {
                for (related in node.allNeighbours) {
                    relationsToDraw.add(DrawableRelation(node.data, related.data))
                }
            }
        }

        fun addNodeToGraph(node: Node<ImmutableVector2>) {
            //1. Can we draw it's related nodes without disaster? Yes.
            graph.addNode(node)
            for (related in node.allNeighbours) {
                relationsToDraw.add(DrawableRelation(node.data, related.data))
            }
        }

        val relationsToDraw = mutableSetOf<DrawableRelation>()

        const val Neighbour = "Neighbour"
    }
}


class DrawableRelation(val from: ImmutableVector2, val to: ImmutableVector2) {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        return (other is DrawableRelation && ((other.from == this.from && other.to == this.to) || (other.to == this.from && other.from == this.to)))
    }

    override fun hashCode(): Int {
        return (from.hashCode() + to.hashCode()) * 23
    }
}

fun node(position: ImmutableVector2 = ImmutableVector2.ZERO, addToGraph: Boolean = true, init: MapNode.() -> Unit = {}): MapNode {
    val node = MapNode(position)
    node.init()
    if (addToGraph)
        Mgo.graph.addNode(node)
    return node
}

fun travelHub(position: ImmutableVector2 = ImmutableVector2.ZERO, addToGraph: Boolean = true, init: MapNode.() -> Unit = {}): MapNode {
    val node = MapNode(position)
    node.init()
    node.addLabel("TravelHub")
    if (addToGraph)
        Mgo.graph.addNode(node)
    Mgo.allPlaces.add(Place(PlaceType.TravelHub, node))
    return node
}

fun MapNode.travelHub(displacement: ImmutableVector2,
                      addNodeToGraph: Boolean = true,
                      init: MapNode.() -> Unit = {}): MapNode {
    val node = nodeWithLabel(displacement, "TravelHub", addNodeToGraph, init)
    Mgo.allPlaces.add(Place(PlaceType.TravelHub, node))
    return node

}

fun MapNode.workPlace(distanceFromStreet: Float = 20f,
                      direction: ImmutableVector2 = ImmutableVector2.Y,
                      addNodeToGraph: Boolean = true,
                      init: MapNode.() -> Unit = {}): MapNode {
    val workPlaceNode = nodeWithLabel(direction * distanceFromStreet, "WorkPlace", addNodeToGraph, init)
    Mgo.allPlaces.add(Place(PlaceType.Workplace, workPlaceNode))
    return workPlaceNode
}


fun MapNode.restaurant(distanceFromStreet: Float = 20f,
                       direction: ImmutableVector2 = ImmutableVector2.Y,
                       addNodeToGraph: Boolean = true,
                       init: MapNode.() -> Unit = {}): MapNode {
    val node = nodeWithLabel(direction * distanceFromStreet, "Restaurant", addNodeToGraph, init)
    Mgo.allPlaces.add(Place(PlaceType.Restaurant, node))
    return node
}

fun MapNode.home(distanceFromStreet: Float = 20f,
                 direction: ImmutableVector2 = ImmutableVector2.Y,
                 addNodeToGraph: Boolean = true,
                 addNpc: Boolean = true,
                 init: MapNode.() -> Unit = {}): MapNode {
    val homeNode = nodeWithLabel(direction * distanceFromStreet, "Home", addNodeToGraph, init)
    val home = Place(PlaceType.Home, homeNode)
    Mgo.allPlaces.add(home)
    if (addNpc) {
        val npc = Injector.inject<ActorFactory>().addNpcAt(home)
        val dieRange = (1..100)
        val infectionRisk = 5
        if (dieRange.random() < infectionRisk) {
            npc.coronaStatus = CoronaStatus.Infected
            npc.symptomatic = false
        }
    }
    return homeNode
}

fun MapNode.nodeWithLabel(displacement: ImmutableVector2,
                          label: String,
                          addNodeToGraph: Boolean = true,
                          init: MapNode.() -> Unit = {}): MapNode {
    return displacedChild(displacement, addNodeToGraph) {
        addLabel(label)
        init()
    }
}

fun MapNode.street(count: Int = 1,
                   distanceBetween: Float = 30f,
                   direction: ImmutableVector2 = ImmutableVector2.X,
                   label: String = "Street",
                   addNodeToGraph: Boolean = true,
                   init: MapNode.() -> Unit = {}): MapNode {
    val previous = this
    val next = previous.displacedChild(direction * distanceBetween, addNodeToGraph) {
        addLabel(label)
        init()
    }
    if (count - 1 > 0)
        next.street(count - 1, distanceBetween, direction, init = init) //Ooh, same init function for all children, madness

    return next
}

fun MapNode.displacedChild(displacement: ImmutableVector2,
                           addNodeToGraph: Boolean = true,
                           init: MapNode.() -> Unit = {}): MapNode {
    val parent = this
    val child = node(parent.data + displacement, addNodeToGraph) {
        init()
    }
    Mgo.graph.connect(parent, child)
    return child
}

