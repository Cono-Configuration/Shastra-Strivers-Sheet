import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class gen {

    static int findKthLargest(int[] nums, int k) {
        int n = nums.length;
        int[] heap = new int[k];
        for (int i = 0; i < k; i++) {
            heap[i] = nums[i];
        }

        for (int i = k / 2 - 1; i >= 0; i--) {
            siftDown(heap, i, k);
        }

        for (int i = k; i < n; i++) {
            if (nums[i] > heap[0]) {
                heap[0] = nums[i];
                siftDown(heap, 0, k);
            }
        }

        return heap[0];
    }

    private static void siftDown(int[] heap, int i, int n) {
        while (2 * i + 1 < n) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (heap[left] < heap[smallest]) {
                smallest = left;
            }
            if (right < n && heap[right] < heap[smallest]) {
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

    static int[] generateRandomArray(int n, Random rng, int minVal, int maxVal) {
        int[] arr = new int[n];
        long range = (long) maxVal - minVal + 1;
        for (int i = 0; i < n; i++) {
            arr[i] = minVal + (int) (Math.abs(rng.nextLong()) % range);
        }
        return arr;
    }

    static void writeTestCase(File inFile, File outFile, int[] nums, int k) throws IOException {
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(nums.length + " " + k);
            for (int i = 0; i < nums.length; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(nums[i]);
            }
            inPw.println();
            inPw.flush();
        }

        int answer = findKthLargest(nums, k);

        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            outPw.println(answer);
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
        } else if (new File("Ayush Dhiraj Purohit – 32 (347–378)/K-th Largest Element in an Array").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit – 32 (347–378)/K-th Largest Element in an Array");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/K-th Largest Element in an Array");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(8888);

        for (int test = 0; test < 100; test++) {
            int[] nums;
            int k;

            if (test == 0) {
                // TUF Example 1
                nums = new int[]{1, 2, 3, 4, 5};
                k = 2;
            } else if (test == 1) {
                // TUF Example 2
                nums = new int[]{-5, 4, 1, 2, -3};
                k = 5;
            } else if (test == 2) {
                // TUF Example 3
                nums = new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6};
                k = 4;
            } else if (test == 3) {
                // Minimal N = 1, K = 1
                nums = new int[]{42};
                k = 1;
            } else if (test == 4) {
                // Minimal N = 1, K = 1 negative boundary
                nums = new int[]{-1000000000};
                k = 1;
            } else if (test == 5) {
                // N = 2, K = 1 (find max)
                nums = new int[]{10, 5};
                k = 1;
            } else if (test == 6) {
                // N = 2, K = 2 (find min)
                nums = new int[]{5, 10};
                k = 2;
            } else if (test == 7) {
                // All identical elements
                nums = new int[]{7, 7, 7, 7, 7};
                k = 3;
            } else if (test == 8) {
                // All zeros
                nums = new int[]{0, 0, 0, 0};
                k = 2;
            } else if (test == 9) {
                // All negatives
                nums = new int[]{-5, -1, -3, -2, -4};
                k = 3;
            } else if (test >= 10 && test < 30) {
                // Small cases: N from 3 to 30
                int n = 3 + (test - 10);
                k = 1 + rng.nextInt(n);
                if (test % 4 == 0) {
                    // Strictly increasing sorted
                    nums = new int[n];
                    int start = rng.nextInt(50) - 25;
                    for (int i = 0; i < n; i++) nums[i] = start + i * 2;
                } else if (test % 4 == 1) {
                    // Strictly decreasing reverse-sorted
                    nums = new int[n];
                    int start = 100;
                    for (int i = 0; i < n; i++) nums[i] = start - i * 3;
                } else if (test % 4 == 2) {
                    // Alternating signs
                    nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = (i % 2 == 0) ? -(i + 1) * 7 : (i + 1) * 7;
                } else {
                    nums = generateRandomArray(n, rng, -100, 100);
                }
            } else if (test >= 30 && test < 60) {
                // Mid-range cases: N from 40 to 600
                int n = 40 + (test - 30) * 19;
                if (test % 5 == 0) {
                    k = 1; // maximum element
                } else if (test % 5 == 1) {
                    k = n; // minimum element
                } else if (test % 5 == 2) {
                    k = n / 2; // median
                } else {
                    k = 1 + rng.nextInt(n);
                }

                if (test % 3 == 0) {
                    nums = generateRandomArray(n, rng, -10000, 10000);
                } else if (test % 3 == 1) {
                    nums = generateRandomArray(n, rng, 1, 5); // heavy duplicates
                } else {
                    nums = generateRandomArray(n, rng, -100000000, 100000000);
                }
            } else if (test >= 60 && test < 80) {
                // Large cases: N from 1000 to 20000
                int n = 1000 + (test - 60) * 950;
                k = (test % 4 == 0) ? 1 : ((test % 4 == 1) ? n : (1 + rng.nextInt(n)));

                if (test % 3 == 0) {
                    nums = generateRandomArray(n, rng, -1000000000, 1000000000);
                } else if (test % 3 == 1) {
                    nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = n - i;
                } else {
                    nums = generateRandomArray(n, rng, -500, 500);
                }
            } else {
                // Boundary maximum constraint cases: N = 50000 to N = 100000
                int n = (test == 99 || test == 90 || test == 91 || test == 92 || test == 93 || test == 94 || test == 95 || test == 96 || test == 97 || test == 98) 
                        ? 100000 : (50000 + (test - 80) * 5000);

                if (test == 90) {
                    nums = generateRandomArray(n, rng, -1000000000, 1000000000);
                    k = 50000;
                } else if (test == 91) {
                    nums = generateRandomArray(n, rng, -1000000000, 1000000000);
                    k = 1; // max
                } else if (test == 92) {
                    nums = generateRandomArray(n, rng, -1000000000, 1000000000);
                    k = n; // min
                } else if (test == 93) {
                    // Already sorted 1 to N
                    nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = i + 1;
                    k = 25000;
                } else if (test == 94) {
                    // Reverse sorted N down to 1
                    nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = n - i;
                    k = 75000;
                } else if (test == 95) {
                    // All zeros
                    nums = new int[n];
                    Arrays.fill(nums, 0);
                    k = 12345;
                } else if (test == 96) {
                    // Alternating -10^9 and 10^9
                    nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = (i % 2 == 0) ? -1000000000 : 1000000000;
                    k = 99999;
                } else if (test == 97) {
                    // Large positive domain [999900000, 1000000000]
                    nums = generateRandomArray(n, rng, 999900000, 1000000000);
                    k = 500;
                } else if (test == 98) {
                    // Large negative domain [-1000000000, -999900000]
                    nums = generateRandomArray(n, rng, -1000000000, -999900000);
                    k = 99500;
                } else if (test == 99) {
                    // Small range [0, 3] with heavy duplicates
                    nums = generateRandomArray(n, rng, 0, 3);
                    k = 45678;
                } else {
                    nums = generateRandomArray(n, rng, -1000000000, 1000000000);
                    k = 1 + rng.nextInt(n);
                }
            }

            String inName = String.format("input%02d.txt", test);
            String outName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inName);
            File outFile = new File(outDir, outName);

            writeTestCase(inFile, outFile, nums, k);
        }

        System.out.println("Generated exactly 100 test cases successfully.");
    }
}
