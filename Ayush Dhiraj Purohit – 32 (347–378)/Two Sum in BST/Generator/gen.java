import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class gen {

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Dual-direction BST Iterator.
     */
    public static class BSTIterator {
        private final Deque<TreeNode> stack;
        private final boolean reverse;

        public BSTIterator(TreeNode root, boolean reverse) {
            this.stack = new ArrayDeque<>();
            this.reverse = reverse;
            pushAll(root);
        }

        private void pushAll(TreeNode node) {
            while (node != null) {
                stack.push(node);
                node = reverse ? node.right : node.left;
            }
        }

        public int next() {
            TreeNode node = stack.pop();
            if (reverse) {
                pushAll(node.left);
            } else {
                pushAll(node.right);
            }
            return node.val;
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }
    }

    /**
     * Optimal Two Sum solver in BST.
     */
    public static boolean findTarget(TreeNode root, long k) {
        if (root == null) return false;

        BSTIterator leftIter = new BSTIterator(root, false);
        BSTIterator rightIter = new BSTIterator(root, true);

        if (!leftIter.hasNext() || !rightIter.hasNext()) return false;

        int leftVal = leftIter.next();
        int rightVal = rightIter.next();

        while (leftVal < rightVal) {
            long sum = (long) leftVal + rightVal;
            if (sum == k) {
                return true;
            } else if (sum < k) {
                if (leftIter.hasNext()) {
                    leftVal = leftIter.next();
                } else {
                    break;
                }
            } else {
                if (rightIter.hasNext()) {
                    rightVal = rightIter.next();
                } else {
                    break;
                }
            }
        }
        return false;
    }

    /**
     * Builds tree from level-order traversal array with -1 representing null.
     */
    public static TreeNode buildTree(int[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == -1) {
            return null;
        }

        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int i = 1;
        while (!queue.isEmpty() && i < arr.length) {
            TreeNode curr = queue.poll();

            if (i < arr.length) {
                if (arr[i] != -1) {
                    curr.left = new TreeNode(arr[i]);
                    queue.offer(curr.left);
                }
                i++;
            }

            if (i < arr.length) {
                if (arr[i] != -1) {
                    curr.right = new TreeNode(arr[i]);
                    queue.offer(curr.right);
                }
                i++;
            }
        }

        return root;
    }

    /**
     * Serializes tree to level-order list of integers (-1 for null), trimming trailing -1s.
     */
    public static List<Integer> serializeTree(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) return list;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            if (curr != null) {
                list.add(curr.val);
                queue.offer(curr.left);
                queue.offer(curr.right);
            } else {
                list.add(-1);
            }
        }

        int last = list.size() - 1;
        while (last >= 0 && list.get(last) == -1) {
            last--;
        }

        List<Integer> result = new ArrayList<>(last + 1);
        for (int i = 0; i <= last; i++) {
            result.add(list.get(i));
        }
        return result;
    }

    /**
     * Inserts value into BST iteratively.
     */
    public static TreeNode insertBST(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);
        TreeNode curr = root;
        while (true) {
            if (val < curr.val) {
                if (curr.left != null) {
                    curr = curr.left;
                } else {
                    curr.left = new TreeNode(val);
                    break;
                }
            } else if (val > curr.val) {
                if (curr.right != null) {
                    curr = curr.right;
                } else {
                    curr.right = new TreeNode(val);
                    break;
                }
            } else {
                break;
            }
        }
        return root;
    }

    /**
     * Builds balanced BST from sorted array.
     */
    public static TreeNode sortedArrayToBST(int[] nums, int left, int right) {
        if (left > right) return null;
        int mid = left + (right - left) / 2;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = sortedArrayToBST(nums, left, mid - 1);
        node.right = sortedArrayToBST(nums, mid + 1, right);
        return node;
    }

    /**
     * Collects all values in tree iteratively.
     */
    public static List<Integer> collectAllValues(TreeNode root) {
        List<Integer> values = new ArrayList<>();
        if (root == null) return values;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            values.add(curr.val);
            if (curr.left != null) queue.offer(curr.left);
            if (curr.right != null) queue.offer(curr.right);
        }
        return values;
    }

    static class TestCase {
        int[] serializedTree;
        long k;
        boolean expected;

        TestCase(int[] serializedTree, long k, boolean expected) {
            this.serializedTree = serializedTree;
            this.k = k;
            this.expected = expected;
        }
    }

    private static File findPackageDir() {
        File curr = new File(".").getAbsoluteFile();
        while (curr != null) {
            File testcases = new File(curr, "Testcases");
            if (testcases.exists() && testcases.isDirectory()) {
                return curr;
            }
            File subTarget = new File(curr, "Two Sum in BST");
            if (subTarget.exists() && new File(subTarget, "Testcases").exists()) {
                return subTarget;
            }
            curr = curr.getParentFile();
        }
        return new File(".");
    }

    public static void main(String[] args) {
        File packageDir = findPackageDir();
        File inputDir = new File(packageDir, "Testcases/Input");
        File outputDir = new File(packageDir, "Testcases/Output");

        if (!inputDir.exists()) inputDir.mkdirs();
        if (!outputDir.exists()) outputDir.mkdirs();

        List<TestCase> testCases = new ArrayList<>();
        Random rand = new Random(20261024L);

        // -------------------------------------------------------------
        // Test Cases 00 - 03: Problem Statement Examples
        // -------------------------------------------------------------
        // 00: Example 1 (k = 9 -> true)
        testCases.add(createFromRaw(new int[]{5, 3, 6, 2, 4, -1, 7}, 9));

        // 01: Example 2 (k = 14 -> false)
        testCases.add(createFromRaw(new int[]{5, 3, 6, 2, 4, -1, 7}, 14));

        // 02: Example 3 (k = 4 -> true)
        testCases.add(createFromRaw(new int[]{2, 1, 3}, 4));

        // 03: Example 4 (single node k = 2 -> false)
        testCases.add(createFromRaw(new int[]{1}, 2));

        // -------------------------------------------------------------
        // Test Cases 04 - 14: Minimal trees & small boundaries
        // -------------------------------------------------------------
        // 04: Two nodes root and right child, target exists (1 + 2 = 3)
        testCases.add(createFromRaw(new int[]{1, -1, 2}, 3));

        // 05: Two nodes root and right child, target does not exist
        testCases.add(createFromRaw(new int[]{1, -1, 2}, 4));

        // 06: Two nodes root and left child, target equals 2 * child (false)
        testCases.add(createFromRaw(new int[]{5, 2}, 4));

        // 07: Two nodes root and left child, target equals 2 * root (false)
        testCases.add(createFromRaw(new int[]{5, 2}, 10));

        // 08: Huge value boundaries near 10^9, target exists (0 + 10^9 = 10^9)
        testCases.add(createFromRaw(new int[]{500000000, 0, 1000000000}, 1000000000));

        // 09: Huge sum up to 1.5 * 10^9
        testCases.add(createFromRaw(new int[]{500000000, 0, 1000000000}, 1500000000L));

        // 10: Negative target sum (impossible since node values >= 0)
        testCases.add(createFromRaw(new int[]{2, 1, 3}, -5));

        // 11: Three nodes left-skewed, target exists (1 + 3 = 4)
        testCases.add(createFromRaw(new int[]{3, 2, -1, 1}, 4));

        // 12: Three nodes left-skewed, target does not exist (6)
        testCases.add(createFromRaw(new int[]{3, 2, -1, 1}, 6));

        // 13: Three nodes right-skewed, target exists (1 + 2 = 3)
        testCases.add(createFromRaw(new int[]{1, -1, 2, -1, 3}, 3));

        // 14: Three nodes right-skewed, target does not exist (10)
        testCases.add(createFromRaw(new int[]{1, -1, 2, -1, 3}, 10));

        // -------------------------------------------------------------
        // Test Cases 15 - 34: Small-to-medium diverse BSTs (N = 5 .. 50)
        // -------------------------------------------------------------
        for (int i = 15; i <= 34; i++) {
            int nodeCount = 5 + (i - 15) * 2; // 5 to 43 nodes
            Set<Integer> uniqueVals = new TreeSet<>();
            while (uniqueVals.size() < nodeCount) {
                uniqueVals.add(rand.nextInt(1000) + 1);
            }
            int[] sorted = uniqueVals.stream().mapToInt(Integer::intValue).toArray();
            TreeNode root;
            if (i % 3 == 0) {
                root = sortedArrayToBST(sorted, 0, sorted.length - 1);
            } else if (i % 3 == 1) {
                List<Integer> list = new ArrayList<>();
                for (int v : sorted) list.add(v);
                Collections.shuffle(list, rand);
                root = null;
                for (int v : list) root = insertBST(root, v);
            } else {
                int low = 0, high = sorted.length - 1;
                root = new TreeNode(sorted[high--]);
                TreeNode curr = root;
                boolean left = true;
                while (low <= high) {
                    if (left) {
                        curr.left = new TreeNode(sorted[low++]);
                        curr = curr.left;
                    } else {
                        curr.right = new TreeNode(sorted[high--]);
                        curr = curr.right;
                    }
                    left = !left;
                }
            }

            long targetK;
            if (i % 2 == 0) {
                // Guaranteed existing pair
                int idx1 = rand.nextInt(sorted.length);
                int idx2 = rand.nextInt(sorted.length);
                while (idx2 == idx1) {
                    idx2 = rand.nextInt(sorted.length);
                }
                targetK = (long) sorted[idx1] + sorted[idx2];
            } else {
                // Target with 2 * element or non-existent sum
                if (rand.nextBoolean()) {
                    targetK = (long) sorted[rand.nextInt(sorted.length)] * 2;
                } else {
                    targetK = (long) sorted[sorted.length - 1] * 2 + 10;
                }
            }
            testCases.add(createFromTree(root, targetK));
        }

        // -------------------------------------------------------------
        // Test Cases 35 - 69: Medium trees (N = 50 .. 1000)
        // -------------------------------------------------------------
        for (int i = 35; i <= 69; i++) {
            int nodeCount = 50 + (i - 35) * 25; // 50 to 900 nodes
            Set<Integer> uniqueVals = new TreeSet<>();
            while (uniqueVals.size() < nodeCount) {
                uniqueVals.add(rand.nextInt(1000000) + 1);
            }
            int[] sorted = uniqueVals.stream().mapToInt(Integer::intValue).toArray();
            TreeNode root;
            if (i % 2 == 0) {
                root = sortedArrayToBST(sorted, 0, sorted.length - 1);
            } else {
                List<Integer> list = new ArrayList<>();
                for (int v : sorted) list.add(v);
                Collections.shuffle(list, rand);
                root = null;
                for (int v : list) root = insertBST(root, v);
            }

            long targetK;
            if (i % 2 == 0) {
                int idx1 = rand.nextInt(sorted.length);
                int idx2 = rand.nextInt(sorted.length);
                while (idx2 == idx1) {
                    idx2 = rand.nextInt(sorted.length);
                }
                targetK = (long) sorted[idx1] + sorted[idx2];
            } else {
                targetK = (long) sorted[sorted.length - 1] + sorted[sorted.length - 2] + 100;
            }
            testCases.add(createFromTree(root, targetK));
        }

        // -------------------------------------------------------------
        // Test Cases 70 - 89: Large-scale trees (N = 1000 .. 4000)
        // -------------------------------------------------------------
        for (int i = 70; i <= 89; i++) {
            int nodeCount = 1000 + (i - 70) * 150; // 1000 to 3850 nodes
            Set<Integer> uniqueVals = new TreeSet<>();
            while (uniqueVals.size() < nodeCount) {
                uniqueVals.add(rand.nextInt(1000000000) + 1);
            }
            int[] sorted = uniqueVals.stream().mapToInt(Integer::intValue).toArray();
            TreeNode root = sortedArrayToBST(sorted, 0, sorted.length - 1);

            long targetK;
            if (i % 2 == 0) {
                int idx1 = rand.nextInt(sorted.length);
                int idx2 = rand.nextInt(sorted.length);
                while (idx2 == idx1) {
                    idx2 = rand.nextInt(sorted.length);
                }
                targetK = (long) sorted[idx1] + sorted[idx2];
            } else {
                targetK = (long) sorted[sorted.length - 1] * 2 + 10;
            }
            testCases.add(createFromTree(root, targetK));
        }

        // -------------------------------------------------------------
        // Test Cases 90 - 99: Boundary & Pathological Stress Tests
        // -------------------------------------------------------------
        // 90: Balanced BST N = 4095, target is min + max (true on very first check)
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            long targetK = (long) sorted[0] + sorted[nNodes - 1];
            testCases.add(createFromTree(root, targetK));
        }

        // 91: Balanced BST N = 4095, target is two adjacent middle elements (true after full scan)
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            long targetK = (long) sorted[nNodes / 2] + sorted[nNodes / 2 + 1];
            testCases.add(createFromTree(root, targetK));
        }

        // 92: Balanced BST N = 4095, target does not exist (false after full scan)
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            long targetK = (long) sorted[nNodes - 1] * 2 + 15;
            testCases.add(createFromTree(root, targetK));
        }

        // 93: Strictly left-skewed chain of 3000 nodes, target exists
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(nNodes);
            TreeNode curr = root;
            for (int k = nNodes - 1; k >= 1; k--) {
                curr.left = new TreeNode(k);
                curr = curr.left;
            }
            testCases.add(createFromTree(root, 1 + nNodes));
        }

        // 94: Strictly left-skewed chain of 3000 nodes, target does not exist
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(nNodes);
            TreeNode curr = root;
            for (int k = nNodes - 1; k >= 1; k--) {
                curr.left = new TreeNode(k);
                curr = curr.left;
            }
            testCases.add(createFromTree(root, nNodes * 2 + 5));
        }

        // 95: Strictly right-skewed chain of 3000 nodes, target exists
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(1);
            TreeNode curr = root;
            for (int k = 2; k <= nNodes; k++) {
                curr.right = new TreeNode(k);
                curr = curr.right;
            }
            testCases.add(createFromTree(root, 1 + nNodes));
        }

        // 96: Strictly right-skewed chain of 3000 nodes, target does not exist
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(1);
            TreeNode curr = root;
            for (int k = 2; k <= nNodes; k++) {
                curr.right = new TreeNode(k);
                curr = curr.right;
            }
            testCases.add(createFromTree(root, -10));
        }

        // 97: Zig-zag tree of 2000 nodes, target exists
        {
            int nNodes = 2000;
            int low = 1, high = nNodes;
            TreeNode root = new TreeNode(high--);
            TreeNode curr = root;
            boolean left = true;
            while (low <= high) {
                if (left) {
                    curr.left = new TreeNode(low++);
                    curr = curr.left;
                } else {
                    curr.right = new TreeNode(high--);
                    curr = curr.right;
                }
                left = !left;
            }
            testCases.add(createFromTree(root, 100 + 1900));
        }

        // 98: Balanced BST N = 8191 with values near 10^9, sum near 1.9 * 10^9
        {
            int nNodes = 8191;
            int[] sorted = new int[nNodes];
            int base = 900000000;
            for (int k = 0; k < nNodes; k++) sorted[k] = base + k * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            long targetK = (long) sorted[100] + sorted[nNodes - 100];
            testCases.add(createFromTree(root, targetK));
        }

        // 99: Maximum constraint test N = 10000 nodes, target does not exist
        {
            int nNodes = 10000;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 5;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            long targetK = (long) sorted[nNodes - 1] * 2 + 5;
            testCases.add(createFromTree(root, targetK));
        }

        System.out.println("Total test cases prepared: " + testCases.size());
        if (testCases.size() != 100) {
            throw new IllegalStateException("Expected exactly 100 test cases, got: " + testCases.size());
        }

        // Write all testcases to disk
        for (int i = 0; i < 100; i++) {
            String filename = String.format("%02d.txt", i);
            File inFile = new File(inputDir, "input" + filename);
            File outFile = new File(outputDir, "output" + filename);

            TestCase tc = testCases.get(i);

            try (PrintWriter inPw = new PrintWriter(new BufferedWriter(new FileWriter(inFile)))) {
                inPw.println(tc.serializedTree.length + " " + tc.k);
                for (int j = 0; j < tc.serializedTree.length; j++) {
                    if (j > 0) inPw.print(" ");
                    inPw.print(tc.serializedTree[j]);
                }
                inPw.println();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (PrintWriter outPw = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))) {
                outPw.println(tc.expected ? "true" : "false");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Successfully generated all 100 input and output test cases!");
    }

    private static TestCase createFromRaw(int[] raw, long k) {
        TreeNode root = buildTree(raw);
        boolean expected = findTarget(root, k);
        return new TestCase(raw, k, expected);
    }

    private static TestCase createFromTree(TreeNode root, long k) {
        List<Integer> serialized = serializeTree(root);
        int[] arr = new int[serialized.size()];
        for (int i = 0; i < serialized.size(); i++) {
            arr[i] = serialized.get(i);
        }
        boolean expected = findTarget(root, k);
        return new TestCase(arr, k, expected);
    }
}
