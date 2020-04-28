import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import data.CoronaStatus
import data.Npc
import ktx.math.minus
import ktx.math.times
import ktx.math.vec2
import data.Place
import data.PlaceType
import factory.ActorFactory
import injection.Injector
import ktx.math.ImmutableVector2
import data.MapNode
import screens.Mgo

fun Vector2.pointIsInside(size: Vector2, point: Vector2):Boolean {
    return point.x < this.x + size.x / 2 && point.x > this.x -size.x && point.y < this.y + size.y / 2 && point.y > this.y - size.y
}


/**
 * Takes a place and finds the travel hub (subway station)
 * closeset to it. This will be the target hub for the npc
 */
fun List<Place>.findPlaceNearby(place: Place) : Place {
    val hub = this.findClosestTo(place.center.x, place.center.y)
    return hub
}

fun List<Place>.findClosestTo(x: Float, y:Float): Place {
    val place = this.minBy { it.center.dst2(x, y) }
    return place!!
}


fun Npc.placeInWalkingRange(place: Place) :Boolean {
    /*
    Walking distance should be both dumber and smarter and not
    actually part of the NPC suite of methods, but really it should
    be MGO.

    So, a place is within walking distance if it is closer than the closes
    TravelHub. Simple
     */

    val distanceToPlace =  this.currentPosition.dst2(place.x, place.y)
    val distanceToClosestTravelHub = Mgo.travelHubs.map { it.center.dst2(this.currentPosition.x, this.currentPosition.y) }.min()

    return distanceToPlace < distanceToClosestTravelHub!!
}

fun Npc.atPlace(place: Place) :Boolean {
    return place.box.contains(this.currentPosition)
}

fun Vector2.placesInRange(range: Float, places: List<Place>) : List<Place> {
    return places.filter { vec2(it.box.x + it.box.width / 2, it.box.y +it.box.height /2).dst(this) < range }
}



fun Body.isWithin(radius: Float, body: Body): Boolean {
    return this.position.dst(body.position) < radius
}

fun Vector2.moveFromTo(body: Body, velocity: Float) {
    body.linearVelocity = body.position.moveTowards(this, velocity)
}

fun Body.moveTowards(body: Body) {
    this.linearVelocity = this.position.moveTowards(body.position, 6f)
}

fun Vector2.directionalVelocity(velocity: Float): Vector2 {
    return (vec2(0f, 0f) - this).nor() * velocity
}

fun Vector2.moveTowards(target: Vector2, velocity: Float): Vector2 {
    return (target - this).nor() * velocity
}

fun ImmutableVector2.dst2(position: Vector2): Float {
    return this.dst2(ImmutableVector2(position.x, position.y))
}

fun Npc.getBehaviorTree() : BehaviorTree<Npc> {

    val reader = Assets.readerForTree("man.tree")
    val parser = BehaviorTreeParser<Npc>(BehaviorTreeParser.DEBUG_NONE)
    this.behaviorTree = parser.parse(reader, this)
    return this.behaviorTree
}



fun nodeFromString(type: Char, position: ImmutableVector2, init: MapNode.() -> Unit = {}) : MapNode {
    val node = node(position, init)
    when (type) {
        's' -> node.addLabel("Street")
        't' -> {
            Mgo.allPlaces.add(Place(PlaceType.TravelHub, node))
            node.addLabel("TravelHub")
        }
        'h' -> {
            val home = Place(PlaceType.Home, node)
            Mgo.allPlaces.add(home)
            node.addLabel("Home")
            val npc = Injector.inject<ActorFactory>().addNpcAt(home, Mgo.workPlaces.random())
            val dieRange = (1..100)
            val infectionRisk = 5
            if (dieRange.random() < infectionRisk) {
                npc.coronaStatus = CoronaStatus.Infected
                npc.symptomatic = false
            }
        }
        'w' -> {
            Mgo.allPlaces.add(Place(PlaceType.Workplace, node))
            node.addLabel("WorkPlace")
        }
        'r' -> {
            Mgo.allPlaces.add(Place(PlaceType.Restaurant, node))
            node.addLabel("Restaurant")
        }
    }
    return node
}


fun node(position: ImmutableVector2, init: MapNode.() -> Unit = {}) : MapNode {
    return node(position, false, init)
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
        val npc = Injector.inject<ActorFactory>().addNpcAt(home, Mgo.workPlaces.random())
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