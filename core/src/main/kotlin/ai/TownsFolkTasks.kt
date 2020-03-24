package ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.math.MathUtils
import components.Npc

class Scavenge : LeafTask<Npc>() {
    override fun execute(): Status {
      val npc = `object`
      if(npc.scavenge()) {
        return Task.Status.SUCCEEDED
      }

      return Task.Status.FAILED
    }

    override fun copyTo(task: Task<Npc>?): Task<Npc> {
      return task!!
    }
}

class LostInterest : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    return task!!
  }

  val probability = 0.25f
  override fun execute(): Status {
    if(MathUtils.random() < probability) {
      //Change desired tile type to something other than we have now!
      val npc = `object`
      npc.lostInterest()
      return Task.Status.FAILED
    }
    return Task.Status.SUCCEEDED
  }

}

class Wander : LeafTask<Npc>() {
  override fun execute(): Status {
    val npc = `object`
    if(npc.wander())
        return Status.SUCCEEDED //As long as the npc is wandering, we keep wandering. The npccontrol will find a tile and when there, will change to idle and then we fail!
    return Status.FAILED
  }

  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    return task!!
  }

}

class WalkTo : LeafTask<Npc>() {
  override fun execute(): Status {
    val npc = `object`
    if(npc.walkTo())
      return Status.SUCCEEDED
    return Status.FAILED
  }

  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    return task!!
  }

}