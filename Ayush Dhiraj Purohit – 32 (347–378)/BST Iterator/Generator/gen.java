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
     * Optimal BST Iterator utilizing Monotonic Stack.
     */
    public static class BSTIterator {
        private final Deque<TreeNode> stack;

        public BSTIterator(TreeNode root) {
            this.stack = new ArrayDeque<>();
            pushAll(root);
        }

        private void pushAll(TreeNode node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        public int next() {
            TreeNode node = stack.pop();
            if (node.right != null) {
                pushAll(node.right);
            }
            return node.val;
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }
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
     * Counts total number of nodes in tree iteratively.
     */
    public static int countNodes(TreeNode root) {
        if (root == null) return 0;
        int count = 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            count++;
            if (curr.left != null) queue.offer(curr.left);
            if (curr.right != null) queue.offer(curr.right);
        }
        return count;
    }

    static class TestCase {
        int[] serializedTree;
        List<String> operations;
        List<String> expectedOutputs;

        TestCase(int[] serializedTree, List<String> operations, List<String> expectedOutputs) {
            this.serializedTree = serializedTree;
            this.operations = operations;
            this.expectedOutputs = expectedOutputs;
        }
    }

    private static File findPackageDir() {
        File curr = new File(".").getAbsoluteFile();
        while (curr != null) {
            File testcases = new File(curr, "Testcases");
            if (testcases.exists() && testcases.isDirectory()) {
                return curr;
            }
            File subTarget = new File(curr, "BST Iterator");
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
        // Test Cases 00 - 02: Problem Statement Examples
        // -------------------------------------------------------------
        // 00: Example 1
        testCases.add(createFromRaw(
            new int[]{7, 3, 15, -1, -1, 9, 20},
            Arrays.asList("next", "next", "hasNext", "next", "hasNext", "next", "hasNext", "next", "hasNext")
        ));

        // 01: Example 2
        testCases.add(createFromRaw(
            new int[]{2, 1, 3},
            Arrays.asList("hasNext", "next", "next", "next", "hasNext")
        ));

        // 02: Example 3
        testCases.add(createFromRaw(
            new int[]{42},
            Arrays.asList("hasNext", "next", "hasNext")
        ));

        // -------------------------------------------------------------
        // Test Cases 03 - 14: Minimal trees, edge operations, boundaries
        // -------------------------------------------------------------
        // 03: Two nodes root and right child, full traversal
        testCases.add(createFromRaw(
            new int[]{1, -1, 2},
            Arrays.asList("hasNext", "next", "hasNext", "next", "hasNext")
        ));

        // 04: Two nodes root and left child, full traversal
        testCases.add(createFromRaw(
            new int[]{2, 1},
            Arrays.asList("next", "hasNext", "next", "hasNext")
        ));

        // 05: Single node with large value 10^9
        testCases.add(createFromRaw(
            new int[]{1000000000},
            Arrays.asList("hasNext", "hasNext", "next", "hasNext", "hasNext")
        ));

        // 06: Three nodes left-skewed [3, 2, -1, 1]
        testCases.add(createFromRaw(
            new int[]{3, 2, -1, 1},
            Arrays.asList("next", "next", "next", "hasNext")
        ));

        // 07: Three nodes right-skewed [1, -1, 2, -1, 3]
        testCases.add(createFromRaw(
            new int[]{1, -1, 2, -1, 3},
            Arrays.asList("hasNext", "next", "hasNext", "next", "hasNext", "next", "hasNext")
        ));

        // 08: Value 0 included
        testCases.add(createFromRaw(
            new int[]{50, 0, 100},
            Arrays.asList("next", "next", "next", "hasNext")
        ));

        // 09: Only next() calls without hasNext()
        testCases.add(createFromRaw(
            new int[]{4, 2, 6, 1, 3, 5, 7},
            Arrays.asList("next", "next", "next", "next", "next", "next", "next")
        ));

        // 10: Multiple consecutive hasNext() calls
        testCases.add(createFromRaw(
            new int[]{10, 5, 15},
            Arrays.asList("hasNext", "hasNext", "hasNext", "next", "hasNext", "hasNext", "next", "next", "hasNext")
        ));

        // 11: 4 nodes zig-zag [4, 1, -1, -1, 3, 2]
        testCases.add(createFromRaw(
            new int[]{4, 1, -1, -1, 3, 2},
            Arrays.asList("next", "next", "next", "next", "hasNext")
        ));

        // 12: Partial traversal (only 2 elements fetched from 5 nodes)
        testCases.add(createFromRaw(
            new int[]{5, 3, 7, 1, -1, -1, 9},
            Arrays.asList("hasNext", "next", "hasNext", "next", "hasNext")
        ));

        // 13: 5 nodes balanced
        testCases.add(createFromRaw(
            new int[]{3, 2, 5, 1, -1, 4},
            Arrays.asList("next", "hasNext", "next", "next", "hasNext", "next", "next", "hasNext")
        ));

        // 14: Single node only next()
        testCases.add(createFromRaw(
            new int[]{999999999},
            Arrays.asList("next")
        ));

        // -------------------------------------------------------------
        // Test Cases 15 - 34: Small-to-medium diverse trees (N = 5 .. 50)
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

            int totalNodes = countNodes(root);
            List<String> ops = generateOperations(totalNodes, totalNodes * 2, rand);
            testCases.add(createFromTree(root, ops));
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

            int totalNodes = countNodes(root);
            int opCount = Math.min(2000, totalNodes + 100);
            List<String> ops = generateOperations(totalNodes, opCount, rand);
            testCases.add(createFromTree(root, ops));
        }

        // -------------------------------------------------------------
        // Test Cases 70 - 89: Large trees (N = 1000 .. 4000)
        // -------------------------------------------------------------
        for (int i = 70; i <= 89; i++) {
            int nodeCount = 1000 + (i - 70) * 150; // 1000 to 3850 nodes
            Set<Integer> uniqueVals = new TreeSet<>();
            while (uniqueVals.size() < nodeCount) {
                uniqueVals.add(rand.nextInt(1000000000) + 1);
            }
            int[] sorted = uniqueVals.stream().mapToInt(Integer::intValue).toArray();
            TreeNode root = sortedArrayToBST(sorted, 0, sorted.length - 1);

            int totalNodes = countNodes(root);
            int opCount = Math.min(5000, totalNodes * 2);
            List<String> ops = generateOperations(totalNodes, opCount, rand);
            testCases.add(createFromTree(root, ops));
        }

        // -------------------------------------------------------------
        // Test Cases 90 - 99: Boundary & Pathological Stress Tests
        // -------------------------------------------------------------
        // 90: Balanced BST N = 4095, full traversal with hasNext
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<String> ops = new ArrayList<>();
            for (int k = 0; k < nNodes; k++) {
                ops.add("hasNext");
                ops.add("next");
            }
            ops.add("hasNext");
            testCases.add(createFromTree(root, ops));
        }

        // 91: Balanced BST N = 4095, huge values near 10^9
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            int base = 900000000;
            for (int k = 0; k < nNodes; k++) sorted[k] = base + k * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<String> ops = generateOperations(nNodes, 5000, rand);
            testCases.add(createFromTree(root, ops));
        }

        // 92: Strictly left-skewed chain of 3000 nodes, full traversal
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(nNodes);
            TreeNode curr = root;
            for (int k = nNodes - 1; k >= 1; k--) {
                curr.left = new TreeNode(k);
                curr = curr.left;
            }
            List<String> ops = new ArrayList<>();
            for (int k = 0; k < nNodes; k++) {
                ops.add("next");
            }
            ops.add("hasNext");
            testCases.add(createFromTree(root, ops));
        }

        // 93: Strictly right-skewed chain of 3000 nodes, full traversal
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(1);
            TreeNode curr = root;
            for (int k = 2; k <= nNodes; k++) {
                curr.right = new TreeNode(k);
                curr = curr.right;
            }
            List<String> ops = new ArrayList<>();
            for (int k = 0; k < nNodes; k++) {
                ops.add("next");
            }
            ops.add("hasNext");
            testCases.add(createFromTree(root, ops));
        }

        // 94: Zig-zag tree of 2000 nodes
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
            List<String> ops = generateOperations(nNodes, 3000, rand);
            testCases.add(createFromTree(root, ops));
        }

        // 95: Left-heavy tree of 4000 nodes
        {
            int nNodes = 4000;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = k + 1;
            TreeNode root = new TreeNode(sorted[3500]);
            root.left = sortedArrayToBST(sorted, 0, 3499);
            root.right = sortedArrayToBST(sorted, 3501, nNodes - 1);
            List<String> ops = generateOperations(nNodes, 4500, rand);
            testCases.add(createFromTree(root, ops));
        }

        // 96: Right-heavy tree of 4000 nodes
        {
            int nNodes = 4000;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = k + 1;
            TreeNode root = new TreeNode(sorted[500]);
            root.left = sortedArrayToBST(sorted, 0, 499);
            root.right = sortedArrayToBST(sorted, 501, nNodes - 1);
            List<String> ops = generateOperations(nNodes, 4500, rand);
            testCases.add(createFromTree(root, ops));
        }

        // 97: Balanced BST N = 7000 nodes, 5000 next() operations
        {
            int nNodes = 7000;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 3;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<String> ops = new ArrayList<>();
            for (int k = 0; k < 5000; k++) {
                ops.add("next");
            }
            ops.add("hasNext");
            testCases.add(createFromTree(root, ops));
        }

        // 98: Balanced BST N = 8191 nodes, all next() operations
        {
            int nNodes = 8191;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = k + 1;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<String> ops = new ArrayList<>();
            for (int k = 0; k < nNodes; k++) {
                ops.add("next");
            }
            ops.add("hasNext");
            testCases.add(createFromTree(root, ops));
        }

        // 99: Maximum boundary test N = 10000 nodes, Q = 15000 operations
        {
            int nNodes = 10000;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 5;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<String> ops = generateOperations(nNodes, 15000, rand);
            testCases.add(createFromTree(root, ops));
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
                inPw.println(tc.serializedTree.length);
                for (int j = 0; j < tc.serializedTree.length; j++) {
                    if (j > 0) inPw.print(" ");
                    inPw.print(tc.serializedTree[j]);
                }
                inPw.println();

                inPw.println(tc.operations.size());
                for (int j = 0; j < tc.operations.size(); j++) {
                    if (j > 0) inPw.print(" ");
                    inPw.print(tc.operations.get(j));
                }
                inPw.println();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (PrintWriter outPw = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))) {
                for (int j = 0; j < tc.expectedOutputs.size(); j++) {
                    if (j > 0) outPw.print(" ");
                    outPw.print(tc.expectedOutputs.get(j));
                }
                outPw.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Successfully generated all 100 input and output test cases!");
    }

    private static List<String> generateOperations(int totalNodes, int maxOps, Random rand) {
        List<String> ops = new ArrayList<>();
        int remainingNext = totalNodes;

        for (int i = 0; i < maxOps; i++) {
            if (remainingNext > 0) {
                if (rand.nextBoolean()) {
                    ops.add("next");
                    remainingNext--;
                } else {
                    ops.add("hasNext");
                }
            } else {
                ops.add("hasNext");
            }
        }
        return ops;
    }

    private static TestCase createFromRaw(int[] raw, List<String> ops) {
        TreeNode root = buildTree(raw);
        BSTIterator iterator = new BSTIterator(root);
        List<String> outputs = new ArrayList<>();

        for (String op : ops) {
            if ("next".equals(op)) {
                outputs.add(String.valueOf(iterator.next()));
            } else if ("hasNext".equals(op)) {
                outputs.add(String.valueOf(iterator.hasNext()));
            }
        }

        return new TestCase(raw, ops, outputs);
    }

    private static TestCase createFromTree(TreeNode root, List<String> ops) {
        List<Integer> serialized = serializeTree(root);
        int[] arr = new int[serialized.size()];
        for (int i = 0; i < serialized.size(); i++) {
            arr[i] = serialized.get(i);
        }

        BSTIterator iterator = new BSTIterator(root);
        List<String> outputs = new ArrayList<>();

        for (String op : ops) {
            if ("next".equals(op)) {
                outputs.add(String.valueOf(iterator.next()));
            } else if ("hasNext".equals(op)) {
                outputs.add(String.valueOf(iterator.hasNext()));
            }
        }

        return new TestCase(arr, ops, outputs);
    }
}
