import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String folderPath = ".\\data\\";
        List<TSPFile> tspFileList = new ArrayList<>();
        List<Path> pList;
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            pList = paths
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            for (Path path : pList) {
                tspFileList.add(new TSPFile(path));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("AFTER read");
        new File("results").mkdir();
        TSP methodImplementation;
        /*
        for(TSPFile data:tspFileList) {
            new File("results//1").mkdir();
            methodImplementation = new TSPOnlyFirstSolution(data, "heuristic1", "results//1//heuristic1_");
            methodImplementation.solve(100);
            methodImplementation = new TSPOnlyFirstSolution(data, "heuristic2", "results//1//heuristic2_");
            methodImplementation.solve(100);
        }*/
        System.out.println("AFTER 1 point");
        /*
        for(TSPFile data:tspFileList){
            new File("results//2").mkdir();
            methodImplementation=new TSPGreedy(data,"random","results//2//random_");
            methodImplementation.solve(100);
            methodImplementation=new TSPGreedy(data,"heuristic1","results//2//heuristic1_");
            methodImplementation.solve(100);
            methodImplementation=new TSPGreedy(data,"heuristic2","results//2//heuristic2_");
            methodImplementation.solve(100);
        }
*/
        System.out.println("AFTER 2 point");
/*
        for(TSPFile data:tspFileList){
            new File("results//3").mkdir();
            Long time;
            switch (data.getName()){
                case "kroA100":time=Long.valueOf(1276816269);break;
                case "kroA150":time= 4311943928L;break;
                case "kroB100":time=Long.valueOf(1041458723);break;
                case "kroB150":time= 4140755699L;break;
                default:time= Long.valueOf(0);
            }
            methodImplementation=new TSPIterateGreedy(data,time,"results//3//random_");
            methodImplementation.solve(100);
        }
*/
        System.out.println("AFTER 3 point");
/*
        for (TSPFile data : tspFileList) {
            new File("results//4").mkdir();
            Long time;
            switch (data.getName()) {
                case "kroA100":
                    time = 1276816269L;
                    break;
                case "kroA150":
                    time = 4311943928L;
                    break;
                case "kroB100":
                    time = 1041458723L;
                    break;
                case "kroB150":
                    time = 4140755699L;
                    break;
                default:
                    time = 0L;
            }
            methodImplementation = new TSPIterateGreedy(data, time, "results//4//random_");
            methodImplementation.multiSolve(1000);
        }
*/
        System.out.println("AFTER 4 point");
/*
        for (TSPFile data : tspFileList) {
            new File("results//5_1").mkdir();
            methodImplementation = new TSPHybridAlgorithm(data, "results//5_1//h1_");
            methodImplementation.solve(100);
        }

        System.out.println("AFTER 5_1 point");

        for (TSPFile data : tspFileList) {
            new File("results//5_2").mkdir();
            methodImplementation = new TSPHybridAlgorithm(data, "results//5_2//h1_");
            methodImplementation.multiSolve(1000);
        }

        System.out.println("AFTER 5_2 point");
        */
    }
}