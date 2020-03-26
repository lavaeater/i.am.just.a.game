package injection

import com.badlogic.gdx.physics.box2d.*

class CollisionListener() : ContactListener {

  override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

  }

  override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
  }

  override fun endContact(contact: Contact?) {

  }


  /**
   * Strategin att använda positioner kanske inte fungerar - vi bör använda kollisioner!
   */

  override fun beginContact(contact: Contact) {

    //If both bodies are static, we're not interested, just return
    if (contact.fixtureA.body.type == BodyDef.BodyType.StaticBody &&
        contact.fixtureB.body.type == BodyDef.BodyType.StaticBody) return
  }

}