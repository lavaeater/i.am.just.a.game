package injection

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import data.GameSettings
import factory.ActorFactory
import factory.BodyFactory
import ktx.box2d.createWorld
import ktx.inject.Context
import screens.MainGameScreen
import systems.*
import ui.IUserInterface
import ui.UserInterface

class Injector {

    companion object {
        private const val timeInterval = 0.1f //Time between ticks in seconds
        private const val minutesPerTick = 5L
        val context = Context()

        fun buildContext(gameSettings: GameSettings) {

            context.register {
                bindSingleton(gameSettings)
                bindSingleton<InputProcessor>(InputMultiplexer())
                bindSingleton<Batch>(SpriteBatch())
                bindSingleton<IToggleBuilds>(BuildToggler())
                bindSingleton<Camera>(OrthographicCamera())
                bind<Viewport> {
                    ExtendViewport(gameSettings.width,
                            gameSettings.height,
                            this.inject())
                }
                bindSingleton(createWorld().apply {
                    setContactListener(CollisionListener())
                })

                bindSingleton(getEngine())

                bindSingleton(BodyFactory(inject()))

                bindSingleton(ActorFactory(
                        inject(),
                        inject()
                ))

                bindSingleton<IUserInterface>(UserInterface(inject()))

                bindSingleton(MainGameScreen(
                        inject(), //inject InputProcessor
                        inject(), //Batch
                        inject(), //Viewport
                        inject(), //Engine (ashley)
                        inject(), //camera
                inject(), //ActorFactory
                inject()))
            }
        }

        inline fun <reified Type : Any> inject(): Type {
            return context.inject()
        }

        private fun getEngine() : Engine {
            return Engine().apply {
                addSystem(AiAndTimeSystem(interval = timeInterval, minutes = minutesPerTick))
                //1. Temporarily disable Corona stuff
                addSystem(InfectionSystem(interval = timeInterval))
                addSystem(NpcControlSystem())
                addSystem(GameInputSystem(
                        inject(),
                        inject<Camera>() as OrthographicCamera,
                        inject()))
                addSystem(BuilderSystem(inject(), inject<Camera>() as OrthographicCamera))
                addSystem(PhysicsSystem(inject()))
                addSystem(
                        RenderSystem(
                                inject(),
                                inject(), true))
                addSystem(FollowCameraSystem(inject()))
            }
        }
    }
}

