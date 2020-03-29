import com.badlogic.gdx.math.Vector2

fun Vector2.pointIsInside(size: Vector2, point: Vector2):Boolean {
    return point.x < this.x + size.x / 2 && point.x > this.x -size.x && point.y < this.y + size.y / 2 && point.y > this.y - size.y
}
