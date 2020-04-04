import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import data.Npc
import ktx.math.minus
import ktx.math.times
import ktx.math.vec2
import data.Place

fun Vector2.pointIsInside(size: Vector2, point: Vector2):Boolean {
    return point.x < this.x + size.x / 2 && point.x > this.x -size.x && point.y < this.y + size.y / 2 && point.y > this.y - size.y
}


/**
 * Takes a place and finds the travel hub (subway station)
 * closeset to it. This will be the target hub for the npc
 */
fun List<Place>.findPlaceNearby(place: Place) : Place {
    val hub = this.findNearest(place.center.x, place.center.y)
    return hub
}

fun List<Place>.findNearest(x: Float, y:Float): Place {
    val place = this.minBy { it.center.dst(x, y) }
    return place!!
}


fun Npc.placeInWalkingRange(place: Place) :Boolean {
    val distance =  this.currentPosition.dst(place.x, place.y)
    return distance < this.walkingRange
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



fun Npc.getBehaviorTree() : BehaviorTree<Npc> {

    val reader = Assets.readerForTree("man.tree")
    val parser = BehaviorTreeParser<Npc>(BehaviorTreeParser.DEBUG_NONE)
    this.behaviorTree = parser.parse(reader, this)
    return this.behaviorTree
}