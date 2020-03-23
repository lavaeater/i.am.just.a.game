package components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body

class Box2dBodyComponent(val body: Body) : Component