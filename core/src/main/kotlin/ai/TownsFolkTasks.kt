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
      Status.RUNNING
    } else {
      if (`object`.moveSomeWhere()) {
        info { "${`object`.name} is walking to ${`object`.thisIsWhereIWantToBe}"}
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
      Status.SUCCEEDED
    } else  {
      info { "${`object`.name} is no longer feeling too hot"}
      Status.FAILED
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

class RobSomeone : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`
    npc.tryToRobSomeone()

    return if(npc.robbing) {
      info { "${`object`.name} is now trying to rob someone"}
      Status.RUNNING
    } else if(npc.robberySucceeded ) {
      info { "${`object`.name} managed to rob someone"}
      Status.SUCCEEDED
    } else {
      info { "${`object`.name} failed at robbery"}
      npc.stopRobbing()
      Status.FAILED
    }
  }
}

class CommitSuicide : LeafTask<Npc>() {
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