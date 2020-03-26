package ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import data.Npc
import data.States
import ktx.log.info

class GoAbout: LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    `object`.goSomeWhere()
    return when(`object`.npcState) {
      States.MovingAbout -> Status.RUNNING
      States.Neutral -> Status.SUCCEEDED
      else -> Status.FAILED
    }
  }
}

class IsThisNpcNeutral : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    return if(`object`.npcState == States.Neutral) {
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
    return if (`object`.npcState == States.Hungry) Status.SUCCEEDED else Status.FAILED
  }
}

class Eat : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`

    /*
    How will all this work, really?
     */

    npc.startEating()
    return when (npc.npcState) {
        States.Eating -> Status.RUNNING
        States.Neutral -> Status.SUCCEEDED
        else -> Status.FAILED //What does failure even mean here?
    }
  }
}

class Sleep : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`

    /*
    How will all this work, really?
     */

    npc.startSleeping()
    return when (npc.npcState) {
      States.Sleeping -> Status.RUNNING
      States.Neutral -> Status.SUCCEEDED
      else -> Status.FAILED //What does failure even mean here?
    }
  }
}

class IsThisNpcTired : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    return if(`object`.npcState == States.Tired) Status.SUCCEEDED else Status.FAILED
  }
}

class IsThisNpcPoor : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    return if(`object`.npcState == States.Poor) Status.SUCCEEDED else Status.FAILED
  }
}

class Work : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`
    npc.startWorking()
    return when (npc.npcState) {
      States.Working -> Status.RUNNING
      States.Neutral -> Status.SUCCEEDED
      else -> Status.FAILED //What does failure even mean here?
    }
  }
}

//import poor:"ai.IsThisNpcBored"
//import work:"ai.HaveFun"

class IsThisNpcBored : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    return if(`object`.npcState == States.Bored) Status.SUCCEEDED else Status.FAILED
  }
}

class HaveFun : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`
    npc.startHavingFun()
    return when (npc.npcState) {
      States.HavingFun -> Status.RUNNING
      States.Neutral -> Status.SUCCEEDED
      else -> Status.FAILED //What does failure even mean here?
    }
  }
}


//import poor:"ai.IsThisNpcLonely"
//import work:"ai.Socialize"

class IsThisNpcLonely : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    return if(`object`.npcState == States.Lonely) Status.SUCCEEDED else Status.FAILED
  }
}

class Socialize : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`
    npc.startSocializing()
    return when (npc.npcState) {
      States.Socializing -> Status.RUNNING
      States.Neutral -> Status.SUCCEEDED
      else -> Status.FAILED //What does failure even mean here?
    }
  }
}

