package data

import graph.Node
import ktx.math.ImmutableVector2

class MapNode(data: ImmutableVector2) : Node<ImmutableVector2>(data) {
    fun move(diffX: Float, diffY: Float) {
        data = ImmutableVector2(data.x + diffX, data.y + diffY)
    }
}