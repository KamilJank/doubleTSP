import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TSPHybridAlgorithm extends TSPGreedy {
    private int populationSize;
    private List<Solution> population;
    private Random generator;

    public TSPHybridAlgorithm(TSPFile data, String firstSolutionMethod, String fileName) {
        super(data, firstSolutionMethod, fileName);
        populationSize = (int) Math.sqrt(size);
        population = new ArrayList<>();
        generator = new Random();
    }

    @Override
    void algorithm() {
        generateRandomPopulation();
        int i = 0;
        Double bestCost = population.get(0).getCost();
        do {
            Solution s = recombination();
            localSearch(s);
            evaluateSolutionAndAddToPopulation(s);
            if (bestCost == population.get(0).getCost()) {
                i++;
            } else {
                i = 0;
                bestCost = population.get(0).getCost();
            }
        } while (i < 100);//is it the best stop condition?
        currentSolution.cloneSolution(population.get(0));
    }

    private void evaluateSolutionAndAddToPopulation(Solution solution) {
        if (solution.getCost() < population.get(populationSize - 1).getCost()) {
            if (addSolutionToPopulation(solution)) {
                population.remove(populationSize);
            }
        }
    }

    private void localSearch(Solution solution) {
        getSolution(solution);
    }

    private Solution recombination() {
        Solution parentA = population.get(generator.nextInt(populationSize));
        Solution parentB = population.get(generator.nextInt(populationSize));
        System.out.println("PAIRS");
        //SORT list
        for (Integer[] i : parentA.getAllCommonSubgraphs(parentB)) {
            System.out.println(Arrays.toString(i));
        }
        int[] childPermutation = new int[size];
        int TS1Size = 0;
        int TS2Size = 0;
        List<Integer[]> subgraphs = parentA.getAllCommonSubgraphs(parentB);
        while (!subgraphs.isEmpty()) {
            Integer[] longestSubgraph = subgraphs.get(0);
            subgraphs.remove(0);
            if (TS1Size + longestSubgraph.length <= halfSize) {
                for (int i = TS1Size; i < TS1Size + longestSubgraph.length; i++) {
                    childPermutation[i] = longestSubgraph[i - TS1Size];
                }
                TS1Size += longestSubgraph.length;
            } else {
                for (int i = TS2Size; i < TS2Size + longestSubgraph.length; i++) {
                    childPermutation[i + halfSize] = longestSubgraph[i - TS2Size];
                }
                TS2Size += longestSubgraph.length;
            }
        }
        Solution childSolution = new Solution(childPermutation);
        childSolution.setCost(calculateCost(childPermutation));
        return childSolution;
    }

    private void generateRandomPopulation() {
        while (population.size() < populationSize) {
            Solution p = new Solution(getFirstSolution());
            p.setCost(calculateCost(p.getPermutation()));
            addSolutionToPopulation(p);
        }
    }

    private boolean addSolutionToPopulation(Solution solution) {
        boolean unique = true;
        for (Solution sol : population.stream().filter(p -> solution.equals(p)).collect(Collectors.toList())) {
            unique = false;
            break;
        }
        if (unique) {
            int i = 0;
            for (Solution s : population) {
                if (s.getCost() < solution.getCost()) {
                    i++;
                }
            }
            population.add(i, solution);
            return true;
        }
        return false;
    }

}
