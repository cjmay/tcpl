import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {
	@Test
	public void testGetName() {
		Node node = new Node("Larry");
		assertEquals("Larry", node.getName());
	}
}
