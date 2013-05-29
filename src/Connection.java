public class Connection {
	private final String from, to;

	/**
	 * Create a new connection from one user to another.
	 * @param from user from which this connection originates
	 * @param to user to which this connection goes
	 */
	public Connection(String from, String to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * Return user from which this connection originates.
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Return user to which this connection goes.
	 */
	public String getTo() {
		return to;
	}

	@Override
	public String toString() {
		return getFrom() + "->" + getTo();
	}

	/**
	 * Return true if and only if both connections go from some user A
	 * to another user B.  That is, the connections must go between
	 * the same users and in the same directions.
	 */
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Connection)) return false;
		Connection other = (Connection) obj;
		return getFrom().equals(other.getFrom())
			&& getTo().equals(other.getTo());
	}

	@Override
	public int hashCode() {
		return getFrom().hashCode() + getTo().hashCode();
	}
}
