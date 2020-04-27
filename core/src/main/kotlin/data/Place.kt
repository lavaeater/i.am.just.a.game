package data

import com.badlogic.gdx.math.Rectangle
import graph.Node
import ktx.math.ImmutableVector2

data class Place(val type: PlaceType = PlaceType.Workplace, val node: Node<ImmutableVector2>, val width : Float  = 10f, val height : Float  = 10f) {
    val box by lazy { Rectangle(node.data.x - width / 2, node.data.y - height / 2, width, height) }
    val x get() =  node.data.x
    val y get() =  node.data.y
    val center get() = node.data
}