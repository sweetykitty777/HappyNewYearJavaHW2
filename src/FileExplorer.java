import java.io.*;
import java.util.*;

public class FileExplorer {

    /** Поле, содержащее в себе максимальный размер графа*/
    private final int N = 10;
    /** Поле, содержащее в себе матрицу смежности ориентированного графа*/
    private int[][] graph = new int[N][N];

    /** Поле, содержащее в себе путь до файла*/
    final private String path = "./src/root/";

    /** Поле, содержащее в себе то, как соотносятся пути до файлов и их номера*/
    private final Map<String, Integer> originNumbers = new HashMap<>();

    /** Поле, содержащее в себе то, как соотносятся пути до файлов и их номера после сортировки по названию*/
    private final Map<String, Integer> newNumbers = new HashMap<>();

    /** Поле, содержащее в себе то, как номера файлов и пути до файлов после сортировки по названию*/
    private final Map<Integer, String> newNumbersMirror = new HashMap<>();

    /** Количество файлов в зависимости */
    private int lastNum = -1;
    public FileExplorer() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                graph[i][j] = 0;
            }
        }
    }

    /** Создание графа*/
    public void createGraph() {
        File place = new File(path);
        if (!place.exists()) {
            System.out.println("Верните фолдер root туда, где взяли, пожалуйста");
        } else {
            findFoldersAndFiles(path);
        }
    }

    /** Печать графа*/
    public void printGraph() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(graph[i][j]);
            }
            System.out.println("");
        }
    }

    /** Поиск зависимостей
     * @param pathToFile - путь к папке*/
    private void findFoldersAndFiles(String pathToFile) {
        File place = new File(pathToFile);
        if (place.exists() && place.isDirectory()) {
            File[] items = place.listFiles();
            if (items != null) {
                for (File item : items) {
                    findFoldersAndFiles(item.getPath());
                }
            }
        } else if (place.exists() && place.isFile()) {
            FileReader fr = null;
            try {
                fr = new FileReader(pathToFile);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
            BufferedReader reader = null;
            if (fr != null) {
                reader = new BufferedReader(fr);
            }
            String line;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while (line != null) {
                if (line.startsWith("require")) {
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
                    System.out.println(e.getMessage());
                }
            }
        } else if (!place.exists()) {
            System.out.println(place + " не существует");
        }
    }

    /** Поиск и вывод ответа*/
    public void solve() {
        LoopChecker checker = new LoopChecker(graph);
        updateGraph();
        if (!checker.findLoop(newNumbersMirror)) {
            printGraph();
            TopologicalSorter a = new TopologicalSorter(graph);
            int[] ans = a.topologicalSort();
            for (int i = 0; i < lastNum + 1; i++) {
                System.out.println(newNumbersMirror.get(ans[i]));
            }
            try {
                createOutputFile(ans);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("We can not solve it");
        }
    }

    /** Сортировка по имени*/
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
                if(files[i].getName().compareTo(files[i + 1].getName()) > 0){
                    isSorted = false;
                    tmp = files[i];
                    files[i] = files[i + 1]; // ну дааа пузыреек
                    files[i + 1] = tmp;
                }
            }
        }
        return files;
    }

    /** Изменение нумерации в соответствии с сортировкой по имени*/
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

    /** Вывод файла с ответом*/
    private void createOutputFile(int[] answer) throws IOException {
        try(FileWriter writer = new FileWriter("./src/output.txt", false)) {
            for (int i = 0; i < lastNum + 1; i++) {
                BufferedReader reader = new BufferedReader(new FileReader(newNumbersMirror.get(answer[i])));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                String ls = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
                reader.close();
                String content = stringBuilder.toString();
                writer.write(content);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
