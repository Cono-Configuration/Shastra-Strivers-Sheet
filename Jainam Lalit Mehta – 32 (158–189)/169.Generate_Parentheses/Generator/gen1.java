import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        int testCase = 0;

        // The constraint is 1 <= n <= 8, so there are exactly 8 possible inputs.
        // Every one of them is included, from the minimum (1) to the maximum (8).
        for (int pairs = 1; pairs <= 8; pairs++) {
            testCase = generateCase(testCase, pairs);
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    static int generateCase(int id, int pairs) throws Exception {

        List<String> answer = new ArrayList<>();
        build(answer, new StringBuilder(), 0, 0, pairs);

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%02d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%02d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.print(pairs + "\r\n");

            for (String s : answer) {
                out.print(s + "\r\n");
            }
        }

        return id + 1;
    }

    static void build(List<String> result, StringBuilder current, int opened, int closed, int pairs) {

        if (current.length() == 2 * pairs) {
            result.add(current.toString());
            return;
        }

        if (opened < pairs) {
            current.append('(');
            build(result, current, opened + 1, closed, pairs);
            current.deleteCharAt(current.length() - 1);
        }

        if (closed < opened) {
            current.append(')');
            build(result, current, opened, closed + 1, pairs);
            current.deleteCharAt(current.length() - 1);
        }
    }
}
