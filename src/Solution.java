import java.util.Arrays;

public class Solution {
    private Double cost;
    private int permutation[];
    Solution(int size){
        permutation = new int[size];
        cost = Double.POSITIVE_INFINITY;
    }
    Solution(int size,Double cost){
        permutation = new int[size];
        this.cost = cost;
    }
    Solution(Solution solution){
        permutation = solution.permutation.clone();
        cost = solution.cost;
    }
    public void cloneSolution(Solution solution){
        permutation = solution.permutation.clone();
        cost = solution.cost;
    }
    public void swapElements(int firstElement, int secondElement) {
        int help = permutation[firstElement];
        permutation[firstElement] = permutation[secondElement];
        permutation[secondElement] = help;
    }
    public int[] getPermutation(){
        return permutation;
    }
    public Double getCost(){
        return cost;
    }
    public void addCost(Double toAdd){
        cost+=toAdd;
    }
    public String toString(){
        return Arrays.toString(permutation) + ";" + cost;
    }
    public void setCost(Double cost){
        this.cost=cost;
    }
    public void setPermutation(int[] permutation){
        this.permutation=permutation.clone();
    }
}
