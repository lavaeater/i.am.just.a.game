package systems

import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import ktx.math.ImmutableVector2
import screens.*

/**
 * This system shall add more nodes to the map - one node at a time!
 *
 * It shall keep track of a list of nodes that can be the base of new nodes being
 * added.
 */
class BuilderSystem(private val buildToggler: IToggleBuilds,
                    private val camera: OrthographicCamera) : IntervalSystem(1f) {
    private var currentNode: MapNode? = null
    private var startedBuilding = false
    private val directions = setOf(ImmutableVector2.X, -ImmutableVector2.X, ImmutableVector2.Y, -ImmutableVector2.Y)
    private var currentDirection = directions.random()
    val randomRange = 1..100


    override fun updateInterval() {
        if (buildToggler.build)
            buildSomeStuff()
        else
            stopBuildingMaybe()
    }

    private fun stopBuildingMaybe() {
        startedBuilding = false
    }

    private fun buildSomeStuff() {
        if (!startedBuilding) {
            startedBuilding = true

            //1. Find a random... travelhub?
            currentNode = Mgo.graph.nodes.filter { it.hasLabel("TravelHub") }.random() as MapNode
        }

        addStreetNode()

        val data = currentNode?.data
        if (data != null) {
            camera.position.lerp(Vector3(data.x, data.y, 0f), 0.5f)
        }
        Mgo.updateDrawableRelations()
    }

    private fun addStreetNode() {
        currentDirection = currentDirection.withRotation90(if (randomRange.random() > 5) -1 else 1)
        currentNode?.street {
            currentNode = when (randomRange.random()) {
                in 1..10 -> restaurant(direction = currentDirection.withRotation90(if (randomRange.random() > 5) -1 else 1)) { }
                in 11..20 -> workPlace(direction = currentDirection.withRotation90(if (randomRange.random() > 5) -1 else 1)) { }
                in 21..40 -> home(direction = currentDirection.withRotation90(if (randomRange.random() > 5) -1 else 1)) { }
                in 41..50 -> travelHub(currentDirection.withRotation90(if (randomRange.random() > 5) -1 else 1) * 20f) { }
                else -> this
            }
        }
    }
}


