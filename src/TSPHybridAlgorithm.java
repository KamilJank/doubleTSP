import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TSPHybridAlgorithm extends TSPGreedy{
    private int populationSize;
    private List<Solution> population;
    private Random generator;

    public TSPHybridAlgorithm(TSPFile data, String firstSolutionMethod, String fileName) {
        super(data, firstSolutionMethod, fileName);
        populationSize= (int) Math.sqrt(size);
        population = new ArrayList<>();
        generator = new Random();
    }

    @Override
    void algorithm() {
        generateRandomPopulation();
       boolean stopCondition=false;
       int i=0;
        do {
            Solution s=recombination();
            localSearch(s);
            evaluateSolutionAndAddToPopulation(s);
            i++;
        }while(i<10||stopCondition);
        currentSolution.cloneSolution(population.get(0));
    }
    private void evaluateSolutionAndAddToPopulation(Solution solution) {
        if(solution.getCost()< population.get(populationSize-1).getCost()){
            if(addSolutionToPopulation(solution)) {
                population.remove(populationSize);
            }
        }
    }

    private void localSearch(Solution solution) {
        getSolution(solution);
    }

    private Solution recombination() {
        Solution parentA=population.get(generator.nextInt(populationSize));
        Solution parentB=population.get(generator.nextInt(populationSize));
        System.out.println( "PAIRS");
        //SORT list
        for(Integer[] i:parentA.getAllCommonSubgraphs(parentB)){
            System.out.println( Arrays.toString(i));
        }
        //TODO
        return population.get(generator.nextInt(populationSize));
    }
    private void generateRandomPopulation() {
        while(population.size()<populationSize){
            Solution p=new Solution(getFirstSolution());
            p.setCost(calculateCost(p.getPermutation()));
            addSolutionToPopulation(p);
        }
    }
    private boolean addSolutionToPopulation(Solution solution) {
        boolean unique=true;
        for(Solution sol:population.stream().filter(p->solution.equals(p)).collect(Collectors.toList())){
            unique=false;
            break;
        }
        if(unique) {
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
