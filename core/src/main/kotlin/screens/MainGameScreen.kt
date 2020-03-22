package screens

import ktx.app.KtxScreen

class MainGameScreen : KtxScreen {
//    private val gameManager by lazy { Ctx.context.inject<GameManager>() }
//    private val gameState by lazy { Ctx.context.inject<GameState>() }
//    private val hud by lazy {Ctx.context.inject<IUserInterface>() }

    init {
//        Gdx.input.inputProcessor = Ctx.context.inject()
    }

    private fun update(delta:Float) {
//        gameManager.update(delta)
//        hud.update(delta)
    }

    override fun show() {
//        gameState.start()
    }

    override fun render(delta: Float) {
        update(delta)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
//        gameManager.resize(width, height)
    }

    override fun pause() {
//        gameManager.pause()
    }

    override fun dispose() {
        super.dispose()
//        gameManager.dispose()
    }
}