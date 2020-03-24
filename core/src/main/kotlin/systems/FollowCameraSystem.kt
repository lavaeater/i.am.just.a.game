package systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.MathUtils
import components.TransformComponent
import ktx.ashley.mapperFor
import factory.ActorFactory
import injection.Injector
import ktx.math.vec2

class FollowCameraSystem(private val camera:Camera) : EntitySystem(300) {
	//Lets just add another character
	private val trackedEntity by lazy { Injector.inject<ActorFactory>().addNpcAt("Steve", vec2(1f,1f)) }
	private lateinit var transformComponent: TransformComponent
	private var needsInit = true
	private val speed = 0.2f

	override fun update(deltaTime: Float) {
		if (needsInit) {
			transformComponent = mapperFor<TransformComponent>()[trackedEntity]
			needsInit = false
		}
		camera.position.x = MathUtils.lerp(camera.position.x, transformComponent.position.x, speed)
		camera.position.y = MathUtils.lerp(camera.position.y, transformComponent.position.y, speed)
		camera.update(true)
	}
}