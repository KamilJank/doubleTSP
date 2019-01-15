public class TSPOnlyFirstSolution extends TSP{
    public TSPOnlyFirstSolution(TSPFile data, String firstSolutionMethod, String fileName) {
        super(data, firstSolutionMethod, fileName);
    }

    @Override
    void algorithm() {
        currentSolution.setPermutation(getFirstSolution());
        currentSolution.setCost(calculateCost(currentSolution.getPermutation()));
    }
}
