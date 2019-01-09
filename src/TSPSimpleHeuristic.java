import java.util.Random;

public class TSPSimpleHeuristic extends TSP {
    public TSPSimpleHeuristic(String name, int size, Double[][] matrix, String fileName) {
        super(name, size, matrix,fileName);
        minTime = 10000000;//0.01 second
    }

    boolean[] choosedElements;

    @Override
    void algorithm() {
        iterations=0;
        evaluatedSolutions=0;
        getSolution();
        currentSolutionCost = calculateCost(currentSolution);
        firstSolution = currentSolution.clone();
        firstSolutionCost = currentSolutionCost;
    }

    private void getSolution() {
        iterations++;
        choosedElements = new boolean[size];
        Random generator = new Random();
        int currentNode = generator.nextInt(size);
        currentSolution[0] = currentNode;
        choosedElements[currentNode] = true;

        for (int i = 1; i < size; i++) {
            currentNode = argMinWithoutRepetition(currentNode);
            if (currentNode == -1) {
                break;
            }
            choosedElements[currentNode] = true;
            currentSolution[i] = currentNode;
        }
        if (currentNode == -1 || getArcCost(currentSolution, size - 1, 0) == Double.POSITIVE_INFINITY) {
            getSolution();
        }
    }

    private Integer argMinWithoutRepetition(int node) {
        Double min = Double.POSITIVE_INFINITY;
        int argmin = -1;
        for (int i = 0; i < size; i++) {
            evaluatedSolutions++;
            if (matrix[node][i] < min && !choosedElements[i]) {
                min = matrix[node][i];
                argmin = i;
            }
        }
        return argmin;
    }
}
