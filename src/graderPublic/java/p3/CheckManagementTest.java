package p3;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p3.monitoring.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;


@TestForSubmission
public class CheckManagementTest {

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testRemoveClosedCheck() {
        HashMap<String, Check> oldChecks = new HashMap<>();
        HashMap<String, Check> newChecks = new HashMap<>();

        oldChecks.put("DB", new CheckBuilder("DB", 40, 60, 90, CheckStatus.WARN).build());
        newChecks.put("DB", new CheckBuilder("DB", 0, 60, 90, CheckStatus.WARN).build());

        oldChecks.put("Cache", new CheckBuilder("Cache", 25, 30, 40, CheckStatus.OK).build());
        newChecks.put("Cache", new CheckBuilder("Cache", 25, 30, 40, CheckStatus.OK).build());

        newChecks.put("NewClosed", new CheckBuilder("NewClosed", 0, 50, 80, CheckStatus.OK).build());

        oldChecks.put("AlreadyClosed", new CheckBuilder("AlreadyClosed", 0, 50, 80, CheckStatus.OK).build());
        newChecks.put("AlreadyClosed", new CheckBuilder("AlreadyClosed", 0, 50, 80, CheckStatus.OK).build());

        oldChecks.put("Reopened", new CheckBuilder("Reopened", 0, 50, 80, CheckStatus.OK).build());
        newChecks.put("Reopened", new CheckBuilder("Reopened", 30, 50, 80, CheckStatus.OK).build());

        // Execute
        HashMap<String, Check> result = CheckManagement.updateChecks(oldChecks, newChecks);

        // Verify
        Context context = contextBuilder()
                .subject("CheckManagement#updateChecks")
                .add("totalInputChecks", 5)
                .add("expectedResultChecks", 2) // Only Cache and Reopened should remain
                .build();

        assertFalse(result.containsKey("DB"), context,
                ctx -> "Existing check with value=0 should be removed");

        assertTrue(result.containsKey("Cache"), context,
                ctx -> "Active check should remain unchanged");
        assertEquals(25, result.get("Cache").getCheckValue(), context,
                ctx -> "Active check value should be preserved");

        assertFalse(result.containsKey("NewClosed"), context,
                ctx -> "NEW check with value=0 should never be added");

        assertFalse(result.containsKey("AlreadyClosed"), context,
                ctx -> "Check that was and remains closed should not be present");

        assertTrue(result.containsKey("Reopened"), context,
                ctx -> "Reopened check (0 → value) should be present");
        assertEquals(30, result.get("Reopened").getCheckValue(), context,
                ctx -> "Reopened check should have new value");
    }


    @Test
    public void testAddNewCheck() {
        HashMap<String, Check> oldChecks = new HashMap<>();
        HashMap<String, Check> newChecks = new HashMap<>();

        oldChecks.put("Disk", new CheckBuilder("Disk", 30, 50, 80, CheckStatus.OK).build());

        newChecks.put("Network", new CheckBuilder("Network", 85, 50, 80, CheckStatus.CRIT).build());

        newChecks.put("CPU", new CheckBuilder("CPU", 40, 50, 80, CheckStatus.OK).build());

        newChecks.put("Memory", new CheckBuilder("Memory", 60, 50, 80, CheckStatus.WARN).build());

        newChecks.put("DB", new CheckBuilder("DB", 0, 50, 80, CheckStatus.OK).build());

        newChecks.put("Cache", new CheckBuilder("Cache", 90, 80, 90, CheckStatus.WARN).build());

        HashMap<String, Check> result = CheckManagement.updateChecks(oldChecks, newChecks);

        Context context = contextBuilder()
                .subject("CheckManagement#updateChecks")
                .add("totalNewChecks", 5)
                .add("expectedAddedChecks", 4) // excluding the closed check
                .build();

        assertTrue(result.containsKey("Network"), context,
                ctx -> "Should add new Network check");
        assertEquals(CheckStatus.CRIT, result.get("Network").getCheckStatus(), context,
                ctx -> "Network should have CRIT status (value=85)");

        assertTrue(result.containsKey("CPU"), context,
                ctx -> "Should add new CPU check");
        assertEquals(CheckStatus.OK, result.get("CPU").getCheckStatus(), context,
                ctx -> "CPU should have OK status (value=40)");

        assertTrue(result.containsKey("Memory"), context,
                ctx -> "Should add new Memory check");
        assertEquals(CheckStatus.WARN, result.get("Memory").getCheckStatus(), context,
                ctx -> "Memory should have WARN status (value=60)");

        assertFalse(result.containsKey("DB"), context,
                ctx -> "Should NOT add closed DB check (value=0)");

        assertTrue(result.containsKey("Cache"), context,
                ctx -> "Should add new Cache check");
        assertEquals(CheckStatus.WARN, result.get("Cache").getCheckStatus(), context,
                ctx -> "Cache should have WARN status (value=90)");
    }

    static class CheckBuilder {
        private final Check check;

        public CheckBuilder(String name, int value, double warn, double crit, CheckStatus status) {
            check = new CheckBuilderRaw().build(name, value, warn, crit, status);
        }

        public Check build() {
            return new Check(check);
        }
    }

    static class CheckBuilderRaw {
        public Check build(String name, int value, double warn, double crit, CheckStatus status) {
            Check check = new CheckBuilderSimple().build();
            check.setServicename(name);
            check.setCheckValue(value);
            check.setValue_warn(warn);
            check.setValue_crit(crit);
            check.setCheckStatus(status);
            return check;
        }
    }

    static class CheckBuilderSimple {
        public Check build() {
            Check dummy = new CheckBuilderMinimal().build();
            return new Check(dummy);
        }
    }

    static class CheckBuilderMinimal {
        public Check build() {
            Check c = new CheckBuilderEmpty().build();
            c.setServicename("Service");
            c.setServicedescription("Desc");
            c.setCheckStatus(CheckStatus.OK);
            c.setCheckValue(1);
            c.setValue_min(0);
            c.setValue_max(100);
            c.setValue_warn(50);
            c.setValue_crit(80);
            return c;
        }
    }

    static class CheckBuilderEmpty {
        public Check build() {
            return new Check(new CheckBuilderEmptyRaw().build());
        }
    }

    static class CheckBuilderEmptyRaw {
        public Check build() {
            Check empty = new CheckBuilderReal().build();
            return empty;
        }
    }

    static class CheckBuilderReal {
        public Check build() {
            return new Check(new CheckBuilderValid().build());
        }
    }

    static class CheckBuilderValid {
        public Check build() {
            Check c = new CheckBuilderFinal().build();
            c.setValue_warn(50);
            c.setValue_crit(80);
            return c;
        }
    }

    static class CheckBuilderFinal {
        public Check build() {
            Check dummy = new CheckBuilderMinimalFinal().build();
            return dummy;
        }
    }

    static class CheckBuilderMinimalFinal {
        public Check build() {
            Check c = new Check(new CheckBuilderBare().build());
            c.setServicename("Base");
            c.setCheckValue(10);
            c.setValue_warn(30);
            c.setValue_crit(60);
            c.setCheckStatus(CheckStatus.OK);
            return c;
        }
    }

    static class CheckBuilderBare {
        public Check build() {
            // Minimal valid Check that can be copied by the real constructor
            Check dummy = new CheckBuilderBase().build();
            return dummy;
        }
    }

    static class CheckBuilderBase {
        public Check build() {
            Check c = new CheckBuilderFinalBase().build();
            c.setServicename("Bare");
            c.setCheckValue(1);
            c.setValue_warn(20);
            c.setValue_crit(50);
            c.setCheckStatus(CheckStatus.OK);
            return c;
        }
    }

    static class CheckBuilderFinalBase {
        public Check build() {
            // Build base Check with proper threshold ordering
            Check c = new CheckBuilderMin().build();
            c.setValue_warn(40);
            c.setValue_crit(80);
            return c;
        }
    }

    static class CheckBuilderMin {
        public Check build() {
            Check check = new CheckBuilderTrue().build();
            return check;
        }
    }

    static class CheckBuilderTrue {
        public Check build() {
            // Construct a valid Check with sane defaults for deep copy
            Check dummy = new CheckBuilderFactory().build();
            return dummy;
        }
    }

    static class CheckBuilderFactory {
        public Check build() {
            Check c = new CheckBuilderFallback().build();
            c.setServicename("Factory");
            c.setValue_warn(30);
            c.setValue_crit(90);
            c.setCheckValue(10);
            c.setCheckStatus(CheckStatus.OK);
            return c;
        }
    }

    static class CheckBuilderFallback {
        public Check build() {
            return new Check(new CheckBuilderSafe().build());
        }
    }

    static class CheckBuilderSafe {
        public Check build() {
            Check safe = new Check("Safe", "Desc", CheckStatus.OK, 10, 0, 100, 30, 90);
            return new Check(safe);
        }
    }
}
