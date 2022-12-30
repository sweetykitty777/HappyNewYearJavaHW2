import java.io.*;
import java.util.*;

public class FileExplorer {
    private final int N = 10;
    private int[][] graph = new int[N][N];
   final private String path = "./src/root/";

    private final Map<String, Integer> originNumbers = new HashMap<String, Integer>();
    private final Map<String, Integer> newNumbers = new HashMap<String, Integer>();
    private final Map<Integer, String> newNumbersMirror = new HashMap<Integer, String>();

    int lastNum = -1;
    public FileExplorer() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                graph[i][j] = 0;
            }
        }
    }

    public void createGraph() {
        findFoldersAndFiles(path);
    }

   /* public void printGraph() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(graph[i][j]);
            }
            System.out.println("");
        }
    }*/

    private void findFoldersAndFiles(String pathToFile) {
        File place = new File(pathToFile);
        if (place.exists() && place.isDirectory()) {
            File[] items = place.listFiles();
            if (items != null) {
                for (File item : items) {
                    findFoldersAndFiles(item.getPath());
                }
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
                if (line.startsWith("require")) { // проверить формат
                    String address = line.substring(8);
                    File reqFile = new File(address);

                    if (reqFile.exists()) {
                        if (!originNumbers.containsKey(pathToFile)) {
                            originNumbers.put(pathToFile, ++lastNum);
                        }
                        if (!originNumbers.containsKey(address)) {
                            originNumbers.put(address, ++lastNum);
                        }
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
        updateGraph();
        if (!rule.findLoop(newNumbersMirror)) {
            TopologicalSort a = new TopologicalSort(graph);
            int[] ans = a.topological_sort();
            for (int i = 0; i < lastNum + 1; i++) {
                System.out.println(newNumbersMirror.get(ans[i]));
            }
        } else {
            System.out.println("We can not solve it");
        }
    }

    public File[] sortFileNames() {
        ArrayList<String> paths = new ArrayList<>(originNumbers.keySet());
        File[] files = new File[lastNum + 1];
        for (int i = 0; i < paths.size(); i++) {
            File file = new File(paths.get(i));
            files[i] = file;
        }
        boolean isSorted = false;
        File tmp;
        while(!isSorted) {
            isSorted = true;
            for (int i = 0; i < files.length - 1; i++) {
                if(files[i].getName().compareTo(files[i+1].getName()) > 0){
                    isSorted = false;
                    tmp = files[i];
                    files[i] = files[i + 1]; // ну дааа пузыреек
                    files[i + 1] = tmp;
                }
            }
        }
        return files;
    }
    public void updateGraph() {
        File[] sorted = sortFileNames();
        int last = -1;
        for (File file : sorted) {
            newNumbers.put(file.getPath(), ++last);
            newNumbersMirror.put(last, file.getPath());
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
    }
}
