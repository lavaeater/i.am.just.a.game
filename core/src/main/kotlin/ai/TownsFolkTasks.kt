package ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.math.MathUtils
import data.Npc
import ktx.log.info

class GoAbout: LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    return if (`object`.onMyWay && !`object`.reachedDestination) {
      info { "${`object`.name} is on his way to ${`object`.thisIsWhereIWantToBe}"}
      Status.RUNNING
    } else {
      if (`object`.moveSomeWhere()) {
        info { "${`object`.name} is now going somewhere" }
        Status.RUNNING
      } else {
        info { "${`object`.name} has reached his goal: ${`object`.thisIsWhereIWantToBe}"}
        Status.SUCCEEDED
      }
    }
  }
}

class IsThisNpcContent : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    return if(`object`.content) {
      info { "${`object`.name} is totally, like, fine"}
      Task.Status.SUCCEEDED
    } else  {
      info { "${`object`.name} is no longer feeling too hot"}
      Task.Status.FAILED
    }
  }
}

class IsThisNpcHungry : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    return if(`object`.hangry) {
      info { "${`object`.name} is hungry"}
      Task.Status.SUCCEEDED
    } else {
      info { "${`object`.name} is not hungry"}
      Task.Status.FAILED
    }
  }
}

class IsThisNpcTired : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    return if(`object`.sleepy){
      info { "${`object`.name} is tired"}
      Task.Status.SUCCEEDED
    } else {
      info { "${`object`.name} is brisk as a lark"}
      Task.Status.FAILED
    }
  }
}

class IsThisNpcPoor : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    return if(`object`.povertyStricken) Task.Status.SUCCEEDED else Task.Status.FAILED
  }
}

class Eat : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`
    return if(npc.eat()) {
      info { "${`object`.name} is now eating"}
      Task.Status.RUNNING
    } else {
      info { "${`object`.name} is finished eating"}
      Task.Status.SUCCEEDED
    }
  }

}

class Sleep : LeafTask<Npc>() {
  override fun execute(): Status {
    val npc = `object`
    return if(npc.sleep())  {
      info { "${`object`.name} is now sleeping"}
      Task.Status.RUNNING
    } else {
      info { "${`object`.name} is done sleeping"}
     Status.SUCCEEDED
    }
  }

  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }
}



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