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
}
