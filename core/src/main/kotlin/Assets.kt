import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable
import data.GameSettings
import java.io.Reader


/**
 * Created by barry on 12/9/15 @ 11:17 PM.
 */
object Assets : Disposable {
    lateinit var gameSettings: GameSettings
    lateinit var am: AssetManager

        fun load(gameSettings: GameSettings): AssetManager {
            Assets.gameSettings = gameSettings
            am = AssetManager()
            fixSprites()

            return am
        }

    private fun fixSprites() {
        val atlas = textureAtlases["man"]!!
        sprites["man"] = mutableMapOf(
                "front" to atlas.createSprite("front").apply {
                    setSize(4f, 4.5f)
                    setOriginCenter()
                },
                "back" to atlas.createSprite("back").apply {
                    setSize(4f, 4.5f)
                    setOriginCenter()
                },
                "left" to atlas.createSprite("left").apply {
                    setSize(4f, 4.5f)
                    setOriginCenter()
                },
                "right" to atlas.createSprite("right").apply {
                    setSize(4f, 4.5f)
                    setOriginCenter()
                }
        )
    }

    private val textureAtlases by lazy {
        mapOf("man" to TextureAtlas(Gdx.files.internal("sprites/man/man.txp")))
    }



    val sprites = mutableMapOf<String, MutableMap<String, Sprite>>()

    private fun createAndAddSprite(spriteCollection: HashMap<String, MutableList<Sprite>>, atlas: TextureAtlas, region: TextureAtlas.AtlasRegion, width: Float, height: Float, spriteKey: String) {
        spriteCollection[spriteKey]!!.add(atlas.createSprite(region.name).apply {
                setSize(width, height)
                setOriginCenter()
        })
    }

    fun readerForTree(treeFileName: String): Reader {
        return Gdx.files.internal("btrees/$treeFileName").reader()
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}
