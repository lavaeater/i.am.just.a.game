package screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import data.Npc
import factory.ActorFactory
import ktx.app.KtxScreen
import ktx.math.amid
import ktx.math.random
import ktx.math.vec2
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

    override fun dispose() {
        super.dispose()
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

    private var needsInit = true
    private fun initializeGame() {
        val r = 0f amid 64f
        for(i in 0..1) {
            Mgo.npcs.add(actorFactory.addNpcAt(position =  vec2(r.random(), r.random())).first)
        }
        needsInit = false
    }
}

class Mgo {
    companion object Mgo {
        val npcs = mutableListOf<Npc>()
        val workPlaceRange = 100f amid 200f
        val restaurantRange = 0f amid 100f
        val homeXRange = -100f amid 200f
        val homeYRange = -100f amid 200f

        val sizeRange = 5f..10f

        val workPlaceSize = vec2(20f, 10f)
        val restaurantSize = vec2(10f, 5f)

        val workPlaces = (1..50).map {
            Place(box = Rectangle(workPlaceRange.random(), workPlaceRange.random(), sizeRange.random(), sizeRange.random()))
        }

        val restaurants = (1..25).map {
            Place(type = PlaceType.Restaurant, box = Rectangle(workPlaceRange.random(), workPlaceRange.random(), sizeRange.random(), sizeRange.random()))
        }

        val homeAreas = (1..100).map {
            Place(type = PlaceType.Home, box = Rectangle(homeXRange.random(), homeYRange.random(), sizeRange.random(), sizeRange.random())) }

        val allPlaces = workPlaces + restaurants + homeAreas
    }
}
fun Vector2.pointIsInside(size: Vector2, point: Vector2):Boolean {
    return point.x < this.x + size.x / 2 && point.x > this.x -size.x && point.y < this.y + size.y / 2 && point.y > this.y - size.y
}

enum class PlaceType {
    Workplace,
    Restaurant,
    Home,
    Tivoli
}

data class Place(val name: String = "Don't matter", val type: PlaceType = PlaceType.Workplace, val box: Rectangle = Rectangle(0f,0f,10f, 10f))