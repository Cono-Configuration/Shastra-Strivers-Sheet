import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";
    static final Random random = new Random(1021);

    public static void main(String[] args) throws Exception {

        File dir = new File(BASE_DIR);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        int testCase = 0;

        testCase = generateCase(testCase, "()");

        testCase = generateCase(testCase, "(())");

        testCase = generateCase(testCase, "()()");

        testCase = generateCase(testCase, "()()()");

        testCase = generateCase(testCase, "((()))");

        testCase = generateCase(testCase, "(((())))");

        testCase = generateCase(testCase, "((((()))))");

        testCase = generateCase(testCase, "((()))()");

        testCase = generateCase(testCase, "()((()))");

        testCase = generateCase(testCase, "((()))(())()");

        testCase = generateCase(testCase, "(()())");

        testCase = generateCase(testCase, "(()(()))");

        testCase = generateCase(testCase, "((())(()))");

        testCase = generateCase(testCase, "(()())(())(()(()))");

        testCase = generateCase(testCase, "(((())))");

        testCase = generateCase(testCase, "(()()())");

        testCase = generateCase(testCase, "((()())(()))");

        testCase = generateCase(testCase, repeat("()", 50));

        testCase = generateCase(testCase, repeat("()", 100));

        testCase = generateCase(testCase, repeat("()", 500));

        for (int depth = 10; depth <= 100; depth += 10) {
            testCase = generateCase(testCase, nested(depth));
        }

        for (int depth = 200; depth <= 2000; depth += 200) {
            testCase = generateCase(testCase, nested(depth));
        }

        for (int i = 0; i < 20; i++) {

            StringBuilder s = new StringBuilder();

            int parts = 5 + random.nextInt(20);

            for (int j = 0; j < parts; j++) {

                int depth = 1 + random.nextInt(20);

                s.append(randomPrimitive(depth));
            }

            testCase = generateCase(testCase, s.toString());
        }

        for (int i = 0; i < 50; i++) {

            int primitiveCount = 1 + random.nextInt(30);

            StringBuilder s = new StringBuilder();

            for (int j = 0; j < primitiveCount; j++) {

                int depth = 1 + random.nextInt(30);

                s.append(randomPrimitive(depth));
            }

            testCase = generateCase(testCase, s.toString());
        }

        for (int i = 0; i < 10; i++) {

            int primitiveCount = 100 + random.nextInt(300);

            StringBuilder s = new StringBuilder();

            for (int j = 0; j < primitiveCount; j++) {

                int depth = 1 + random.nextInt(100);

                s.append(randomPrimitive(depth));
            }

            testCase = generateCase(testCase, s.toString());
        }

        testCase = generateCase(testCase, repeat("()", 5000));

        testCase = generateCase(testCase, nested(5000));

        StringBuilder largeMixed = new StringBuilder();

        for (int i = 0; i < 1000; i++) {

            int depth = 1 + random.nextInt(50);

            largeMixed.append(randomPrimitive(depth));
        }

        testCase = generateCase(testCase, largeMixed.toString());

        for (int i = 0; i < 7; i++) {

            StringBuilder s = new StringBuilder();

            int parts = 500 + random.nextInt(1000);

            for (int j = 0; j < parts; j++) {

                int depth = 1 + random.nextInt(20);

                s.append(randomPrimitive(depth));
            }

            testCase = generateCase(testCase, s.toString());
        }

        System.out.println("Generated " + testCase + " test cases.");
    }

    static int generateCase(int id, String input) throws Exception {

        String output = solve(input);

        String inputFile =
                BASE_DIR + File.separator +
                String.format("input%02d.txt", id);

        String outputFile =
                BASE_DIR + File.separator +
                String.format("output%02d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            in.println(input);
            out.println(output);
        }

        return id + 1;
    }

    static String solve(String S) {

        StringBuilder result = new StringBuilder();

        int depth = 0;

        for (char ch : S.toCharArray()) {

            if (ch == '(') {

                if (depth > 0) {
                    result.append(ch);
                }

                depth++;

            } else {

                depth--;

                if (depth > 0) {
                    result.append(ch);
                }
            }
        }

        return result.toString();
    }

    static String nested(int depth) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < depth; i++) {
            s.append('(');
        }

        for (int i = 0; i < depth; i++) {
            s.append(')');
        }

        return s.toString();
    }

    static String randomPrimitive(int maxDepth) {

        StringBuilder s = new StringBuilder();

        int depth = 0;

        s.append('(');
        depth++;

        int operations = 1 + random.nextInt(maxDepth * 2);

        for (int i = 0; i < operations; i++) {

            if (depth < maxDepth && random.nextBoolean()) {
                s.append('(');
                depth++;
            } else if (depth > 1) {
                s.append(')');
                depth--;
            } else {
                s.append('(');
                depth++;
            }
        }

        while (depth > 1) {
            s.append(')');
            depth--;
        }

        s.append(')');

        return s.toString();
    }

    static String repeat(String str, int count) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(str);
        }

        return result.toString();
    }
}