import java.util.Scanner;
public class Main {

    /** Точка входа*/
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            FileExplorer a = new FileExplorer();
            a.createGraph();
            a.solve();
            String answer;
            System.out.println("Repeat? y to agree/n or anything to leave");
            answer = in.next();
            if (!answer.equals("y")) {
                break;
            }
        }
        in.close();
    }
}