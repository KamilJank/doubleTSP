public class TSPGreedy extends TSP {
    public TSPGreedy(TSPFile data, String firstSolutionMethod, String fileName) {
        super(data, firstSolutionMethod, fileName);
    }

    @Override
    void algorithm() {
        currentSolution.setPermutation(getFirstSolution());
        currentSolution.setCost(calculateCost(currentSolution.getPermutation()));
        getSolution();
    }

    protected void getSolution() {
        Double costChange;
        do {
            costChange = getNextResult();
            currentSolution.addCost(costChange);
        } while (costChange < 0);
    }

    protected Double getNextResult() {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Double costChange = calculateCostChangeOnSwap(i, j);
                if (costChange < 0) {
                    currentSolution.swapElements(i, j);
                    return costChange;
                }
            }
        }
        return new Double(0);
    }
}
