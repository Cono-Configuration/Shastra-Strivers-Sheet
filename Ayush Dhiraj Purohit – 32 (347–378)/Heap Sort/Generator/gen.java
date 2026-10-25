import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class gen {

    static void heapSort(int[] nums) {
        int n = nums.length;
        if (n <= 1) {
            return;
        }

        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(nums, i, n);
        }

        for (int i = n - 1; i > 0; i--) {
            int temp = nums[0];
            nums[0] = nums[i];
            nums[i] = temp;

            siftDown(nums, 0, i);
        }
    }

    private static void siftDown(int[] nums, int i, int n) {
        while (2 * i + 1 < n) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            if (nums[left] > nums[largest]) {
                largest = left;
            }
            if (right < n && nums[right] > nums[largest]) {
                largest = right;
            }

            if (largest != i) {
                int temp = nums[i];
                nums[i] = nums[largest];
                nums[largest] = temp;
                i = largest;
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

    static void writeTestCase(File inFile, File outFile, int[] nums) throws IOException {
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(nums.length);
            for (int i = 0; i < nums.length; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(nums[i]);
            }
            inPw.println();
            inPw.flush();
        }

        int[] sorted = Arrays.copyOf(nums, nums.length);
        heapSort(sorted);

        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            for (int i = 0; i < sorted.length; i++) {
                if (i > 0) outPw.print(" ");
                outPw.print(sorted[i]);
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
        } else if (new File("Ayush Dhiraj Purohit – 32 (347–378)/Heap Sort").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit – 32 (347–378)/Heap Sort");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Heap Sort");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(7331);

        for (int test = 0; test < 100; test++) {
            int[] nums;

            if (test == 0) {
                // TUF Example 1
                nums = new int[]{7, 4, 1, 5, 3};
            } else if (test == 1) {
                // TUF Example 2
                nums = new int[]{5, 4, 4, 1, 1};
            } else if (test == 2) {
                // Example 3
                nums = new int[]{12, 11, 13, 5, 6, 7};
            } else if (test == 3) {
                // Minimal N = 1
                nums = new int[]{42};
            } else if (test == 4) {
                // Minimal N = 1 negative boundary
                nums = new int[]{-1000000000};
            } else if (test == 5) {
                // N = 2 unsorted
                nums = new int[]{10, 5};
            } else if (test == 6) {
                // N = 2 sorted
                nums = new int[]{5, 10};
            } else if (test == 7) {
                // N = 2 identical
                nums = new int[]{7, 7};
            } else if (test == 8) {
                // All zeros
                nums = new int[]{0, 0, 0, 0};
            } else if (test == 9) {
                // All negatives
                nums = new int[]{-5, -1, -3, -2, -4};
            } else if (test >= 10 && test < 30) {
                // Small cases: N from 3 to 30
                int n = 3 + (test - 10);
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
                if (test % 4 == 0) {
                    nums = generateRandomArray(n, rng, -10000, 10000);
                } else if (test % 4 == 1) {
                    // Heavy duplicates
                    nums = generateRandomArray(n, rng, 1, 5);
                } else if (test % 4 == 2) {
                    // Almost sorted
                    nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = i * 2;
                    for (int s = 0; s < 3; s++) {
                        int a = rng.nextInt(n);
                        int b = rng.nextInt(n);
                        int tmp = nums[a];
                        nums[a] = nums[b];
                        nums[b] = tmp;
                    }
                } else {
                    nums = generateRandomArray(n, rng, -100000000, 100000000);
                }
            } else if (test >= 60 && test < 80) {
                // Large cases: N from 1000 to 20000
                int n = 1000 + (test - 60) * 950;
                if (test % 3 == 0) {
                    nums = generateRandomArray(n, rng, -1000000000, 1000000000);
                } else if (test % 3 == 1) {
                    // Reverse sorted array
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
                    // Full scale random N = 100000
                    nums = generateRandomArray(n, rng, -1000000000, 1000000000);
                } else if (test == 91) {
                    // Already sorted 1 to N
                    nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = i + 1;
                } else if (test == 92) {
                    // Reverse sorted N down to 1
                    nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = n - i;
                } else if (test == 93) {
                    // All zeros
                    nums = new int[n];
                    Arrays.fill(nums, 0);
                } else if (test == 94) {
                    // All elements positive boundary 10^9
                    nums = new int[n];
                    Arrays.fill(nums, 1000000000);
                } else if (test == 95) {
                    // All elements negative boundary -10^9
                    nums = new int[n];
                    Arrays.fill(nums, -1000000000);
                } else if (test == 96) {
                    // Alternating -10^9 and 10^9
                    nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = (i % 2 == 0) ? -1000000000 : 1000000000;
                } else if (test == 97) {
                    // Large positive domain [999900000, 1000000000]
                    nums = generateRandomArray(n, rng, 999900000, 1000000000);
                } else if (test == 98) {
                    // Large negative domain [-1000000000, -999900000]
                    nums = generateRandomArray(n, rng, -1000000000, -999900000);
                } else if (test == 99) {
                    // Small range [0, 3] with heavy duplicates
                    nums = generateRandomArray(n, rng, 0, 3);
                } else {
                    if (test % 2 == 0) {
                        nums = generateRandomArray(n, rng, -1000000000, 1000000000);
                    } else {
                        nums = new int[n];
                        for (int i = 0; i < n; i++) nums[i] = -n + i * 2;
                    }
                }
            }

            String inName = String.format("input%02d.txt", test);
            String outName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inName);
            File outFile = new File(outDir, outName);

            writeTestCase(inFile, outFile, nums);
        }

        System.out.println("Generated exactly 100 test cases successfully.");
    }
}
