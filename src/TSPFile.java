import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TSPFile {
    private Map<Double, Double[]> verticesDict;
    private Double[][] distMatrix;
    private int dimension;
    private String name;

    public TSPFile(Path path) {
        readFile(path);
        calculateDist();
    }

    private void readFile(Path path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toString()));
            this.name = bufferedReader.readLine().split(":")[1].replaceAll("\\s", "");
            omitLine(bufferedReader, 2);
            this.dimension = Integer.parseInt(bufferedReader.readLine().split(":")[1].replaceAll("\\s", ""));
            omitLine(bufferedReader, 2);
            String line;
            verticesDict = new HashMap<>();
            while (!(line = bufferedReader.readLine()).equals("EOF")) {
                String tabSingleLine[] = line.split("\\s");
                Double[] coords = {Double.parseDouble(tabSingleLine[1]), Double.parseDouble(tabSingleLine[2])};
                verticesDict.put(Double.parseDouble(tabSingleLine[0]) - 1, coords);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateDist() {
        this.distMatrix = new Double[this.dimension][this.dimension];
        for (int r = 0; r < this.dimension - 1; r++) {
            for (int c = r + 1; c < this.dimension; c++) {
                Long distance = Math.round(Math.sqrt(Math.pow((verticesDict.get(new Double(r))[0]) - (verticesDict.get(new Double(c))[0]), 2) + Math.pow((verticesDict.get(new Double(r))[1]) - (verticesDict.get(new Double(c))[1]), 2)));
                Double distance2 = distance.doubleValue();
                this.distMatrix[r][c] = distance2;
                this.distMatrix[c][r] = distance2;
            }
        }
        for (int i = 0; i < this.dimension; i++) {
            this.distMatrix[i][i] = Double.POSITIVE_INFINITY;
        }
    }

    private void omitLine(BufferedReader br, int amount) {
        for (int i = 0; i < amount; i++) {
            try {
                br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<Double, Double[]> getVerticesDict() {
        return verticesDict;
    }

    public Double[][] getDistMatrix() {
        return distMatrix;
    }

    public int getDimension() {
        return dimension;
    }

    public String getName() {
        return name;
    }
}
