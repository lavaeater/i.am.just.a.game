package ui

import Assets
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.scene2d.KTableWidget
import ktx.scene2d.label
import ktx.scene2d.table
import data.CoronaStats
import systems.AiAndTimeSystem

class UserInterface(
    private val batch: Batch,
    debug: Boolean = false,
 private val coronaStuff: Boolean = true): IUserInterface {

  override val hudViewPort = ExtendViewport(uiWidth, uiHeight, OrthographicCamera())
  override val stage = Stage(hudViewPort, batch)
      .apply {
    isDebugAll = debug
  }

  companion object {
    private const val aspectRatio = 16 / 9
    const val uiWidth = 800f
    const val uiHeight = uiWidth * aspectRatio
  }

  private val labelStyle = Label.LabelStyle(Assets.standardFont, Color.WHITE)
  private lateinit var infoLabel: Label
  private lateinit var npcLabel: Label

  init {
    setup()
  }

  override fun update(delta: Float) {
    batch.projectionMatrix = stage.camera.combined
    if(coronaStuff)
      updateCoronaInfo()

    showCurrentNpcInfo()
    stage.act(delta)
    stage.draw()
  }

  private fun updateCoronaInfo() {
    infoLabel.setText("""
Date and time: ${AiAndTimeSystem.currentDateTime}
Susceptible: ${CoronaStats.susceptible}      
Infected: ${CoronaStats.infected}
Recovered: ${CoronaStats.recovered}
Dead: ${CoronaStats.dead}
Asymptomatic: ${CoronaStats.asymptomatic}
Symptomatic staying home: ${CoronaStats.symptomaticThatStayAtHome}
    """)
  }

  private fun showCurrentNpcInfo() {
    if(CoronaStats.currentNpc != null){
      npcLabel.setText(CoronaStats.currentNpc.toString())
    } else {
      npcLabel.setText("No current NPC selected")
    }
  }

  override fun dispose() {
    stage.dispose()
  }

  override fun clear() {
    stage.clear()
  }


  private fun setup() {
    stage.clear()

    setupCoronaStats()
  }

  private lateinit var rootTable: KTableWidget

  private lateinit var infoBoard: KTableWidget

  private fun setupCoronaStats() {
    infoBoard = table {
      label("""
Controls and stuff:
WASD                    -> Control camera
Left and Right          -> Switch NPC to follow
c                       -> Stop following NPC
z                       -> Center camera //stop complaining
u, j                    -> zoom in and out
k, l                    -> rotate camera
r                       -> Reset Sim
      """)
      infoLabel = label("InfoLabel")
      npcLabel = label("NpcInfo")
    }

    rootTable = table {
      setFillParent(true)
      bottom()
      left()
      add(infoBoard).expand().align(Align.bottomLeft)
      pad(10f)
    }

    stage.addActor(rootTable)
  }
}