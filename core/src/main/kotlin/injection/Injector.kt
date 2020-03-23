package injection

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import data.GameSettings
import ktx.inject.Context

class Injector {

    companion object {
        val context = Context()



        fun buildContext(gameSettings: GameSettings) {
            context.register {
                bindSingleton(gameSettings)

                bindSingleton<InputProcessor>(InputMultiplexer())
                bindSingleton<Batch>(SpriteBatch())
                bindSingleton<Camera>(OrthographicCamera())
                bind<Viewport> {
                    ExtendViewport(gameSettings.width,
                            gameSettings.height,
                            this.inject())
                }

                //                bindSingleton(createWorld().apply {
//                    setContactListener(CollisionListener(this@register.inject()))
//                })
//                bindSingleton(BodyFactory(this.inject()))
//                bindSingleton<IMapManager>(MapManager(
//                        this.inject(),
//                        this.inject()))

//                bind {
//                    ActorFactory(
//                            this.inject(),
//                            this.inject(),
//                            this.inject(),
//                            this.inject(),
//                            this.inject())
//                }

//                bindSingleton(getEngine(this))

//                bindSingleton(TileManager())
//                bindSingleton(Player(name = "William Hamparsomian"))

                //Bind provider for a viewport with the correct settings for this game!

//                bindSingleton<Telegraph>(MessageTelegraph(this.inject()))

//                bindSingleton<MessageDispatcher>(
//                        com.badlogic.gdx.ai.msg.MessageManager
//                                .getInstance().apply {
//                                    addListeners(this@register.inject(),
//                                            Messages.CollidedWithImpassibleTerrain,
//                                            Messages.EncounterOver,
//                                            Messages.PlayerWentToAPlace,
//                                            Messages.FactsUpdated,
//                                            Messages.PlayerMetSomeone,
//                                            Messages.StoryCompleted)
//                                })
//



//                bindSingleton<IUserInterface>(
//                        UserInterface(
//                                this.inject(),
//                                this.inject(),
//                                this.inject<InputProcessor>() as InputMultiplexer,
//                                this.inject()))
//
//                bindSingleton(ConversationManager(
//                        this.inject(),
//                        this.inject()
//                ))
//
//                bindSingleton(StoryManager())
//
//                bindSingleton(PlacesOfTheWorld())
//
//                bindSingleton(GameManager(
//                        this.inject(),
//                        this.inject(),
//                        this.inject(),
//                        this.provider(),
//                        this.inject(),
//                        this.provider(),
//                        this.inject(),
//                        this.inject(),
//                        this.inject(),
//                        this.inject()))
            }
        }
    }
}

//bindSingleton(FactsOfTheWorld(Gdx.app.getPreferences("default"))
//.apply {
//    setupInitialFacts()
//})
//bindSingleton(RulesOfTheWorld()) //Might be pointless
//bindSingleton(GameState())


//private fun getEngine(context: Context) : Engine {
//    return Engine().apply {
//        addSystem(GameInputSystem(
//                inputProcessor = context.inject(),
//                gameState = context.inject()))
//        addSystem(NpcControlSystem())
//        addSystem(RenderMapSystem(
//                context.inject(),
//                context.inject(),
//                context.inject(),
//                true))
//        addSystem(RenderCharactersSystem(context.inject()))
//        addSystem(RenderFeatureSystem(context.inject()))
//        addSystem(AiSystem())
//        addSystem(PhysicsSystem(context.inject()))
////			  addSystem(PhysicsDebugSystem(
////						context.inject(),
////						context.inject()))
//        addSystem(WorldFactsSystem())
//        addSystem(FollowCameraSystem(context.inject()))
//        addSystem(PlayerEntityDiscoverySystem())
//        addSystem(FeatureDiscoverySystem())
//    }
//}