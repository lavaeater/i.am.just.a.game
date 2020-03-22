import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.Disposable
import data.GameSettings
import ktx.scene2d.Scene2DSkin
import ktx.style.skin
import ktx.style.textButton
import java.io.Reader
import com.badlogic.gdx.graphics.g2d.TextureRegion



/**
 * Created by barry on 12/9/15 @ 11:17 PM.
 */
object Assets : Disposable {
    lateinit var gameSettings: GameSettings
    lateinit var am: AssetManager

        fun load(gameSettings: GameSettings): AssetManager {
        Assets.gameSettings = gameSettings
        am = AssetManager()
//
//        initializeMapTiles()
//        initializeCharacterSprites()
//
//        initAnimatedCharacterSprites()
//
//        initializeFeatureSprites()
//
//        initializeFonts()
//
//        initializeScene2dDefaultSkin()

        return am
    }
    override fun dispose() {
        TODO("Not yet implemented")
    }
}
//    private val atlases by lazy {
//        mapOf(
//                "darkdirt" to TextureAtlas(Gdx.files.internal("tiles/darkdirt/darkdirt.txp")),
//                "darkgrass" to TextureAtlas(Gdx.files.internal("tiles/darkgrass/darkgrass.txp")),
//                "desert" to TextureAtlas(Gdx.files.internal("tiles/desert/desert.txp")),
//                "dirt" to TextureAtlas(Gdx.files.internal("tiles/dirt/dirt.txp")),
//                "grass" to TextureAtlas(Gdx.files.internal("tiles/grass/grass.txp")),
//                "rock" to TextureAtlas(Gdx.files.internal("tiles/rock/rock.txp")),
//                "water" to TextureAtlas(Gdx.files.internal("tiles/water/water.txp")))
//    }
//
//    private val characters by lazy {
//        mapOf(
//                "townsfolk" to TextureAtlas(Gdx.files.internal("chars/mtownsfolk/mtownsfolk.txp")),
//                "femaleranger" to TextureAtlas(Gdx.files.internal("chars/franger/franger.txp"))
//        )
//    }
//
//    val splashScreen by lazy {
//        Texture(Gdx.files.internal("ui/graphics/splashscreen.gif"))
//    }
//
//    val speechBTexture by lazy { Texture(Gdx.files.internal("ui/graphics/speechbubble.png")) }
//    val speechBubble by lazy { NinePatch(speechBTexture, 14, 8,12,12) }
//
//    val tableNinePatch by lazy { Texture(Gdx.files.internal("ui/graphics/convobackground.png"))}
//    val tableBackGround by lazy { NinePatch(tableNinePatch, 4, 4, 4, 4 ) }
//
//    lateinit var standardFont: BitmapFont
//
//    val IDLE = "idle"
//    val WALK = "walk"
//    val GESTURE = "gesture"
//    val ATTACK = "attack"
//    val DEATH = "death"
//
//    val animatedCharacters by lazy {
//        mapOf("femalerogue" to TextureAtlas(Gdx.files.internal("chars/frogue/frogue.txp")),
//                "orc" to TextureAtlas(Gdx.files.internal("chars/forc/forc.txp")),
//                "williamhamparsomian" to TextureAtlas(Gdx.files.internal("chars/williamhamparsomian/williamhamparsomian.txp")),
//                "ulricawikren" to TextureAtlas(Gdx.files.internal("chars/saleswomanblonde/saleswomanblonde.txp")),
//                "andreaslindblad" to TextureAtlas(Gdx.files.internal("chars/andreaslindblad/andreaslindblad.txp")),
//                "babakvarfan" to TextureAtlas(Gdx.files.internal("chars/babakvarfan/babakvarfan.txp")),
//                "carlsagan" to TextureAtlas(Gdx.files.internal("chars/carlsagan/carlsagan.txp")),
//                "stephenhawking" to TextureAtlas(Gdx.files.internal("chars/stephenhawking/stephenhawking.txp")),
//                "sandrafaber" to TextureAtlas(Gdx.files.internal("chars/sandrafaber/sandrafaber.txp")),
//                "carolynshoemaker" to TextureAtlas(Gdx.files.internal("chars/carolynshoemaker/carolynshoemaker.txp")),
//                "kimdinhthi" to TextureAtlas(Gdx.files.internal("chars/kimdinhthi/kimdinhthi.txp"))
//        )
//    }
//    val portraits by lazy {
//        mapOf("femalerogue" to Texture(Gdx.files.internal("chars/frogue/portrait.png")),
//                "orc" to Texture(Gdx.files.internal("chars/forc/portrait.png")))
//    }
//
//    val music by lazy {
//        Gdx.audio.newMusic(Gdx.files.internal("music/ambient.mp3")).apply {
//            isLooping = true
//        }
//    }
//
//    val beamonHeadshots by lazy {
//        mapOf(
//                "WilliamHamparsomian" to Texture(Gdx.files.internal("chars/portraits/WilliamHamparsomian.png")),
//                "AndreasLindblad" to Texture(Gdx.files.internal("chars/portraits/AndreasLindblad.png")),
//                "KimDinhThi" to Texture(Gdx.files.internal("chars/portraits/KimDinhThi.png")),
//                "BabakVarfan" to Texture(Gdx.files.internal("chars/portraits/BabakVarfan.png")),
//                "CarlSagan" to Texture(Gdx.files.internal("chars/portraits/CarlSagan.png")),
//                "SandraFaber" to Texture(Gdx.files.internal("chars/portraits/SandraFaber.png")),
//                "CarolynShoemaker" to Texture(Gdx.files.internal("chars/portraits/CarolynShoemaker.png")),
//                "StephenHawking" to Texture(Gdx.files.internal("chars/portraits/StephenHawking.png")),
//                "Flexbert" to Texture(Gdx.files.internal("chars/portraits/StephenHawking.png")),
//                "UlricaWikren" to Texture(Gdx.files.internal("chars/portraits/UlricaWikren.png")))
//    }
//
//    val animatedCharacterSprites by lazy { mutableMapOf<String, Map<String, List<Sprite>>>() }
//
//    val codeToExtraTiles by lazy { mutableMapOf<String, List<Sprite>>() }
//
//    val tileSprites by lazy { mutableMapOf<String, HashMap<String, Sprite>>() }
//

//
//    private fun initializeScene2dDefaultSkin() {
//        //val mySkin = Skin(Gdx.files.internal("skins/uiskin.json"))
//
//        val skin = skin {
//            textButton {
//                font = standardFont
//                fontColor = Color.BLACK
//                downFontColor = Color.GRAY
//            }
//        }
//
//        Scene2DSkin.defaultSkin = skin
//    }
//
//    private fun initializeFonts() {
//        val fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"))
//
//        val fontParams = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
//            color = Color.WHITE
//            size = gameSettings.baseFontSize
//        }
//
//        standardFont =  fontGenerator.generateFont(fontParams)
//
//        fontGenerator.dispose()
//    }
//
//    private fun initAnimatedCharacterSprites() {
//        //We group the animations, this is good
//        val width = 6f
//        val height = 7f
//        val finalMap = mutableMapOf<String, MutableMap<String, MutableList<Sprite>>>()
//        for (atlasMap in animatedCharacters) {
//            val atlas = atlasMap.value
//
//            val spriteCollection = hashMapOf<String, MutableList<Sprite>>()
//            finalMap[atlasMap.key] = spriteCollection
//
//            spriteCollection[IDLE] = mutableListOf()
//            for (region in atlas.regions.filter { it.name.contains(IDLE) }) {
//                createAndAddSprite(spriteCollection, atlas, region, width, height, IDLE)
//            }
//            spriteCollection[WALK] = mutableListOf()
//            for (region in atlas.regions.filter { it.name.contains(WALK) }) {
//                createAndAddSprite(spriteCollection, atlas, region, width, height, WALK)
//            }
//            spriteCollection[GESTURE] = mutableListOf()
//            for (region in atlas.regions.filter { it.name.contains(GESTURE) }) {
//                createAndAddSprite(spriteCollection, atlas, region, width, height, GESTURE)
//            }
//
//            spriteCollection[ATTACK] = mutableListOf()
//            for (region in atlas.regions.filter { it.name.contains(ATTACK) }) {
//                createAndAddSprite(spriteCollection, atlas, region, width, height, ATTACK)
//            }
//
//            spriteCollection[DEATH] = mutableListOf()
//            for (region in atlas.regions.filter { it.name.contains(DEATH) }) {
//                createAndAddSprite(spriteCollection, atlas, region, width, height, DEATH)
//            }
//        }
//        animatedCharacterSprites.putAll(finalMap)
//    }
//
//    private fun createAndAddSprite(spriteCollection: HashMap<String, MutableList<Sprite>>, atlas: TextureAtlas, region: TextureAtlas.AtlasRegion, width: Float, height: Float, spriteKey: String) {
//        spriteCollection[spriteKey]!!.add(atlas.createSprite(region.name).apply {
//            setSize(width, height)
//            setOriginCenter()
//        })
//    }
//
//    private fun initializeCharacterSprites() {
//        for (atlasMap in characters) {
//            val atlas = atlasMap.value
//            tileSprites.put(atlasMap.key, hashMapOf())
//            for (region in atlas.regions) {
//                val sprite = atlas.createSprite(region.name)
//                sprite.setSize(4f, 4.5f)
//                tileSprites[atlasMap.key]!!.put(region.name, sprite)
//            }
//        }
//    }
//
//    val featureSprites = mutableMapOf<String, MutableList<Sprite>>()
//
//    private fun initializeFeatureSprites() {
//        featureSprites["house"] = mutableListOf()
//        featureSprites["house"]!!.add(Sprite(Texture(Gdx.files.internal("tiles/features/house.png"))))
//    }
//
//    private fun initializeMapTiles() {
//        for (atlasMap in atlases) {
//            val atlas = atlasMap.value
//            tileSprites.put(atlasMap.key, hashMapOf())
//            for (region in atlas.regions) {
//                if (region.name != "blank") {
//                    fixBleeding(region)
//                    val sprite = atlas.createSprite(region.name)
//                    tileSprites[atlasMap.key]!!.put(region.name, sprite)
//                }
//            }
//        }
//    }
//
//    override fun dispose() {
//        for (atlas in atlases.values)
//            atlas.dispose()
//    }
//
//    fun readerForTree(treeFileName: String): Reader {
//        return Gdx.files.internal("btrees/$treeFileName").reader()
//    }
//
//    fun fixBleeding(region: TextureRegion) {
//        val fix = 0.01f
//        val x = region.regionX.toFloat()
//        val y = region.regionY.toFloat()
//        val width = region.regionWidth.toFloat()
//        val height = region.regionHeight.toFloat()
//        val invTexWidth = 1f / region.texture.width
//        val invTexHeight = 1f / region.texture.height
//        region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight) // Trims Region
//    }
//}
