import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class gen {

    private static void shuffle(int[] arr, Random rng) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    private static void siftDown(long[] heapSum, int[] heapI, int[] heapJ, int i, int size) {
        while (2 * i + 1 < size) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            if (heapSum[left] > heapSum[largest]) {
                largest = left;
            }
            if (right < size && heapSum[right] > heapSum[largest]) {
                largest = right;
            }

            if (largest != i) {
                long tmpSum = heapSum[i];
                heapSum[i] = heapSum[largest];
                heapSum[largest] = tmpSum;

                int tmpI = heapI[i];
                heapI[i] = heapI[largest];
                heapI[largest] = tmpI;

                int tmpJ = heapJ[i];
                heapJ[i] = heapJ[largest];
                heapJ[largest] = tmpJ;

                i = largest;
            } else {
                break;
            }
        }
    }

    public static long[] maxCombinations(int[] nums1, int[] nums2, int k) {
        int n = nums1.length;
        int[] a = nums1.clone();
        int[] b = nums2.clone();
        Random rng = new Random(42);
        shuffle(a, rng);
        Arrays.sort(a);
        shuffle(b, rng);
        Arrays.sort(b);

        int m = Math.min(n, k);
        long[] heapSum = new long[m];
        int[] heapI = new int[m];
        int[] heapJ = new int[m];

        for (int idx = 0; idx < m; idx++) {
            int i = n - 1 - idx;
            heapSum[idx] = (long) a[i] + b[n - 1];
            heapI[idx] = i;
            heapJ[idx] = n - 1;
        }

        int size = m;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(heapSum, heapI, heapJ, i, size);
        }

        long[] result = new long[k];
        for (int step = 0; step < k; step++) {
            result[step] = heapSum[0];
            int i = heapI[0];
            int j = heapJ[0];

            if (j > 0) {
                heapSum[0] = (long) a[i] + b[j - 1];
                heapJ[0] = j - 1;
                siftDown(heapSum, heapI, heapJ, 0, size);
            } else {
                heapSum[0] = heapSum[size - 1];
                heapI[0] = heapI[size - 1];
                heapJ[0] = heapJ[size - 1];
                size--;
                if (size > 0) {
                    siftDown(heapSum, heapI, heapJ, 0, size);
                }
            }
        }

        return result;
    }

    // Brute force solver for small verification
    public static long[] bruteForce(int[] nums1, int[] nums2, int k) {
        int n = nums1.length;
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                list.add((long) nums1[i] + nums2[j]);
            }
        }
        list.sort(Collections.reverseOrder());
        long[] res = new long[k];
        for (int i = 0; i < k; i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    // Classic PriorityQueue + Visited Set solver for medium verification
    static class Node implements Comparable<Node> {
        long sum;
        int i, j;
        Node(long sum, int i, int j) {
            this.sum = sum;
            this.i = i;
            this.j = j;
        }
        @Override
        public int compareTo(Node o) {
            return Long.compare(o.sum, this.sum); // Max-heap
        }
    }

    public static long[] pqVisitedSolver(int[] nums1, int[] nums2, int k) {
        int n = nums1.length;
        int[] a = nums1.clone();
        int[] b = nums2.clone();
        Random rng = new Random(123);
        shuffle(a, rng);
        Arrays.sort(a);
        shuffle(b, rng);
        Arrays.sort(b);

        // Reverse to descending order
        for (int l = 0, r = n - 1; l < r; l++, r--) {
            int t = a[l]; a[l] = a[r]; a[r] = t;
        }
        for (int l = 0, r = n - 1; l < r; l++, r--) {
            int t = b[l]; b[l] = b[r]; b[r] = t;
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        HashSet<Long> visited = new HashSet<>();

        pq.add(new Node((long) a[0] + b[0], 0, 0));
        visited.add(0L);

        long[] result = new long[k];
        for (int step = 0; step < k; step++) {
            Node top = pq.poll();
            result[step] = top.sum;

            if (top.i + 1 < n) {
                long key1 = (((long) (top.i + 1)) << 32) | (long) top.j;
                if (!visited.contains(key1)) {
                    visited.add(key1);
                    pq.add(new Node((long) a[top.i + 1] + b[top.j], top.i + 1, top.j));
                }
            }

            if (top.j + 1 < n) {
                long key2 = (((long) top.i) << 32) | (long) (top.j + 1);
                if (!visited.contains(key2)) {
                    visited.add(key2);
                    pq.add(new Node((long) a[top.i] + b[top.j + 1], top.i, top.j + 1));
                }
            }
        }
        return result;
    }

    private static void writeTestCase(File inDir, File outDir, int testIdx, int n, int k, int[] nums1, int[] nums2) throws IOException {
        String baseName = String.format("input%02d.txt", testIdx);
        String outName = String.format("output%02d.txt", testIdx);
        File inFile = new File(inDir, baseName);
        File outFile = new File(outDir, outName);

        // Solve using optimal solver
        long[] result = maxCombinations(nums1, nums2, k);

        // Verification against brute force or visited set
        if ((long) n * n <= 20000L) {
            long[] brute = bruteForce(nums1, nums2, k);
            if (!Arrays.equals(result, brute)) {
                throw new RuntimeException("Mismatch with brute force on test " + testIdx);
            }
        } else if (n <= 2000 && k <= 2000) {
            long[] pqRes = pqVisitedSolver(nums1, nums2, k);
            if (!Arrays.equals(result, pqRes)) {
                throw new RuntimeException("Mismatch with PQ-visited solver on test " + testIdx);
            }
        }

        // Write input
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(n + " " + k);
            for (int i = 0; i < n; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(nums1[i]);
            }
            inPw.println();
            for (int i = 0; i < n; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(nums2[i]);
            }
            inPw.println();
            inPw.flush();
        }

        // Write output
        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            for (int i = 0; i < k; i++) {
                if (i > 0) outPw.print(" ");
                outPw.print(result[i]);
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
        } else if (new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Maximum Sum Combination").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Maximum Sum Combination");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Maximum Sum Combination");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(20261025);

        for (int test = 0; test < 100; test++) {
            int n;
            int k;
            int[] nums1;
            int[] nums2;

            if (test == 0) {
                // TUF Example 1
                n = 2; k = 2;
                nums1 = new int[]{7, 3};
                nums2 = new int[]{1, 6};
            } else if (test == 1) {
                // TUF Example 2
                n = 3; k = 2;
                nums1 = new int[]{3, 4, 5};
                nums2 = new int[]{2, 6, 3};
            } else if (test == 2) {
                // Example 3
                n = 4; k = 4;
                nums1 = new int[]{1, 4, 2, 3};
                nums2 = new int[]{2, 5, 1, 6};
            } else if (test == 3) {
                // Example 4 (minimal 1, 1)
                n = 1; k = 1;
                nums1 = new int[]{10};
                nums2 = new int[]{20};
            } else if (test == 4) {
                // Minimal negative extreme
                n = 1; k = 1;
                nums1 = new int[]{-1000000000};
                nums2 = new int[]{1000000000};
            } else if (test == 5) {
                // All identical elements
                n = 6; k = 10;
                nums1 = new int[]{5, 5, 5, 5, 5, 5};
                nums2 = new int[]{3, 3, 3, 3, 3, 3};
            } else if (test == 6) {
                // All negative numbers
                n = 4; k = 5;
                nums1 = new int[]{-5, -2, -9, -1};
                nums2 = new int[]{-4, -8, -1, -3};
            } else if (test == 7) {
                // K = 1 minimum retrieval
                n = 10; k = 1;
                nums1 = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
                nums2 = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            } else if (test == 8) {
                // K = N * N full combination retrieval
                n = 3; k = 9;
                nums1 = new int[]{1, 2, 3};
                nums2 = new int[]{4, 5, 6};
            } else if (test == 9) {
                // Extreme bounds positive and negative
                n = 5; k = 7;
                nums1 = new int[]{1000000000, -1000000000, 0, 500, -500};
                nums2 = new int[]{1000000000, -1000000000, 1, -1, 0};
            } else if (test < 30) {
                // Small cases: n in [2, 40], k in [1, min(n*n, 50)]
                n = 2 + rng.nextInt(39);
                long maxK = Math.min((long) n * n, 50L);
                k = 1 + rng.nextInt((int) maxK);
                nums1 = new int[n];
                nums2 = new int[n];

                int mode = test % 5;
                if (mode == 0) {
                    // Small numbers [-20, 20]
                    for (int i = 0; i < n; i++) {
                        nums1[i] = -20 + rng.nextInt(41);
                        nums2[i] = -20 + rng.nextInt(41);
                    }
                } else if (mode == 1) {
                    // Sorted / reverse sorted
                    for (int i = 0; i < n; i++) {
                        nums1[i] = i * 5;
                        nums2[i] = 100 - i * 3;
                    }
                } else if (mode == 2) {
                    // Duplicates
                    int v1 = rng.nextInt(100);
                    int v2 = rng.nextInt(100);
                    for (int i = 0; i < n; i++) {
                        nums1[i] = (rng.nextBoolean() ? v1 : v1 + 1);
                        nums2[i] = (rng.nextBoolean() ? v2 : v2 - 1);
                    }
                } else if (mode == 3) {
                    // Wide range [-10^8, 10^8]
                    for (int i = 0; i < n; i++) {
                        nums1[i] = -100000000 + rng.nextInt(200000001);
                        nums2[i] = -100000000 + rng.nextInt(200000001);
                    }
                } else {
                    // Single huge maximum outlier
                    for (int i = 0; i < n; i++) {
                        nums1[i] = rng.nextInt(10);
                        nums2[i] = rng.nextInt(10);
                    }
                    nums1[rng.nextInt(n)] = 1000000;
                    nums2[rng.nextInt(n)] = 2000000;
                }
            } else if (test < 60) {
                // Medium cases: n in [50, 1000], k in [10, 1000]
                n = 50 + rng.nextInt(951);
                long maxK = Math.min((long) n * n, 1000L);
                k = 10 + rng.nextInt((int) maxK - 9);
                nums1 = new int[n];
                nums2 = new int[n];

                int mode = test % 5;
                if (mode == 0) {
                    // Pure uniform random [-10^6, 10^6]
                    for (int i = 0; i < n; i++) {
                        nums1[i] = -1000000 + rng.nextInt(2000001);
                        nums2[i] = -1000000 + rng.nextInt(2000001);
                    }
                } else if (mode == 1) {
                    // Highly duplicated numbers
                    for (int i = 0; i < n; i++) {
                        nums1[i] = rng.nextInt(5);
                        nums2[i] = rng.nextInt(5);
                    }
                } else if (mode == 2) {
                    // Arithmetic progression
                    int start1 = rng.nextInt(1000);
                    int step1 = 1 + rng.nextInt(10);
                    int start2 = rng.nextInt(1000);
                    int step2 = 1 + rng.nextInt(10);
                    for (int i = 0; i < n; i++) {
                        nums1[i] = start1 + i * step1;
                        nums2[i] = start2 + i * step2;
                    }
                } else if (mode == 3) {
                    // Clustered around zeros and large extremes
                    for (int i = 0; i < n; i++) {
                        if (rng.nextBoolean()) {
                            nums1[i] = rng.nextInt(100) - 50;
                        } else {
                            nums1[i] = (rng.nextBoolean() ? 1000000000 - rng.nextInt(1000) : -1000000000 + rng.nextInt(1000));
                        }
                        if (rng.nextBoolean()) {
                            nums2[i] = rng.nextInt(100) - 50;
                        } else {
                            nums2[i] = (rng.nextBoolean() ? 1000000000 - rng.nextInt(1000) : -1000000000 + rng.nextInt(1000));
                        }
                    }
                } else {
                    // All positive values [1, 10^9]
                    for (int i = 0; i < n; i++) {
                        nums1[i] = 1 + rng.nextInt(1000000000);
                        nums2[i] = 1 + rng.nextInt(1000000000);
                    }
                }
            } else if (test < 80) {
                // Large cases: n in [2000, 20000], k in [100, 5000]
                n = 2000 + rng.nextInt(18001);
                k = 100 + rng.nextInt(4901);
                nums1 = new int[n];
                nums2 = new int[n];

                int mode = test % 4;
                if (mode == 0) {
                    // Full range random [-10^9, 10^9]
                    for (int i = 0; i < n; i++) {
                        nums1[i] = (int) (rng.nextLong() % 1000000001L);
                        nums2[i] = (int) (rng.nextLong() % 1000000001L);
                    }
                } else if (mode == 1) {
                    // Many ties
                    for (int i = 0; i < n; i++) {
                        nums1[i] = rng.nextInt(20);
                        nums2[i] = rng.nextInt(20);
                    }
                } else if (mode == 2) {
                    // All negative numbers [-10^9, -1]
                    for (int i = 0; i < n; i++) {
                        nums1[i] = -1 - (int) (Math.abs(rng.nextLong()) % 1000000000L);
                        nums2[i] = -1 - (int) (Math.abs(rng.nextLong()) % 1000000000L);
                    }
                } else {
                    // Dense values with occasional extremes
                    for (int i = 0; i < n; i++) {
                        nums1[i] = rng.nextInt(10000);
                        nums2[i] = rng.nextInt(10000);
                    }
                    nums1[0] = 1000000000;
                    nums2[0] = 1000000000;
                }
            } else {
                // Max constraint cases: n in [50000, 100000], k up to 10^5
                if (test == 80) {
                    n = 50000; k = 50000;
                } else if (test == 85) {
                    n = 80000; k = 80000;
                } else if (test == 90 || test == 99) {
                    n = 100000; k = 100000;
                } else {
                    n = 50000 + rng.nextInt(50001);
                    k = 1000 + rng.nextInt(Math.min(n, 100000) - 999);
                }

                nums1 = new int[n];
                nums2 = new int[n];

                int mode = test % 5;
                if (mode == 0) {
                    // Large random [-10^9, 10^9]
                    for (int i = 0; i < n; i++) {
                        nums1[i] = (int) (rng.nextLong() % 1000000001L);
                        nums2[i] = (int) (rng.nextLong() % 1000000001L);
                    }
                } else if (mode == 1) {
                    // All identical values
                    int v1 = 7777777;
                    int v2 = 8888888;
                    Arrays.fill(nums1, v1);
                    Arrays.fill(nums2, v2);
                } else if (mode == 2) {
                    // Already sorted in ascending order
                    for (int i = 0; i < n; i++) {
                        nums1[i] = i * 2;
                        nums2[i] = i * 3;
                    }
                } else if (mode == 3) {
                    // Sorted in descending order
                    for (int i = 0; i < n; i++) {
                        nums1[i] = (n - i) * 5;
                        nums2[i] = (n - i) * 4;
                    }
                } else {
                    // Extreme positive boundary
                    for (int i = 0; i < n; i++) {
                        nums1[i] = 1000000000 - (i % 100);
                        nums2[i] = 1000000000 - ((i * 2) % 100);
                    }
                }
            }

            writeTestCase(inDir, outDir, test, n, k, nums1, nums2);
        }

        System.out.println("Generated 100 test cases successfully.");
    }
}
