package ui

import Assets
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.actors.keepWithinParent
import ktx.scene2d.KTableWidget
import ktx.scene2d.label
import ktx.scene2d.table
import systems.CoronaStats

class UserInterface(
    private val batch: Batch,
    debug: Boolean = false): IUserInterface {

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

  init {
    setup()
  }

  override fun update(delta: Float) {
    batch.projectionMatrix = stage.camera.combined
    updateCoronaInfo()
    stage.act(delta)
    stage.draw()
  }

  private fun updateCoronaInfo() {
    infoLabel.setText("""
Susceptible: ${CoronaStats.susceptible}      
Infected: ${CoronaStats.infected}
Recovered: ${CoronaStats.recovered}
Dead: ${CoronaStats.dead}
Asymptomatic: ${CoronaStats.asymptomatic}
Symptomatic staying home: ${CoronaStats.symptomaticThatStayAtHome}
    """.trimIndent())
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

  private lateinit var scoreBoard: KTableWidget

  private fun setupCoronaStats() {
    scoreBoard = table {
      label("""
Controls and stuff:
WASD                  -> Control camera
Arrow Left and right  -> Switch NPC to follow
c                     -> Stop following NPC
z                     -> Center camera //stop complaining
u, j                  -> zoom in and out
k, l                  -> rotate camera

      """.trimIndent())
      infoLabel = label("InfoLabel") {
        setWrap(true)
        keepWithinParent()
      }.cell(fill = true, align = Align.bottomLeft, padLeft = 16f, padBottom = 2f)
      isVisible = true
      pack()
      width = 300f

    }

    rootTable = table {
      setFillParent(true)
      bottom()
      left()
      add(scoreBoard).expand().align(Align.bottomLeft)
    }

    stage.addActor(rootTable)
  }
}