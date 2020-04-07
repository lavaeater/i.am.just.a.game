package graph

class Graph<T>() {
    val nodes = mutableSetOf<Node<T>>()
    private val labels = mutableMapOf<String, MutableSet<Node<T>>>()

    //PropertyMap just contains all properties that actually HAVE a property, not their values.
    val propertyMap = mutableMapOf<String, MutableSet<Node<T>>>()

    fun addNodeToPropertyMap(node: Node<T>, property: Property) {
        if (!propertyMap.containsKey(property.name))
            propertyMap[property.name] = mutableSetOf()

        propertyMap[property.name]!!.add(node)
    }

    fun removeNodeFromPropertyMap(node: Node<T>, property: Property) {
        if (propertyMap.containsKey(property.name))
            propertyMap[property.name]!!.remove(node)
    }

    fun addPropertyToNode(node: Node<T>, property: Property) {
        addNodeToPropertyMap(node, property)
        node.addProperty(property)
    }

    fun removePropertyFromNode(node: Node<T>, property: Property) {
		removeNodeFromPropertyMap(node, property)
        node.removeProperty(property)
    }

    fun addLabelToNode(label: String, node: Node<T>) {
        if (!labels.containsKey(label))
            labels[label] = mutableSetOf()

        labels[label]!!.add(node)
    }

    fun removeLabelFromNode(label: String, node: Node<T>) {
        if (!labels.containsKey(label)) return

        labels[label]!!.remove(node)
    }

    fun addNode(node: Node<T>) {
        nodes.add(node)
        for (p in node.properties.values) {
			addNodeToPropertyMap(node, p)
        }
    }

    fun addAll(elements: Array<out Node<T>>) {
		for (node in elements)
			addNode(node)
    }

    fun removeNodes(vararg node: Node<T>) {
        nodes.removeAll(node)
    }

    fun thatHaveProperties(nodes: Collection<Node<T>>, vararg propertiesToFind: String): Sequence<Node<T>> {
        return propertyMap.filterKeys { propertiesToFind.contains(it) }.flatMap { it.value }.asSequence()
    }

    fun withLabels(nodes: Collection<Node<T>>, vararg labelsToFind: String): Sequence<Node<T>> {
        return labels.filterKeys { labelsToFind.contains(it) }.flatMap { it.value }.intersect(nodes).asSequence()
    }
}


