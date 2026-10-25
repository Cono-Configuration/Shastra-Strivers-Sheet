import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class gen {

    public static void heapify(int[] nums, int ind, int val) {
        int n = nums.length;
        if (ind < 0 || ind >= n) {
            return;
        }

        nums[ind] = val;

        if (ind > 0 && nums[ind] < nums[(ind - 1) / 2]) {
            siftUp(nums, ind);
        } else {
            siftDown(nums, ind, n);
        }
    }

    private static void siftUp(int[] nums, int ind) {
        while (ind > 0) {
            int parent = (ind - 1) / 2;
            if (nums[ind] < nums[parent]) {
                swap(nums, ind, parent);
                ind = parent;
            } else {
                break;
            }
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
                swap(nums, ind, smallest);
                ind = smallest;
            } else {
                break;
            }
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void buildMinHeap(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(arr, i, n);
        }
    }

    static class TestCase {
        int[] nums;
        int ind;
        int val;
        int[] output;

        TestCase(int[] nums, int ind, int val) {
            this.nums = Arrays.copyOf(nums, nums.length);
            this.ind = ind;
            this.val = val;

            int[] copy = Arrays.copyOf(nums, nums.length);
            heapify(copy, ind, val);
            this.output = copy;
        }
    }

    public static void main(String[] args) throws IOException {
        String baseDir = "e:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Heapify Algorithm";
        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) {
            inDir.mkdirs();
        }
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        List<TestCase> tests = new ArrayList<>();
        Random rng = new Random(133742L);

        // 00 - 02: Exact Sample cases from problem statement
        tests.add(new TestCase(new int[]{1, 4, 5, 5, 7, 6}, 5, 2));
        tests.add(new TestCase(new int[]{2, 4, 3, 6, 5, 7, 8, 7}, 0, 7));
        tests.add(new TestCase(new int[]{2, 6, 3, 8, 9, 4}, 3, 1));

        // 03 - 14: Minimal & small edge cases
        tests.add(new TestCase(new int[]{10}, 0, 5)); // N=1
        tests.add(new TestCase(new int[]{0}, 0, -10000)); // N=1 min boundary
        tests.add(new TestCase(new int[]{10000}, 0, 10000)); // N=1 no change
        tests.add(new TestCase(new int[]{5, 10}, 0, 20)); // N=2 root sifts down
        tests.add(new TestCase(new int[]{5, 10}, 1, 1)); // N=2 leaf sifts up
        tests.add(new TestCase(new int[]{5, 10}, 1, 15)); // N=2 no change
        tests.add(new TestCase(new int[]{10, 20, 30}, 0, 50)); // N=3 root sifts down to left
        tests.add(new TestCase(new int[]{10, 30, 20}, 0, 50)); // N=3 root sifts down to right
        tests.add(new TestCase(new int[]{10, 20, 30}, 2, 5)); // N=3 right child sifts up
        tests.add(new TestCase(new int[]{10, 20, 30}, 1, 5)); // N=3 left child sifts up
        tests.add(new TestCase(new int[]{1, 2, 3, 4}, 3, -5)); // N=4 leaf to root
        tests.add(new TestCase(new int[]{1, 2, 3, 4, 5}, 0, 100)); // N=5 root to leaf

        // 15 - 34: Small-to-Medium Trees (N in [7, 100])
        // 15: Perfect heap of size 7, root update
        {
            int[] h = new int[]{1, 2, 3, 4, 5, 6, 7};
            tests.add(new TestCase(h, 0, 100));
        }

        // 16: Perfect heap of size 7, leaf update
        {
            int[] h = new int[]{1, 2, 3, 4, 5, 6, 7};
            tests.add(new TestCase(h, 6, -10));
        }

        // 17: Heap of size 15, internal node sifting up
        {
            int[] h = makeHeapRange(15, 10, 10);
            tests.add(new TestCase(h, 5, 5));
        }

        // 18: Heap of size 15, internal node sifting down
        {
            int[] h = makeHeapRange(15, 10, 10);
            tests.add(new TestCase(h, 2, 200));
        }

        // 19: All identical elements, leaf to smaller
        {
            int[] h = new int[15];
            Arrays.fill(h, 5);
            tests.add(new TestCase(h, 14, 1));
        }

        // 20: All identical elements, root to larger
        {
            int[] h = new int[15];
            Arrays.fill(h, 5);
            tests.add(new TestCase(h, 0, 10));
        }

        // 21: Negative numbers heap, update with positive
        {
            int[] h = new int[]{-50, -40, -35, -20, -15, -10, -5};
            tests.add(new TestCase(h, 0, 50));
        }

        // 22: Negative numbers heap, update leaf with large negative
        {
            int[] h = new int[]{-50, -40, -35, -20, -15, -10, -5};
            tests.add(new TestCase(h, 6, -100));
        }

        // 23 - 34: Random min-heaps with N in [20, 100]
        for (int i = 23; i <= 34; i++) {
            int n = 20 + (i - 23) * 7;
            int[] h = makeRandomHeap(n, rng, -1000, 1000);
            int ind = rng.nextInt(n);
            int val = (rng.nextBoolean()) ? -2000 : 2000;
            tests.add(new TestCase(h, ind, val));
        }

        // 35 - 69: Medium-to-Large Trees (N in [127, 5000])
        // 35: N = 127, root to max
        {
            int[] h = makeHeapRange(127, 1, 1);
            tests.add(new TestCase(h, 0, 10000));
        }

        // 36: N = 127, leaf to min
        {
            int[] h = makeHeapRange(127, 1, 1);
            tests.add(new TestCase(h, 126, -10000));
        }

        // 37: N = 255, internal node sifting down
        {
            int[] h = makeHeapRange(255, 1, 2);
            tests.add(new TestCase(h, 15, 10000));
        }

        // 38: N = 511, internal node sifting up
        {
            int[] h = makeHeapRange(511, 10, 5);
            tests.add(new TestCase(h, 250, 1));
        }

        // 39: N = 1023, full tree root update
        {
            int[] h = makeHeapRange(1023, -5000, 10);
            tests.add(new TestCase(h, 0, 10000));
        }

        // 40: N = 2047, full tree leaf update
        {
            int[] h = makeHeapRange(2047, -5000, 5);
            tests.add(new TestCase(h, 2046, -10000));
        }

        // 41 - 69: Randomized min-heaps of sizes 500 to 5000
        for (int i = 41; i <= 69; i++) {
            int n = 300 + (i - 41) * 160;
            int[] h = makeRandomHeap(n, rng, -8000, 8000);
            int ind = (i % 2 == 0) ? rng.nextInt(n / 2) : (n / 2 + rng.nextInt(n - n / 2));
            int val = (i % 3 == 0) ? -10000 : ((i % 3 == 1) ? 10000 : rng.nextInt(20001) - 10000);
            tests.add(new TestCase(h, ind, val));
        }

        // 70 - 89: Pathological & Deep Stress Cases (N in [10000, 50000])
        // 70: N = 10000, root to +10000
        {
            int[] h = makeHeapRange(10000, -5000, 1);
            tests.add(new TestCase(h, 0, 10000));
        }

        // 71: N = 10000, last leaf to -10000
        {
            int[] h = makeHeapRange(10000, -5000, 1);
            tests.add(new TestCase(h, 9999, -10000));
        }

        // 72: N = 20000, identical 0s, update mid to -5000
        {
            int[] h = new int[20000];
            tests.add(new TestCase(h, 10000, -5000));
        }

        // 73: N = 20000, identical 0s, update mid to +5000
        {
            int[] h = new int[20000];
            tests.add(new TestCase(h, 5000, 5000));
        }

        // 74: N = 30000, left-spine deep sift-up
        {
            int[] h = makeHeapRange(30000, 0, 1);
            // Index of left spine: 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383
            tests.add(new TestCase(h, 16383, -10000));
        }

        // 75: N = 30000, right-spine deep sift-up
        {
            int[] h = makeHeapRange(30000, 0, 1);
            // Right spine: 0, 2, 6, 14, 30, 62, 126, 254, 510, 1022, 2046, 4094, 8190, 16382
            tests.add(new TestCase(h, 16382, -10000));
        }

        // 76 - 89: Diverse large heaps with random queries
        for (int i = 76; i <= 89; i++) {
            int n = 15000 + (i - 76) * 2500;
            int[] h = makeRandomHeap(n, rng, -10000, 10000);
            int ind = rng.nextInt(n);
            int val = (rng.nextBoolean()) ? -10000 : 10000;
            tests.add(new TestCase(h, ind, val));
        }

        // 90 - 99: Boundary Maximum Constraint Cases (N = 100000)
        // 90: N = 100000, root to max
        {
            int[] h = makeHeapRange(100000, -50000, 1);
            tests.add(new TestCase(h, 0, 10000));
        }

        // 91: N = 100000, last leaf to min
        {
            int[] h = makeHeapRange(100000, -50000, 1);
            tests.add(new TestCase(h, 99999, -10000));
        }

        // 92: N = 100000, internal node at 50000 to -10000
        {
            int[] h = makeHeapRange(100000, -50000, 1);
            tests.add(new TestCase(h, 50000, -10000));
        }

        // 93: N = 100000, root no-op (stays min)
        {
            int[] h = makeHeapRange(100000, -50000, 1);
            tests.add(new TestCase(h, 0, -50000));
        }

        // 94: N = 100000, leaf no-op
        {
            int[] h = makeHeapRange(100000, -50000, 1);
            tests.add(new TestCase(h, 99999, h[99999]));
        }

        // 95: N = 100000, all identical (42), update root to 100
        {
            int[] h = new int[100000];
            Arrays.fill(h, 42);
            tests.add(new TestCase(h, 0, 100));
        }

        // 96: N = 100000, all negative values, update root to 0
        {
            int[] h = makeHeapRange(100000, -10000, 0);
            tests.add(new TestCase(h, 0, 0));
        }

        // 97: N = 100000, all positive values, update leaf to -10000
        {
            int[] h = makeHeapRange(100000, 1, 0);
            tests.add(new TestCase(h, 99999, -10000));
        }

        // 98: N = 100000, random heap, random index
        {
            int[] h = makeRandomHeap(100000, rng, -10000, 10000);
            int ind = rng.nextInt(100000);
            int val = rng.nextInt(20001) - 10000;
            tests.add(new TestCase(h, ind, val));
        }

        // 99: N = 100000, deep alternating sifting
        {
            int[] h = makeRandomHeap(100000, rng, -10000, 10000);
            tests.add(new TestCase(h, 0, 10000));
        }

        System.out.println("Total test cases prepared: " + tests.size());

        for (int i = 0; i < tests.size(); i++) {
            TestCase tc = tests.get(i);
            String testName = String.format("%02d", i);

            File inFile = new File(inDir, "input" + testName + ".txt");
            try (PrintWriter pw = new PrintWriter(new FileWriter(inFile))) {
                pw.printf("%d %d %d%n", tc.nums.length, tc.ind, tc.val);
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < tc.nums.length; j++) {
                    sb.append(tc.nums[j]);
                    if (j < tc.nums.length - 1) {
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

    private static int[] makeHeapRange(int n, int start, int step) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = start + i * step;
        }
        buildMinHeap(arr);
        return arr;
    }

    private static int[] makeRandomHeap(int n, Random rng, int minVal, int maxVal) {
        int[] arr = new int[n];
        int range = maxVal - minVal + 1;
        for (int i = 0; i < n; i++) {
            arr[i] = minVal + rng.nextInt(range);
        }
        buildMinHeap(arr);
        return arr;
    }
}
