package p3;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p3.monitoring.ServerMonitor;
import java.lang.reflect.Field;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class ServerMonitorTest {
    private ServerMonitor monitor;

    @BeforeEach
    public void setUp() {
        monitor = new ServerMonitor(100);
    }

    @AfterEach
    public void tearDown() {
        monitor = null;
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H1_ServerMonitorTests.json", data = "putOverwriteTest")
    public void testPutOverwriteWithSize(@Property("key") String key,
                                         @Property("initialLoad") int initial,
                                         @Property("newLoad") int updated) throws Exception {

        int sizeBeforeFirstPut = getMonitorSize();
        monitor.put(key, initial);
        int sizeAfterFirstPut = getMonitorSize();

        // Should increase by 1 if it's a new key
        boolean isNewInsertion = (sizeAfterFirstPut == sizeBeforeFirstPut + 1);

        monitor.put(key, updated);
        int sizeAfterSecondPut = getMonitorSize();

        Context context = contextBuilder()
                .subject("ServerMonitor#put")
                .add("key", key)
                .add("expectedLoad", updated)
                .add("actualLoad", monitor.get(key))
                .add("isNewInsertion", isNewInsertion)
                .add("sizeBeforeFirstPut", sizeBeforeFirstPut)
                .add("sizeAfterFirstPut", sizeAfterFirstPut)
                .add("sizeAfterSecondPut", sizeAfterSecondPut)
                .build();

        assertEquals(updated, monitor.get(key), context,
                result -> "Expected the value to be overwritten with the new load.");

        assertEquals(sizeBeforeFirstPut + 1, sizeAfterFirstPut, context,
                result -> "Expected size to increase after first insertion.");

        assertEquals(sizeAfterFirstPut, sizeAfterSecondPut, context,
                result -> "Expected size to remain unchanged when overwriting existing key.");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H1_ServerMonitorTests.json", data = "getTest")
    public void testGet(@Property("key") String key,
                        @Property("load") Integer load,
                        @Property("shouldExist") Boolean shouldExist) {
        if (shouldExist) {
            monitor.put(key, load);
            Context context = contextBuilder()
                    .subject("ServerMonitor#get")
                    .add("key", key)
                    .add("expected", load)
                    .add("actual", monitor.get(key))
                    .build();

            assertEquals(load, monitor.get(key), context,
                    result -> "Expected to retrieve the correct load for the given key.");
        } else {
            Context context = contextBuilder()
                    .subject("ServerMonitor#get (missing key)")
                    .add("key", key)
                    .add("expected", -1)
                    .add("actual", monitor.get(key))
                    .build();

            assertEquals(-1, monitor.get(key), context,
                    result -> "Expected get() to return -1 for a key that doesn't exist.");
        }
    }


    @ParameterizedTest
    @JsonClasspathSource(value = "H1_ServerMonitorTests.json", data = "getTest")
    public void testRemoveWithSize(@Property("key") String key,
                           @Property("load") int load,
                           @Property("shouldExist") boolean shouldExist) throws Exception {

        if (shouldExist) {
            monitor.put(key, load);
        }

        int sizeBefore = getMonitorSize();
        boolean removed = monitor.remove(key);
        int sizeAfter = getMonitorSize();

        Context context = contextBuilder()
                .subject("ServerMonitor#remove")
                .add("key", key)
                .add("shouldExist", shouldExist)
                .add("removed", removed)
                .add("sizeBefore", sizeBefore)
                .add("sizeAfter", sizeAfter)
                .add("valueAfterRemove", monitor.get(key))
                .build();

        assertEquals(shouldExist, removed, context,
                result -> "Expected remove() to return " + shouldExist + " for key: " + key);

        if (shouldExist) {
            assertEquals(sizeBefore - 1, sizeAfter, context,
                    result -> "Expected size to decrease after successful removal.");
            assertEquals(-1, monitor.get(key), context,
                    result -> "Expected the key to be removed from the monitor.");
        } else {
            assertEquals(sizeBefore, sizeAfter, context,
                    result -> "Expected size to remain unchanged when key does not exist.");
        }
    }


    @ParameterizedTest
    @JsonClasspathSource(value = "H1_ServerMonitorTests.json", data = "hashTest")
    public void testHash(@Property("key") String key,
                         @Property("expectedHash") int expectedHash) {

        Context context = contextBuilder()
                .subject("ServerMonitor#hash")
                .add("key", key)
                .add("expectedHash", expectedHash)
                .add("actualHash", monitor.hash(key))
                .build();

        assertEquals(expectedHash, monitor.hash(key), context,
                result -> "Expected hash() to produce the correct hash for key.");
    }

    private int getMonitorSize() throws Exception {
        Field sizeField = monitor.getClass().getDeclaredField("size");
        sizeField.setAccessible(true);
        return sizeField.getInt(monitor);
    }
}
