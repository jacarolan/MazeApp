import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class QueueTest {
    /**
     * Default constructor for test class QueueTest
     */
    public QueueTest() {
        
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
    public void testQueue() {
        Queue<Integer> s = new Queue<Integer>();
        s.enqueue(32);
        s.enqueue(19);
        s.enqueue(11);
        Queue<Integer> st = s.clone();
        assertEquals(s.isEmpty(), false);
        assertEquals(s.size(), 3);
        assertEquals((Integer)s.front(), (Integer)32);
        assertEquals((Integer)s.dequeue(), (Integer)32);
        assertEquals((Integer)s.front(), (Integer)19);
        assertEquals((Integer)s.dequeue(), (Integer)19);
        assertEquals((Integer)s.size(), (Integer)1);
        s.dequeue();
        assertEquals(s.isEmpty(), true);
        
        assertEquals(st.isEmpty(), false);
        assertEquals(st.size(), 3);
        assertEquals((Integer)st.front(), (Integer)32);
        assertEquals((Integer)st.dequeue(), (Integer)32);
        assertEquals((Integer)st.front(), (Integer)19);
        assertEquals((Integer)st.dequeue(), (Integer)19);
        assertEquals((Integer)st.size(), (Integer)1);
        st.dequeue();
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
