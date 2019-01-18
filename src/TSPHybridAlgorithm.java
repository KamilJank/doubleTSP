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
        generateFirstPopulation();
        int i = 0;
        Double bestCost = population.get(0).getCost();
        do {
            Solution s = recombination();
            localSearch(s);
            evaluateSolutionAndAddToPopulation(s);
            if (bestCost <= population.get(0).getCost()) {
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
                if (TS2Size + longestSubgraph.length <= halfSize) {
                    for (int i = TS2Size; i < TS2Size + longestSubgraph.length; i++) {
                        childPermutation[i + halfSize] = longestSubgraph[i - TS2Size];
                    }
                    TS2Size += longestSubgraph.length;
                } else {
                    for (int i = TS1Size; i < halfSize; i++) {
                        childPermutation[i] = longestSubgraph[i - TS1Size];
                    }
                    int firstCapacity = halfSize - TS1Size;
                    TS1Size = halfSize;
                    for (int i = TS2Size; i < TS2Size + longestSubgraph.length - firstCapacity; i++) {
                        childPermutation[i + halfSize] = longestSubgraph[i + firstCapacity - TS2Size];
                    }
                    TS2Size += longestSubgraph.length - firstCapacity;

                }
            }
        }
        Solution childSolution = new Solution(childPermutation);
        childSolution.setCost(calculateCost(childPermutation));
        return childSolution;
    }

    private void generateFirstPopulation() {
        population = new ArrayList<>();
        while (population.size() < populationSize) {
            Solution p = new Solution(getFirstSolution());
            p.setCost(calculateCost(p.getPermutation()));
            getSolution(p);
            addSolutionToPopulation(p);
        }
    }

    private boolean addSolutionToPopulation(Solution solution) {
        boolean unique = population.stream().filter(solution::equals).collect(Collectors.toList()).stream().noneMatch(sol -> true);
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
