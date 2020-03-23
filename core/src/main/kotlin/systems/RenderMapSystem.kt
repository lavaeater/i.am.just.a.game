package systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.app.use
import managers.GameManager
import map.IMapManager
import map.TileFog
import kotlin.math.roundToInt

class RenderMapSystem(
    private val batch: Batch,
    private val camera: Camera,
    private val mapManager: IMapManager,
    val fogOfWar:Boolean = true) : EntitySystem(0) {

  override fun update(deltaTime: Float) {
    super.update(deltaTime)
    if(!fogOfWar) {
      val tileX = camera.position.tileX()
      val tileY = camera.position.tileY()

      batch.projectionMatrix = camera.combined
      batch.use {

        val tilesToRender = mapManager.getVisibleTiles(tileX, tileY)

        for (tileInstance in tilesToRender) {

          val xPos = (tileInstance.x * 8).toFloat()
          val yPos = (tileInstance.y * 8).toFloat()

          tileInstance.baseSprite.setPosition(xPos, yPos)
          tileInstance.baseSprite.draw(batch)
          for (extraSprite in tileInstance.extraSprites) {
            extraSprite.setPosition(xPos, yPos)
            extraSprite.draw(batch)
          }
        }
      }
    } else {
      renderMapWithFogOfWar()
    }
   }

  fun renderMapWithFogOfWar() {
    val tileX = camera.position.tileX()
    val tileY = camera.position.tileY()

    batch.projectionMatrix = camera.combined
    batch.use {

      for(tile in mapManager.getVisibleTilesWithFog(tileX, tileY, 8)) {

        val xPos = tile.x * 8f
        val yPos = tile.y * 8f

        tile.baseSprite.setPosition(xPos, yPos)
        when(tile.fogStatus) {
          TileFog.NotSeen -> tile.baseSprite.color = Color.BLACK
          TileFog.Seen -> tile.baseSprite.color = Color.GRAY
          TileFog.Seeing -> tile.baseSprite.color = Color.WHITE
        }
        tile.baseSprite.draw(batch)
          for (extraSprite in tile.extraSprites) {
            extraSprite.setPosition(xPos, yPos)
            when(tile.fogStatus) {
              TileFog.NotSeen -> extraSprite.color = Color.BLACK
              TileFog.Seen -> extraSprite.color = Color.GRAY
              TileFog.Seeing -> extraSprite.color = Color.WHITE
            }
            extraSprite.draw(batch)
          }
      }
    }
  }
}

fun Vector2.tileX(factor: Int = GameManager.TILE_SIZE): Int {
  return (this.x / factor).roundToInt()
}

fun Vector2.toTile(factor: Int = GameManager.TILE_SIZE):Pair<Int,Int> {
  return Pair(tileX(factor), tileY(factor))
}

fun Vector3.toTile(factor: Int = GameManager.TILE_SIZE):Pair<Int,Int> {
  return Pair(tileX(factor), tileY(factor))
}

fun Vector2.tileY(factor: Int = GameManager.TILE_SIZE): Int {
  return (this.y / factor).roundToInt()
}

fun Vector3.tileX(factor: Int = GameManager.TILE_SIZE): Int {
  return (this.x / factor).roundToInt()
}

fun Vector3.tileY(factor: Int = GameManager.TILE_SIZE): Int {
  return (this.y / factor).roundToInt()
}