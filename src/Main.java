//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    private static List<TSP> tspList = new ArrayList<>();
    private static String algorithm;
    private static String solverType;
    private static Map<String, Long> time;
    private static int timesRepeat;
    private static String fileName;
    private static void readAllFiles(final String folderPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(Main::readFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Read done");
    }

    private static void readFile(Path path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path.toString())));
            String name = br.readLine().split(":")[1].replaceAll("\\s", "");
            omitLine(br, 2);
            int size = Integer.parseInt(br.readLine().split(":")[1].replaceAll("\\s", ""));
            omitLine(br, 3);
            // TSP atspRandom = new TSPSimpleHeuristic(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size));//,1000000000);
            TSP tsp = getAtspType(name, size, br);
            //TSP tsp = new TSPGreedy(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size));
            tspList.add(tsp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void omitLine(BufferedReader br, int amount) {
        for (int i = 0; i < amount; i++) {
            try {
                br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static Double[][] readftv(BufferedReader br, int size) throws IOException {
        Double[][] completeMatrix = new Double[size][size];
        int mainCounter = 0;
        int counter = 0;
        Double[] matrix = new Double[size];
        while (mainCounter < size) {

            String singleLine = br.readLine();
            String tabSingleLine[] = singleLine.split("\\s");
            for (String element : tabSingleLine) {
                if (element.equals("EOF")) {
                    completeMatrix[mainCounter] = matrix;
                    return completeMatrix;
                }
                if (!element.equals("")) {
                    if (counter == size) {
                        completeMatrix[mainCounter] = matrix;
                        matrix = new Double[size];
                        mainCounter++;
                        counter = 0;
                    }
                    matrix[counter] = Integer.parseInt(element) != 0 ? new Double(Integer.parseInt(element)) : Double.POSITIVE_INFINITY;
                    counter++;
                }
            }
        }
        return completeMatrix;
    }

    private static Double[][] readOther(BufferedReader br, int size) throws IOException {
        int mainCounter = 0;
        Double[][] completeMatrix = new Double[size][size];
        while (mainCounter < size) {
            int counter = 0;
            Double[] matrix = new Double[size];
            while (counter < size) {
                String singleLine = br.readLine();
                String tabSingleLine[] = singleLine.split("\\s");
                for (String element : tabSingleLine) {
                    if (!element.equals("")) {
                        matrix[counter] = Integer.parseInt(element) != 0 ? new Double(Integer.parseInt(element)) : Double.POSITIVE_INFINITY;
                        counter++;
                    }
                }
            }
            completeMatrix[mainCounter] = matrix;
            mainCounter++;
        }
        return completeMatrix;
    }


    private static TSP getAtspType(String name, int size, BufferedReader br) throws IOException {
        TSP tsp = null;
        switch (algorithm.toLowerCase()) {
            case "greedy":
                tsp = new TSPGreedy(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size),fileName);
                break;
            case "steepest":
                tsp = new TSPSteepest(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size),fileName);
                break;
            case "random":
                tsp = new TSPRandom(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size), time.get(name),fileName);
                break;
            case "simpeheuristic":
                tsp = new TSPSimpleHeuristic(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size),fileName);
                break;
            case "simulated":
                tsp = new TSPSimulatedAnnealing(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size),fileName);
                break;
            case "tabu":
                tsp = new TSPTabu(name, size, name.contains("ftv") ? readftv(br, size) : readOther(br, size),fileName);
                break;
            default:
                break;
        }
        return tsp;
    }


    public static void main(String[] args) throws IOException {
        String[] tab;
        if (!(args.length == 0)) {
            tab = args;
        } else {
            Scanner in = new Scanner(System.in);
            tab = in.nextLine().split(" ");
        }
        algorithm = tab[1];
        solverType = tab[2];
        new File(".\\solutions\\"+algorithm).mkdirs();
        fileName=".\\solutions\\"+algorithm+"\\"+solverType+"_";
        if (solverType.toLowerCase().equals("multi")) {
            timesRepeat = Integer.parseInt(tab[3]);
            fileName+=timesRepeat+"_";
        }
        if (algorithm.toLowerCase().equals("random")) {
            //time = Long.valueOf(tab[3]);
            //System.out.println(tab[3]);
            //time=new ObjectMapper().readValue(tab[3], new TypeReference<Map<String, Long>>(){});
            if (solverType.toLowerCase().equals("multi")) {
                timesRepeat = Integer.parseInt(tab[4]);
                fileName+=timesRepeat+"_";
            }
        }
        readAllFiles(tab[0]);
        for (TSP tsp : tspList) {
            switch (solverType.toLowerCase()) {
                case "general":
                    tsp.solve();
                    break;
                case "time":
                    tsp.solveTimeOnly();
                    break;
                case "multi":
                    tsp.multisolveWithMileagePrints(timesRepeat);
                    break;
            }
        }
        //"atsp"
        //"test1"
        //folder algorithm solver (time if algorithm==random) (repeats if solver==multi)
    }
}
