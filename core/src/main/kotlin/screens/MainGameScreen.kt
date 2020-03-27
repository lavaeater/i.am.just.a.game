package screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
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
        for(i in 0..100) {
            MasterGameObjectWithStuff.npcs.add(actorFactory.addNpcAt(position =  vec2(r.random(), r.random())).first)
        }
        needsInit = false
    }
}

object MasterGameObjectWithStuff {
    val npcs= mutableListOf<Npc>()
    val cr = 0f amid 500f
    val workPlaces = mapOf(
            "ICA" to vec2(cr.random(), cr.random()),
            "Coop" to vec2(cr.random(), cr.random()),
            "Vironova" to vec2(cr.random(), cr.random()),
            "Dell" to vec2(cr.random(), cr.random()),
            "ABB" to vec2(cr.random(), cr.random()),
            "Beamon People" to vec2(cr.random(), cr.random()),
            "Stockholms Universitet" to vec2(cr.random(), cr.random()),
            "KTH" to vec2(cr.random(), cr.random()),
            "WHO" to vec2(cr.random(), cr.random()),
            "Folkhälsomyndigheten" to vec2(cr.random(), cr.random()),
            "DN" to vec2(cr.random(), cr.random()),
            "SVD" to vec2(cr.random(), cr.random()),
            "SVT" to vec2(cr.random(), cr.random()),
            "SR" to vec2(cr.random(), cr.random()),
            "Asus" to vec2(cr.random(), cr.random()),
            "Microsoft" to vec2(cr.random(), cr.random()),
            "Hermods" to vec2(cr.random(), cr.random()),
            "Mitsubishi" to vec2(cr.random(), cr.random()),
            "Mieli AB" to vec2(cr.random(), cr.random()),
            "Regeringen" to vec2(cr.random(), cr.random()))


    val restaurants = mapOf(
            "McDonalds" to vec2(cr.random(), cr.random()),
            "KB" to vec2(cr.random(), cr.random()),
            "Akropolis" to vec2(cr.random(), cr.random()),
            "Pinchos" to vec2(cr.random(), cr.random()))
}