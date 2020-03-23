package components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.ai.btree.BehaviorTree


class AiComponent<T>(val behaviorTree: BehaviorTree<T>) : Component