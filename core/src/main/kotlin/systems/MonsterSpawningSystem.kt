package systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.math.MathUtils
import components.PlayerComponent
import components.TransformComponent
import factory.ActorFactory
import map.IMapManager
import ktx.ashley.allOf
import ktx.ashley.mapperFor

class MonsterSpawningSystem(
		val areWeTesting:Boolean,
		private val actorFactory: ActorFactory,
		private val mapManager: IMapManager) : IntervalIteratingSystem(allOf(PlayerComponent::class).get(), 5f) {

  var weHaveSpawned = false
  val transformMpr = mapperFor<TransformComponent>()
  val spawnProb = 85

  val spawningProbs = mapOf(
      "grass" to
          mapOf(0..15 to "sneakypanther",
              16..85 to "orc"),
      "desert" to mapOf(0..15 to "snake",
          16..85 to "orc"))


  override fun processEntity(entity: Entity) {
    /*
    For every type of tile within some radius from the player, for every 10 seconds, there is some chance of a creature being spawned.

    The creature will have some sort of behavior tree and it will perhaps be dangerous.

    Some creatures can use stealth, etc.

    The players sight radius is not unlimited.

    This system only deals with spawning random monsters that might show up basically anywhere as
    autonomous beings that run around.

    There should be some kind of cap on the amount of monsters at the same time

     */

    if (areWeTesting) {

      if(!weHaveSpawned) {
//We need to spawn a fool!
        val position = transformMpr[entity].position.toTile()

        // We get a ring of tiles instead of an area

        actorFactory.addNpcAtTileWithAnimation(type = "orc", x = position.first + 1, y = position.second + 1, spriteKey = "orc")

        weHaveSpawned = true
      }

    } else {

      if (MathUtils.random(100) < spawnProb) {


        //We need to spawn a fool!
        val position = transformMpr[entity].position.toTile()

        // We get a ring of tiles instead of an area

        val someTilesInRange = mapManager.getBandOfTiles(position, 3, 3).filter {
          it.tile.tileType != "rock" && it.tile.tileType != "water"
        }

        val randomlySelectedTile = someTilesInRange[MathUtils.random(0, someTilesInRange.count() - 1)]

        val dieRoll = MathUtils.random(100)
        val npcType = spawningProbs[randomlySelectedTile.tile.tileType]!!.filterKeys { it.contains(dieRoll) }.values.firstOrNull()
        if (npcType != null) {
          if (npcType == "orc")
            actorFactory.addNpcAtTileWithAnimation(type = npcType, x = randomlySelectedTile.x, y = randomlySelectedTile.y, spriteKey = "saleswomanblonde")
          else
            actorFactory.addNpcEntityAtTile(type = npcType, x = randomlySelectedTile.x, y = randomlySelectedTile.y)
        }


        //Just try adding a townsfolk dude at that position - or rather, use a different type of NPC
        // but use the same graphic for now
        //The towndude we start with doesn't need to care about the terrain or nothin!

        /*
        We need some kind of structure for the probability of a certain type of npc to be instantiated.

        In the actor factory, obviously.

        No, here. This is where we do that.

         */
      }
    }
  }
}