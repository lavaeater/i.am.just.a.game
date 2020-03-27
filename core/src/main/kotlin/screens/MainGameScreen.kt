package screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
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
        for(i in 0..999) {
            Mgo.npcs.add(actorFactory.addNpcAt(position =  vec2(r.random(), r.random())).first)
        }
        needsInit = false
    }
}

object Mgo {
    val npcs= mutableListOf<Npc>()
    val workPlaceRange = 0f amid 200f
    val restaurantRange = 0f amid 100f
    val homeXRange = -100f amid 200f
    val homeYRange = -100f amid 200f

    val workPlaceSize = vec2(20f, 10f)
    val restaurantSize = vec2(10f, 5f)

    val workPlaces = mapOf(
            "ICA" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Coop" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Vironova" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Dell" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "ABB" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Beamon People" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Stockholms Universitet" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "KTH" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "WHO" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Folkhälsomyndigheten" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "DN" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "SVD" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "SVT" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "SR" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Asus" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Microsoft" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Hermods" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Mitsubishi" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Mieli AB" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Regeringen" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "AtlasCopco" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "IKEA" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "BB" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "QQ" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "CC" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "B People" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "asfasef" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "asdf" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "tjhtjh" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "67969" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "123123" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "4646" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "SjyjjyT" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "S456nn" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "ewfwef8" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Microsoft2" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Hermods123" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Mitsubishi123333" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Mieli AB 23423" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Regeringencasc  4234" to vec2(workPlaceRange.random(), workPlaceRange.random()))


    val restaurants = mapOf(
            "McDonalds" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "KB" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Akropolis" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Pinchos" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "KGB" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Loffes" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Burger King" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "McDonalds2" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "KB" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Akropolis4" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Pinchos1" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "KGB2" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Loffes4" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Burger King1" to vec2(workPlaceRange.random(), workPlaceRange.random()),
            "Whatevs4" to vec2(workPlaceRange.random(), workPlaceRange.random())
    )

    val homeAreas = (1..100).map { vec2(homeXRange.random(), homeYRange.random()) }
}

fun Vector2.pointIsInside(size: Vector2, point: Vector2):Boolean {
    return point.x < this.x + size.x / 2 && point.x > this.x -size.x && point.y < this.y + size.y / 2 && point.y > this.y - size.y
}