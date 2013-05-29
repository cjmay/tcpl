import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
	@Test
	public void testGetName() {
		User user = new User("Larry");
		assertEquals("Larry", user.getName());
	}
}
