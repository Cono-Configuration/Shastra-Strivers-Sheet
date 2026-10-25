import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class gen {

    static int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        int maxFreq = 0;

        for (char c : tasks) {
            freq[c - 'A']++;
            if (freq[c - 'A'] > maxFreq) {
                maxFreq = freq[c - 'A'];
            }
        }

        int countMax = 0;
        for (int i = 0; i < 26; i++) {
            if (freq[i] == maxFreq) {
                countMax++;
            }
        }

        int minIntervals = (maxFreq - 1) * (n + 1) + countMax;
        return Math.max(tasks.length, minIntervals);
    }

    static int leastIntervalHeap(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char c : tasks) freq[c - 'A']++;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int f : freq) {
            if (f > 0) pq.add(f);
        }

        int time = 0;
        List<Integer> temp = new ArrayList<>();

        while (!pq.isEmpty()) {
            temp.clear();
            int cycle = n + 1;
            int taskCount = 0;

            while (cycle > 0 && !pq.isEmpty()) {
                int current = pq.poll();
                if (current > 1) {
                    temp.add(current - 1);
                }
                taskCount++;
                cycle--;
            }

            for (int remaining : temp) {
                pq.add(remaining);
            }

            time += pq.isEmpty() ? taskCount : (n + 1);
        }

        return time;
    }

    static void writeTestCase(File inFile, File outFile, char[] tasks, int n) throws IOException {
        // Write input
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(tasks.length + " " + n);
            for (int i = 0; i < tasks.length; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(tasks[i]);
            }
            inPw.println();
            inPw.flush();
        }

        // Solve and dual verify
        int ansFormula = leastInterval(tasks, n);
        int ansHeap = leastIntervalHeap(tasks, n);

        if (ansFormula != ansHeap) {
            throw new IllegalStateException("Verification mismatch: formula (" + ansFormula + ") != heap (" + ansHeap + ")");
        }

        // Write output
        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            outPw.println(ansFormula);
            outPw.flush();
        }
    }

    static char[] generateRandomTasks(int len, Random rng, int numDistinct) {
        char[] tasks = new char[len];
        for (int i = 0; i < len; i++) {
            tasks[i] = (char) ('A' + rng.nextInt(numDistinct));
        }
        return tasks;
    }

    public static void main(String[] args) throws IOException {
        File cur = new File(".");
        File baseDir;
        if (new File(cur, "Testcases").exists() || new File(cur, "Generator").exists() || new File(cur, "Problem.md").exists()) {
            baseDir = cur;
        } else if (new File(cur, "../Testcases").exists() || new File(cur, "../Problem.md").exists()) {
            baseDir = new File(cur, "..");
        } else if (new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Task Scheduler").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Task Scheduler");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Task Scheduler");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(20261025);

        for (int test = 0; test < 100; test++) {
            char[] tasks;
            int n;

            if (test == 0) {
                // TUF Example 1
                tasks = new char[]{'A', 'A', 'A', 'B', 'B', 'B'};
                n = 2;
            } else if (test == 1) {
                // TUF Example 2
                tasks = new char[]{'A', 'C', 'A', 'B', 'D', 'B'};
                n = 1;
            } else if (test == 2) {
                // TUF Example 3
                tasks = new char[]{'A', 'A', 'A', 'B', 'B', 'B'};
                n = 3;
            } else if (test == 3) {
                // TUF Example 4
                tasks = new char[]{'A', 'A', 'B'};
                n = 0;
            } else if (test == 4) {
                // Single task
                tasks = new char[]{'A'};
                n = 0;
            } else if (test == 5) {
                // Single task max cooldown
                tasks = new char[]{'Z'};
                n = 100;
            } else if (test == 6) {
                // Two identical tasks n=0
                tasks = new char[]{'A', 'A'};
                n = 0;
            } else if (test == 7) {
                // Two identical tasks n=5
                tasks = new char[]{'A', 'A'};
                n = 5;
            } else if (test == 8) {
                // 26 distinct tasks n=2
                tasks = new char[26];
                for (int i = 0; i < 26; i++) tasks[i] = (char) ('A' + i);
                n = 2;
            } else if (test == 9) {
                // 26 distinct tasks n=50
                tasks = new char[26];
                for (int i = 0; i < 26; i++) tasks[i] = (char) ('A' + i);
                n = 50;
            } else if (test < 30) {
                // Small cases: len in [5, 50], n in [0, 20]
                int len = 5 + (test - 10) * 2;
                n = (test % 4 == 0) ? 0 : rng.nextInt(15);
                int numDistinct = 2 + rng.nextInt(6);
                tasks = generateRandomTasks(len, rng, numDistinct);
            } else if (test < 60) {
                // Medium cases: len in [100, 2000], n in [0, 50]
                int len = 100 + (test - 30) * 65;
                n = (test % 5 == 0) ? 0 : (1 + rng.nextInt(45));
                if (test % 4 == 0) {
                    // Skewed: 80% 'A'
                    tasks = new char[len];
                    int aCount = (int) (len * 0.8);
                    for (int i = 0; i < aCount; i++) tasks[i] = 'A';
                    for (int i = aCount; i < len; i++) tasks[i] = (char) ('B' + rng.nextInt(5));
                } else if (test % 4 == 1) {
                    // 2 distinct tasks
                    tasks = generateRandomTasks(len, rng, 2);
                } else {
                    tasks = generateRandomTasks(len, rng, 26);
                }
            } else if (test < 80) {
                // Large cases: len in [5000, 30000], n in [0, 100]
                int len = 5000 + (test - 60) * 1250;
                n = (test % 6 == 0) ? 0 : rng.nextInt(101);
                int distinct = (test % 3 == 0) ? 3 : (10 + rng.nextInt(17));
                tasks = generateRandomTasks(len, rng, distinct);
            } else {
                // Max constraint cases: len in [50000, 100000], n in [0, 100]
                int len;
                if (test < 86) {
                    len = 50000 + (test - 80) * 8000;
                } else {
                    len = 100000;
                }

                if (test == 86) {
                    n = 0;
                    tasks = generateRandomTasks(len, rng, 26);
                } else if (test == 87) {
                    n = 1;
                    tasks = generateRandomTasks(len, rng, 26);
                } else if (test == 88) {
                    n = 2;
                    tasks = generateRandomTasks(len, rng, 26);
                } else if (test == 89) {
                    n = 5;
                    tasks = generateRandomTasks(len, rng, 15);
                } else if (test == 90) {
                    n = 10;
                    tasks = generateRandomTasks(len, rng, 10);
                } else if (test == 91) {
                    n = 20;
                    tasks = generateRandomTasks(len, rng, 8);
                } else if (test == 92) {
                    n = 50;
                    tasks = generateRandomTasks(len, rng, 5);
                } else if (test == 93) {
                    // Max N = 100000, Max n = 100
                    n = 100;
                    tasks = generateRandomTasks(len, rng, 26);
                } else if (test == 94) {
                    // All identical tasks 'A'
                    n = 100;
                    tasks = new char[len];
                    for (int i = 0; i < len; i++) tasks[i] = 'A';
                } else if (test == 95) {
                    // Perfectly balanced 26 tasks
                    n = 100;
                    tasks = new char[len];
                    for (int i = 0; i < len; i++) tasks[i] = (char) ('A' + (i % 26));
                } else if (test == 96) {
                    // Two tasks tie for max frequency
                    n = 50;
                    tasks = new char[len];
                    for (int i = 0; i < len; i++) tasks[i] = (char) ('A' + (i % 2));
                } else if (test == 97) {
                    // 5 tasks tie for max frequency
                    n = 25;
                    tasks = new char[len];
                    for (int i = 0; i < len; i++) tasks[i] = (char) ('A' + (i % 5));
                } else {
                    n = rng.nextInt(101);
                    tasks = generateRandomTasks(len, rng, 26);
                }
            }

            String inFileName = String.format("input%02d.txt", test);
            String outFileName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inFileName);
            File outFile = new File(outDir, outFileName);

            writeTestCase(inFile, outFile, tasks, n);
        }

        System.out.println("Generated 100 testcases successfully.");
    }
}
