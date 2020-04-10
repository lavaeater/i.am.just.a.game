package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import components.CameraFollowComponent
import components.KeyboardControlComponent
import components.NpcComponent
import data.CoronaStats
import data.CoronaStatus
import directionalVelocity
import ktx.app.KtxInputAdapter
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.ashley.remove
import ktx.math.vec2
import screens.Mgo
import java.util.*

class GameInputSystem(
        private val inputProcessor: InputProcessor,
        private val camera: OrthographicCamera,
        private val speed: Float = 20f) :
        KtxInputAdapter,
        IteratingSystem(allOf(NpcComponent::class).get(), 45) {

    var currentEntityIndex = 0

    private var pInput = true
    var processInput: Boolean
        get() = this.pInput
        set(value) {
            this.pInput = processInput
            if (value) {
                (inputProcessor as InputMultiplexer).addProcessor(this)
            } else {
                (inputProcessor as InputMultiplexer).removeProcessor(this)
            }
        }

    init {
        (inputProcessor as InputMultiplexer).addProcessor(this)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        ctrlBody?.linearVelocity = vec2(x, y).directionalVelocity(speed)
    }

    var y = 0f;
    var x = 0f
    private val kbCtrlMpr = mapperFor<KeyboardControlComponent>()
    private val npcComponentMapper = mapperFor<NpcComponent>()

    var ctrlId: UUID? = null
    var ctrlBody: Body? = null

    override fun keyDown(keycode: Int): Boolean {

        //Use keys to select which character to follow
        if (!processInput) return false
        when (keycode) {
            Input.Keys.A -> camera.position.x -= 10f
            Input.Keys.D -> camera.position.x += 10f
            Input.Keys.W -> camera.position.y += 10f
            Input.Keys.S -> camera.position.y -= 10f
            Input.Keys.LEFT -> nextNpc()
            Input.Keys.RIGHT -> previousNpc()
            Input.Keys.C -> clearFollow()
            Input.Keys.Z -> centerCamera()
            Input.Keys.R -> resetSim()
            Input.Keys.U -> zoom(0.5f)
            Input.Keys.J -> zoom(-0.5f)
            Input.Keys.K -> rotateCam(5f)
            Input.Keys.L -> rotateCam(-5f)
            Input.Keys.N -> nextHub()
            Input.Keys.M -> previousHub()
        }
        return true
    }

    var hubIndex = 0
    private fun nextHub() {
        clearFollow()
        camera.position.x = Mgo.graph.withLabels("")
    }

    private fun resetSim() {
        for (e in entities) {
            val n = npcComponentMapper[e].npc
            n.coronaStatus = CoronaStatus.Susceptible
            n.symptomatic = false
            n.iWillStayAtHome = false
        }
        CoronaStats.dead = 0
        CoronaStats.infected = 0
        CoronaStats.susceptible = entities.count { npcComponentMapper.has(it) }
        CoronaStats.asymptomatic = 0
        CoronaStats.recovered = 0
        CoronaStats.symptomaticThatStayAtHome = 0

        /*
        Set up some infected npcs!
         */
        val numberOfInfected = (5..15).random()
        for(i in 0..numberOfInfected) {
            npcComponentMapper[entities[i]].npc.coronaStatus = CoronaStatus.Infected
            npcComponentMapper[entities[i]].npc.infectionDate = AiAndTimeSystem.currentDateTime.toLocalDate()
        }
    }

    private fun centerCamera() {
        val maxX = Mgo.allPlaces.map { it.center.x }.max()!!
        val maxY = Mgo.allPlaces.map { it.center.y }.max()!!

        camera.position.x = maxX / 2
        camera.position.y = maxY / 2
    }

    fun clearFollow() {
        entities.firstOrNull { followMapper.has(it) }?.remove<CameraFollowComponent>()
        CoronaStats.currentNpc = null
    }

    val followMapper = mapperFor<CameraFollowComponent>()

    private fun previousNpc() {
        currentEntityIndex--
        if (currentEntityIndex < 0)
            currentEntityIndex = entities.indexOf(entities.last())

        val newEntityToFollow = entities.elementAt(currentEntityIndex)
        entities.filter { followMapper.has(it) }.forEach { it.remove<CameraFollowComponent>() }
        newEntityToFollow.add(CameraFollowComponent())
        CoronaStats.currentNpc = npcComponentMapper[newEntityToFollow]!!.npc
    }

    private fun nextNpc() {
        currentEntityIndex++
        if (currentEntityIndex > entities.size() - 1)
            currentEntityIndex = 0

        val newEntityToFollow = entities.elementAt(currentEntityIndex)
        entities.filter { followMapper.has(it) }.forEach { it.remove<CameraFollowComponent>() }

        newEntityToFollow.add(CameraFollowComponent())

        CoronaStats.currentNpc = npcComponentMapper[newEntityToFollow]!!.npc
    }

    private fun rotateCam(angle: Float) {
        camera.rotate(angle)
    }

    private fun zoom(zoom: Float) {
        camera.zoom += zoom
    }

    private fun touchYtoScreenY(y: Int): Int {
        return Gdx.graphics.height - 1 - y
    }

    private fun touchToVector(touchX: Int, touchY: Int): Vector2 {
        return vec2((Gdx.graphics.width / 2 - touchX).toFloat(), (Gdx.graphics.height / 2 - touchYtoScreenY(touchY)).toFloat()).nor()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (!processInput) return false

        val dirV = touchToVector(screenX, screenY)
        x = dirV.x
        y = dirV.y
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (!processInput) return false
        x = 0f
        y = 0f
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (!processInput) return false
        val dirV = touchToVector(screenX, screenY)

        x = dirV.x
        y = dirV.y
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        if (!processInput) return false
        when (keycode) {
            Input.Keys.A, Input.Keys.LEFT -> x = 0f
            Input.Keys.D, Input.Keys.RIGHT -> x = 0f
            Input.Keys.W, Input.Keys.UP -> y = 0f
            Input.Keys.S, Input.Keys.DOWN -> y = 0f
        }
        return true
    }
}