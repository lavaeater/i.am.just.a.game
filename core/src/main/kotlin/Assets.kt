import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import data.GameSettings
import ktx.scene2d.Scene2DSkin
import ktx.style.skin
import ktx.style.textButton
import java.io.Reader


/**
 * Created by barry on 12/9/15 @ 11:17 PM.
 */
object Assets : Disposable {
    val standardFont by lazy { fixAFont() }
    lateinit var gameSettings: GameSettings
    lateinit var am: AssetManager

        fun load(gameSettings: GameSettings): AssetManager {
            Assets.gameSettings = gameSettings
            am = AssetManager()
            fixScene2DSkin()
            fixSprites()

            return am
        }

    private fun fixSprites() {

        //All of this could be converted into one giant nice little map call. This is fine for now.
        val manAtlas = textureAtlases["man"] ?: error("No man atlas found, that's an issue")
        sprites["man"] = manAtlas.regions.map {
            it.name to manAtlas.createSprite(it.name).apply {
                setSize(4f, 4.5f)
                setOriginCenter()
            }
        }.toMap()

        val needAtlas = textureAtlases["needs"] ?: error("No atlas for needs found, this is bad")
        sprites["needs"] = needAtlas.regions.map {
            it.name to needAtlas.createSprite(it.name).apply {
                setSize(1f, 1f)
                setOriginCenter()
            }
        }.toMap()
    }

    private val textureAtlases by lazy {
        mapOf("man" to TextureAtlas(Gdx.files.internal("sprites/man/man.txp")),
        "needs" to TextureAtlas(Gdx.files.internal("sprites/needs/needs.txp")))
    }



    val sprites = mutableMapOf<String, Map<String, Sprite>>()

    private fun createAndAddSprite(spriteCollection: HashMap<String, MutableList<Sprite>>, atlas: TextureAtlas, region: TextureAtlas.AtlasRegion, width: Float, height: Float, spriteKey: String) {
        spriteCollection[spriteKey]!!.add(atlas.createSprite(region.name).apply {
                setSize(width, height)
                setOriginCenter()
        })
    }

    private fun fixScene2DSkin() {

        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("ui/skin.json"))
    }

    private fun fixAFont() : BitmapFont {
        val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"))

        val fontParams = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            color = Color.WHITE
            size = gameSettings.baseFontSize
        }

        val standardFont =  fontGenerator.generateFont(fontParams)

        fontGenerator.dispose()
        return standardFont
    }

    fun readerForTree(treeFileName: String): Reader {
        return Gdx.files.internal("btrees/$treeFileName").reader()
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}
