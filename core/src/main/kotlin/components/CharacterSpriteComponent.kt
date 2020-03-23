package components

import com.badlogic.ashley.core.Component

class CharacterSpriteComponent(val spriteKey: String,
                               val animated:Boolean = false,
                               var currentAnim:String = "idle",
                               var currentIndex : Int = 0,
                               var deltaTime: Float = 0f) : Component

