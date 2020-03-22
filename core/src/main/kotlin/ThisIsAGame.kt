import com.badlogic.gdx.Screen
import data.GameSettings
import ktx.app.KtxGame
import screens.MainGameScreen

class ThisIsAGame(private val gameSettings: GameSettings = GameSettings()) : KtxGame<Screen>() {

    private lateinit var mainGameScreen: MainGameScreen


    override fun create() {
//        Gdx.app.logLevel = Application.LOG_ERROR

        Assets.load(gameSettings)

//        VisUI.load(VisUI.SkinScale.X1)
//        Ctx.buildContext(gameSettings)
        mainGameScreen = MainGameScreen()
        addScreen(mainGameScreen)
        setScreen<MainGameScreen>()
    }

    override fun dispose() {
        super.dispose()
//        VisUI.dispose()
    }
}
