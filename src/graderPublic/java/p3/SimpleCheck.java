package p3;

public class SimpleCheck {
    public int value;
    public double warn;
    public double crit;
    public String status;

    public SimpleCheck(int value, double warn, double crit, String status) {
        this.value = value;
        this.warn = warn;
        this.crit = crit;
        this.status = status;
    }

    public String toString() {
        return String.format("SimpleCheck{value=%d, warn=%.1f, crit=%.1f, status=%s}", value, warn, crit, status);
    }
}
