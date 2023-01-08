import java.util.Map;
import java.util.Vector;

/**
 * Класс, проверяющий граф на наличие циклов
 */
public class LoopChecker {

    /** Поле, содержащее в себе максимальный размер графа*/
    private static final int N = 10;

    /** Поле, содержащее в себе граф*/
    private static int[][] graph = new int[N][N];

    /** Воспомогательное поле для метода dfs, хранит посещенные места */
    static int[] used;

    /** Воспомогательное поле для метода dfs, хранит флаг */
    static int flag;

    /** Воспомогательное поле для метода dfs, здесь окажется цикл*/
    static Vector<Integer> path = new Vector<Integer>();

    /** Конструктор
     * @param graph - матрица смежности ориентированного графа*/
    LoopChecker(int[][] graph) {
        LoopChecker.graph = graph;
        used = new int[N];
        for (int i = 0; i < N; i++) {
            used[i] = 0;
        }
    }

    /** Поиск в глубину*/
    static void dfs(int v) {
        if (flag == 1)
            return;
        used[v] = 1;
        path.add(v);
        for (int i = 0; i < N; i++) {
            if (graph[v][i] == 1) {
                if (used[i] == 1) {
                    path.add(i);
                    flag = 1;
                    return;
                } else{
                    dfs(i);
                }
            }
            if (flag == 1)
                return;
        }
        used[v] = 2;
        path.remove(path.size() - 1);
    }

    /** Ищет цикл
     * @param names принимает мэп, в котором хранится название файла для каждого из численных обозначений */
    Boolean findLoop(Map<Integer, String> names) {
        for (int i = 0; i < N; i++)
            if (used[i] == 0) {
                dfs(i);
                if (flag == 1)
                    break;
            }
        if (flag == 1) {
            int i = path.size() - 2;
            int to = path.lastElement();
            while (path.get(i) != to) i--;
            System.out.println("Loop:");
            for (; i < path.size() - 1; i++)
                System.out.print(names.get(path.get(i)) + " ");
            System.out.println();
            return true;
        } else {
            return false;
        }
    }

}
