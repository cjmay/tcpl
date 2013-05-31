public class Edge {
	private final Node from, to;
	private double scale;
	private Integer category;

	/**
	 * Create a new edge from one node to another.
	 * @param from node from which this edge originates
	 * @param to node to which this edge goes
	 */
	public Edge(Node from, Node to) {
		this.from = from;
		this.to = to;
		scale = 1.0;
	}

	/**
	 * Return node from which this edge originates.
	 */
	public Node getFrom() {
		return from;
	}

	/**
	 * Return node to which this edge goes.
	 */
	public Node getTo() {
		return to;
	}

	/**
	 * Return category of edge.
	 */
	public Integer getCategory() {
		return category;
	}

	/**
	 * Return scale of edge.
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Set scale of edge.
	 * @param sca new edge scale
	 */
	public void setScale(double sca) {
		scale = sca;
	}

	/**
	 * Set category of edge.
	 * @param cat new edge category
	 */
	public void setCategory(Integer cat) {
		category = cat;
	}

	@Override
	public String toString() {
		return getFrom().toString() + "->" + getTo().toString();
	}

	/**
	 * Return true if and only if both edges go from some node A
	 * to another node B.  That is, the edges must go between
	 * the same nodes and in the same directions.
	 */
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Edge)) return false;
		Edge other = (Edge) obj;
		return getFrom().equals(other.getFrom())
			&& getTo().equals(other.getTo());
	}

	@Override
	public int hashCode() {
		return getFrom().hashCode() + getTo().hashCode();
	}
}
