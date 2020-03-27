package ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import data.Needs
import data.Npc
import data.States
import screens.Mgo
import screens.pointIsInside

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
        npc.hasAnyNeeds() -> Status.FAILED
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
      npc.hasAnyNeeds() ->  Status.FAILED
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

    if(npc.hasNeed(Needs.Rest))
      npc.startSleeping()

    return when(npc.npcState == States.Sleeping && npc.hasNeed(Needs.Rest)) {
      true -> Status.RUNNING
      else -> {
        npc.stopDoingIt()
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

class CanEat : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {

    val npc = `object`

    return if(Mgo.restaurants.any { it.value.pointIsInside(Mgo.restaurantSize, npc.currentPosition) })
      Status.SUCCEEDED
    else
      Status.FAILED
  }
}

class MoveToEat: LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {

    /*
    Check if we can eliminate multiple calls to methods to start
    tasks - if they are already running. Could be nift
     */
    val npc = `object`

    if(npc.npcState != States.GoingToEat && npc.currentPosition.dst(npc.thisIsWhereIWantToBe) > 5f) {
      val restaurant = Mgo.restaurants.values.minBy { it.dst(npc.currentPosition) }!!
      npc.goToEatAt(restaurant)
    }
    return when {
      npc.npcState == States.GoingToEat -> Status.RUNNING
      else -> Status.SUCCEEDED
    }
  }
}

class Eat : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {
    //Eating takes one hour and costs 100
    val npc = `object`

    if(npc.hasNeed(Needs.Fuel)) npc.startEating()

    return when(npc.npcState == States.Eating && npc.hasNeed(Needs.Fuel)) {
      true -> Status.RUNNING
      else -> {
        npc.stopDoingIt()
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

    if(npc.hasNeed(Needs.Money)) npc.startWorking()

    return when(npc.npcState == States.Working && npc.hasNeed(Needs.Money)) {
      true -> Status.RUNNING
      else -> {
        npc.stopDoingIt()
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

    if(npc.hasNeed(Needs.Fun))
      npc.startHavingFun()

    return when(npc.npcState == States.HavingFun && npc.hasNeed(Needs.Fun)) {
      true -> Status.RUNNING
      else -> {
        npc.stopDoingIt()
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

    if(npc.hasNeed(Needs.Social))
      npc.startSocializing()

    return when(npc.npcState == States.Socializing && npc.hasNeed(Needs.Social)) {
      true -> Status.RUNNING
      else -> {
        npc.stopDoingIt()
        Status.SUCCEEDED
      } //What does failure even mean here?
    }
  }
}

class CanWork : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {

    val npc = `object`

    return if(npc.currentPosition.dst(Mgo.workPlaces[npc.workplace]!!) < 3f)
      Status.SUCCEEDED
    else
      Status.FAILED
  }
}

class MoveToWork: LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {

    /*
    Check if we can eliminate multiple calls to methods to start
    tasks - if they are already running. Could be nift
     */
    val npc = `object`

    if(npc.npcState != States.GoingToWork && npc.currentPosition.dst(Mgo.workPlaces[npc.workplace]!!) > 5f) {
      npc.goToWork()
    }
    return when {
      npc.npcState == States.GoingToWork -> Status.RUNNING
      else -> Status.SUCCEEDED
    }
  }
}

class CanSleep : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {

    val npc = `object`

    return if(npc.currentPosition.dst(npc.homeCoord) < 3f)
      Status.SUCCEEDED
    else
      Status.FAILED
  }
}

class MoveToSleep
  : LeafTask<Npc>() {
  override fun copyTo(task: Task<Npc>?): Task<Npc> {
    TODO("Not yet implemented")
  }

  override fun execute(): Status {

    /*
    Check if we can eliminate multiple calls to methods to start
    tasks - if they are already running. Could be nift
     */
    val npc = `object`

    if(npc.npcState != States.GoingHomeToSleep && npc.currentPosition.dst(npc.homeCoord) > 5f) {
      npc.goHomeToSleep()
    }
    return when {
      npc.npcState == States.GoingHomeToSleep -> Status.RUNNING
      else -> Status.SUCCEEDED
    }
  }
}