import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectionTest {
	private static final String PERSON1 = "Kernighan";
	private static final String PERSON2 = "Ritchie";
	private static final String PERSON3 = "Stallman";

	@Test
	public void testNodeGetters() {
		Connection connection = new Connection(PERSON1, PERSON2);
		assertEquals(PERSON1, connection.getFrom());
		assertEquals(PERSON2, connection.getTo());
	}

	@Test
	public void testEquals() {
		Connection connection1 = new Connection(PERSON1, PERSON2);
		Connection connection2 = new Connection(PERSON1, PERSON2);
		assertEquals(connection1, connection2);
	}

	@Test
	public void testEqualsReverse() {
		Connection connection1 = new Connection(PERSON1, PERSON2);
		Connection connection2 = new Connection(PERSON2, PERSON1);
		assertFalse(connection1.equals(connection2));
	}

	@Test
	public void testEqualsDiffTo() {
		Connection connection1 = new Connection(PERSON1, PERSON2);
		Connection connection2 = new Connection(PERSON1, PERSON3);
		assertFalse(connection1.equals(connection2));
	}

	@Test
	public void testEqualsDiffFrom() {
		Connection connection1 = new Connection(PERSON1, PERSON3);
		Connection connection2 = new Connection(PERSON2, PERSON3);
		assertFalse(connection1.equals(connection2));
	}

	@Test
	public void testHashCode() {
		Connection connection1 = new Connection(PERSON1, PERSON2);
		Connection connection2 = new Connection(PERSON1, PERSON2);
		assertEquals(connection1.hashCode(), connection2.hashCode());
	}
}
