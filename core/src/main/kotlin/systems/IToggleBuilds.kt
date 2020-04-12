package systems

interface IToggleBuilds {
    val build: Boolean
    fun toggle()
}

class BuildToggler : IToggleBuilds {
    private var _build = false
    override val build get() = _build

    override fun toggle() {
        _build = !_build
    }
}
