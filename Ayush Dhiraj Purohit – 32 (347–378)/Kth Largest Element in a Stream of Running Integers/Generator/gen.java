import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class gen {

    static class KthLargestSimulator {
        private final int k;
        private final int[] heap;
        private int size;

        KthLargestSimulator(int k, int[] nums) {
            this.k = k;
            this.heap = new int[k];
            this.size = 0;

            for (int num : nums) {
                addInternal(num);
            }
        }

        private void addInternal(int val) {
            if (size < k) {
                heap[size] = val;
                siftUp(size);
                size++;
            } else if (val > heap[0]) {
                heap[0] = val;
                siftDown(0);
            }
        }

        int add(int val) {
            addInternal(val);
            return heap[0];
        }

        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap[i] < heap[parent]) {
                    int temp = heap[i];
                    heap[i] = heap[parent];
                    heap[parent] = temp;
                    i = parent;
                } else {
                    break;
                }
            }
        }

        private void siftDown(int i) {
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
                    int temp = heap[i];
                    heap[i] = heap[smallest];
                    heap[smallest] = temp;
                    i = smallest;
                } else {
                    break;
                }
            }
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

    static void writeTestCase(File inFile, File outFile, int k, int[] nums, int[] queries) throws IOException {
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(k + " " + nums.length);
            for (int i = 0; i < nums.length; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(nums[i]);
            }
            inPw.println();
            inPw.println(queries.length);
            for (int i = 0; i < queries.length; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(queries[i]);
            }
            inPw.println();
            inPw.flush();
        }

        KthLargestSimulator sim = new KthLargestSimulator(k, nums);
        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            for (int i = 0; i < queries.length; i++) {
                if (i > 0) outPw.print(" ");
                outPw.print(sim.add(queries[i]));
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
        } else if (new File("Ayush Dhiraj Purohit – 32 (347–378)/Kth Largest Element in a Stream of Running Integers").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit – 32 (347–378)/Kth Largest Element in a Stream of Running Integers");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Kth Largest Element in a Stream of Running Integers");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(54321);

        for (int test = 0; test < 100; test++) {
            int k;
            int[] nums;
            int[] queries;

            if (test == 0) {
                // TUF Example 1
                k = 3;
                nums = new int[]{4, 5, 8, 2};
                queries = new int[]{3, 5, 10, 9, 4};
            } else if (test == 1) {
                // Example 2: empty initial nums
                k = 1;
                nums = new int[0];
                queries = new int[]{5, 10, 2, 12};
            } else if (test == 2) {
                // Example 3: zeros
                k = 2;
                nums = new int[]{0, 0};
                queries = new int[]{-1, 0, 1};
            } else if (test == 3) {
                // Minimal negative boundary
                k = 1;
                nums = new int[0];
                queries = new int[]{-1000000000};
            } else if (test == 4) {
                // Minimal positive boundary
                k = 1;
                nums = new int[0];
                queries = new int[]{1000000000};
            } else if (test == 5) {
                // K = 1 running max
                k = 1;
                nums = new int[]{10, 20, 5};
                queries = new int[]{30, 25, 40, 15};
            } else if (test == 6) {
                // Initial nums has size < K
                k = 3;
                nums = new int[]{10};
                queries = new int[]{20, 30, 40, 50};
            } else if (test == 7) {
                // All identical values
                k = 3;
                nums = new int[]{7, 7, 7, 7};
                queries = new int[]{7, 7, 7, 7, 7};
            } else if (test == 8) {
                // All negative numbers
                k = 2;
                nums = new int[]{-50, -40, -30};
                queries = new int[]{-20, -60, -10, -5};
            } else if (test == 9) {
                // Alternating positive and negative
                k = 2;
                nums = new int[]{-10, 10};
                queries = new int[]{-20, 20, -30, 30};
            } else if (test >= 10 && test < 30) {
                // Small cases
                int n = rng.nextInt(15);
                int q = 5 + rng.nextInt(20);
                k = 1 + rng.nextInt(Math.max(1, n + 1));
                if (test % 3 == 0) {
                    // Strictly increasing queries
                    nums = generateRandomArray(n, rng, -50, 50);
                    queries = new int[q];
                    int start = 100;
                    for (int i = 0; i < q; i++) queries[i] = start + i * 5;
                } else if (test % 3 == 1) {
                    // Strictly decreasing queries
                    nums = generateRandomArray(n, rng, 50, 150);
                    queries = new int[q];
                    int start = -50;
                    for (int i = 0; i < q; i++) queries[i] = start - i * 5;
                } else {
                    nums = generateRandomArray(n, rng, -100, 100);
                    queries = generateRandomArray(q, rng, -100, 100);
                }
            } else if (test >= 30 && test < 60) {
                // Mid-range cases
                int n = 30 + (test - 30) * 10;
                int q = 100 + (test - 30) * 25;
                k = (test % 4 == 0) ? 1 : ((test % 4 == 1) ? 10 : (1 + rng.nextInt(n + 1)));

                if (test % 3 == 0) {
                    nums = generateRandomArray(n, rng, -10000, 10000);
                    queries = generateRandomArray(q, rng, -10000, 10000);
                } else if (test % 3 == 1) {
                    // Heavy duplicates
                    nums = generateRandomArray(n, rng, 1, 5);
                    queries = generateRandomArray(q, rng, 1, 5);
                } else {
                    nums = generateRandomArray(n, rng, -100000000, 100000000);
                    queries = generateRandomArray(q, rng, -100000000, 100000000);
                }
            } else if (test >= 60 && test < 80) {
                // Large cases
                int n = 1000 + (test - 60) * 450;
                int q = 5000 + (test - 60) * 500;
                k = (test % 3 == 0) ? 50 : ((test % 3 == 1) ? 500 : (1 + rng.nextInt(n)));

                nums = generateRandomArray(n, rng, -1000000000, 1000000000);
                queries = generateRandomArray(q, rng, -1000000000, 1000000000);
            } else {
                // Boundary maximum constraint cases (up to 100,000 queries)
                if (test == 90) {
                    // K = 50000, N = 50000, Q = 50000
                    k = 50000;
                    nums = generateRandomArray(50000, rng, -1000000000, 1000000000);
                    queries = generateRandomArray(50000, rng, -1000000000, 1000000000);
                } else if (test == 91) {
                    // K = 1, N = 0, Q = 100000 (running maximum stress)
                    k = 1;
                    nums = new int[0];
                    queries = generateRandomArray(100000, rng, -1000000000, 1000000000);
                } else if (test == 92) {
                    // K = 99999, N = 99999, Q = 1
                    k = 99999;
                    nums = generateRandomArray(99999, rng, -1000000000, 1000000000);
                    queries = new int[]{0};
                } else if (test == 93) {
                    // Strictly increasing queries with large Q = 70000 (sift-down stress)
                    k = 20000;
                    nums = generateRandomArray(30000, rng, -1000000000, 0);
                    queries = new int[70000];
                    for (int i = 0; i < queries.length; i++) queries[i] = i * 10;
                } else if (test == 94) {
                    // Strictly decreasing queries with large Q = 70000 (O(1) discard stress)
                    k = 20000;
                    nums = generateRandomArray(30000, rng, 0, 1000000000);
                    queries = new int[70000];
                    for (int i = 0; i < queries.length; i++) queries[i] = -i * 10;
                } else if (test == 95) {
                    // All zeros
                    k = 10000;
                    nums = new int[20000];
                    queries = new int[80000];
                } else if (test == 96) {
                    // Alternating -10^9 and 10^9
                    k = 25000;
                    nums = new int[25000];
                    for (int i = 0; i < nums.length; i++) nums[i] = (i % 2 == 0) ? -1000000000 : 1000000000;
                    queries = new int[50000];
                    for (int i = 0; i < queries.length; i++) queries[i] = (i % 2 == 0) ? 1000000000 : -1000000000;
                } else if (test == 97) {
                    // High positive domain
                    k = 500;
                    nums = generateRandomArray(10000, rng, 999900000, 1000000000);
                    queries = generateRandomArray(90000, rng, 999900000, 1000000000);
                } else if (test == 98) {
                    // High negative domain
                    k = 500;
                    nums = generateRandomArray(10000, rng, -1000000000, -999900000);
                    queries = generateRandomArray(90000, rng, -1000000000, -999900000);
                } else if (test == 99) {
                    // Heavy duplicates
                    k = 10000;
                    nums = generateRandomArray(20000, rng, 0, 3);
                    queries = generateRandomArray(80000, rng, 0, 3);
                } else {
                    k = 1000;
                    nums = generateRandomArray(10000, rng, -1000000000, 1000000000);
                    queries = generateRandomArray(40000, rng, -1000000000, 1000000000);
                }
            }

            String inName = String.format("input%02d.txt", test);
            String outName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inName);
            File outFile = new File(outDir, outName);

            writeTestCase(inFile, outFile, k, nums, queries);
        }

        System.out.println("Generated exactly 100 test cases successfully.");
    }
}
