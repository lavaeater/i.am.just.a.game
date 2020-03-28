package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import components.CharacterSpriteComponent
import components.NpcComponent
import components.TransformComponent
import components.VisibleComponent
import data.NeedsAndStuff
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.graphics.use
import ktx.math.amid
import ktx.math.random
import screens.Mgo
import screens.PlaceType

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
  private val npcMapper = mapperFor<NpcComponent>()

  private val shapeRenderer = ShapeRenderer()

  private val scaleAmount = 1f amid 0.5f

  override fun processEntity(entity: Entity?, deltaTime: Float) {
    val transform = transformMapper[entity]
    val spriteComponent = spriteMapper[entity]

    //TODO: Fix sprites
    val manSprite = Assets.sprites[spriteComponent.spriteKey]!!.entries.first().value
    val x = transform.position.x - manSprite.width / 2
    val y = transform.position.y - manSprite.height / 3
    manSprite.setPosition(x, y)
    manSprite.draw(batch)

//    if(npcMapper.has(entity)) {
//      val npc = npcMapper[entity].npc
//
////      val needSpritesToDraw = npc.npcNeeds.map {
////        it.key.toString() to Assets.sprites["needs"]!![it.key.toString()] ?: error("No sprite found for ${it.key}") }.forEachIndexed { index, spriteAndKey ->
////        if(NeedsAndStuff.statesToNeeds[npc.npcState] == spriteAndKey.first) {
////          spriteAndKey.second?.setScale(scaleAmount.random())
////        }
////        spriteAndKey.second?.setPosition(x + manSprite.width, y + manSprite.height - (index * spriteAndKey.second?.height!!))
////        spriteAndKey.second?.draw(batch)
////        spriteAndKey.second?.setScale(1f)
////      }
//    }

  }

  override fun update(deltaTime: Float) {
    forceSort()
    camera.update(true)
    batch.projectionMatrix = camera.combined

    shapeRenderer.projectionMatrix = camera.combined
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

    Mgo.allPlaces.forEach {
      when(it.type) {
        PlaceType.Home ->     shapeRenderer.setColor(1f, 1f, 0f, 0f)
        PlaceType.Workplace ->     shapeRenderer.setColor(0f, 0f, 1f, 0f)
        PlaceType.Restaurant ->     shapeRenderer.setColor(0f, 1f, 0f, 0f)
        PlaceType.Tivoli ->     shapeRenderer.setColor(1f, 0f, 0f, 0f)
      }
      shapeRenderer.rect(it.box.x, it.box.y, it.box.width, it.box.height)
    }
    shapeRenderer.end()


    batch.use {
      //Draw workplaces


      super.update(deltaTime)
    }
  }
}