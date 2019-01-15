import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        /*
        for(Double[] li : tspFileList.get(0).getDistMatrix()){
            System.out.println(Arrays.toString(li));
        }*/
        System.out.println("AFTER read");
        TSPHybridAlgorithm greedy=new TSPHybridAlgorithm(tspFileList.get(0),"random","1test1_g_r");
        greedy.solve(1);
    }
}