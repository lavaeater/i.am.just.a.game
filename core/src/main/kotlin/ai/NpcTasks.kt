package ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import data.Needs
import data.Npc
import data.States
import ktx.log.info

class GoAbout: LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {

    /*
    Check if we can eliminate multiple calls to methods to start
    tasks - if they are already running. Could be nift
     */

    val npc = `object`
    npc.goSomeWhere()
    return when {
        npc.hasAnyNeeds() -> {
//          info { "${npc.name} has some needs and should stop moving about" }
          Status.FAILED
        }
        npc.npcState == States.OnTheMove -> Status.RUNNING
        else -> Status.SUCCEEDED
    }
  }
}

class IsThisNpcNeutral : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    val npc = `object`
    return when {
      npc.hasAnyNeeds() -> {
//        info { "${npc.name} has some needs and is absolutely not neutral anymore" }
        Status.FAILED
      }
      npc.npcState == States.Neutral -> Status.SUCCEEDED
      else -> Status.FAILED
    }
  }
}

class IsThisNpcTired : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //For this one we will simply check if needs contains sleepneed
    return if(`object`.hasNeed(Needs.Rest)) Status.SUCCEEDED else Status.FAILED
  }
}

class Sleep : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`

    npc.startSleeping()
    return when(npc.npcState == States.Sleeping && npc.hasNeed(Needs.Rest)) {
      true -> Status.RUNNING
      else -> {
        npc.stopSleeping()
        Status.SUCCEEDED
      } //What does failure even mean here?
    }
  }
}

class IsThisNpcHungry : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //For this one we will simply check if needs contains sleepneed
    return if(`object`.hasNeed(Needs.Fuel)) Status.SUCCEEDED else Status.FAILED
  }
}

class Eat : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`

    npc.startEating()
    return when(npc.npcState == States.Eating && npc.hasNeed(Needs.Fuel)) {
      true -> Status.RUNNING
      else -> {
        npc.stopEating()
        Status.SUCCEEDED
      } //What does failure even mean here?
    }
  }
}

class IsThisNpcPoor : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //For this one we will simply check if needs contains sleepneed
    return if(`object`.hasNeed(Needs.Money)) Status.SUCCEEDED else Status.FAILED
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
    return when(npc.npcState == States.Working && npc.hasNeed(Needs.Money)) {
      true -> Status.RUNNING
      else -> {
        npc.stopWorking()
        Status.SUCCEEDED
      } //What does failure even mean here?
    }
  }
}

class IsThisNpcBored : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //For this one we will simply check if needs contains sleepneed
    return if(`object`.hasNeed(Needs.Fun)) Status.SUCCEEDED else Status.FAILED
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
    return when(npc.npcState == States.HavingFun && npc.hasNeed(Needs.Fun)) {
      true -> Status.RUNNING
      else -> {
        npc.stopHavingFun()
        Status.SUCCEEDED
      } //What does failure even mean here?
    }
  }
}

class IsThisNpcLonely : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //For this one we will simply check if needs contains sleepneed
    return if(`object`.hasNeed(Needs.Social)) Status.SUCCEEDED else Status.FAILED
  }
}

class MeetPeople : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`

    npc.startSocializing()
    return when(npc.npcState == States.Socializing && npc.hasNeed(Needs.Social)) {
      true -> Status.RUNNING
      else -> {
        npc.stopSocializing()
        Status.SUCCEEDED
      } //What does failure even mean here?
    }
  }
}