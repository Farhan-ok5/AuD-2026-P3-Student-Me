package p3;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import p3.monitoring.ServerMonitor;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestForSubmission
public class RehashingTest {

    private ServerMonitor monitor;

    @BeforeEach
    public void setUp() {
        monitor = new ServerMonitor(10);
    }

    @AfterEach
    public void tearDown() {
        monitor = null;
    }


    @Test
    public void testRehashDoublesTableSize() throws Exception {
        Field tableField = ServerMonitor.class.getDeclaredField("table");
        tableField.setAccessible(true);

        int initialCapacity = ((Object[]) tableField.get(monitor)).length;

        int threshold = (int) (initialCapacity * 0.75) + 1;
        for (int i = 0; i < threshold; i++) {
            monitor.put("server" + i, i);
        }
        int newCapacity = ((Object[]) tableField.get(monitor)).length;
        assertEquals(initialCapacity * 2, newCapacity,
                "Expected the table size to double after rehashing.");
    }

}
