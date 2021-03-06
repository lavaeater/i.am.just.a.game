package ai.pathfinding

import com.badlogic.gdx.utils.Queue
import graph.Node
import screens.Mgo

/*
Inspired, as always, by the excellent Red Blog Games article

https://www.redblobgames.com/pathfinding/a-star/implementation.html
 */


class BreadthFirst {
    companion object {
        fun<T> searchIGuess(start: Node<T>, goal: Node<T>): MutableMap<Node<T>, Node<T>?> {
            val frontier = Queue<Node<T>>()
            val cameFrom = mutableMapOf<Node<T>,Node<T>?>(start to null)
            frontier.addFirst(start)

            while (!frontier.isEmpty) {
                val current = frontier.removeFirst()
                if(current == goal)
                    break

                for(next in current.neighbours(Mgo.Neighbour)) {
                    if(!cameFrom.containsKey(next) && (next.hasLabel("Road") || next == goal)) {
                        frontier.addFirst(next)
                        cameFrom[next] = current
                    }
                }
            }
            return cameFrom
        }
    }
}

