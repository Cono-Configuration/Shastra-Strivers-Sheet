import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.Random;

public class gen {

    private static void siftDown(long[] heap, int i, int size) {
        while (2 * i + 1 < size) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (heap[left] < heap[smallest]) {
                smallest = left;
            }
            if (right < size && heap[right] < heap[smallest]) {
                smallest = right;
            }

            if (smallest != i) {
                long tmp = heap[i];
                heap[i] = heap[smallest];
                heap[smallest] = tmp;
                i = smallest;
            } else {
                break;
            }
        }
    }

    static long connectSticks(int[] sticks) {
        int n = sticks.length;
        if (n <= 1) return 0L;

        long[] heap = new long[n];
        for (int i = 0; i < n; i++) heap[i] = sticks[i];

        int size = n;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(heap, i, size);
        }

        long totalCost = 0L;
        while (size > 1) {
            long first = heap[0];
            heap[0] = heap[size - 1];
            size--;
            siftDown(heap, 0, size);

            long second = heap[0];
            long cost = first + second;
            totalCost += cost;

            heap[0] = cost;
            siftDown(heap, 0, size);
        }

        return totalCost;
    }

    static long connectSticksPQ(int[] sticks) {
        if (sticks == null || sticks.length <= 1) return 0L;
        PriorityQueue<Long> pq = new PriorityQueue<>(sticks.length);
        for (int s : sticks) pq.add((long) s);
        long total = 0L;
        while (pq.size() > 1) {
            long a = pq.poll();
            long b = pq.poll();
            long cost = a + b;
            total += cost;
            pq.add(cost);
        }
        return total;
    }

    static void writeTestCase(File inFile, File outFile, int[] sticks) throws IOException {
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(sticks.length);
            for (int i = 0; i < sticks.length; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(sticks[i]);
            }
            inPw.println();
            inPw.flush();
        }

        long resHeap = connectSticks(sticks);
        long resPQ = connectSticksPQ(sticks);

        if (resHeap != resPQ) {
            throw new IllegalStateException("Verification mismatch: resHeap (" + resHeap + ") != resPQ (" + resPQ + ")");
        }

        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            outPw.println(resHeap);
            outPw.flush();
        }
    }

    static int[] generateRandomArray(int n, Random rng, int minVal, int maxVal) {
        int[] arr = new int[n];
        long range = (long) maxVal - minVal + 1;
        for (int i = 0; i < n; i++) {
            arr[i] = minVal + (int) (Math.abs(rng.nextLong()) % range);
        }
        return arr;
    }

    public static void main(String[] args) throws IOException {
        File cur = new File(".");
        File baseDir;
        if (new File(cur, "Testcases").exists() || new File(cur, "Generator").exists() || new File(cur, "Problem.md").exists()) {
            baseDir = cur;
        } else if (new File(cur, "../Testcases").exists() || new File(cur, "../Problem.md").exists()) {
            baseDir = new File(cur, "..");
        } else if (new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Minimum Cost to Connect Sticks").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Minimum Cost to Connect Sticks");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Minimum Cost to Connect Sticks");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(20261025);

        for (int test = 0; test < 100; test++) {
            int[] sticks;

            if (test == 0) {
                // TUF Example 1
                sticks = new int[]{2, 4, 3};
            } else if (test == 1) {
                // TUF Example 2
                sticks = new int[]{1, 8, 3, 5};
            } else if (test == 2) {
                // TUF Example 3
                sticks = new int[]{5};
            } else if (test == 3) {
                // TUF Example 4
                sticks = new int[]{4, 3, 2, 6, 1, 5};
            } else if (test == 4) {
                // Single stick extreme
                sticks = new int[]{1000000000};
            } else if (test == 5) {
                // N=2 distinct
                sticks = new int[]{10, 20};
            } else if (test == 6) {
                // N=2 identical
                sticks = new int[]{15, 15};
            } else if (test == 7) {
                // N=4 all identical
                sticks = new int[]{5, 5, 5, 5};
            } else if (test == 8) {
                // N=8 powers of 2 count
                sticks = new int[]{2, 2, 2, 2, 2, 2, 2, 2};
            } else if (test == 9) {
                // Fibonacci numbers
                sticks = new int[]{1, 2, 3, 5, 8};
            } else if (test < 30) {
                // Small cases N in [3, 50]
                int n = 3 + (test - 10) * 2;
                if (test % 4 == 0) {
                    sticks = new int[n];
                    for (int i = 0; i < n; i++) sticks[i] = i + 1;
                } else if (test % 4 == 1) {
                    sticks = new int[n];
                    for (int i = 0; i < n; i++) sticks[i] = n - i;
                } else {
                    sticks = generateRandomArray(n, rng, 1, 500);
                }
            } else if (test < 60) {
                // Medium cases N in [100, 2000]
                int n = 100 + (test - 30) * 65;
                if (test % 4 == 0) {
                    sticks = new int[n];
                    for (int i = 0; i < n; i++) sticks[i] = (i % 5) + 1;
                } else {
                    sticks = generateRandomArray(n, rng, 1, 100000);
                }
            } else if (test < 80) {
                // Large cases N in [5000, 30000]
                int n = 5000 + (test - 60) * 1250;
                sticks = generateRandomArray(n, rng, 1, 1000000000);
            } else {
                // Max constraint cases N in [50000, 100000]
                int n;
                if (test < 85) {
                    n = 50000 + (test - 80) * 7500;
                } else {
                    n = 100000;
                }

                if (test == 85) {
                    // All 1s
                    sticks = new int[n];
                    for (int i = 0; i < n; i++) sticks[i] = 1;
                } else if (test == 86) {
                    // All 10^4
                    sticks = new int[n];
                    for (int i = 0; i < n; i++) sticks[i] = 10000;
                } else if (test == 87) {
                    // All 10^9
                    sticks = new int[n];
                    for (int i = 0; i < n; i++) sticks[i] = 1000000000;
                } else if (test == 88) {
                    // 1 to N
                    sticks = new int[n];
                    for (int i = 0; i < n; i++) sticks[i] = i + 1;
                } else if (test == 89) {
                    // N down to 1
                    sticks = new int[n];
                    for (int i = 0; i < n; i++) sticks[i] = n - i;
                } else if (test == 90) {
                    sticks = generateRandomArray(n, rng, 1, 100);
                } else if (test == 91) {
                    sticks = generateRandomArray(n, rng, 1, 10000);
                } else if (test == 92) {
                    sticks = generateRandomArray(n, rng, 1, 1000000000);
                } else if (test == 95) {
                    // 90% ones, 10% large
                    sticks = new int[n];
                    int ones = (int) (n * 0.9);
                    for (int i = 0; i < ones; i++) sticks[i] = 1;
                    for (int i = ones; i < n; i++) sticks[i] = 1000000 + rng.nextInt(1000000);
                } else {
                    sticks = generateRandomArray(n, rng, 1, 1000000000);
                }
            }

            String inFileName = String.format("input%02d.txt", test);
            String outFileName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inFileName);
            File outFile = new File(outDir, outFileName);

            writeTestCase(inFile, outFile, sticks);
        }

        System.out.println("Generated 100 testcases successfully.");
    }
}
