package the.game

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import data.GameSettings
import injection.Injector
import ktx.app.KtxGame
import screens.MainGameScreen

class ThisIsAGame(private val gameSettings: GameSettings = GameSettings()) : KtxGame<Screen>() {

    private lateinit var mainGameScreen: MainGameScreen


    override fun create() {
        Gdx.app.logLevel = Application.LOG_INFO

        Assets.load(gameSettings)

//        VisUI.load(VisUI.SkinScale.X1)
        Injector.buildContext(gameSettings)
        mainGameScreen = Injector.inject()
        addScreen(mainGameScreen)
        setScreen<MainGameScreen>()
    }

    override fun dispose() {
        super.dispose()
//        VisUI.dispose()
    }
}