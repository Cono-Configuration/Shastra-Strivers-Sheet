import java.io.*;
import java.util.*;

public class gen1 {

    static final String BASE_DIR = "Testcases1";

    static int id = 0;

    // Signatures already written. Two cases with the same length and the same answer
    // exercise the same behaviour, so only the first one is kept.
    static Set<String> seen = new HashSet<>();

    public static void main(String[] args) throws Exception {

        new File(BASE_DIR + File.separator + "Input").mkdirs();
        new File(BASE_DIR + File.separator + "Output").mkdirs();

        // Examples and the "Now Your Turn" input
        add("123", 6);
        add("0232", 8);
        add("02", 0);

        // Every numeral of length 1 and 2, with the targets its expressions can reach
        // plus 0. Each numeral is checked for a target from its own answers.
        List<String> numerals = new ArrayList<>();
        for (char a = '0'; a <= '9'; a++) {
            numerals.add(String.valueOf(a));
            for (char b = '0'; b <= '9'; b++) {
                numerals.add("" + a + b);
            }
        }
        for (String s : numerals) {
            addWithReachableTargets(s);
        }

        // Random numerals of length 3 to 10, with many zeros to test the leading-zero rule
        Random rnd = new Random(188);
        for (int t = 0; t < 120; t++) {
            int len = 3 + rnd.nextInt(8);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                int d = rnd.nextInt(10);
                if (rnd.nextInt(4) == 0) d = 0;
                sb.append((char) ('0' + d));
            }
            addWithReachableTargets(sb.toString());
        }

        System.out.println("Generated " + id + " test cases.");
    }

    // Adds the numeral with a target that some expression reaches, and with target 0,
    // plus one random target in a small range.
    static void addWithReachableTargets(String numeral) throws Exception {
        Map<Long, List<String>> byValue = new TreeMap<>();
        enumerate(numeral, byValue);

        List<Long> values = new ArrayList<>(byValue.keySet());
        if (!values.isEmpty()) {
            add(numeral, values.get(values.size() / 2).intValue());
            add(numeral, values.get(0).intValue());
        }
        add(numeral, 0);
    }

    // Reference: try every choice of "no operator", '+', '-', or '*' between digits.
    // Keeps only expressions without leading zeros, evaluated with usual precedence.
    static void enumerate(String numeral, Map<Long, List<String>> byValue) {

        int n = numeral.length();
        int total = 1;
        for (int i = 0; i < n - 1; i++) total *= 4;

        for (int code = 0; code < total; code++) {

            List<String> operands = new ArrayList<>();
            List<Character> ops = new ArrayList<>();
            StringBuilder current = new StringBuilder();
            current.append(numeral.charAt(0));

            for (int i = 1; i < n; i++) {
                int g = (code >> (2 * (i - 1))) & 3;
                if (g == 0) {
                    current.append(numeral.charAt(i));
                } else {
                    operands.add(current.toString());
                    ops.add(g == 1 ? '+' : g == 2 ? '-' : '*');
                    current = new StringBuilder();
                    current.append(numeral.charAt(i));
                }
            }
            operands.add(current.toString());

            boolean valid = true;
            for (String s : operands) {
                if (s.length() > 1 && s.charAt(0) == '0') { valid = false; break; }
            }
            if (!valid) continue;

            long sum = 0;
            int sign = 1;
            long term = Long.parseLong(operands.get(0));
            for (int k = 0; k < ops.size(); k++) {
                long next = Long.parseLong(operands.get(k + 1));
                if (ops.get(k) == '*') {
                    term *= next;
                } else {
                    sum += sign * term;
                    sign = ops.get(k) == '+' ? 1 : -1;
                    term = next;
                }
            }
            sum += sign * term;

            StringBuilder expr = new StringBuilder();
            for (int k = 0; k < operands.size(); k++) {
                expr.append(operands.get(k));
                if (k < ops.size()) expr.append(ops.get(k));
            }

            byValue.computeIfAbsent(sum, x -> new ArrayList<>()).add(expr.toString());
        }
    }

    // Writes the case unless an equal-behaviour case was already written.
    static void add(String numeral, long desired) throws Exception {

        if (numeral.length() < 1 || numeral.length() > 10) {
            throw new IllegalStateException("length out of range");
        }
        for (char c : numeral.toCharArray()) {
            if (c < '0' || c > '9') throw new IllegalStateException("non-digit");
        }
        if (desired < -(1L << 31) || desired > (1L << 31) - 1) {
            throw new IllegalStateException("target out of range");
        }

        Map<Long, List<String>> byValue = new TreeMap<>();
        enumerate(numeral, byValue);
        List<String> hits = new ArrayList<>(byValue.getOrDefault(desired, new ArrayList<>()));
        Collections.sort(hits);

        // Behaviour signature: length, whether a zero digit is present (leading-zero rule),
        // whether the answer is empty, and whether the target is 0
        String signature = numeral.length()
                + "|" + (numeral.indexOf('0') >= 0 ? "zero" : "nozero")
                + "|" + (hits.isEmpty() ? "empty" : "found")
                + "|" + (desired == 0 ? "t0" : "tN");
        if (!seen.add(signature)) {
            return;
        }

        String inputFile = BASE_DIR + File.separator + "Input" + File.separator
                + String.format("input%03d.txt", id);
        String outputFile = BASE_DIR + File.separator + "Output" + File.separator
                + String.format("output%03d.txt", id);

        try (PrintWriter in = new PrintWriter(new FileWriter(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {
            in.print(numeral + "\r\n" + desired + "\r\n");
            for (String h : hits) {
                out.print(h + "\r\n");
            }
        }

        id++;
    }
}
