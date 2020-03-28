package statemachine

/**
 *
 */
class State(val state: String) {
    private val edges = hashMapOf<String, Edge>()   // Convert to HashMap with event as key
    private val stateActions = mutableListOf<(State) -> Unit>()

    /**
     * Creates an edge from a [State] to another when a [BaseEvent] occurs
     * @param event: Transition event
     * @param targetState: Next state
     * @param init: I find it as weird as you do, here you go https://kotlinlang.org/docs/reference/lambdas.html
     */
    fun edge(event: String, targetState: String, init: Edge.() -> Unit) {
        val edge = Edge(event, targetState)
        edge.init()

        if (edges.containsKey(event)) {
            throw Error("Adding multiple edges for the same event is invalid")
        }

        edges.put(event, edge)
    }

    /**
     * Action performed by state
     */
    fun action(action: (State) -> Unit) {
        stateActions.add(action)
    }

    /**
     * Enter the state and run all actions
     */
    fun enter() {
        // Every action takes the current state
        stateActions.forEach { it(this) }
    }

    /**
     * Get the appropriate statemachine.Edge for the Event
     */
    fun getEdgeForEvent(event: String): Edge {
        try {
            return edges[event]!!
        } catch (e: KotlinNullPointerException) {
            throw IllegalStateException("Event $event isn't registered with state ${this.state}")
        }
    }

    fun canAcceptEvent(event: String):Boolean  {
        return edges.containsKey(event)
    }

    override fun toString(): String {
        return state.toString()
    }

}
