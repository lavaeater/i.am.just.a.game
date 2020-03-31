package screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.viewport.Viewport
import data.CoronaStatus
import factory.ActorFactory
import ktx.app.KtxScreen
import ktx.math.*
import screens.Mgo.Companion.npcs
import screens.Mgo.Companion.travelHubs
import systems.GameInputSystem
import systems.RenderSystem

class MainGameScreen(
        inputProcessor: InputProcessor,
        private val batch: Batch,
        private val viewPort: Viewport,
        private val engine: Engine,
        private val camera: Camera,
        private val actorFactory: ActorFactory) : KtxScreen {

    init {
        Gdx.input.inputProcessor = inputProcessor
    }

    private fun update(delta:Float) {
        engine.update(delta)
    }

    override fun show() {
        if (needsInit)
            initializeGame()
    }

    override fun render(delta: Float) {
        update(delta)
    }

    override fun resize(width: Int, height: Int) {
        viewPort.update(width, height)
        batch.projectionMatrix = camera.combined
    }

    override fun pause() {
//        gameManager.pause()

    }

    private fun stopTheWorld() {
        for (system in engine.systems.filter {
            it !is RenderSystem }) {
            system.setProcessing(false)
            if (system is GameInputSystem) {
                system.processInput = false
            }

        }
    }

    private val friendRange = 1..10
    private var needsInit = true
    private fun initializeGame() {
        val r = 0f amid 64f

        val infectionRisk = 5
        val dieRange = (1..100)

        for(i in 1..Mgo.numberOfNpcs) {
            val rect = Mgo.getRandomRectangle()
            val npc = actorFactory.addNpcAt(rect = rect).first
            npcs.add(npc)
            Mgo.homeAreas.add(npc.home)
            if(dieRange.random() < infectionRisk) {
                npc.coronaStatus = CoronaStatus.Infected
                npc.symptomatic = false
            }
        }


        //Set up some friends!
        for(npc in npcs) {
            for(i in 1..friendRange.random()) {
                //1. Find a friend
                val friend = npcs.random()
                //1. add both as friends to each other!
                npc.friends.add(friend)
                friend.friends.add(npc)
            }
        }

        //Set up some travel hubs
        val distanceBetweenHubs = 100
        val numberOfHubs = Mgo.homeCircle.circumference() / distanceBetweenHubs
        val angleBetweenHubs = 360f / numberOfHubs
        var currentAngle = 0f
        for(i in 0..numberOfHubs.toInt()) {
            val positionOfHub = ImmutableVector2.Y.withRotationDeg(currentAngle) * Mgo.homeCircle.radius
            Mgo.travelHubs.add(Place(type = PlaceType.TravelHub(), box = Rectangle(positionOfHub.x, positionOfHub.y, 10f, 10f)))
            currentAngle += angleBetweenHubs
        }

        Mgo.travelHubs.add(Place(type = PlaceType.TravelHub("Central"), box = Rectangle(-5f,-5f, 10f, 10f)))

        needsInit = false
    }
}
