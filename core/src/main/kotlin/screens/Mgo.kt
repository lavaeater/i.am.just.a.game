package screens

import data.*
import factory.ActorFactory
import graph.Graph
import graph.Node
import injection.Injector
import ktx.math.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MapNode(data: ImmutableVector2) : Node<ImmutableVector2>(data)

class Mgo {


    companion object {
        val npcs = mutableListOf<Npc>()
        val allPlaces = mutableListOf<Place>()
        val graph = Graph<ImmutableVector2>()

        val workPlaces get() = allPlaces.filter { it.type == PlaceType.Workplace }

        val restaurants get()= allPlaces.filter { it.type == PlaceType.Restaurant }

        val travelHubs get()= allPlaces.filter { it.type == PlaceType.TravelHub }.toMutableList()

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
                    street (4, direction = -ImmutableVector2.Y ){
                        workPlace {  }
                    }
                    street(10,direction = ImmutableVector2.X) {
                        home {  }
                    }
                    street(4, direction = -ImmutableVector2.X) {
                        restaurant {  }
                    }

                }
            }
        }

        fun updateDrawableRelations() {
            for(node in graph.nodes) {
                for(related in node.allNeighbours) {
                    relationsToDraw.add(DrawableRelation(node.data, related.data))
                }
            }
        }

        val relationsToDraw = mutableSetOf<DrawableRelation>()

        const val Neighbour = "Neighbour"
    }
}


class DrawableRelation(val from: ImmutableVector2, val to: ImmutableVector2) {
    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        return (other is DrawableRelation && ((other.from == this.from && other.to == this.to) || (other.to == this.from && other.from == this.to)) )
    }

    override fun hashCode(): Int {
        return (from.hashCode() + to.hashCode()) * 23
    }
}

fun node(position: ImmutableVector2 = ImmutableVector2.ZERO, init: MapNode.() -> Unit = {}): MapNode {
    val node = MapNode(position)
    node.init()
    Mgo.graph.addNode(node)
    return node
}

fun MapNode.travelHub(displacement: ImmutableVector2,
                      init: MapNode.() -> Unit = {}): MapNode {
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
    val home = Place(PlaceType.Home, homeNode)
    Mgo.allPlaces.add(home)
    val npc = Injector.inject<ActorFactory>().addNpcAt(home)
    val dieRange = (1..100)
    val infectionRisk = 5
    if (dieRange.random() < infectionRisk) {
        npc.coronaStatus = CoronaStatus.Infected
        npc.symptomatic = false
    }
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
