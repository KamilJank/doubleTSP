public class TSPGreedy extends TSP {
    public TSPGreedy(TSPFile data, String firstSolutionMethod, String fileName) {
        super(data, firstSolutionMethod, fileName);
    }

    @Override
    void algorithm() {
        currentSolution.setPermutation(getFirstSolution());
        currentSolution.setCost(calculateCost(currentSolution.getPermutation()));
        getSolution(currentSolution);
    }

    protected void getSolution(Solution baseSolution) {
        Double costChange;
        do {
            costChange = getNextResult(baseSolution);
            baseSolution.addCost(costChange);
        } while (costChange < 0);
    }

    protected Double getNextResult(Solution baseSolution) {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Double costChange = calculateCostChangeOnSwap(baseSolution.getPermutation(),i, j);
                if (costChange < 0) {
                    baseSolution.swapElements(i, j);
                    return costChange;
                }
            }
        }
        return new Double(0);
    }
}
