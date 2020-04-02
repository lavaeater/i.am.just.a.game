package factory

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import components.*
import data.Npc
import getBehaviorTree
import ktx.math.vec2

class ActorFactory(
		private val engine: Engine,
		private val bodyFactory: BodyFactory) {

  val names = listOf("Lars", "Sven", "Steve", "Harry", "James", "Carl", "Gene", "Alan", "Fredrick the Great", "Ivan", "Hal", "George", "Louis", "Felix")
  private fun randomNpcName() : String {

    return names.random()
  }


  fun addNpcAt(name: String = randomNpcName(), rect: Rectangle): Pair<Npc, Entity> {
    val npc = Npc(name, getNpcId(name),rect)

    npcByKeys[npc.id] = npc

    val entity = engine.createEntity().apply {
      add(TransformComponent())
      add(AiComponent(npc.getBehaviorTree()))
      add(NpcComponent(npc))
      add(CharacterSpriteComponent("man"))
      add(VisibleComponent())
      add(Box2dBodyComponent(createNpcBody(vec2(rect.x, rect.y), npc)))
    }
    engine.addEntity(entity)
    return Pair(npc, entity)
  }
  
  private fun createNpcBody(position: Vector2, npc: Npc) : Body {
    return bodyFactory.createBody(0.8f, 1.6f, 15f, position, BodyDef.BodyType.DynamicBody)
        .apply { userData = npc }
  }

  companion object {
    val npcByKeys = mutableMapOf<String, Npc>()
    var npcIds: Int = 0
    fun nextCharacterId():Int {
      return npcIds++
    }

    fun getNpcId(name:String):String {
      return "${name}_${nextCharacterId()}"
    }
  }
}


//fun Npc.getBehaviorTree() : BehaviorTree<Npc> {

//  val reader = Assets.readerForTree("orc.tree") else Assets.readerForTree("townfolk.tree")
//  val parser = BehaviorTreeParser(BehaviorTreeParser.DEBUG_NONE)
//  return parser.parse(reader, this)
//}