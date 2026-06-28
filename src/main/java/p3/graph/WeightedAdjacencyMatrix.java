package p3.graph;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.HashSet;
import java.util.Set;

/**
 * A light wrapper around a 2D array of boolean that represents an adjacency matrix.
 * <p>
 * The symmetric adjacency matrix is a square matrix that represents an undirected graph. The entry at index (i, j) is not
 * {@code 0}, iff a connection from the node with index {@code i} to the node with index {@code j} exists.
 * Otherwise, it is a number that represents the weight of the edge.
 *
 * @see AdjacencyRepresentation
 */
@DoNotTouch
public class WeightedAdjacencyMatrix implements AdjacencyRepresentation {


    /**
     * The underlying array that stores the adjacency matrix.
     */
    private int[][] matrix;

    /**
     * Constructs a new adjacency matrix with a specified number of nodes.
     *
     * @param size the specified number of nodes.
     */
    public WeightedAdjacencyMatrix(final int size) {
        if(size < 0)
            throw new IllegalArgumentException("The size of the matrix must not be negative.");

        matrix = new int[size][size];
    }


    /**
     * Adds a new undirected edge with a specified weight to the graph between two specified nodes.
     * @param node the node
     * @param other the other node
     * @param weight the specified weight
     */
    public void addEdge(final int node, final int other, final int weight) {
        matrix[node][other] = weight;
        matrix[other][node] = weight;
    }


    /**
     * Adds a new undirected edge with a default weight of 1 to the graph between two specified nodes.
     *
     * @param node the index of the node the edge starts at.
     * @param other   the index of the node the edge ends at.
     */
    @Override
    public void addEdge(final int node, final int other) {
        addEdge(node, other, 1);
    }

    /**
     * Checks if there is an undirected edge between two specified nodes.
     *
     * @param node The index of the node the edge starts at.
     * @param other   The index of the node the edge ends at.
     *
     * @return true if there exists an edge between two nodes.
     */
    @Override
    public boolean hasEdge(final int node, final int other) {
        return matrix[node][other] != 0 && matrix[other][node] != 0;
    }

    /**
     * Returns a set of all neighboring nodes from a specified node.
     * @param node the index of the specified node to get the adjacent nodes of.
     *
     * @return Set of neighboring nodes from a specified node.
     */
    @Override
    public Set<Integer> getAdjacentIndices(final int node) {
        final Set<Integer> result = new HashSet<>();

        for(int i = 0 ; i < matrix.length; i++) {
            if( matrix[node][i] != 0 ) {
                result.add(i);
            } // end of if
        } // end of for

        return result;
    }

    @Override
    public int size() {
        return matrix.length;
    }

    @Override
    public void grow() {

        int newSize = matrix.length + 1;
        int[][] newMatrix = new int[newSize][newSize];

        // Copy the old matrix into the new matrix
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }

        matrix = newMatrix;
    }

    /**
     * Returns the underlying adjacency matrix of the graph.
     *
     * @return the underlying adjacency matrix of the graph.
     */
    public int[][] getMatrix() {
        return matrix;
    }
}
