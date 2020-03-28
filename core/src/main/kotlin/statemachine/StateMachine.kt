package statemachine

/**
 * Builds and operates state machines
 */
class StateMachine private constructor(private val initialState: String, private val globalStateAction: (String) -> Unit) {
    lateinit var currentState: State
    val states = mutableListOf<State>()

    fun state(stateName: String, init: State.() -> Unit) {
        val state = State(stateName)
        state.init()

        states.add(state)
    }

    /**
     * Translates state state to an object
     */
    private fun getState(state: String): State {
        return states.first { s -> s.state == state  }
    }

    /**
     * Initializes the [StateMachine] and puts it on the first state
     */
    fun initialize() {
        currentState = getState(initialState)
        currentState.enter()
        globalStateAction(currentState.state)
    }

    /**
     * Gives the FSM an event to act upon, state is then changed and actions are performed
     */
    fun acceptEvent(e: String) {
        try {
            val edge = currentState.getEdgeForEvent(e)

            // Indirectly get the state stored in edge
            // The syntax is weird to guarantee that the states are changed
            // once the actions are performed
            // This line just queries the next state state (Class) from the
            // state list and retrieves the corresponding state object.
            val state = edge.applyTransition { getState(it) }
            state.enter()

            currentState = state
            globalStateAction(currentState.state)
        } catch (exc: NoSuchElementException) {
            throw IllegalStateException("This state doesn't support " +
                    "transition on ${e}")
        }
    }

    companion object {
        fun buildStateMachine(initialState: String, globalStateAction: (String) -> Unit, init: StateMachine.() -> Unit): StateMachine {
            val stateMachine = StateMachine(initialState, globalStateAction)
            stateMachine.init()
            return stateMachine
        }
    }
}
