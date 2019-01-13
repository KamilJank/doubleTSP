import java.util.Random;

public class TSPIterateGreedy extends TSPGreedy {
    public TSPIterateGreedy(TSPFile data, String firstSolutionMethod, long calculationTime, String fileName) {
        super(data, firstSolutionMethod, fileName);
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
        getSolution();
        bestIterationSolution.cloneSolution(currentSolution);

        long startTime = System.nanoTime();
        do {
            perturbation();
            getSolution();
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
        System.out.println(a+" "+b+" "+c);
        System.out.println(currentSolution.toString());
        Double costChange = calculateCostChangeOnSwap(a, b);
        currentSolution.swapElements(a,b);
        costChange += calculateCostChangeOnSwap(b, c);
        currentSolution.swapElements(b,c);
        currentSolution.addCost(costChange);
        System.out.println(currentSolution.toString());
    }
}
