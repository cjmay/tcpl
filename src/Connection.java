public class Connection {
	private String f, t;

	public Connection(String from, String to) {
		f = from;
		t = to;
	}

	public String getFrom() {
		return f;
	}

	public String getTo() {
		return t;
	}

	@Override
	public String toString() {
		return getFrom() + "->" + getTo();
	}

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
