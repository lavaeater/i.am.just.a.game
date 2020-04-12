package systems

import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import ktx.math.ImmutableVector2
import screens.*

class BuilderSystem(private val buildToggler: IToggleBuilds,
                    private val camera: OrthographicCamera) : IntervalSystem(0.5f) {
    private var currentNode: MapNode? = null
    private var startedBuilding = false
    private val directions = setOf(ImmutableVector2.X, -ImmutableVector2.X, ImmutableVector2.Y, -ImmutableVector2.Y)
    private var currentDirection = directions.random()

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

        //What does this function return, really? The last node? The first node? Aaah?
        val displaceRange = 100f..200f

        //We just want to add ONE child per iteration, so let's make that happen later, when this bit actually works...


        if (currentNode?.hasLabel("TravelHub") == true) {
            //1. Pick a random direction
            currentDirection = currentDirection.withRotation90(1)
            currentNode?.street((10..25).random(), direction = currentDirection) {
                home(direction = currentDirection.withRotation90(1)) {
                    currentNode = this
                }
                home(direction = currentDirection.withRotation90(-1)) {
                    currentNode = this
                }
            }
        }


        if (currentNode?.hasLabel("Home") == true) {
            currentDirection = currentDirection.withRotation90(1)
            currentNode?.street(5, direction =  currentDirection) {
                currentNode = this
            }
        }


        if (currentNode?.hasLabel("Restaurant") == true || currentNode?.hasLabel("WorkPlace") == true) {
            currentNode?.street(1, direction = currentDirection) {
                currentDirection = currentDirection.withRotation90(1)
                travelHub(currentDirection * 20f) {
                    currentNode = this
                }
            }
        }


        val restaurant = (1..2).random() == 1
        if (currentNode?.hasLabel("Street") == true) {
            currentDirection = currentDirection.withRotation90(1)
            currentNode?.street((5..9).random(), direction = currentDirection) {
                if (restaurant)
                    restaurant(direction = currentDirection) {
                        currentNode = this
                    }
                else
                    workPlace(direction = currentDirection) {
                        currentNode = this
                    }
            }
        }


        val data = currentNode?.data
        camera.position.x = data?.x ?: 0f
        camera.position.y = data?.y ?: 0f
        //What is current node after a few iterations? Will it ever be something other than street / travelhub?

        Mgo.updateDrawableRelations()

    }


}