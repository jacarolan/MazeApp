import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class StackTest {
    /**
     * Default constructor for test class StackTest
     */
    public StackTest() {
        
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() {
        
    }
    
    @Test
    public void testStack() {
        Stack<Integer> s = new Stack<Integer>();
        s.push(11);
        s.push(19);
        s.push(32);
        Stack<Integer> st = s.clone();
        assertEquals(s.isEmpty(), false);
        assertEquals(s.size(), 3);
        assertEquals((Integer)s.top(), (Integer)32);
        assertEquals((Integer)s.pop(), (Integer)32);
        assertEquals((Integer)s.top(), (Integer)19);
        assertEquals((Integer)s.pop(), (Integer)19);
        assertEquals((Integer)s.size(), (Integer)1);
        s.pop();
        assertEquals(s.isEmpty(), true);
        
        assertEquals(st.isEmpty(), false);
        assertEquals(st.size(), 3);
        assertEquals((Integer)st.top(), (Integer)32);
        assertEquals((Integer)st.pop(), (Integer)32);
        assertEquals((Integer)st.top(), (Integer)19);
        assertEquals((Integer)st.pop(), (Integer)19);
        assertEquals((Integer)st.size(), (Integer)1);
        st.pop();
        assertEquals(st.isEmpty(), true);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown() {
        
    }
}
