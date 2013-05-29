public class Edge {
	private final Node from, to;

	/**
	 * Create a new edge from one node to another.
	 * @param from node from which this edge originates
	 * @param to node to which this edge goes
	 */
	public Edge(Node from, Node to) {
		this.from = from;
		this.to = to;
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
