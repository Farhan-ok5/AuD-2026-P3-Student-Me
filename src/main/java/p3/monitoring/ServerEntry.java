package p3.monitoring;

/**
 * Represents a server resource entry in the monitoring system.
 * Tracks CPU load metrics for individual servers.
 */
public class ServerEntry {

    private String serverName;
    private int cpuLoad;

    /**
     * Creates a new server entry.
     *
     * @param serverName Unique server identifier
     * @param cpuLoad    Current CPU load percentage (0-100)
     */
    public ServerEntry(String serverName, int cpuLoad) {
        this.serverName = serverName;
        this.cpuLoad = cpuLoad;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(int cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

}
