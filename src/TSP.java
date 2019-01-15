import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public abstract class TSP {
    protected int size;
    protected int halfSize;
    private double avgTime;
    private double avgSolution;

    Solution currentSolution;
    Solution worstSolution;
    Solution bestSolution;
    private BufferedWriter fileWriter;
    private final TSPFile problemInstance;
    protected String firstSolutionMethod;

    public TSP(TSPFile data, String firstSolutionMethod, String fileName) {
        problemInstance = data;
        this.firstSolutionMethod = firstSolutionMethod;
        size = problemInstance.getDimension();
        halfSize = size / 2;
        currentSolution = new Solution(size);
        worstSolution = new Solution(size, Double.NEGATIVE_INFINITY);
        bestSolution = new Solution(size);
        try {
            fileWriter = new BufferedWriter(new FileWriter(fileName + problemInstance.getName() + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int[] getFirstSolution() {
        int[] permutation = new int[size];
        switch (firstSolutionMethod.toLowerCase()) {
            case "random":
                return generateRandomPermutations(permutation);
            case "heuristic1":
                return heuristicFirstSolution1(permutation);
            case "heuristic2":
                return heuristicFirstSolution2(permutation);
            default:
                return generateRandomPermutations(permutation);
        }
    }

    protected int[] heuristicFirstSolution1(int permutation[]) {
        boolean[] choosedElements = new boolean[size];
        Random generator = new Random();
        int currentNode = generator.nextInt(size);
        permutation[0] = currentNode;
        choosedElements[currentNode] = true;

        for (int i = 1; i < size; i++) {
            currentNode = argMinWithoutRepetition(currentNode, choosedElements);
            choosedElements[currentNode] = true;
            permutation[i] = currentNode;
        }
        return permutation;
    }

    protected int[] heuristicFirstSolution2(int permutation[]) {
        boolean[] choosedElements = new boolean[size];
        Random generator = new Random();

        int firstListNode = generator.nextInt(size);
        permutation[0] = firstListNode;
        choosedElements[firstListNode] = true;

        int secondListNode = argMaxArcLength(firstListNode);
        permutation[halfSize] = secondListNode;
        choosedElements[secondListNode] = true;

        for (int i = 1; i < halfSize; i++) {
            firstListNode = argMinWithoutRepetition(firstListNode, choosedElements);
            choosedElements[firstListNode] = true;
            permutation[i] = firstListNode;

            secondListNode = argMinWithoutRepetition(secondListNode, choosedElements);
            choosedElements[secondListNode] = true;
            permutation[halfSize + i] = secondListNode;
        }
        return permutation;
    }

    private int argMaxArcLength(int node) {
        Double max = Double.NEGATIVE_INFINITY;
        int argmax = -1;
        for (int i = 0; i < size; i++) {
            if (i != node) {
                Double newMax = problemInstance.getArcCost(node, i);
                if (newMax > max) {
                    max = newMax;
                    argmax = i;
                }
            }
        }
        return argmax;
    }

    private int argMinWithoutRepetition(int node, boolean choosedElements[]) {
        Double min = Double.POSITIVE_INFINITY;
        int argmin = -1;
        for (int i = 0; i < size; i++) {
            Double newMin = problemInstance.getArcCost(node, i);
            if (newMin < min && !choosedElements[i]) {
                min = newMin;
                argmin = i;
            }
        }
        return argmin;
    }

    protected int[] generateRandomPermutations(int permutation[]) {
        for (int i = 0; i < size; i++) {
            permutation[i] = i;
        }
        Random generator = new Random();
        int help;
        int randomArg;
        for (int i = size - 1; i > 0; i--) {
            help = permutation[i];
            randomArg = generator.nextInt(i + 1);
            permutation[i] = permutation[randomArg];
            permutation[randomArg] = help;
        }
        return permutation;
    }

    protected Double getArcCost(int solution[], int firstElement, int secondElement) {
        return problemInstance.getArcCost(solution[firstElement], solution[secondElement]);
    }

    protected Double calculateCost(int solution[]) {
        Double sumOfSolutions = new Double(0);
        for (int i = 0; i < halfSize - 1; i++) {
            sumOfSolutions += getArcCost(solution, i, i + 1);
        }
        sumOfSolutions += getArcCost(solution, halfSize - 1, 0);

        for (int i = halfSize; i < size - 1; i++) {
            sumOfSolutions += getArcCost(solution, i, i + 1);
        }
        sumOfSolutions += getArcCost(solution, size - 1, halfSize);
        return sumOfSolutions;
    }

    protected Double calculateCostChangeOnSwap(int[] permutation,int firstElement, int secondElement) {
        int firstElementListIndex = firstElement / halfSize;
        int secondElementListIndex = secondElement / halfSize;
        int fL = Math.floorMod((firstElement - 1), halfSize) + (halfSize * firstElementListIndex);
        int fR = ((firstElement + 1) % halfSize) + (halfSize * firstElementListIndex);
        int sL = Math.floorMod((secondElement - 1), halfSize) + (halfSize * secondElementListIndex);
        int sR = ((secondElement + 1) % halfSize) + (halfSize * secondElementListIndex);
        if (fL == secondElement) {
            return 0
                    - getArcCost(permutation, firstElement, fR)
                    - getArcCost(permutation, sL, secondElement)
                    + getArcCost(permutation, secondElement, fR)
                    + getArcCost(permutation, sL, firstElement);
        }
        if (fR == secondElement) {
            return 0
                    - getArcCost(permutation, fL, firstElement)
                    - getArcCost(permutation, secondElement, sR)
                    + getArcCost(permutation, fL, secondElement)
                    + getArcCost(permutation, firstElement, sR);
        }
        return 0
                - getArcCost(permutation, fL, firstElement)
                - getArcCost(permutation, firstElement, fR)
                - getArcCost(permutation, sL, secondElement)
                - getArcCost(permutation, secondElement, sR)
                + getArcCost(permutation, fL, secondElement)
                + getArcCost(permutation, secondElement, fR)
                + getArcCost(permutation, sL, firstElement)
                + getArcCost(permutation, firstElement, sR);
    }

    private void evaluateCurrentSolution() {
        if (bestSolution.getCost() > currentSolution.getCost()) {
            bestSolution.cloneSolution(currentSolution);
        }
        if (worstSolution.getCost() < currentSolution.getCost()) {
            worstSolution.cloneSolution(currentSolution);
        }
    }


    public void solve(int startsNumber) {
        double l = 0;
        long sumOfSolutions = 0;
        long estimatedTime = 0;
        do {
            long startTime = System.nanoTime();
            algorithm();
            estimatedTime += System.nanoTime() - startTime;
            evaluateCurrentSolution();
            sumOfSolutions += currentSolution.getCost();
            l++;
        } while (l < startsNumber);

        avgTime = estimatedTime / l;
        avgSolution = sumOfSolutions / l;
        try {
            fileWriter.write(problemInstance.getName() + "\n");
            fileWriter.write(bestSolution.toString() + ";" + worstSolution.toString() + ";" + avgSolution + ";" + avgTime);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void multisolveWithMileagePrints(int times) {
        try {
            fileWriter.write(problemInstance.getName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < times; i++) {
            algorithm();
            try {
                fileWriter.write(currentSolution.toString() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    abstract void algorithm();
}
