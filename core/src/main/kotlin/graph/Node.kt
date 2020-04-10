package graph

open class Node<T>(val data: T) {
	private val relations = mutableMapOf<String, MutableSet<Node<T>>>()
	val allNeighbours: Iterable<Node<T>> get() = relations.map { it.value }.flatten()

	fun addNeighbour(relatedNode: Node<T>) {
		addRelation(Neighbour, relatedNode)
	}

	fun addRelation(name:String, relatedNode: Node<T>) {
		if(!relations.containsKey(name))
			relations[name] = mutableSetOf()

		relations[name]!!.add(relatedNode)
	}

	fun neighbours(relationToFind:String) : Sequence<Node<T>> {
		return if(relations.containsKey(relationToFind)) relations[relationToFind]!!.asSequence() else emptySequence()
	}

	fun neighbour(relationToFind: String) : Node<T>? {
		return relations[relationToFind]?.firstOrNull()
	}

	fun neighbours(relationsToFind: Collection<String>) : Sequence<Node<T>> {
		return relations.filterKeys { relationsToFind.contains(it) }.flatMap { it.value }.asSequence()
	}

	fun hasRelation(relation: String): Boolean {
		return relations.containsKey(relation)
	}

	val properties = mutableMapOf<String, Property>()
	private val _labels = mutableSetOf<String>()
	val labels : Set<String> get() = _labels.toSet()

	fun addLabel(label:String) {
		_labels.add(label)
	}

	fun hasLabel(label:String) : Boolean {
		return _labels.contains(label)
	}

	fun removeLabel(label:String) {
		_labels.remove(label)
	}

	fun addProperty(property: Property) {
		properties[property.name] = property
	}

	fun removeProperty(property: Property) {
		properties.remove(property.name)
	}

	companion object {
		const val Neighbour = "Neighbour"
	}
}