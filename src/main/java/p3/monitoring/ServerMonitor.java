package p3.monitoring;

import java.util.LinkedList;
import static org.tudalgo.algoutils.student.Student.crash;


/**
 * Implements a hash table-based server monitor with dynamic rehashing.
 */

public class ServerMonitor {

    private LinkedList<ServerEntry>[] table;
    private int size;
    private static final double LOAD_FACTOR = 0.75;

    /**
     * Initializes a new  hashtable with specified capacity for server monitoring.
     *
     * @param capacity Initial number of buckets in hash table
     */
    @SuppressWarnings("unchecked")
    public ServerMonitor(int capacity) {
        table = new LinkedList[capacity];
        size = 0;
    }

    /**
     * Adds or updates a server's CPU load in the hashtable.
     * Triggers rehashing if load factor exceeds certain threshold.
     *
     * @param serverName Unique server identifier
     * @param cpuLoad    Current CPU load percentage (0-100)
     */

    public void put(String serverName, int cpuLoad) {
        crash();
    }

    /**
     * Retrieves the CPU load for a given server.
     *
     * @param serverName Server to look up
     * @return CPU load or -1 if server not found
     */

    public Integer get(String serverName) {
        return crash();
    }

    /**
     * Removes a given server from hashtable.
     *
     * @param serverName Server to remove
     * @return true if server was found and removed, false otherwise
     */

    public boolean remove(String serverName) {
        return crash();
    }


    /**
     * Computes a simple hash value for a given key using ASCII summation.
     * This hash function adds the ASCII values of all characters in the key
     * and then computes the modulo with the table size to ensure it fits
     * within the array bounds.
     *
     * @param key The key (e.g., server name) to hash.
     * @return An integer index representing the hash bucket for the key.
     */

    public int hash(String key) {
        return crash();
    }

    /**
     * Doubles the size of the underlying hashtable and re-inserts all existing entries.
     * This method is called automatically when the load factor exceeds a defined threshold.
     * It creates a new larger table and rehashes all existing entries using the original.
     * {@code put()} method to ensure proper redistribution.
     * This helps maintain efficient lookup and insertion times as the number of entries grows.
     */
    public void rehash() {
        crash();
    }

    /**
     * Computes a polynomial rolling hash value for the given key.
     * This implementation uses the formula:
     * hash(key) = (Σ (key[i] - 'a' + 1) * p^i) mod m
     * where p is a prime number (31) and m is the table size.
     *
     *  <p>Note: Special care is taken to ensure the result is always a valid
     *  array index (hint: watch for negative values during modulo operations).
     *
     * @param key The input string to be hashed
     * @return The computed hash value within the range of the hash table size
     * @throws IllegalArgumentException if the input key is null or empty
     */
    public int rollingHash(String key) {
        return crash();
    }
}
