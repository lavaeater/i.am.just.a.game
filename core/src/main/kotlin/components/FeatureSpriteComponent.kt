package components

import com.badlogic.ashley.core.Component

class FeatureSpriteComponent(val spriteKey: String,
                               val animated:Boolean = false,
                               var currentIndex : Int = 0,
                               var deltaTime: Float = 0f) : Component