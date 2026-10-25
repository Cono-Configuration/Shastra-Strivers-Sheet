import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class gen {

    // Optimal Bucket Sort Solver
    public static int[] topKFrequentBucket(int[] nums, int k) {
        int n = nums.length;
        HashMap<Integer, Integer> freqMap = new HashMap<>(n * 2);
        for (int x : nums) {
            freqMap.put(x, freqMap.getOrDefault(x, 0) + 1);
        }

        List<List<Integer>> buckets = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            buckets.add(null);
        }

        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            int val = entry.getKey();
            int freq = entry.getValue();
            if (buckets.get(freq) == null) {
                buckets.set(freq, new ArrayList<>());
            }
            buckets.get(freq).add(val);
        }

        int[] result = new int[k];
        int count = 0;

        for (int f = n; f >= 1 && count < k; f--) {
            List<Integer> bucket = buckets.get(f);
            if (bucket != null) {
                Collections.sort(bucket);
                for (int val : bucket) {
                    result[count++] = val;
                    if (count == k) break;
                }
            }
        }

        return result;
    }

    // Brute Force / Comparator Sort Solver for Dual Verification
    static class ElementFreq implements Comparable<ElementFreq> {
        int val;
        int freq;

        ElementFreq(int val, int freq) {
            this.val = val;
            this.freq = freq;
        }

        @Override
        public int compareTo(ElementFreq o) {
            if (this.freq != o.freq) {
                return Integer.compare(o.freq, this.freq); // Descending frequency
            }
            return Integer.compare(this.val, o.val); // Ascending value
        }
    }

    public static int[] topKFrequentSort(int[] nums, int k) {
        int n = nums.length;
        HashMap<Integer, Integer> freqMap = new HashMap<>(n * 2);
        for (int x : nums) {
            freqMap.put(x, freqMap.getOrDefault(x, 0) + 1);
        }

        List<ElementFreq> list = new ArrayList<>(freqMap.size());
        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            list.add(new ElementFreq(entry.getKey(), entry.getValue()));
        }

        Collections.sort(list);

        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = list.get(i).val;
        }
        return result;
    }

    private static void shuffle(int[] arr, Random rng) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    private static void writeTestCase(File inDir, File outDir, int testIdx, int n, int k, int[] nums) throws IOException {
        String baseName = String.format("input%02d.txt", testIdx);
        String outName = String.format("output%02d.txt", testIdx);
        File inFile = new File(inDir, baseName);
        File outFile = new File(outDir, outName);

        // Optimal solver
        int[] resultBucket = topKFrequentBucket(nums, k);

        // Verification with sort solver
        int[] resultSort = topKFrequentSort(nums, k);
        if (!Arrays.equals(resultBucket, resultSort)) {
            throw new RuntimeException("Mismatch between Bucket and Sort solvers on test " + testIdx);
        }

        // Write input
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(n + " " + k);
            for (int i = 0; i < n; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(nums[i]);
            }
            inPw.println();
            inPw.flush();
        }

        // Write output
        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            for (int i = 0; i < k; i++) {
                if (i > 0) outPw.print(" ");
                outPw.print(resultBucket[i]);
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
        } else if (new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Top K Frequent Elements").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Top K Frequent Elements");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Top K Frequent Elements");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(20261025);

        for (int test = 0; test < 100; test++) {
            int n;
            int k;
            int[] nums;

            if (test == 0) {
                // TUF Example 1
                n = 6; k = 2;
                nums = new int[]{1, 1, 1, 2, 2, 3};
            } else if (test == 1) {
                // TUF Example 2
                n = 1; k = 1;
                nums = new int[]{4};
            } else if (test == 2) {
                // Example 3
                n = 8; k = 3;
                nums = new int[]{4, 1, -1, 2, -1, 2, 3, -1};
            } else if (test == 3) {
                // Example 4
                n = 5; k = 2;
                nums = new int[]{5, 5, 10, 10, 20};
            } else if (test == 4) {
                // Minimal negative extreme
                n = 1; k = 1;
                nums = new int[]{-1000000000};
            } else if (test == 5) {
                // All identical elements
                n = 10; k = 1;
                nums = new int[]{7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
            } else if (test == 6) {
                // All distinct elements, k = n
                n = 8; k = 8;
                nums = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
            } else if (test == 7) {
                // Geometric frequencies
                nums = new int[]{1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4};
                n = nums.length;
                k = 3;
            } else if (test == 8) {
                // Negative numbers with diverse frequencies
                nums = new int[]{-10, -10, -10, -20, -20, -30};
                n = nums.length;
                k = 2;
            } else if (test == 9) {
                // Boundary values +-10^9
                nums = new int[]{-1000000000, 1000000000, 0, -1000000000, 1000000000, 1000000000};
                n = nums.length;
                k = 2;
            } else if (test < 30) {
                // Small cases: n in [2, 50]
                n = 2 + rng.nextInt(49);
                int numDistinct = 1 + rng.nextInt(Math.min(n, 10));
                k = 1 + rng.nextInt(numDistinct);
                int[] distinctVals = new int[numDistinct];
                for (int i = 0; i < numDistinct; i++) {
                    distinctVals[i] = rng.nextInt(51) - 25;
                }

                nums = new int[n];
                for (int i = 0; i < n; i++) {
                    nums[i] = distinctVals[rng.nextInt(numDistinct)];
                }

                // Recount distinct values actually used
                HashMap<Integer, Integer> map = new HashMap<>();
                for (int x : nums) map.put(x, 1);
                k = Math.min(k, map.size());
            } else if (test < 60) {
                // Medium cases: n in [100, 2000]
                n = 100 + rng.nextInt(1901);
                int mode = test % 5;
                int numDistinct;
                if (mode == 0) numDistinct = 5 + rng.nextInt(15);
                else if (mode == 1) numDistinct = 50 + rng.nextInt(100);
                else if (mode == 2) numDistinct = n; // all unique
                else numDistinct = 2 + rng.nextInt(5);

                int[] distinctVals = new int[numDistinct];
                for (int i = 0; i < numDistinct; i++) {
                    distinctVals[i] = (mode == 3) ? (rng.nextInt(2000000001) - 1000000000) : (i * 3 - 500);
                }

                nums = new int[n];
                if (mode == 2) {
                    for (int i = 0; i < n; i++) nums[i] = i - (n / 2);
                    shuffle(nums, rng);
                } else {
                    for (int i = 0; i < n; i++) {
                        nums[i] = distinctVals[rng.nextInt(numDistinct)];
                    }
                }

                HashMap<Integer, Integer> map = new HashMap<>();
                for (int x : nums) map.put(x, 1);
                k = 1 + rng.nextInt(Math.min(map.size(), 50));
            } else if (test < 80) {
                // Large cases: n in [5000, 25000]
                n = 5000 + rng.nextInt(20001);
                int mode = test % 4;
                int numDistinct;
                if (mode == 0) numDistinct = 20;
                else if (mode == 1) numDistinct = 500;
                else if (mode == 2) numDistinct = 2000;
                else numDistinct = 2; // only 2 elements

                int[] distinctVals = new int[numDistinct];
                for (int i = 0; i < numDistinct; i++) {
                    distinctVals[i] = (int) (rng.nextLong() % 1000000001L);
                }

                nums = new int[n];
                for (int i = 0; i < n; i++) {
                    nums[i] = distinctVals[rng.nextInt(numDistinct)];
                }

                HashMap<Integer, Integer> map = new HashMap<>();
                for (int x : nums) map.put(x, 1);
                k = 1 + rng.nextInt(Math.min(map.size(), 500));
            } else {
                // Max constraint cases: n in [50000, 100000]
                if (test == 80) n = 50000;
                else if (test == 85) n = 75000;
                else if (test == 90 || test == 99) n = 100000;
                else n = 50000 + rng.nextInt(50001);

                nums = new int[n];
                int mode = test % 5;

                if (mode == 0) {
                    // Skewed Zipf-like distribution with 50 distinct elements
                    int numDistinct = 50;
                    int[] distinctVals = new int[numDistinct];
                    for (int i = 0; i < numDistinct; i++) {
                        distinctVals[i] = (int) (rng.nextLong() % 1000000001L);
                    }
                    int idx = 0;
                    for (int i = 0; i < numDistinct; i++) {
                        int count = (n / (i + 2));
                        for (int c = 0; c < count && idx < n; c++) {
                            nums[idx++] = distinctVals[i];
                        }
                    }
                    while (idx < n) {
                        nums[idx++] = distinctVals[0];
                    }
                    shuffle(nums, rng);
                    k = 10;
                } else if (mode == 1) {
                    // All identical values
                    Arrays.fill(nums, 888888888);
                    k = 1;
                } else if (mode == 2) {
                    // All distinct values: k = 5000
                    for (int i = 0; i < n; i++) {
                        nums[i] = i * 2 - n;
                    }
                    shuffle(nums, rng);
                    k = Math.min(10000, n);
                } else if (mode == 3) {
                    // Bipolar extremes -10^9 and 10^9 with distinct frequencies
                    int countNeg = n / 3;
                    for (int i = 0; i < countNeg; i++) nums[i] = -1000000000;
                    for (int i = countNeg; i < n; i++) nums[i] = 1000000000;
                    shuffle(nums, rng);
                    k = 2;
                } else {
                    // Uniform across 1000 distinct values
                    int numDistinct = 1000;
                    int[] distinctVals = new int[numDistinct];
                    for (int i = 0; i < numDistinct; i++) {
                        distinctVals[i] = -500000000 + i * 1000000;
                    }
                    for (int i = 0; i < n; i++) {
                        nums[i] = distinctVals[rng.nextInt(numDistinct)];
                    }
                    shuffle(nums, rng);
                    k = 100;
                }

                HashMap<Integer, Integer> map = new HashMap<>();
                for (int x : nums) map.put(x, 1);
                k = Math.min(k, map.size());
            }

            writeTestCase(inDir, outDir, test, n, k, nums);
        }

        System.out.println("Generated 100 test cases successfully.");
    }
}
