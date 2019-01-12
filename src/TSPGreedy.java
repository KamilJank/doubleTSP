public class TSPGreedy extends TSP {
    public TSPGreedy(TSPFile data,String firstSolutionMethod, String fileName) {
        super(data,firstSolutionMethod,fileName);
    }

    @Override
    void algorithm() {
        currentSolution=getFirstSolution(currentSolution);
        Double costChange;
        do {
            costChange = getNextResult();
        } while (costChange<0);
        currentSolutionCost = calculateCost(currentSolution);
    }

    protected Double getNextResult() {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Double costChange = calculateCostChangeOnSwap(i, j);
                if (costChange < 0) {
                    swapElements(i, j);
                    return costChange;
                }
            }
        }
        return new Double(0);
    }
}
