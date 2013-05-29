public class Node {
	private final String name;
	private Integer category;

	/**
	 * Create a new node with a given name
	 * @param name name of node
	 */
	public Node(String name) {
		this.name = name;
	}

	/**
	 * Return name of node.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Return true if and only if both nodes have the same name.
	 */
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Node)) return false;
		Node other = (Node) obj;
		return getName().equals(other.getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer c) {
		category = c;
	}
}
