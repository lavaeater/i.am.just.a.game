package ai.pathfinding

class PriorityQueue<T> {
    private val items = mutableListOf<Pair<T, Int>>()

    fun push(value: T, priority: Int) {
        items.add(Pair(value, priority))
    }

    val isEmpty get() = items.isEmpty()

    fun pop():T {
        items.sortBy { it.second }
        return items.removeAt(0).first
    }
}