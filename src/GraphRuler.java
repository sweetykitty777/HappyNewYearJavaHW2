import java.util.Map;
import java.util.Vector;

public class GraphRuler {
    private static final int N = 10;
    private static int[][] graph = new int[N][N];

    static int[] used;
    static int flag;
    static Vector<Integer> path = new Vector<Integer>();

    GraphRuler(int[][] graph) {
        GraphRuler.graph = graph;
        used = new int[N];
        for (int i = 0; i < N; i++) {
            used[i] = 0;
        }
    }

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
