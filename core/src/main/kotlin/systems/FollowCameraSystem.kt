package systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.MathUtils
import components.TransformComponent
import injection.Ctx
import ktx.ashley.mapperFor
import factory.ActorFactory

class FollowCameraSystem(private val camera:Camera) : EntitySystem(300) {
	private val trackedEntity by lazy { Ctx.context.inject<ActorFactory>().addHeroEntity() }
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