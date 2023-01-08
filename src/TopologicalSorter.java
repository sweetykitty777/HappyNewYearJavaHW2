public class TopologicalSorter {
    /** Поле, содержащее в себе максимальный размер графа*/
    static int N = 10;

    /** Поле, содержащее в себе граф*/
    private static int[][] graph = new int[N][N];

    /** Воспомогательное поле для метода dfs, хранит посещенные места */
    static int[] used = new int [N];

    /** Хранит отсортированные вершины*/
    int[] ans = new int[N];

    /** Считает количество добавленных чисел в ответ**/
    int last = -1;
    /** Топологический сортировщик
     * @param gr - матрица смежности ориентированного графа*/
    public TopologicalSorter(int[][] gr) {
        graph = gr;
    }

    /** Поиск в глубину*/
    void dfs(int v) {
        used[v] = 1;
        for (int i = 0; i < N; ++i) {
            if (graph[v][i] == 1) {
                if (used[i] == 0) {
                    dfs(i);
                }
            }
        }
        ans[++last] = v;
    }

    /** Топологическая сортировка
     * @return номера вершин в отсортированном порядке */
    int[] topologicalSort() {
        for (int i = 0; i < N; ++i)
            used[i] = 0;
        for (int i = 0; i < N; ++i)
            if (used[i] == 0)
                dfs(i);
        return ans;
    }
}
