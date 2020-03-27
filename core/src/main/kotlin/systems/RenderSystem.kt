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
import data.NpcDataAndStuff
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.graphics.use
import ktx.math.amid
import ktx.math.random
import screens.MasterGameObjectWithStuff

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

    if(npcMapper.has(entity)) {
      val npc = npcMapper[entity].npc

      val needSpritesToDraw = npc.npcNeeds.map {
        it.need.toString() to Assets.sprites["needs"]!![it.need.toString()] ?: error("No sprite found for ${it.need}") }.forEachIndexed { index, spriteAndKey ->
        if(NpcDataAndStuff.statesToNeeds[npc.npcState] == spriteAndKey.first) {
          spriteAndKey.second?.setScale(scaleAmount.random())
        }
        spriteAndKey.second?.setPosition(x + manSprite.width, y + manSprite.height - (index * spriteAndKey.second?.height!!))
        spriteAndKey.second?.draw(batch)
        spriteAndKey.second?.setScale(1f)
      }
    }

  }

  override fun update(deltaTime: Float) {
    forceSort()
    camera.update(true)
    batch.projectionMatrix = camera.combined

    shapeRenderer.projectionMatrix = camera.combined
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
      MasterGameObjectWithStuff.workPlaces.values.forEach  { shapeRenderer.rect(it.x -10f, it.y - 5f, 20f, 10f) }
    shapeRenderer.end()


    batch.use {
      //Draw workplaces


      super.update(deltaTime)
    }
  }
}