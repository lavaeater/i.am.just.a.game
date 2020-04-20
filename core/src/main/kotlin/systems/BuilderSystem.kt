package systems

import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Queue
import data.CoronaStatus
import data.Place
import data.PlaceType
import factory.ActorFactory
import injection.Injector
import ktx.math.ImmutableVector2
import ktx.math.random
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
    val nodeQueue = Queue<MapNode>()


    override fun updateInterval() {
        if (buildToggler.build)
            buildSomeStuff()
        else
            stopBuildingMaybe()
    }

    private fun stopBuildingMaybe() {
        startedBuilding = false
        currentNode = null
    }

    private fun findSuitableBuildArea() {
        /*
        Find a suitable area and start building. How?
         */
        val vectors = Mgo.graph.nodes.map { it.data }

        val minX = vectors.map { it.x }.min()!! - 100f
        val minY =vectors.map { it.y }.min()!! - 100f
        val maxX = vectors.map { it.x }.max()!! + 100f
        val maxY = vectors.map { it.y }.max()!! + 100f



        var foundAnArea = false
        val maxTries = 10
        var tries =0
        var centerX = 0f
        var centerY = 0f
        while(!foundAnArea && tries < maxTries) {
            tries++
            centerX = (minX..maxX).random()
            centerY = (minY..maxY).random()
            val potentialArea = Rectangle(centerX - 50f, centerY - 50f, 100f, 100f)
            foundAnArea = evaluateArea(potentialArea)
        }

        if(foundAnArea) {
            currentNode = node(ImmutableVector2(centerX, centerY), false) {
                addLabel("TravelHub")
                Mgo.allPlaces.add(Place(PlaceType.TravelHub, this))
            }
            nodeQueue.addLast(currentNode)
        }
    }

    private fun evaluateArea(area: Rectangle) : Boolean {
        return !Mgo.graph.nodes.map { it.data }.any { area.contains(it.x, it.y) }
    }

    private fun buildSomeStuff() {
        if (!startedBuilding) {
            startedBuilding = true

            //1. Find a random... travelhub?
//            currentNode = Mgo.graph.nodes.maxBy { it.data.x.absoluteValue + it.data.y.absoluteValue } as MapNode
            findSuitableBuildArea()
        }

        // Check if we need building right now

        if(nodeQueue.isEmpty) {
            buildNewBlock()
        } else {
            val nodeToAdd = nodeQueue.removeFirst()
            if(nodeToAdd.hasLabel("Home")) {
                val npc = Injector.inject<ActorFactory>().addNpcAt(Mgo.allPlaces.first { it.node == nodeToAdd })
                val dieRange = (1..100)
                val infectionRisk = 5
                if (dieRange.random() < infectionRisk) {
                    npc.coronaStatus = CoronaStatus.Infected
                    npc.symptomatic = false
                }
            }
            Mgo.addNodeToGraph(nodeToAdd)
        }


        val data = currentNode?.data
        if (data != null) {
            camera.position.lerp(Vector3(data.x, data.y, 0f), 0.5f)
        }
//        Mgo.updateDrawableRelations()
    }

    private fun buildNewBlock() {
        /*
        Thankfully our new strategy is Da Bomb. For now.

        So, we create a block using our normal builders, but without adding them to the graph.
        And then, after, we add them, one by one to the graph, for a cool effect.

        We start by NOT using anything random at all, static stuff, then we change everything up
        as we progress.
         */
        var rotationDirection = -1..1
        if(currentNode?.hasLabel("TravelHub") == true) {

            currentDirection = ImmutableVector2.X
            currentNode?.street(10, 40f, currentDirection, addNodeToGraph = false) {
                var sideStreetDirection = currentDirection.withRotation90(rotationDirection.random())
                nodeQueue.addLast(this)
                street(10, 20f, sideStreetDirection, addNodeToGraph = false) {
                    nodeQueue.addLast(this)
                    home(direction = sideStreetDirection.withRotation90(rotationDirection.random()), addNodeToGraph = false, addNpc = false) {
                        nodeQueue.addLast(this)
                        currentNode = this
                    }
                }
            }
        } else {
            /*
            Build something else as some kind of strange continuation of what we're doing.
             */
            currentNode = currentNode?.travelHub(displacement = ImmutableVector2.Y * 100f,  addNodeToGraph = false) {
                nodeQueue.addLast(this)
            }
        }
    }
}


