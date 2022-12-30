import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileExplorer {
    private int N = 10;
    private int[][] graph = new int[N][N];
    private String path = "./src/root/";

    private Map<String, Integer> originNumbers = new HashMap<String, Integer>();

    int lastNum = 0;
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

    private void findFoldersAndFiles(String pathToFile) {
        File place = new File(pathToFile);
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

                    if (reqFile.isFile() && reqFile.canRead()) {
                        if (!originNumbers.containsKey(pathToFile)) {
                            originNumbers.put(pathToFile, ++lastNum);
                        }
                        if (!originNumbers.containsKey(address)) {
                            originNumbers.put(address, ++lastNum);
                        }
                        graph[originNumbers.get(pathToFile)][originNumbers.get(address)] = 1;
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
}
