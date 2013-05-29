public class User {
	private final String name;

	/**
	 * Create a new user with a given name
	 * @param name name of user
	 */
	public User(String name) {
		this.name = name;
	}

	/**
	 * Return name of user.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return true if and only if both users have the same name.
	 */
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof User)) return false;
		User other = (User) obj;
		return getName().equals(other.getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}
}
