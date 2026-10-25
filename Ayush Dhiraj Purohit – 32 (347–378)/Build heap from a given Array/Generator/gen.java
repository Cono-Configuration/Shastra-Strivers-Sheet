import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class gen {

    public static void buildMinHeap(int[] nums) {
        int n = nums.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(nums, i, n);
        }
    }

    private static void siftDown(int[] nums, int ind, int n) {
        while (true) {
            int left = 2 * ind + 1;
            int right = 2 * ind + 2;
            int smallest = ind;

            if (left < n && nums[left] < nums[smallest]) {
                smallest = left;
            }
            if (right < n && nums[right] < nums[smallest]) {
                smallest = right;
            }

            if (smallest != ind) {
                int temp = nums[ind];
                nums[ind] = nums[smallest];
                nums[smallest] = temp;
                ind = smallest;
            } else {
                break;
            }
        }
    }

    static class TestCase {
        int[] input;
        int[] output;

        TestCase(int[] arr) {
            this.input = Arrays.copyOf(arr, arr.length);
            int[] copy = Arrays.copyOf(arr, arr.length);
            buildMinHeap(copy);
            this.output = copy;
        }
    }

    public static void main(String[] args) throws IOException {
        String baseDir = "e:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Build heap from a given Array";
        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) {
            inDir.mkdirs();
        }
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        List<TestCase> tests = new ArrayList<>();
        Random rng = new Random(888123L);

        // 00 - 02: Exact Sample cases from problem statement
        tests.add(new TestCase(new int[]{6, 5, 2, 7, 1, 7}));
        tests.add(new TestCase(new int[]{2, 3, 4, 1, 7, 3, 9, 4, 6}));
        tests.add(new TestCase(new int[]{5, 4, 3, 2, 1}));

        // 03 - 14: Minimal & small edge cases
        tests.add(new TestCase(new int[]{42})); // N=1
        tests.add(new TestCase(new int[]{-10000})); // N=1 min boundary
        tests.add(new TestCase(new int[]{10000})); // N=1 max boundary
        tests.add(new TestCase(new int[]{10, 5})); // N=2 descending
        tests.add(new TestCase(new int[]{5, 10})); // N=2 ascending
        tests.add(new TestCase(new int[]{7, 7})); // N=2 duplicates
        tests.add(new TestCase(new int[]{3, 2, 1})); // N=3 descending
        tests.add(new TestCase(new int[]{1, 2, 3})); // N=3 ascending
        tests.add(new TestCase(new int[]{5, 5, 5})); // N=3 duplicates
        tests.add(new TestCase(new int[]{4, 3, 2, 1})); // N=4 descending
        tests.add(new TestCase(new int[]{-1, -2, -3, -4, -5})); // N=5 negative descending
        tests.add(new TestCase(new int[]{1, 2, 3, 4, 5})); // N=5 ascending

        // 15 - 34: Small-to-Medium arrays (N in [7, 100])
        // 15: N = 7, strictly descending
        tests.add(new TestCase(new int[]{7, 6, 5, 4, 3, 2, 1}));

        // 16: N = 7, strictly ascending
        tests.add(new TestCase(new int[]{1, 2, 3, 4, 5, 6, 7}));

        // 17: N = 15, strictly descending
        {
            int[] arr = new int[15];
            for (int k = 0; k < 15; k++) arr[k] = 15 - k;
            tests.add(new TestCase(arr));
        }

        // 18: N = 15, strictly ascending
        {
            int[] arr = new int[15];
            for (int k = 0; k < 15; k++) arr[k] = k + 1;
            tests.add(new TestCase(arr));
        }

        // 19: N = 15, alternating values
        {
            int[] arr = new int[15];
            for (int k = 0; k < 15; k++) arr[k] = (k % 2 == 0) ? 100 : 10;
            tests.add(new TestCase(arr));
        }

        // 20: N = 15, all identical elements (0)
        {
            int[] arr = new int[15];
            Arrays.fill(arr, 0);
            tests.add(new TestCase(arr));
        }

        // 21: N = 31, descending
        {
            int[] arr = new int[31];
            for (int k = 0; k < 31; k++) arr[k] = 31 - k;
            tests.add(new TestCase(arr));
        }

        // 22: N = 31, ascending
        {
            int[] arr = new int[31];
            for (int k = 0; k < 31; k++) arr[k] = k * 10;
            tests.add(new TestCase(arr));
        }

        // 23 - 34: Random arrays of size 20 to 100
        for (int i = 23; i <= 34; i++) {
            int n = 20 + (i - 23) * 7;
            int[] arr = makeRandomArray(n, rng, -1000, 1000);
            tests.add(new TestCase(arr));
        }

        // 35 - 69: Medium-to-Large arrays (N in [127, 5000])
        // 35: N = 127 descending
        {
            int[] arr = new int[127];
            for (int k = 0; k < 127; k++) arr[k] = 1000 - k;
            tests.add(new TestCase(arr));
        }

        // 36: N = 255 descending
        {
            int[] arr = new int[255];
            for (int k = 0; k < 255; k++) arr[k] = 2000 - k;
            tests.add(new TestCase(arr));
        }

        // 37: N = 511 ascending
        {
            int[] arr = new int[511];
            for (int k = 0; k < 511; k++) arr[k] = -2500 + k * 10;
            tests.add(new TestCase(arr));
        }

        // 38: N = 1023 descending
        {
            int[] arr = new int[1023];
            for (int k = 0; k < 1023; k++) arr[k] = 5000 - k * 5;
            tests.add(new TestCase(arr));
        }

        // 39: N = 2047 alternating
        {
            int[] arr = new int[2047];
            for (int k = 0; k < 2047; k++) arr[k] = (k % 2 == 0) ? 5000 : -5000;
            tests.add(new TestCase(arr));
        }

        // 40: N = 4095 ascending
        {
            int[] arr = new int[4095];
            for (int k = 0; k < 4095; k++) arr[k] = k;
            tests.add(new TestCase(arr));
        }

        // 41 - 69: Randomized arrays of sizes 300 to 5000
        for (int i = 41; i <= 69; i++) {
            int n = 300 + (i - 41) * 160;
            int[] arr = makeRandomArray(n, rng, -8000, 8000);
            tests.add(new TestCase(arr));
        }

        // 70 - 89: Pathological & Deep Stress Cases (N in [10000, 50000])
        // 70: N = 10000, strictly descending (worst-case downward swaps)
        {
            int[] arr = new int[10000];
            for (int k = 0; k < 10000; k++) arr[k] = 10000 - k;
            tests.add(new TestCase(arr));
        }

        // 71: N = 10000, strictly ascending (0 swaps)
        {
            int[] arr = new int[10000];
            for (int k = 0; k < 10000; k++) arr[k] = k;
            tests.add(new TestCase(arr));
        }

        // 72: N = 20000, all identical (0)
        {
            int[] arr = new int[20000];
            Arrays.fill(arr, 0);
            tests.add(new TestCase(arr));
        }

        // 73: N = 20000, single element at index 0 is 10000, rest are -10000
        {
            int[] arr = new int[20000];
            Arrays.fill(arr, -10000);
            arr[0] = 10000;
            tests.add(new TestCase(arr));
        }

        // 74: N = 30000, sorted blocks
        {
            int[] arr = new int[30000];
            for (int k = 0; k < 30000; k++) arr[k] = (k % 500) - 250;
            tests.add(new TestCase(arr));
        }

        // 75: N = 30000, reverse sorted blocks
        {
            int[] arr = new int[30000];
            for (int k = 0; k < 30000; k++) arr[k] = 250 - (k % 500);
            tests.add(new TestCase(arr));
        }

        // 76 - 89: Diverse large arrays with random distributions
        for (int i = 76; i <= 89; i++) {
            int n = 15000 + (i - 76) * 2500;
            int[] arr = makeRandomArray(n, rng, -10000, 10000);
            tests.add(new TestCase(arr));
        }

        // 90 - 99: Boundary Maximum Constraint Cases (N = 100000)
        // 90: N = 100000, strictly descending (worst-case sift down work)
        {
            int[] arr = new int[100000];
            for (int k = 0; k < 100000; k++) arr[k] = 100000 - k;
            tests.add(new TestCase(arr));
        }

        // 91: N = 100000, strictly ascending (best-case)
        {
            int[] arr = new int[100000];
            for (int k = 0; k < 100000; k++) arr[k] = k;
            tests.add(new TestCase(arr));
        }

        // 92: N = 100000, all identical elements (777)
        {
            int[] arr = new int[100000];
            Arrays.fill(arr, 777);
            tests.add(new TestCase(arr));
        }

        // 93: N = 100000, alternating maximum bounds (+10000, -10000)
        {
            int[] arr = new int[100000];
            for (int k = 0; k < 100000; k++) arr[k] = (k % 2 == 0) ? 10000 : -10000;
            tests.add(new TestCase(arr));
        }

        // 94: N = 100000, all negative numbers
        {
            int[] arr = new int[100000];
            for (int k = 0; k < 100000; k++) arr[k] = -10000 + (k % 10000);
            tests.add(new TestCase(arr));
        }

        // 95: N = 100000, all positive numbers
        {
            int[] arr = new int[100000];
            for (int k = 0; k < 100000; k++) arr[k] = 1 + (k % 10000);
            tests.add(new TestCase(arr));
        }

        // 96 - 99: High-stress randomized uniform arrays of size 100000
        for (int i = 96; i <= 99; i++) {
            int[] arr = makeRandomArray(100000, rng, -10000, 10000);
            tests.add(new TestCase(arr));
        }

        System.out.println("Total test cases prepared: " + tests.size());

        for (int i = 0; i < tests.size(); i++) {
            TestCase tc = tests.get(i);
            String testName = String.format("%02d", i);

            File inFile = new File(inDir, "input" + testName + ".txt");
            try (PrintWriter pw = new PrintWriter(new FileWriter(inFile))) {
                pw.println(tc.input.length);
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < tc.input.length; j++) {
                    sb.append(tc.input[j]);
                    if (j < tc.input.length - 1) {
                        sb.append(' ');
                    }
                }
                pw.println(sb.toString());
            }

            File outFile = new File(outDir, "output" + testName + ".txt");
            try (PrintWriter pw = new PrintWriter(new FileWriter(outFile))) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < tc.output.length; j++) {
                    sb.append(tc.output[j]);
                    if (j < tc.output.length - 1) {
                        sb.append(' ');
                    }
                }
                pw.println(sb.toString());
            }
        }

        System.out.println("Successfully generated all 100 input and output test cases!");
    }

    private static int[] makeRandomArray(int n, Random rng, int minVal, int maxVal) {
        int[] arr = new int[n];
        int range = maxVal - minVal + 1;
        for (int i = 0; i < n; i++) {
            arr[i] = minVal + rng.nextInt(range);
        }
        return arr;
    }
}
