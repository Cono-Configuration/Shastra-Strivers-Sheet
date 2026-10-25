import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class gen {

    static void makeMinHeap(int[] nums) {
        int n = nums.length;
        for (int i = (n - 2) / 2; i >= 0; i--) {
            int curr = i;
            while (2 * curr + 1 < n) {
                int left = 2 * curr + 1;
                int right = 2 * curr + 2;
                int smallest = curr;

                if (nums[left] < nums[smallest]) {
                    smallest = left;
                }
                if (right < n && nums[right] < nums[smallest]) {
                    smallest = right;
                }

                if (smallest != curr) {
                    int tmp = nums[curr];
                    nums[curr] = nums[smallest];
                    nums[smallest] = tmp;
                    curr = smallest;
                } else {
                    break;
                }
            }
        }
    }

    static void convertMinToMaxHeap(int[] nums) {
        int n = nums.length;
        for (int i = (n - 2) / 2; i >= 0; i--) {
            siftDown(nums, i, n);
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

    static int[] generateRandomMinHeap(int n, Random rng, int minVal, int maxVal) {
        int[] arr = new int[n];
        long range = (long) maxVal - minVal + 1;
        for (int i = 0; i < n; i++) {
            arr[i] = minVal + (int) (Math.abs(rng.nextLong()) % range);
        }
        makeMinHeap(arr);
        return arr;
    }

    static void writeTestCase(File inFile, File outFile, int[] nums) throws IOException {
        // Write the input (which is guaranteed to be a valid min-heap)
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(nums.length);
            for (int i = 0; i < nums.length; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(nums[i]);
            }
            inPw.println();
            inPw.flush();
        }

        // Convert in-place to max-heap and write expected output
        int[] copy = Arrays.copyOf(nums, nums.length);
        convertMinToMaxHeap(copy);

        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            for (int i = 0; i < copy.length; i++) {
                if (i > 0) outPw.print(" ");
                outPw.print(copy[i]);
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
        } else if (new File("Ayush Dhiraj Purohit – 32 (347–378)/Convert Min Heap to Max Heap").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit – 32 (347–378)/Convert Min Heap to Max Heap");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Convert Min Heap to Max Heap");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(2026);

        for (int test = 0; test < 100; test++) {
            int[] nums;

            if (test == 0) {
                // TUF Example 1
                nums = new int[]{10, 20, 30, 21, 23};
            } else if (test == 1) {
                // TUF Example 2
                nums = new int[]{-5, -4, -3, -2, -1};
            } else if (test == 2) {
                // Example 3
                nums = new int[]{1, 2, 3, 4};
            } else if (test == 3) {
                // Minimal N = 1
                nums = new int[]{42};
            } else if (test == 4) {
                // Minimal N = 1 negative boundary
                nums = new int[]{-1000000000};
            } else if (test == 5) {
                // N = 2, strictly increasing
                nums = new int[]{5, 10};
            } else if (test == 6) {
                // N = 2, identical elements
                nums = new int[]{7, 7};
            } else if (test == 7) {
                // All zeros
                nums = new int[]{0, 0, 0};
            } else if (test == 8) {
                // Full 3-level tree of identical elements
                nums = new int[]{8, 8, 8, 8, 8, 8, 8};
            } else if (test == 9) {
                // Powers of 2
                nums = new int[]{1, 2, 4, 8, 16, 32, 64};
            } else if (test >= 10 && test < 30) {
                // Small cases: N from 3 to 30
                int n = 3 + (test - 10);
                if (test % 3 == 0) {
                    // Strictly increasing sorted array (naturally valid min-heap)
                    nums = new int[n];
                    int start = rng.nextInt(100) - 50;
                    for (int i = 0; i < n; i++) {
                        nums[i] = start + i * 2;
                    }
                } else if (test % 3 == 1) {
                    // Alternating negative and positive made into min-heap
                    nums = new int[n];
                    for (int i = 0; i < n; i++) {
                        nums[i] = (i % 2 == 0) ? -(i + 1) * 5 : (i + 1) * 5;
                    }
                    makeMinHeap(nums);
                } else {
                    // Random small min-heap
                    nums = generateRandomMinHeap(n, rng, -100, 100);
                }
            } else if (test >= 30 && test < 60) {
                // Mid-range cases: N from 40 to 600
                int n = 40 + (test - 30) * 19;
                if (test % 4 == 0) {
                    // Random min-heap with moderate values
                    nums = generateRandomMinHeap(n, rng, -10000, 10000);
                } else if (test % 4 == 1) {
                    // Heavy duplicates in small range
                    nums = generateRandomMinHeap(n, rng, 1, 5);
                } else if (test % 4 == 2) {
                    // Strictly sorted array 1 to N
                    nums = new int[n];
                    for (int i = 0; i < n; i++) nums[i] = i + 1;
                } else {
                    // Min-heap with wide range
                    nums = generateRandomMinHeap(n, rng, -100000000, 100000000);
                }
            } else if (test >= 60 && test < 80) {
                // Large cases: N from 1000 to 20000
                int n = 1000 + (test - 60) * 950;
                if (test % 3 == 0) {
                    nums = generateRandomMinHeap(n, rng, -1000000000, 1000000000);
                } else if (test % 3 == 1) {
                    // Full sorted array
                    nums = new int[n];
                    for (int i = 0; i < n; i++) {
                        nums[i] = -n / 2 + i;
                    }
                } else {
                    nums = generateRandomMinHeap(n, rng, -500, 500);
                }
            } else {
                // Boundary maximum constraint cases: N = 50000 to N = 100000
                int n = (test == 99 || test == 90 || test == 91 || test == 92 || test == 93 || test == 94 || test == 95 || test == 96 || test == 97 || test == 98) 
                        ? 100000 : (50000 + (test - 80) * 5000);

                if (test == 90) {
                    // Random min-heap of size 100000 with values [-10^9, 10^9]
                    nums = generateRandomMinHeap(n, rng, -1000000000, 1000000000);
                } else if (test == 91) {
                    // Strictly increasing sorted array 1, 2, ..., N (valid min-heap)
                    nums = new int[n];
                    for (int i = 0; i < n; i++) {
                        nums[i] = i + 1;
                    }
                } else if (test == 92) {
                    // All zeros
                    nums = new int[n];
                    Arrays.fill(nums, 0);
                } else if (test == 93) {
                    // All elements positive boundary 10^9
                    nums = new int[n];
                    Arrays.fill(nums, 1000000000);
                } else if (test == 94) {
                    // All elements negative boundary -10^9
                    nums = new int[n];
                    Arrays.fill(nums, -1000000000);
                } else if (test == 95) {
                    // Alternating -10^9 and 10^9 made into min-heap
                    nums = new int[n];
                    for (int i = 0; i < n; i++) {
                        nums[i] = (i % 2 == 0) ? -1000000000 : 1000000000;
                    }
                    makeMinHeap(nums);
                } else if (test == 96) {
                    // Large positive domain [999900000, 1000000000]
                    nums = generateRandomMinHeap(n, rng, 999900000, 1000000000);
                } else if (test == 97) {
                    // Large negative domain [-1000000000, -999900000]
                    nums = generateRandomMinHeap(n, rng, -1000000000, -999900000);
                } else if (test == 98) {
                    // Very small range with massive duplicates [0, 3]
                    nums = generateRandomMinHeap(n, rng, 0, 3);
                } else if (test == 99) {
                    // Dense uniform range [-5*10^8, 5*10^8]
                    nums = generateRandomMinHeap(n, rng, -500000000, 500000000);
                } else {
                    if (test % 2 == 0) {
                        nums = generateRandomMinHeap(n, rng, -1000000000, 1000000000);
                    } else {
                        nums = new int[n];
                        for (int i = 0; i < n; i++) {
                            nums[i] = -n + i * 2;
                        }
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
