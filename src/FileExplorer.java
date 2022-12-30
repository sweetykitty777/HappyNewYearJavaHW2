import java.io.*;
import java.util.*;

public class FileExplorer {
    private int N = 10;
    private int[][] graph = new int[N][N];
    private String path = "./src/root/";

    private Map<String, Integer> originNumbers = new HashMap<String, Integer>();
    private Map<String, Integer> newNumbers = new HashMap<String, Integer>();

    int lastNum = -1;
    public FileExplorer() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                graph[i][j] = 0;
            }
        }
    }

    public void createGraph() {
        findFoldersAndFiles(this.path);
    }

    public void printGraph() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(graph[i][j]);
            }
            System.out.println("");
        }
    }

    private void findFoldersAndFiles(String pathToFile) {
        File place = new File(pathToFile);
        System.out.println(pathToFile);
        if (place.isDirectory()) {
            File[] items = place.listFiles();
            for (File item : items) { // а если null
                findFoldersAndFiles(item.getPath());
            }
        } else {
            FileReader fr = null;
            try {
                fr = new FileReader(pathToFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            BufferedReader reader = new BufferedReader(fr);
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (line != null) {
                if (line.startsWith("require")) { // проверить номер и null ли
                    String address = line.substring(8);
                    File reqFile = new File(address);

                    if (reqFile.exists()) {
                        if (!originNumbers.containsKey(pathToFile)) {
                            originNumbers.put(pathToFile, ++lastNum);
                        }
                        if (!originNumbers.containsKey(address)) {
                            originNumbers.put(address, ++lastNum);
                        }
                        System.out.println(originNumbers.get(pathToFile));
                        graph[originNumbers.get(pathToFile)][originNumbers.get(address)] = 1;
                    } else {
                        System.out.println("Some troubles with " + address);
                    }
                }
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void solve() {
        GraphRuler rule = new GraphRuler(graph);
        if (!rule.findLoop()) {
            System.out.println("No loops!");
            updateGraph();
        } else {
            System.out.println("OMG WE CAN NOT SOLVE IT");
        }
    }

    public List<String> sortFileNames() {
        List<String> sortedKeys = new ArrayList(originNumbers.keySet());
        Collections.sort(sortedKeys); // i love stackoverflow
        return sortedKeys;
    }
    public void updateGraph() {
        List<String> sortee = sortFileNames();
        int last = -1;
        for (int i = 0; i < sortee.size(); i++) {
            newNumbers.put(sortee.get(i), ++last);
        }
        int[][] graphTemporary = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                graphTemporary[i][j] = 0;
            }
        }
        for (Map.Entry<String, Integer> entry : originNumbers.entrySet()) {
            for (Map.Entry<String, Integer> entry2 : originNumbers.entrySet()) {
                if (graph[entry.getValue()][entry2.getValue()] == 1) {
                    graphTemporary[newNumbers.get(entry.getKey())][newNumbers.get(entry2.getKey())] = 1;
                }
            }
        }
        graph = graphTemporary;
        printGraph();
        for (Map.Entry<String, Integer> entry : originNumbers.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue() + " " + newNumbers.get(entry.getKey()));
        }
    }
}
