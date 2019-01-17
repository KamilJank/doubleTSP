import java.util.*;

public class Solution {
    private Double cost;
    private int permutation[];
    private List<Integer[]> vertices;

    Solution(int size) {
        permutation = new int[size];
        cost = Double.POSITIVE_INFINITY;
        vertices=null;
    }

    Solution(int size, Double cost) {
        permutation = new int[size];
        this.cost = cost;
        vertices=null;
    }

    Solution(Solution solution) {
        permutation = solution.permutation.clone();
        cost = solution.cost;
        vertices=solution.vertices;
    }

    Solution(int[] permutation) {
        this.permutation = permutation.clone();
        cost = Double.POSITIVE_INFINITY;
        vertices=null;
    }

    public void cloneSolution(Solution solution) {
        permutation = solution.permutation.clone();
        cost = solution.cost;
        vertices=solution.vertices;
    }

    public void swapElements(int firstElement, int secondElement) {
        int help = permutation[firstElement];
        permutation[firstElement] = permutation[secondElement];
        permutation[secondElement] = help;
        vertices=null;
    }

    public int[] getPermutation() {
        return permutation;
    }

    public void setPermutation(int[] permutation) {
        this.permutation = permutation.clone();
        vertices=null;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public void addCost(Double toAdd) {
        cost += toAdd;
    }

    public String toString() {
        return Arrays.toString(permutation) + ";" + cost;
    }

    public boolean equals(Solution sol) {
        if (this.cost.equals(sol.cost)) {
            if (Objects.equals(this.calculateSimilarity(sol),this.permutation.length)) {
                return true;
            }
        }
        return false;
    }
    private List<Integer[]> getVerticesPairs() {
        if(vertices!=null){
            return vertices;
        }
        int[] permutationTemp=permutation;
        List<Integer[]> verticesPairs = new ArrayList<>();
        for (int i = 0; i < permutationTemp.length / 2 - 1; i++) {
            Integer[] tempTab = {permutationTemp[i], permutationTemp[i + 1]};
            Arrays.sort(tempTab);
            verticesPairs.add(tempTab);
        }
        Integer[] tempTab1 = {permutationTemp[permutationTemp.length / 2 - 1], permutationTemp[0]};
        Arrays.sort(tempTab1);
        verticesPairs.add(tempTab1);
        for (int i = permutationTemp.length / 2; i < permutationTemp.length - 1; i++) {
            Integer[] tempTab = {permutationTemp[i], permutationTemp[i + 1]};
            Arrays.sort(tempTab);
            verticesPairs.add(tempTab);
        }
        Integer[] tempTab2 = {permutationTemp[permutationTemp.length - 1], permutationTemp[permutationTemp.length / 2]};
        Arrays.sort(tempTab2);
        verticesPairs.add(tempTab2);
        vertices=verticesPairs;
        return verticesPairs;
    }

    public List<Integer[]> getCommonEdgesList(List<Integer[]> firstVerticesList, List<Integer[]> secondVerticesList) {
        List<Integer[]> commonVerticesPairs = new ArrayList<>();
        for (Integer[] pair : firstVerticesList) {
            for (Integer[] pair2 : secondVerticesList) {
                if (Objects.equals(pair[0],pair2[0])&&Objects.equals(pair[1],pair2[1])) {
                    commonVerticesPairs.add(pair);
                    break;
                }
            }
        }
        return commonVerticesPairs;
    }

    public List<Integer> getCommonVerticesList(List<Integer[]> commonEdges) {
        List<Integer> commonVertices = new ArrayList<>();
        for (int i = 0; i < this.permutation.length; i++) {
            commonVertices.add(i);
        }
        for (Integer[] edge : commonEdges) {
            for (Integer vertex : edge) {
                commonVertices.remove(vertex);
            }
        }
        return commonVertices;
    }

    public Integer[] findPair(Integer[] pairA, List<Integer[]> tempList) {
        for (Integer[] pair:tempList) {
            if(Objects.equals(pair[0],pairA[0])||Objects.equals(pair[0],pairA[pairA.length-1])||Objects.equals(pair[pair.length-1],pairA[0])||Objects.equals(pair[pair.length-1],pairA[pairA.length-1])){
                return pair;
            }
        }
        return null;
    }

    public Integer[] makeOneEdge(Integer[] pairA, Integer[] pairB) {
        int size = pairA.length + pairB.length-1;
        if(size>this.permutation.length/2){
            size=this.permutation.length/2;
        }
        Integer[] newEdge = new Integer[size];
        int index = 0;
        try {
            if (Objects.equals(pairA[0],pairB[0])) {
                for (int i = pairB.length - 1; i > 0; i--) {
                    newEdge[index] = pairB[i];
                    index++;
                }
                for (int i = 0; i < pairA.length; i++) {
                    newEdge[index] = pairA[i];
                    index++;
                }

            } else {
                if (Objects.equals(pairA[0],pairB[pairB.length - 1])) {
                    for (int i = 0; i < pairB.length; i++) {
                        newEdge[index] = pairB[i];
                        index++;
                    }
                    for (int i = 1; i < pairA.length; i++) {
                        newEdge[index] = pairA[i];
                        index++;
                    }
                } else {
                    if (Objects.equals(pairA[pairA.length - 1],pairB[0])) {
                        for (int i = 0; i < pairA.length; i++) {
                            newEdge[index] = pairA[i];
                            index++;
                        }
                        for (int i = 1; i < pairB.length; i++) {
                            newEdge[index] = pairB[i];
                            index++;
                        }

                    } else {
                        for (int i = 0; i < pairA.length; i++) {
                            newEdge[index] = pairA[i];
                            index++;
                        }
                        for (int i = pairB.length - 2; i >= 0; i--) {
                            newEdge[index] = pairB[i];
                            index++;
                        }
                    }
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            return newEdge;
        }
        return newEdge;
    }


    public List<Integer[]> getFinalEdgesList(List<Integer[]> tempList) {
        List<Integer[]> finalList = new ArrayList<>();
        while (!tempList.isEmpty()) {
            Integer[] pairA = tempList.get(0);
            tempList.remove(0);
            Integer[] pairB = findPair(pairA,tempList);
            if (pairB != null) {
                tempList.remove(pairB);
                tempList.add(makeOneEdge(pairA, pairB));
            } else {
                finalList.add(pairA);
            }
        }
        return finalList;
    }

    public List<Integer[]> getAllCommonSubgraphs(Solution secondSolution) {
        List<Integer[]> commonEdgesList = getCommonEdgesList(getVerticesPairs(), secondSolution.getVerticesPairs());
        List<Integer[]> finalList=getFinalEdgesList(commonEdgesList);
       List<Integer> vertices = getCommonVerticesList(finalList);
        for (Integer vertex:vertices) {
            Integer[] tempTab={vertex};
            finalList.add(0,tempTab);
        }
        Collections.sort(finalList, new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                return o2.length-o1.length;
            }
        });
        return finalList;
    }

    public int calculateSimilarity(Solution secondSolution){
        List<Integer[]> commonVerticesPairs = getCommonEdgesList(getVerticesPairs(), secondSolution.getVerticesPairs());
        return commonVerticesPairs.size();
    }


}
