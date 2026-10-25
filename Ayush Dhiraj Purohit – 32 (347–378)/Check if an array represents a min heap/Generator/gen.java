import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class gen {

    static boolean isMinHeap(int[] nums) {
        int n = nums.length;
        for (int i = 0; i <= (n - 2) / 2; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < n && nums[i] > nums[left]) {
                return false;
            }
            if (right < n && nums[i] > nums[right]) {
                return false;
            }
        }
        return true;
    }

    static int[] generateValidHeap(int n, Random rng, int minVal, int maxVal) {
        int[] arr = new int[n];
        long range = (long) maxVal - minVal + 1;
        for (int i = 0; i < n; i++) {
            arr[i] = minVal + (int) (Math.abs(rng.nextLong()) % range);
        }
        // Bottom-up min-heapify
        for (int i = (n - 2) / 2; i >= 0; i--) {
            int curr = i;
            while (2 * curr + 1 < n) {
                int left = 2 * curr + 1;
                int right = 2 * curr + 2;
                int smallest = curr;

                if (arr[left] < arr[smallest]) {
                    smallest = left;
                }
                if (right < n && arr[right] < arr[smallest]) {
                    smallest = right;
                }

                if (smallest != curr) {
                    int tmp = arr[curr];
                    arr[curr] = arr[smallest];
                    arr[smallest] = tmp;
                    curr = smallest;
                } else {
                    break;
                }
            }
        }
        return arr;
    }

    static void writeTestCase(File inFile, File outFile, int[] nums) throws IOException {
        boolean expected = isMinHeap(nums);

        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(nums.length);
            for (int i = 0; i < nums.length; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(nums[i]);
            }
            inPw.println();
            inPw.flush();
        }

        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            outPw.println(expected ? "true" : "false");
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
        } else if (new File("Ayush Dhiraj Purohit – 32 (347–378)/Check if an array represents a min heap").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit – 32 (347–378)/Check if an array represents a min heap");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Check if an array represents a min heap");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(1337);

        for (int test = 0; test < 100; test++) {
            int[] nums;

            if (test == 0) {
                // TUF Example 1
                nums = new int[]{10, 20, 30, 21, 23};
            } else if (test == 1) {
                // TUF Example 2
                nums = new int[]{10, 20, 30, 25, 15};
            } else if (test == 2) {
                // Example 3
                nums = new int[]{1, 3, 2, 4, 5, 6};
            } else if (test == 3) {
                // Minimal N = 1
                nums = new int[]{42};
            } else if (test == 4) {
                // Minimal N = 1 negative boundary
                nums = new int[]{-1000000000};
            } else if (test == 5) {
                // N = 2, valid
                nums = new int[]{5, 10};
            } else if (test == 6) {
                // N = 2, invalid
                nums = new int[]{10, 5};
            } else if (test == 7) {
                // All duplicate values
                nums = new int[]{7, 7, 7, 7, 7, 7, 7};
            } else if (test == 8) {
                // All negative, valid
                nums = new int[]{-100, -80, -70, -60, -50, -40};
            } else if (test == 9) {
                // All negative, invalid at root
                nums = new int[]{-50, -100, -80};
            } else if (test >= 10 && test < 25) {
                // Small cases: N from 3 to 25
                int n = 3 + (test - 10);
                if (test % 3 == 0) {
                    // Strictly increasing sorted array (always valid min-heap)
                    nums = new int[n];
                    int start = rng.nextInt(100) - 50;
                    for (int i = 0; i < n; i++) {
                        nums[i] = start + i * 3;
                    }
                } else if (test % 3 == 1) {
                    // Strictly decreasing reverse-sorted (invalid)
                    nums = new int[n];
                    int start = 100 + rng.nextInt(100);
                    for (int i = 0; i < n; i++) {
                        nums[i] = start - i * 2;
                    }
                } else {
                    // Valid heap with a potential planted violation at the last element
                    nums = generateValidHeap(n, rng, -1000, 1000);
                    if (test % 2 == 0 && n > 1) {
                        int parent = (n - 1 - 1) / 2;
                        nums[n - 1] = nums[parent] - 5; // planted violation at last leaf
                    }
                }
            } else if (test >= 25 && test < 50) {
                // Mid-range cases: N from 30 to 500
                int n = 30 + (test - 25) * 20;
                if (test % 4 == 0) {
                    // 100% valid heap
                    nums = generateValidHeap(n, rng, -100000, 100000);
                } else if (test % 4 == 1) {
                    // Valid heap with violation near root
                    nums = generateValidHeap(n, rng, -100000, 100000);
                    if (n > 2) {
                        nums[2] = nums[0] - 10;
                    }
                } else if (test % 4 == 2) {
                    // Valid heap with violation deep in tree
                    nums = generateValidHeap(n, rng, -100000, 100000);
                    int target = n - 1 - rng.nextInt(Math.min(5, n / 2));
                    int parent = (target - 1) / 2;
                    nums[target] = nums[parent] - 1;
                } else {
                    // Completely random array
                    nums = new int[n];
                    for (int i = 0; i < n; i++) {
                        nums[i] = rng.nextInt(200001) - 100000;
                    }
                }
            } else if (test >= 50 && test < 80) {
                // Large cases: N from 1000 to 20000
                int n = 1000 + (test - 50) * 650;
                if (test % 4 == 0) {
                    // Valid heap with wide range
                    nums = generateValidHeap(n, rng, -1000000000, 1000000000);
                } else if (test % 4 == 1) {
                    // Valid heap with many duplicates
                    nums = generateValidHeap(n, rng, 1, 10);
                } else if (test % 4 == 2) {
                    // Valid heap with subtle single violation at the exact middle
                    nums = generateValidHeap(n, rng, -1000000, 1000000);
                    int mid = n / 2;
                    int parent = (mid - 1) / 2;
                    nums[mid] = nums[parent] - 1;
                } else {
                    // Sorted array with one inverted adjacent pair near the end
                    nums = new int[n];
                    for (int i = 0; i < n; i++) {
                        nums[i] = i * 2;
                    }
                    if (test % 2 == 1 && n > 10) {
                        int pos = n - 2;
                        int parent = (pos - 1) / 2;
                        nums[pos] = nums[parent] - 10;
                    }
                }
            } else {
                // Boundary maximum constraint cases: N = 50000 to N = 100000
                int n = (test == 99 || test == 90 || test == 91 || test == 92 || test == 93 || test == 94 || test == 95 || test == 96 || test == 97 || test == 98) 
                        ? 100000 : (50000 + (test - 80) * 5000);

                if (test == 90) {
                    // Valid heap with N = 100000
                    nums = generateValidHeap(n, rng, -1000000000, 1000000000);
                } else if (test == 91) {
                    // Strictly increasing sorted array of size 100000 (valid)
                    nums = new int[n];
                    for (int i = 0; i < n; i++) {
                        nums[i] = -50000 + i;
                    }
                } else if (test == 92) {
                    // All zeros (valid)
                    nums = new int[n];
                    Arrays.fill(nums, 0);
                } else if (test == 93) {
                    // Valid heap of size 100000 with violation at the VERY LAST ELEMENT
                    nums = generateValidHeap(n, rng, -500000000, 500000000);
                    int parent = (n - 1 - 1) / 2;
                    nums[n - 1] = nums[parent] - 1;
                } else if (test == 94) {
                    // Valid heap of size 100000 with violation at index 1 (root's left child)
                    nums = generateValidHeap(n, rng, -500000000, 500000000);
                    nums[1] = nums[0] - 1;
                } else if (test == 95) {
                    // Strictly decreasing reverse-sorted array of size 100000 (invalid)
                    nums = new int[n];
                    for (int i = 0; i < n; i++) {
                        nums[i] = 100000 - i;
                    }
                } else if (test == 96) {
                    // Alternating -10^9 and 10^9
                    nums = new int[n];
                    for (int i = 0; i < n; i++) {
                        nums[i] = (i % 2 == 0) ? -1000000000 : 1000000000;
                    }
                } else if (test == 97) {
                    // Valid heap near positive boundary [999900000, 1000000000]
                    nums = generateValidHeap(n, rng, 999900000, 1000000000);
                } else if (test == 98) {
                    // Valid heap near negative boundary [-1000000000, -999900000]
                    nums = generateValidHeap(n, rng, -1000000000, -999900000);
                } else if (test == 99) {
                    // Valid heap with violation at the last internal node index (n - 2) / 2
                    nums = generateValidHeap(n, rng, -1000000000, 1000000000);
                    int lastInternal = (n - 2) / 2;
                    int leftChild = 2 * lastInternal + 1;
                    nums[leftChild] = nums[lastInternal] - 1;
                } else {
                    // Mixed large test cases
                    if (test % 2 == 0) {
                        nums = generateValidHeap(n, rng, -1000000000, 1000000000);
                    } else {
                        nums = generateValidHeap(n, rng, -1000000000, 1000000000);
                        int idx = rng.nextInt(n / 2) + n / 2;
                        int parent = (idx - 1) / 2;
                        nums[idx] = nums[parent] - 100;
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
