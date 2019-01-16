import java.util.Random;

public class TSPIterateGreedy extends TSPGreedy {
    public TSPIterateGreedy(TSPFile data, long calculationTime, String fileName) {
        super(data, "random", fileName);
        bestIterationSolution = new Solution(size);
        this.calculationTime = calculationTime;
        generator = new Random();
    }
    private Solution bestIterationSolution;
    private long calculationTime;
    private Random generator;

    @Override
    void algorithm() {
        currentSolution.setPermutation(getFirstSolution());
        currentSolution.setCost(calculateCost(currentSolution.getPermutation()));
        getSolution(currentSolution);
        bestIterationSolution.cloneSolution(currentSolution);

        long startTime = System.nanoTime();
        do {
            perturbation();
            getSolution(currentSolution);
            evaluation();
        } while (System.nanoTime() - startTime < calculationTime);
    }

    private void evaluation() {
        if (currentSolution.getCost() < bestIterationSolution.getCost()) {
            bestIterationSolution.cloneSolution(currentSolution);
        }else{
            currentSolution.cloneSolution(bestIterationSolution);
        }
    }

    private void perturbation() {
        int a= generator.nextInt(size);
        int b= generator.nextInt(size);
        int c= generator.nextInt(size);
        Double costChange = calculateCostChangeOnSwap(currentSolution.getPermutation(),a, b);
        currentSolution.swapElements(a,b);
        costChange += calculateCostChangeOnSwap(currentSolution.getPermutation(),b, c);
        currentSolution.swapElements(b,c);
        currentSolution.addCost(costChange);
    }
}
