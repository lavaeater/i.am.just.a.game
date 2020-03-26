package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.MathUtils
import components.CameraFollowComponent
import components.NpcComponent
import components.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class FollowCameraSystem(private val camera:Camera) : IteratingSystem(allOf(CameraFollowComponent::class).get()) {
	private val speed = 1f

	private val transformMapper = mapperFor<TransformComponent>()
	private val npcMapper = mapperFor<NpcComponent>()

	private var accTime = 0f
	override fun processEntity(entity: Entity, deltaTime: Float) {
		accTime += deltaTime
		val transformComponent = transformMapper[entity]
		if(accTime > 2f) {
			accTime = 0f
			npcMapper[entity].npc.log()
		}
		camera.position.x = MathUtils.lerp(camera.position.x, transformComponent.position.x, speed)
		camera.position.y = MathUtils.lerp(camera.position.y, transformComponent.position.y, speed)
		camera.update(true)
	}
}