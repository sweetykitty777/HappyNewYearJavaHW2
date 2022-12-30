import java.util.ArrayList;
import java.util.Vector;

public class GraphRuler {
    private static int N = 10;
    private static int[][] graph = new int[N][N];

    static int used[], flag, n, m;

    static Vector<Integer> path = new Vector<Integer>();

    GraphRuler(int[][] graph) {
        this.graph = graph;
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
            int to = graph[v][i];
            if (used[to] == 1) {
                path.add(to);
                flag = 1;
                return;
            }
            else
                dfs(to);
            if (flag == 1)
                return;
        }
        used[v] = 2;
        path.remove(path.size() - 1);
    }

    Boolean findLoop() {
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
            System.out.println("looop!");
            for (; i < path.size() - 1; i++)
                System.out.print(path.get(i) + " ");
            System.out.println();
            return true;
        } else {
            return false;
        }
    }

}
