package p3.monitoring;

/**
 * Represents a monitoring check configuration in an IT monitoring system.
 * Each check monitors a specific service with configurable thresholds and status.
 */
public class Check {
    private String servicename;
    private String servicedescription;
    private CheckStatus checkStatus;
    private int checkValue;
    private double value_min;
    private double value_max;
    private double value_warn;
    private double value_crit;

    /**
     * Creates a deep copy of another Check instance.
     *
     * @param other Check to copy (must not be null)
     * @throws IllegalArgumentException if other is null
     * @throws IllegalStateException    if warn threshold exceeds critical threshold
     */

    public Check(Check other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot copy null Check");
        }
        this.servicename = other.servicename;
        this.checkValue = other.checkValue;
        this.value_min = other.value_min;
        this.value_max = other.value_max;
        this.value_warn = other.value_warn;
        this.value_crit = other.value_crit;

        this.servicedescription = other.servicedescription;
        this.checkStatus = other.checkStatus;

        if (this.value_warn > this.value_crit) {
            throw new IllegalStateException(
                    String.format("Invalid thresholds: warn (%.1f) > crit (%.1f)",
                            this.value_warn, this.value_crit)
            );
        }
    }

    public Check(String name, String desc, CheckStatus status, int value,
                 double min, double max, double warn, double crit) {
        this.servicename = name;
        this.servicedescription = desc;
        this.checkStatus = status;
        this.checkValue = value;
        this.value_min = min;
        this.value_max = max;
        this.value_warn = warn;
        this.value_crit = crit;

        if (this.value_warn > this.value_crit) {
            throw new IllegalStateException("warn > crit");
        }
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getServicedescription() {
        return servicedescription;
    }

    public void setServicedescription(String servicedescription) {
        this.servicedescription = servicedescription;
    }

    public CheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(CheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public int getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(int checkValue) {
        this.checkValue = checkValue;
    }

    public double getValue_min() {
        return value_min;
    }

    public void setValue_min(double value_min) {
        this.value_min = value_min;
    }

    public double getValue_max() {
        return value_max;
    }

    public void setValue_max(double value_max) {
        this.value_max = value_max;
    }

    public double getValue_warn() {
        return value_warn;
    }

    public void setValue_warn(double value_warn) {
        this.value_warn = value_warn;
    }

    public double getValue_crit() {
        return value_crit;
    }

    public void setValue_crit(double value_crit) {
        this.value_crit = value_crit;
    }

    @Override
    public String toString() {
        return "Check{" +
                "servicename='" + servicename + '\'' +
                ", servicedescription='" + servicedescription + '\'' +
                ", checkStatus=" + checkStatus +
                ", checkValue=" + checkValue +
                ", value_min=" + value_min +
                ", value_max=" + value_max +
                ", value_warn=" + value_warn +
                ", value_crit=" + value_crit +
                '}';
    }
}
