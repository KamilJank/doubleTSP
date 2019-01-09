public class TSPRandom extends TSP {
    public TSPRandom(String name, int size, Double[][] matrix, long time, String fileName) {
        super(name, size, matrix,fileName);
        timePerMileage = time;
        nextSolution = new int[size];
        minTime = 100000000;//0.1 second
    }

    private int nextSolution[];

    private long timePerMileage;

    @Override
    void algorithm() {
        long startTime = System.nanoTime();
        iterations=0;
        evaluatedSolutions=0;
        //do {
            firstSolution = generateRandomPermutations(firstSolution);
            firstSolutionCost = calculateCost(firstSolution);
            iterations++;
            evaluatedSolutions++;
        //} while (firstSolutionCost == Double.POSITIVE_INFINITY);//to get acceptable solution
        currentSolution = firstSolution.clone();
        currentSolutionCost = firstSolutionCost;
        //System.out.println(timePerMileage);
        while (System.nanoTime() - startTime < timePerMileage) {
            iterations++;
            generateRandomPermutations(nextSolution);
            Double cost = calculateCost(nextSolution);
            if (cost < currentSolutionCost) {
                currentSolutionCost = cost;
                currentSolution = nextSolution.clone();
            }
        }
    }
}
