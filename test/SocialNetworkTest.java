import org.junit.Test;
import static org.junit.Assert.*;

public class SocialNetworkTest {
	private final static User PERSON1 = new User("Kernighan");
	private final static User PERSON2 = new User("Ritchie");
	private final static User PERSON3 = new User("Stallman");

	@Test
	public void testSizeGettersEmpty() {
		SocialNetwork network = new SocialNetwork();
		assertEquals(0, network.getNumNodes());
		assertEquals(0, network.getNumConnections());
	}

	@Test
	public void testSizeGetters() {
		SocialNetwork network = new SocialNetwork();

		Connection connection1 = new Connection(PERSON1, PERSON2);
		network.add(connection1);

		Connection connection2 = new Connection(PERSON3, PERSON2);
		network.add(connection2);

		assertEquals(3, network.getNumNodes());
		assertEquals(2, network.getNumConnections());
	}
}
