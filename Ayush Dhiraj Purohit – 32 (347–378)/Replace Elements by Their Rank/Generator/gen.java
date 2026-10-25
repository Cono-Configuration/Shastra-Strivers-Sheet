import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class gen {

    private static void siftDown(int[] heapVal, int[] heapIdx, int i, int n) {
        while (2 * i + 1 < n) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (heapVal[left] < heapVal[smallest]) {
                smallest = left;
            }
            if (right < n && heapVal[right] < heapVal[smallest]) {
                smallest = right;
            }

            if (smallest != i) {
                int tmpVal = heapVal[i];
                heapVal[i] = heapVal[smallest];
                heapVal[smallest] = tmpVal;

                int tmpIdx = heapIdx[i];
                heapIdx[i] = heapIdx[smallest];
                heapIdx[smallest] = tmpIdx;

                i = smallest;
            } else {
                break;
            }
        }
    }

    static int[] replaceWithRank(int[] arr) {
        int n = arr.length;
        if (n == 0) return new int[0];

        int[] heapVal = new int[n];
        int[] heapIdx = new int[n];

        for (int i = 0; i < n; i++) {
            heapVal[i] = arr[i];
            heapIdx[i] = i;
        }

        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(heapVal, heapIdx, i, n);
        }

        int[] result = new int[n];
        int rank = 0;
        long prevVal = Long.MIN_VALUE;
        int heapSize = n;

        while (heapSize > 0) {
            int val = heapVal[0];
            int idx = heapIdx[0];

            if (val != prevVal) {
                rank++;
                prevVal = val;
            }

            result[idx] = rank;

            heapVal[0] = heapVal[heapSize - 1];
            heapIdx[0] = heapIdx[heapSize - 1];
            heapSize--;

            if (heapSize > 0) {
                siftDown(heapVal, heapIdx, 0, heapSize);
            }
        }

        return result;
    }

    static int[] solveWithSort(int[] arr) {
        int n = arr.length;
        if (n == 0) return new int[0];
        int[] temp = arr.clone();
        Arrays.sort(temp);
        int[] unique = new int[n];
        unique[0] = temp[0];
        int u = 1;
        for (int i = 1; i < n; i++) {
            if (temp[i] != temp[i - 1]) {
                unique[u++] = temp[i];
            }
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = Arrays.binarySearch(unique, 0, u, arr[i]) + 1;
        }
        return res;
    }

    static int[] generateRandomArray(int n, Random rng, int minVal, int maxVal) {
        int[] arr = new int[n];
        long range = (long) maxVal - minVal + 1;
        for (int i = 0; i < n; i++) {
            arr[i] = minVal + (int) (Math.abs(rng.nextLong()) % range);
        }
        return arr;
    }

    static void writeTestCase(File inFile, File outFile, int[] arr) throws IOException {
        int n = arr.length;

        // Write input
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(n);
            for (int i = 0; i < n; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(arr[i]);
            }
            inPw.println();
            inPw.flush();
        }

        // Solve and dual verify
        int[] heapRanks = replaceWithRank(arr);
        int[] sortRanks = solveWithSort(arr);

        if (!Arrays.equals(heapRanks, sortRanks)) {
            throw new IllegalStateException("Verification failure: heapRanks != sortRanks");
        }

        // Write output
        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            for (int i = 0; i < n; i++) {
                if (i > 0) outPw.print(" ");
                outPw.print(heapRanks[i]);
            }
            outPw.println();
            outPw.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        File cur = new File(".");
        File baseDir;
        if (new File(cur, "Testcases").exists() || new File(cur, "Generator").exists() || new File(cur, "Problem.md").exists()) {
            baseDir = cur;
        } else if (new File(cur, "../Testcases").exists() || new File(cur, "../Problem.md").exists()) {
            baseDir = new File(cur, "..");
        } else if (new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Replace Elements by Their Rank").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Replace Elements by Their Rank");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Replace Elements by Their Rank");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(20261025);

        for (int test = 0; test < 100; test++) {
            int[] arr;

            if (test == 0) {
                // TUF Example 1
                arr = new int[]{20, 15, 26, 2, 98, 6};
            } else if (test == 1) {
                // TUF Example 2
                arr = new int[]{1, 5, 8, 15, 8, 25, 9};
            } else if (test == 2) {
                // TUF Example 3
                arr = new int[]{100, 100, 100, 100};
            } else if (test == 3) {
                // TUF Example 4
                arr = new int[]{37, -9, 12, -9, -9};
            } else if (test == 4) {
                // Single element positive
                arr = new int[]{42};
            } else if (test == 5) {
                // Single element minimum constraint
                arr = new int[]{-1000000000};
            } else if (test == 6) {
                // Single element maximum constraint
                arr = new int[]{1000000000};
            } else if (test == 7) {
                // N=2 distinct
                arr = new int[]{10, 5};
            } else if (test == 8) {
                // N=2 identical
                arr = new int[]{5, 5};
            } else if (test == 9) {
                // All negative with duplicates
                arr = new int[]{-10, -5, -20, -5, -30, -10, -5, -40};
            } else if (test < 30) {
                // Small cases N in [3, 50]
                int n = 3 + (test - 10) * 2;
                if (test % 4 == 0) {
                    // Strictly increasing
                    arr = new int[n];
                    for (int i = 0; i < n; i++) arr[i] = i * 10 - 100;
                } else if (test % 4 == 1) {
                    // Strictly decreasing
                    arr = new int[n];
                    for (int i = 0; i < n; i++) arr[i] = (n - i) * 5;
                } else if (test % 4 == 2) {
                    // Binary values 0 and 1
                    arr = generateRandomArray(n, rng, 0, 1);
                } else {
                    // Mixed
                    arr = generateRandomArray(n, rng, -500, 500);
                }
            } else if (test < 60) {
                // Medium cases N in [100, 2000]
                int n = 100 + (test - 30) * 65;
                if (test % 5 == 0) {
                    // Narrow range: massive duplicates
                    arr = generateRandomArray(n, rng, -5, 5);
                } else if (test % 5 == 1) {
                    // All distinct values
                    arr = new int[n];
                    for (int i = 0; i < n; i++) arr[i] = i - n / 2;
                    // Shuffle
                    for (int i = n - 1; i > 0; i--) {
                        int j = rng.nextInt(i + 1);
                        int tmp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = tmp;
                    }
                } else {
                    arr = generateRandomArray(n, rng, -100000, 100000);
                }
            } else if (test < 80) {
                // Large cases N in [5000, 30000]
                int n = 5000 + (test - 60) * 1250;
                if (test % 4 == 0) {
                    // Narrow range
                    arr = generateRandomArray(n, rng, -50, 50);
                } else {
                    arr = generateRandomArray(n, rng, -1000000000, 1000000000);
                }
            } else {
                // Max constraint cases N in [50000, 100000]
                int n;
                if (test < 85) {
                    n = 50000 + (test - 80) * 7500;
                } else {
                    n = 100000;
                }

                if (test == 85) {
                    // All identical
                    arr = new int[n];
                    Arrays.fill(arr, 7);
                } else if (test == 86) {
                    // All distinct sorted
                    arr = new int[n];
                    for (int i = 0; i < n; i++) arr[i] = i + 1;
                } else if (test == 87) {
                    // All distinct reverse sorted
                    arr = new int[n];
                    for (int i = 0; i < n; i++) arr[i] = n - i;
                } else if (test == 88) {
                    // Random across full range
                    arr = generateRandomArray(n, rng, -1000000000, 1000000000);
                } else if (test == 89) {
                    // Heavy duplicates
                    arr = generateRandomArray(n, rng, -100, 100);
                } else if (test == 90) {
                    // All negative
                    arr = generateRandomArray(n, rng, -1000000000, -1);
                } else if (test == 91) {
                    // Strictly non-negative
                    arr = generateRandomArray(n, rng, 0, 1000000000);
                } else if (test == 92) {
                    // Alternating few values
                    arr = new int[n];
                    for (int i = 0; i < n; i++) arr[i] = (i % 5) * 1000;
                } else if (test == 93) {
                    // Sawtooth pattern
                    arr = new int[n];
                    for (int i = 0; i < n; i++) arr[i] = (i % 100) - 50;
                } else if (test == 94) {
                    // Extreme values near boundary
                    arr = new int[n];
                    for (int i = 0; i < n; i++) {
                        arr[i] = (i % 2 == 0) ? (-1000000000 + (i % 100)) : (1000000000 - (i % 100));
                    }
                } else if (test == 95) {
                    // Clustered
                    arr = new int[n];
                    for (int i = 0; i < n; i++) {
                        int cluster = (i / 1000) * 10000;
                        arr[i] = cluster + rng.nextInt(50);
                    }
                } else if (test == 96) {
                    // 50% identical, 50% unique
                    arr = new int[n];
                    for (int i = 0; i < n / 2; i++) arr[i] = 42;
                    for (int i = n / 2; i < n; i++) arr[i] = i * 3;
                } else {
                    arr = generateRandomArray(n, rng, -1000000000, 1000000000);
                }
            }

            String inFileName = String.format("input%02d.txt", test);
            String outFileName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inFileName);
            File outFile = new File(outDir, outFileName);

            writeTestCase(inFile, outFile, arr);
        }

        System.out.println("Generated 100 testcases successfully.");
    }
}
