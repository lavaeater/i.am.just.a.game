package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import components.CharacterSpriteComponent
import components.TransformComponent
import components.VisibleComponent
import ktx.app.use
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class RenderSystem(private val batch: Batch) :
        SortedIteratingSystem(
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
    val sprite = Sprite() // Assets.tileSprites[spriteComponent.spriteKey]!!.entries.first().value //Just to test it
    sprite.setPosition(transform.position.x - sprite.width / 2, transform.position.y - sprite.height / 3)

    sprite.draw(batch)
  }

  override fun update(deltaTime: Float) {
    forceSort()
    batch.use {
      super.update(deltaTime)
    }
  }

}