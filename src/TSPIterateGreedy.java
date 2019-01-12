public class TSPIterateGreedy extends TSPGreedy {
    public TSPIterateGreedy(TSPFile data, String firstSolutionMethod, long calculationTime, String fileName) {
        super(data, firstSolutionMethod, fileName);
        bestIterationSolution = new int[size];
        this.calculationTime = calculationTime;
    }

    private Double bestIterationSolutionCost;
    private int bestIterationSolution[];
    private long calculationTime;

    @Override
    void algorithm() {
        currentSolution = getFirstSolution(currentSolution);
        bestIterationSolution = currentSolution.clone();
        currentSolutionCost = calculateCost(currentSolution);
        bestIterationSolutionCost = currentSolutionCost;
        long startTime = System.nanoTime();
        do {
            Double costChange;
            do {
                costChange = getNextResult();
                currentSolutionCost += costChange;
            } while (costChange < 0);
            evaluationAndPerturbation();
        } while (System.nanoTime() - startTime < calculationTime);
        currentSolutionCost = bestIterationSolutionCost;
        currentSolution = bestIterationSolution.clone();
    }

    private void evaluationAndPerturbation() {
        if (currentSolutionCost < bestIterationSolutionCost) {
            bestIterationSolutionCost = currentSolutionCost;
            bestIterationSolution = currentSolution.clone();
        }
        perturbation();
    }

    private void perturbation() {
        //TODO
    }
}
