package screens

import data.*
import graph.Graph
import graph.Node
import ktx.math.*

class MapNode(data: ImmutableVector2) : Node<ImmutableVector2>(data)

class Mgo {


    companion object {
        val npcs = mutableListOf<Npc>()
        val allPlaces = mutableListOf<Place>()
        val graph = Graph<ImmutableVector2>()

        val workPlaces by lazy { allPlaces.filter { it.type == PlaceType.Workplace } }

        val restaurants by lazy { allPlaces.filter { it.type == PlaceType.Restaurant } }

        val homes by lazy { allPlaces.filter { it.type == PlaceType.Home } }

        val travelHubs by lazy { allPlaces.filter { it.type == PlaceType.TravelHub } }

        fun buildWithBuilder() {

            node{
                addLabel("Street")
                val numberOfHubs = (0..10).random()
                var degreesPerHub = 360f / numberOfHubs
                for (hub in 0 until numberOfHubs) {
                    displacedChild((ImmutableVector2.X * 500f).withAngleDeg(hub * degreesPerHub)) {
                        addLabel("TravelHub")
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
                degreesPerHub = 360f / 4
                for (centerHub in 0 until 2) {
                    displacedChild((ImmutableVector2.X * 100f).withAngleDeg(centerHub * degreesPerHub)) {
                        addLabel("TravelHub")
                        street(5, 100f) {
                            street( 5, 50f, ImmutableVector2.Y) {
                                workPlace(20f, ImmutableVector2.X)
                            }
                        }
                    }
                }
            }

            for (homeNode in graph.nodes.filter { it.hasLabel("Home") }) {
                allPlaces.add(Place(PlaceType.Home, homeNode))
            }

            for (workNode in graph.nodes.filter { it.hasLabel("WorkPlace") }) {
                allPlaces.add(Place(PlaceType.Workplace, workNode))
            }

            for (travelNode in graph.nodes.filter { it.hasLabel("TravelHub") }) {
                allPlaces.add(Place(PlaceType.TravelHub, travelNode))
            }
        }


        const val Neighbour = "Neighbour"
    }
}

fun node(position: ImmutableVector2 = ImmutableVector2.ZERO, init: MapNode.() -> Unit = {}): MapNode {
    val node = MapNode(position)
    node.init()
    Mgo.graph.addNode(node)
    return node
}

fun MapNode.travelHub(displacement: ImmutableVector2,
init: MapNode.()-> Unit) : MapNode {
    val node = nodeWithLabel(displacement, "TravelHub", init)
    Mgo.allPlaces.add(Place(PlaceType.TravelHub, node))
    return node

}

fun MapNode.workPlace(distanceFromStreet: Float = 20f,
                      direction: ImmutableVector2 = ImmutableVector2.Y,
                      init: MapNode.() -> Unit = {}): MapNode {
    val workPlaceNode = nodeWithLabel(direction * distanceFromStreet, "WorkPlace", init)
    Mgo.allPlaces.add(Place(PlaceType.Workplace, workPlaceNode))
    return workPlaceNode
}


fun MapNode.restaurant(distanceFromStreet: Float = 20f,
                       direction: ImmutableVector2 = ImmutableVector2.Y,
                       init: MapNode.() -> Unit = {}): MapNode {
    val node = nodeWithLabel(direction * distanceFromStreet, "Restaurant", init)
    Mgo.allPlaces.add(Place(PlaceType.Restaurant, node))
    return node
}

fun MapNode.home(distanceFromStreet: Float = 20f,
                 direction: ImmutableVector2 = ImmutableVector2.Y,
                 init: MapNode.() -> Unit = {}): MapNode {
    val homeNode = nodeWithLabel(direction * distanceFromStreet, "Home", init)
    Mgo.allPlaces.add(Place(PlaceType.Home, homeNode))
    return homeNode
}

fun MapNode.nodeWithLabel(displacement: ImmutableVector2,
                          label: String,
                          init: MapNode.() -> Unit = {}): MapNode {
    return displacedChild(displacement) {
        addLabel(label)
        init()
    }
}

fun MapNode.street(count: Int = 1,
                   distanceBetween: Float = 30f,
                   direction: ImmutableVector2 = ImmutableVector2.X,
                   label: String = "Street",
                   init: MapNode.() -> Unit = {}): MapNode {
    val previous = this
    val next = previous.displacedChild(direction * distanceBetween) {
        addLabel(label)
        init()
    }
    if (count - 1 > 0)
        next.street(count - 1, distanceBetween, direction, init = init) //Ooh, same init function for all children, madness

    return next
}

fun MapNode.displacedChild(displacement: ImmutableVector2,
                           init: MapNode.() -> Unit = {}): MapNode {
    val parent = this
    val child = node(parent.data + displacement) {
        init()
    }
    Mgo.graph.connect(parent, child)
    return child
}
