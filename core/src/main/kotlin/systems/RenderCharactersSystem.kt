package systems

import Assets
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import components.CharacterSpriteComponent
import components.TransformComponent
import components.VisibleComponent
import ktx.app.use
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class RenderCharactersSystem(private val batch:Batch) :
    SortedIteratingSystem(
        allOf(CharacterSpriteComponent::class,
            TransformComponent::class,
            VisibleComponent::class
        ).get(),
        EntityYOrderComparator(),
        5) {
  private val transformMapper = mapperFor<TransformComponent>()
  private val spriteMapper = mapperFor<CharacterSpriteComponent>()

  override fun processEntity(entity: Entity, deltaTime: Float) {
    val transform = transformMapper[entity]
    val spriteComponent = spriteMapper[entity]
    when(spriteComponent.animated) {
      true -> renderAnimatedCharacter(transform, spriteComponent, deltaTime)
      false -> renderRegularCharacter(transform, spriteComponent)
    }
  }
  private val frameRate = 1f / 2f
  private fun renderAnimatedCharacter(transform: TransformComponent,
                                      spriteComponent: CharacterSpriteComponent,
                                      deltaTime: Float) {

    //Lets animate these at 12 frames per second, as a test.
    val spriteSet = Assets.animatedCharacterSprites[spriteComponent.spriteKey]!![spriteComponent.currentAnim]!!
    spriteComponent.deltaTime += deltaTime
    if(spriteComponent.deltaTime > frameRate)
    {
      spriteComponent.deltaTime = 0f
      val maxIndex = spriteSet.count() - 1
      spriteComponent.currentIndex++
      if(spriteComponent.currentIndex > maxIndex)
        spriteComponent.currentIndex = 0
    }
    val sprite = spriteSet[spriteComponent.currentIndex]

    sprite.setCenter(transform.position.x,
        transform.position.y + sprite.width / 4)

    batch.color = Color.BLACK

    batch.draw(sprite,
        sprite.x,
        sprite.y - sprite.height / 5,
        0f,
        0f,
        sprite.width,
        sprite.height,
        1f,
        .5f,
        30f)
    batch.color = Color.WHITE

    sprite.draw(batch)
  }

  private fun renderRegularCharacter(transform: TransformComponent, spriteComponent: CharacterSpriteComponent) {
    val sprite = Assets.tileSprites[spriteComponent.spriteKey]!!.entries.first().value //Just to test it
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

