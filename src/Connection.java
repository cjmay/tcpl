public class Connection {
	private final User from, to;

	/**
	 * Create a new connection from one user to another.
	 * @param from user from which this connection originates
	 * @param to user to which this connection goes
	 */
	public Connection(User from, User to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * Return user from which this connection originates.
	 */
	public User getFrom() {
		return from;
	}

	/**
	 * Return user to which this connection goes.
	 */
	public User getTo() {
		return to;
	}

	@Override
	public String toString() {
		return getFrom().toString() + "->" + getTo().toString();
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
