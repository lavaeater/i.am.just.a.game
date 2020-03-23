package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import components.FeatureSpriteComponent
import components.TransformComponent
import components.VisibleComponent
import ktx.app.use
import ktx.ashley.allOf
import ktx.ashley.mapperFor

/*
TODO: This code needs to be moved into the render character system
to get valid y-order to keep people in front and back of each other correctly
 */

class RenderFeatureSystem(private val batch: Batch) :
    SortedIteratingSystem(
        allOf(FeatureSpriteComponent::class,
            TransformComponent::class,
            VisibleComponent::class).get(),
        EntityYOrderComparator(),
        10
    ) {

  private val transformMapper = mapperFor<TransformComponent>()
  private val spriteMapper = mapperFor<FeatureSpriteComponent>()

  override fun processEntity(entity: Entity, deltaTime: Float) {
    val transform = transformMapper[entity]
    val spriteComponent = spriteMapper[entity]
    when(spriteComponent.animated) {
      true -> renderAnimatedFeature(transform, spriteComponent, deltaTime)
      false -> renderRegularFeature(transform, spriteComponent)
    }
  }

  private fun renderRegularFeature(transform: TransformComponent, spriteComponent: FeatureSpriteComponent) {
    val sprite = Assets.featureSprites[spriteComponent.spriteKey]!!.first() //Just to test it
    sprite.setPosition(transform.position.x - sprite.width / 2, transform.position.y - sprite.height / 3)

    sprite.draw(batch)
  }

  override fun update(deltaTime: Float) {
    forceSort()
    batch.use {
      super.update(deltaTime)
    }
  }

  private fun renderAnimatedFeature(transform: TransformComponent, spriteComponent: FeatureSpriteComponent, deltaTime: Float) {
  }
}