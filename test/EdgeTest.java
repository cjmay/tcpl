import org.junit.Test;
import static org.junit.Assert.*;

public class EdgeTest {
	private static final Node PERSON1 = new Node("Kernighan");
	private static final Node PERSON2 = new Node("Ritchie");
	private static final Node PERSON3 = new Node("Stallman");

	@Test
	public void testNodeGetters() {
		Edge edge = new Edge(PERSON1, PERSON2);
		assertEquals(PERSON1, edge.getFrom());
		assertEquals(PERSON2, edge.getTo());
	}

	@Test
	public void testEquals() {
		Edge edge1 = new Edge(PERSON1, PERSON2);
		Edge edge2 = new Edge(PERSON1, PERSON2);
		assertEquals(edge1, edge2);
	}

	@Test
	public void testEqualsReverse() {
		Edge edge1 = new Edge(PERSON1, PERSON2);
		Edge edge2 = new Edge(PERSON2, PERSON1);
		assertFalse(edge1.equals(edge2));
	}

	@Test
	public void testEqualsDiffTo() {
		Edge edge1 = new Edge(PERSON1, PERSON2);
		Edge edge2 = new Edge(PERSON1, PERSON3);
		assertFalse(edge1.equals(edge2));
	}

	@Test
	public void testEqualsDiffFrom() {
		Edge edge1 = new Edge(PERSON1, PERSON3);
		Edge edge2 = new Edge(PERSON2, PERSON3);
		assertFalse(edge1.equals(edge2));
	}

	@Test
	public void testHashCode() {
		Edge edge1 = new Edge(PERSON1, PERSON2);
		Edge edge2 = new Edge(PERSON1, PERSON2);
		assertEquals(edge1.hashCode(), edge2.hashCode());
	}
}
