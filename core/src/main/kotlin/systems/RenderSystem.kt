package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import components.CharacterSpriteComponent
import components.TransformComponent
import components.VisibleComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.graphics.use

class RenderSystem(
        private val batch: Batch,
        private val camera: Camera) : SortedIteratingSystem(
                allOf(CharacterSpriteComponent::class,
                        TransformComponent::class,
                        VisibleComponent::class
                ).get(),
                EntityYOrderComparator(),
                5) {
  private val transformMapper = mapperFor<TransformComponent>()
  private val spriteMapper = mapperFor<CharacterSpriteComponent>()

  override fun processEntity(entity: Entity?, deltaTime: Float) {
    val transform = transformMapper[entity]
    val spriteComponent = spriteMapper[entity]

    //TODO: Fix sprites
    val sprite = Assets.sprites[spriteComponent.spriteKey]!!.entries.first().value
    sprite.setPosition(transform.position.x - sprite.width / 2, transform.position.y - sprite.height / 3)

    sprite.draw(batch)
  }

  override fun update(deltaTime: Float) {
    forceSort()
    batch.projectionMatrix = camera.combined
    camera.update(true)

    batch

    batch.use {
      super.update(deltaTime)
    }
  }
}