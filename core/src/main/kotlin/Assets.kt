import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable
import data.GameSettings


/**
 * Created by barry on 12/9/15 @ 11:17 PM.
 */
object Assets : Disposable {
    lateinit var gameSettings: GameSettings
    lateinit var am: AssetManager

        fun load(gameSettings: GameSettings): AssetManager {
        Assets.gameSettings = gameSettings
        am = AssetManager()

        return am
    }

    val sprites by lazy {
        mapOf("man" to TextureAtlas(Gdx.files.internal("sprites/man/man.txp")))
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}
