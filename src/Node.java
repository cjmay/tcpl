public class Node {
	private final String name;
	private Integer category;
	private double scale;

	/**
	 * Create a new node with a given name
	 * @param name name of node
	 */
	public Node(String name) {
		this.name = name;
		scale = 1.0;
	}

	/**
	 * Return name of node.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return category of node.
	 */
	public Integer getCategory() {
		return category;
	}

	/**
	 * Set category of node.
	 * @param cat new node category
	 */
	public void setCategory(Integer cat) {
		category = cat;
	}

	/**
	 * Return scale of node.
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Set scale of node.
	 * @param sca new node scale
	 */
	public void setScale(double sca) {
		scale = sca;
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
}
