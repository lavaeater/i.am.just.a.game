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
import components.Box2dBodyComponent
import components.CameraFollowComponent
import components.KeyboardControlComponent
import components.NpcComponent
import ktx.app.KtxInputAdapter
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.ashley.remove
import ktx.math.vec2
import java.util.*
import ktx.log.info

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
      if(value) {
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
    if(!processInput) return false
    when (keycode) {
      Input.Keys.A, Input.Keys.LEFT -> nextNpc()
      Input.Keys.D, Input.Keys.RIGHT -> previousNpc()
      Input.Keys.W, Input.Keys.UP -> y = -1f
      Input.Keys.S, Input.Keys.DOWN -> y = 1f
      Input.Keys.U -> zoom(0.5f)
      Input.Keys.J -> zoom(-0.5f)
      Input.Keys.K -> rotateCam(5f)
      Input.Keys.L -> rotateCam(-5f)
    }
    return true
  }


  val followMapper = mapperFor<CameraFollowComponent>()

  private fun previousNpc() {
    currentEntityIndex--
    if(currentEntityIndex < 0 )
      currentEntityIndex = entities.indexOf(entities.last())

    val newEntityToFollow = entities.elementAt(currentEntityIndex)
    entities.filter { followMapper.has(it) }.forEach { it.remove<CameraFollowComponent>() }
    newEntityToFollow.add(CameraFollowComponent())
   }

  private fun nextNpc() {
    currentEntityIndex++
    if(currentEntityIndex > entities.size()-1)
      currentEntityIndex = 0

    val newEntityToFollow = entities.elementAt(currentEntityIndex)
    entities.filter { followMapper.has(it) }.forEach { it.remove<CameraFollowComponent>() }

    newEntityToFollow.add(CameraFollowComponent())
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

  private fun touchToVector(touchX: Int, touchY: Int):Vector2 {
    return vec2((Gdx.graphics.width / 2 - touchX).toFloat(), (Gdx.graphics.height / 2 - touchYtoScreenY(touchY)).toFloat()).nor()
  }

  override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
    if(!processInput) return false

    val dirV = touchToVector(screenX, screenY)
    x = dirV.x
    y = dirV.y
    return true
  }

  override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
    if(!processInput) return false
    x = 0f
    y = 0f
    return true
  }

  override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
    if(!processInput) return false
    val dirV = touchToVector(screenX, screenY)

    x = dirV.x
    y = dirV.y
    return true
  }

  override fun keyUp(keycode: Int): Boolean {
    if(!processInput) return false
    when (keycode) {
      Input.Keys.A, Input.Keys.LEFT -> x = 0f
      Input.Keys.D, Input.Keys.RIGHT -> x = 0f
      Input.Keys.W, Input.Keys.UP -> y = 0f
      Input.Keys.S, Input.Keys.DOWN -> y = 0f
    }
    return true
  }
}