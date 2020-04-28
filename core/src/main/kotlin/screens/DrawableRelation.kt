package screens

import ktx.math.ImmutableVector2

class DrawableRelation(val from: ImmutableVector2, val to: ImmutableVector2) {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        return (other is DrawableRelation && ((other.from == this.from && other.to == this.to) || (other.to == this.from && other.from == this.to)))
    }

    override fun hashCode(): Int {
        return (from.hashCode() + to.hashCode()) * 23
    }
}