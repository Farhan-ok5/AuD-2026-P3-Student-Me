package p3.monitoring;

/**
 * Represents the status levels for monitoring checks.
 * Follows standard monitoring conventions with escalating severity.
 */
public enum CheckStatus {
    OK(0),      // Normal operation
    WARN(1),    // Warning condition
    CRIT(2);   // Critical failure

    /**
     * Creates a status enum with numeric severity level.
     *
     * @param severity Numeric value (higher = more severe)
     */
    CheckStatus(int severity) {
    }
}
