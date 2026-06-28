package p3.monitoring;

import java.util.HashMap;
import static org.tudalgo.algoutils.student.Student.crash;

/**
 * Manages the lifecycle of monitoring checks including status updates and threshold evaluations.
 * Implements core monitoring logic for IT infrastructure services.
 */

public class CheckManagement {

    /**
     * Merges old and new check states according to monitoring rules.
     *
     * <p>Rules:
     * <ul>
     *   <li>New checks are added</li>
     *   <li>Existing checks update values as well as status based on thresholds</li>
     *   <li>Checks with value=0 are removed</li>
     * </ul>
     *
     * @param oldChecks Existing checks (input won't be modified)
     * @param newChecks Incoming check data (input won't be modified)
     */
    public static HashMap<String, Check> updateChecks(
            HashMap<String, Check> oldChecks,
            HashMap<String, Check> newChecks) {
            return crash();
    }
}