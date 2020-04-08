package ai.pathfinding

import graph.Node
import ktx.math.ImmutableVector2
import screens.Mgo

class StarIsBorn {
    companion object {
        /**
         * The algo below goes from node to node neighbour-style, so cost
         * is always 1
         */

        fun cost(from: Node<ImmutableVector2>, to: Node<ImmutableVector2>): Int {
            return from.data.dst2(to.data).toInt()
        }

        fun<T> findPath(start: Node<T>, goal: Node<T>, heuristic: (from: Node<T>, to: Node<T>) -> Int ): MutableMap<Node<T>, Node<T>?> {
            val frontier = PriorityQueue<Node<T>>()
            val cameFrom = mutableMapOf<Node<T>, Node<T>?>(start to null)
            val costSoFar = mutableMapOf(start to 0)
            var newCost = 0
            var priority = 0

            frontier.push(start, 0)

            while (!frontier.isEmpty) {
                val current = frontier.pop()
                if(current == goal)
                    break

                for(next in current.neighbours(Mgo.Neighbour)) {
                    newCost = costSoFar[current]!! + 1
                    if((!costSoFar.containsKey(next) || newCost < costSoFar[next]!! ) && (next.hasLabel("Road") || next == goal)) {
                        costSoFar[next] = newCost
                        priority = newCost + heuristic(goal, next)
                        frontier.push(next, priority)
                        cameFrom[next] = current
                    }
                }
            }
            return cameFrom
        }
    }
}