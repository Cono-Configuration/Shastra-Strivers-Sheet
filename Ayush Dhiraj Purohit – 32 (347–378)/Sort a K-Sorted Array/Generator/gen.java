import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class gen {

    static void siftUp(int[] heap, int i) {
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

    static void siftDown(int[] heap, int i, int n) {
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

    static void sortKSorted(int[] nums, int k) {
        int n = nums.length;
        if (n <= 1 || k == 0) return;

        int heapCapacity = Math.min(k + 1, n);
        int[] heap = new int[heapCapacity];
        int heapSize = 0;
        int writeIdx = 0;

        for (int i = 0; i < n; i++) {
            if (heapSize < heapCapacity) {
                heap[heapSize] = nums[i];
                siftUp(heap, heapSize);
                heapSize++;
            } else {
                nums[writeIdx++] = heap[0];
                heap[0] = nums[i];
                siftDown(heap, 0, heapSize);
            }
        }

        while (heapSize > 0) {
            nums[writeIdx++] = heap[0];
            heap[0] = heap[heapSize - 1];
            heapSize--;
            if (heapSize > 0) {
                siftDown(heap, 0, heapSize);
            }
        }
    }

    static int[] makeKSorted(int[] sortedArr, int k, Random rng) {
        int n = sortedArr.length;
        if (n <= 1 || k == 0) {
            return sortedArr.clone();
        }

        int[] orig = new int[n];
        for (int i = 0; i < n; i++) orig[i] = i;

        int passes = Math.min(8, Math.max(3, k));
        for (int p = 0; p < passes; p++) {
            for (int i = 0; i < n - 1; i++) {
                if (rng.nextBoolean()) {
                    if (Math.abs(i - orig[i + 1]) <= k && Math.abs((i + 1) - orig[i]) <= k) {
                        int tmp = orig[i];
                        orig[i] = orig[i + 1];
                        orig[i + 1] = tmp;
                    }
                }
            }
            for (int i = n - 2; i >= 0; i--) {
                if (rng.nextBoolean()) {
                    if (Math.abs(i - orig[i + 1]) <= k && Math.abs((i + 1) - orig[i]) <= k) {
                        int tmp = orig[i];
                        orig[i] = orig[i + 1];
                        orig[i + 1] = tmp;
                    }
                }
            }
        }

        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            if (Math.abs(i - orig[i]) > k) {
                throw new IllegalStateException("Displacement bound exceeded: index " + i + " orig " + orig[i] + " k " + k);
            }
            nums[i] = sortedArr[orig[i]];
        }
        return nums;
    }

    static int[] generateRandomSorted(int n, Random rng, int minVal, int maxVal) {
        int[] arr = new int[n];
        long range = (long) maxVal - minVal + 1;
        for (int i = 0; i < n; i++) {
            arr[i] = minVal + (int) (Math.abs(rng.nextLong()) % range);
        }
        Arrays.sort(arr);
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

        int[] copy = nums.clone();
        sortKSorted(copy, k);

        // Verification against Arrays.sort
        int[] check = nums.clone();
        Arrays.sort(check);
        if (!Arrays.equals(copy, check)) {
            throw new IllegalStateException("sortKSorted result does not match Arrays.sort");
        }

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
        } else if (new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Sort a K-Sorted Array").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Sort a K-Sorted Array");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Sort a K-Sorted Array");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(20261025);

        for (int test = 0; test < 100; test++) {
            int[] nums;
            int k;

            if (test == 0) {
                // TUF Example 1
                nums = new int[]{6, 5, 3, 2, 8, 10, 9};
                k = 3;
            } else if (test == 1) {
                // TUF Example 2
                nums = new int[]{3, 2, 1, 5, 6, 4};
                k = 2;
            } else if (test == 2) {
                // TUF Example 3
                nums = new int[]{1, 2, 3, 4, 5};
                k = 0;
            } else if (test == 3) {
                // Single element positive
                nums = new int[]{42};
                k = 0;
            } else if (test == 4) {
                // Single element minimum constraint
                nums = new int[]{-1000000000};
                k = 0;
            } else if (test == 5) {
                // N=2, K=1 swapped
                nums = new int[]{10, 5};
                k = 1;
            } else if (test == 6) {
                // N=2, K=1 already sorted
                nums = new int[]{5, 10};
                k = 1;
            } else if (test == 7) {
                // N=2, K=0 sorted
                nums = new int[]{-5, 5};
                k = 0;
            } else if (test == 8) {
                // N=10, K=3 all identical
                nums = new int[]{7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
                k = 3;
            } else if (test == 9) {
                // All negative elements, N=8, K=2
                int[] s = new int[]{-10, -8, -6, -5, -4, -3, -2, -1};
                k = 2;
                nums = makeKSorted(s, k, rng);
            } else if (test < 20) {
                // Small cases N in [3, 15]
                int n = 3 + (test - 10);
                k = (test % 4 == 0) ? 0 : rng.nextInt(Math.min(n, 5));
                int[] s = generateRandomSorted(n, rng, -100, 100);
                nums = makeKSorted(s, k, rng);
            } else if (test < 40) {
                // Small-to-medium cases N in [20, 100]
                int n = 20 + (test - 20) * 4;
                if (test % 5 == 0) {
                    k = 0;
                } else if (test % 5 == 1) {
                    k = 1;
                } else {
                    k = 2 + rng.nextInt(Math.min(n, 15));
                }
                int[] s = generateRandomSorted(n, rng, -10000, 10000);
                nums = makeKSorted(s, k, rng);
            } else if (test < 60) {
                // Medium cases N in [200, 2000]
                int n = 200 + (test - 40) * 90;
                if (test % 6 == 0) {
                    k = 0;
                } else if (test % 6 == 1) {
                    k = 1;
                } else {
                    k = 5 + rng.nextInt(Math.min(n, 50));
                }
                int[] s = generateRandomSorted(n, rng, -1000000, 1000000);
                nums = makeKSorted(s, k, rng);
            } else if (test < 80) {
                // Large cases N in [5000, 30000]
                int n = 5000 + (test - 60) * 1250;
                if (test % 7 == 0) {
                    k = 0;
                } else if (test % 7 == 1) {
                    k = 2;
                } else {
                    k = 10 + rng.nextInt(Math.min(n, 100));
                }
                int[] s = generateRandomSorted(n, rng, -1000000000, 1000000000);
                nums = makeKSorted(s, k, rng);
            } else {
                // Max constraint cases N in [50000, 100000]
                int n;
                if (test < 90) {
                    n = 50000 + (test - 80) * 4000;
                } else {
                    n = 100000;
                }

                if (test == 93) {
                    k = 0; // Max size N=100000, K=0
                } else if (test == 94) {
                    k = 1; // Max size N=100000, K=1
                } else if (test == 95) {
                    k = 5;
                } else if (test == 96) {
                    k = 25;
                } else if (test == 97) {
                    k = 100;
                } else if (test == 98) {
                    k = 250;
                } else if (test == 99) {
                    k = 500;
                } else {
                    k = (test % 4 == 0) ? 0 : (5 + rng.nextInt(100));
                }

                int[] s;
                if (test == 97) {
                    // Duplicates heavy
                    s = generateRandomSorted(n, rng, -50, 50);
                } else {
                    s = generateRandomSorted(n, rng, -1000000000, 1000000000);
                }
                nums = makeKSorted(s, k, rng);
            }

            String inFileName = String.format("input%02d.txt", test);
            String outFileName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inFileName);
            File outFile = new File(outDir, outFileName);

            writeTestCase(inFile, outFile, nums, k);
        }

        System.out.println("Generated 100 testcases successfully.");
    }
}
