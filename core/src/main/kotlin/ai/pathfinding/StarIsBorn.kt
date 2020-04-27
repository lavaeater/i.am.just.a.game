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

        fun<T> calculatePath(start: Node<T>, goal: Node<T>, heuristic: (from: Node<T>, to: Node<T>) -> Int ): MutableList<Node<T>> {
            val cameFrom = aStarIsBorn(start, goal, heuristic)
            val path = mutableListOf<Node<T>>()
            var current = goal
            while (current != start) {
                path.add(0, current)
                current = cameFrom[current]!!
            }

            path.add(0, start)
            return path
        }

        fun<T> aStarIsBorn(start: Node<T>, goal: Node<T>, heuristic: (from: Node<T>, to: Node<T>) -> Int ): MutableMap<Node<T>, Node<T>?> {
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
                    //We make the cost be actual distance between nodes instead, for more accurate estimates
                    newCost = costSoFar[current]!! + heuristic(current, goal)
                    if((!costSoFar.containsKey(next) || newCost < costSoFar[next]!! )) {
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