public class TopologicalSort {
    static int N = 10;
    private static int[][] graph = new int[N][N];
   static int used[] = new int [N];
    int [] ans = new int[N];
    int last = -1;
    public TopologicalSort(int[][] gr) {
        graph = gr;
    }

    void dfs (int v) {
        used[v] = 1;
        for (int i=0; i < N; ++i) {
            if (graph[v][i] == 1) {
                int to = i;
                if (used[to] == 0) {
                    dfs(to);
                }
            }
        }
        ans[++last] = v;
    }

    int[] topological_sort() {
        for (int i = 0; i < N; ++i)
            used[i] = 0;
        for (int i = 0; i < N; ++i)
            if (used[i] == 0)
                dfs(i);
        for (int i = 0; i < N; i++) {
            System.out.print(ans[i] + " ");
        }
        return ans;
    }
}
