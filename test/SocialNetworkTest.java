import org.junit.Test;
import static org.junit.Assert.*;

public class SocialNetworkTest {
	private final static Node PERSON1 = new Node("Kernighan");
	private final static Node PERSON2 = new Node("Ritchie");
	private final static Node PERSON3 = new Node("Stallman");

	@Test
	public void testSizeGettersEmpty() {
		SocialNetwork network = new SocialNetwork();
		assertEquals(0, network.getNumNodes());
		assertEquals(0, network.getNumEdges());
	}

	@Test
	public void testSizeGetters() {
		SocialNetwork network = new SocialNetwork();

		Edge edge1 = new Edge(PERSON1, PERSON2);
		network.add(edge1);

		Edge edge2 = new Edge(PERSON3, PERSON2);
		network.add(edge2);

		assertEquals(3, network.getNumNodes());
		assertEquals(2, network.getNumEdges());
	}
}
