package util;

import p3.graph.AdjacencyGraph;
import p3.graph.AdjacencyMatrix;
import p3.graph.AdjacencyRepresentation;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectionUtil {

    public static void setAdjacencyMatrix(AdjacencyMatrix matrix, int[][] matrixArray) throws ReflectiveOperationException {
        setField(matrix, "matrix", matrixArray);
    }

    public static int[][] getAdjacencyMatrix(AdjacencyMatrix matrix) throws ReflectiveOperationException {
        return getField(matrix, "matrix");
    }


    public static void setNodeToIndex(AdjacencyGraph<Integer> graph, Map<Integer, Integer> nodeToIndex) throws ReflectiveOperationException {
        setField(graph, "nodeToIndex", nodeToIndex);
    }

    public static Map<Integer, Integer> getNodeToIndex(AdjacencyGraph<Integer> graph) throws ReflectiveOperationException {
        return getField(graph, "nodeToIndex");
    }

    public static void setIndexToNode(AdjacencyGraph<Integer> graph, Map<Integer, Integer> indexToNode) throws ReflectiveOperationException {
        setField(graph, "indexToNode", indexToNode);
    }

    public static Map<Integer, Integer> getIndexToNode(AdjacencyGraph<Integer> graph) throws ReflectiveOperationException {
        return getField(graph, "indexToNode");
    }

    @SuppressWarnings("unchecked")
    private static <T> T getField(Object instance, String fieldName) throws ReflectiveOperationException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
    }

    private static void setField(Object instance, String fieldName, Object value) throws ReflectiveOperationException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    public static void setWeights(AdjacencyGraph<Integer> graph, Map<Integer, Map<Integer, Integer>> weights) throws ReflectiveOperationException {
        setField(graph, "weights", weights);
    }

    public static Map<Integer, Map<Integer, Integer>> getWeights(AdjacencyGraph<Integer> graph) throws ReflectiveOperationException {
        return getField(graph, "weights");
    }

    public static AdjacencyRepresentation getRepresentation(AdjacencyGraph<Integer> graph) throws ReflectiveOperationException {
        return getField(graph, "representation");
    }
}
