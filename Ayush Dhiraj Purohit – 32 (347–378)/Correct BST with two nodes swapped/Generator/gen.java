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
     * Optimal BST recovery solver.
     */
    public static void recoverTree(TreeNode root) {
        if (root == null) return;

        TreeNode first = null;
        TreeNode middle = null;
        TreeNode last = null;
        TreeNode prev = null;

        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }

            curr = stack.pop();

            if (prev != null && prev.val > curr.val) {
                if (first == null) {
                    first = prev;
                    middle = curr;
                } else {
                    last = curr;
                }
            }
            prev = curr;

            curr = curr.right;
        }

        if (first != null && last != null) {
            int temp = first.val;
            first.val = last.val;
            last.val = temp;
        } else if (first != null && middle != null) {
            int temp = first.val;
            first.val = middle.val;
            middle.val = temp;
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
     * Collects all node references in the tree iteratively.
     */
    public static List<TreeNode> collectNodes(TreeNode root) {
        List<TreeNode> list = new ArrayList<>();
        if (root == null) return list;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            list.add(curr);
            if (curr.left != null) queue.offer(curr.left);
            if (curr.right != null) queue.offer(curr.right);
        }
        return list;
    }

    /**
     * Collects nodes in in-order sequence iteratively.
     */
    public static List<TreeNode> collectInOrderNodes(TreeNode root) {
        List<TreeNode> list = new ArrayList<>();
        if (root == null) return list;
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            list.add(curr);
            curr = curr.right;
        }
        return list;
    }

    static class TestCase {
        int[] corruptedTree;
        List<Integer> expectedRecovered;

        TestCase(int[] corruptedTree, List<Integer> expectedRecovered) {
            this.corruptedTree = corruptedTree;
            this.expectedRecovered = expectedRecovered;
        }
    }

    private static File findPackageDir() {
        File curr = new File(".").getAbsoluteFile();
        while (curr != null) {
            File testcases = new File(curr, "Testcases");
            if (testcases.exists() && testcases.isDirectory()) {
                return curr;
            }
            File subTarget = new File(curr, "Correct BST with two nodes swapped");
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
        // 00: Example 1 [1, 3, -1, -1, 2] -> [3, 1, -1, -1, 2]
        testCases.add(createFromRaw(new int[]{1, 3, -1, -1, 2}));

        // 01: Example 2 [3, 1, 4, -1, -1, 2] -> [2, 1, 4, -1, -1, 3]
        testCases.add(createFromRaw(new int[]{3, 1, 4, -1, -1, 2}));

        // 02: Example 3 [2, 3, 1] -> [2, 1, 3]
        testCases.add(createFromRaw(new int[]{2, 3, 1}));

        // 03: Example 4 [1, 2] -> [2, 1]
        testCases.add(createFromRaw(new int[]{1, 2}));

        // -------------------------------------------------------------
        // Test Cases 04 - 14: Minimal trees, small boundaries
        // -------------------------------------------------------------
        // 04: Two nodes root and right child swapped: [2, -1, 1] -> [1, -1, 2]
        testCases.add(createFromRaw(new int[]{2, -1, 1}));

        // 05: Three nodes left-skewed, root and leaf swapped: [1, 2, -1, 3] -> [3, 2, -1, 1]
        testCases.add(createFromRaw(new int[]{1, 2, -1, 3}));

        // 06: Three nodes left-skewed, adjacent nodes swapped: [3, 1, -1, 2] -> [3, 2, -1, 1]
        testCases.add(createFromRaw(new int[]{3, 1, -1, 2}));

        // 07: Three nodes right-skewed, root and leaf swapped: [3, -1, 2, -1, 1] -> [1, -1, 2, -1, 3]
        testCases.add(createFromRaw(new int[]{3, -1, 2, -1, 1}));

        // 08: Three nodes right-skewed, adjacent nodes swapped: [1, -1, 3, -1, 2] -> [1, -1, 2, -1, 3]
        testCases.add(createFromRaw(new int[]{1, -1, 3, -1, 2}));

        // 09: Huge boundary values near 10^9: [1000000000, 500000000, 0] -> [500000000, 0, 1000000000]
        testCases.add(createFromRaw(new int[]{1000000000, 500000000, 0}));

        // 10: 4 nodes zig-zag corrupted
        testCases.add(createFromRaw(new int[]{1, -1, 4, 2, -1, -1, 3}));

        // 11: 4 nodes, root swapped with middle
        testCases.add(createFromRaw(new int[]{1, 2, 4, -1, 3}));

        // 12: 5 nodes, root and right leaf swapped
        testCases.add(createFromRaw(new int[]{7, 2, 5, 1, 3, -1, 4}));

        // 13: 5 nodes, adjacent swap in left subtree
        testCases.add(createFromRaw(new int[]{4, 3, 5, 2, 1}));

        // 14: 5 nodes, adjacent swap in right subtree
        testCases.add(createFromRaw(new int[]{3, 2, 4, 1, -1, -1, 5}));

        // -------------------------------------------------------------
        // Test Cases 15 - 34: Small-to-medium diverse trees (N = 6 .. 50)
        // -------------------------------------------------------------
        for (int i = 15; i <= 34; i++) {
            int nodeCount = 6 + (i - 15) * 2; // 6 to 44 nodes
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

            List<TreeNode> inOrder = collectInOrderNodes(root);
            if (i % 2 == 0) {
                // Adjacent swap
                int idx = rand.nextInt(inOrder.size() - 1);
                swapNodes(inOrder.get(idx), inOrder.get(idx + 1));
            } else {
                // Non-adjacent swap
                int idx1 = rand.nextInt(inOrder.size() - 2);
                int idx2 = idx1 + 2 + rand.nextInt(inOrder.size() - idx1 - 2);
                swapNodes(inOrder.get(idx1), inOrder.get(idx2));
            }
            testCases.add(createFromTree(root));
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

            List<TreeNode> inOrder = collectInOrderNodes(root);
            if (i % 2 == 0) {
                int idx = rand.nextInt(inOrder.size() - 1);
                swapNodes(inOrder.get(idx), inOrder.get(idx + 1));
            } else {
                int idx1 = rand.nextInt(inOrder.size() - 2);
                int idx2 = idx1 + 2 + rand.nextInt(inOrder.size() - idx1 - 2);
                swapNodes(inOrder.get(idx1), inOrder.get(idx2));
            }
            testCases.add(createFromTree(root));
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

            List<TreeNode> inOrder = collectInOrderNodes(root);
            if (i % 2 == 0) {
                int idx = rand.nextInt(inOrder.size() - 1);
                swapNodes(inOrder.get(idx), inOrder.get(idx + 1));
            } else {
                int idx1 = rand.nextInt(inOrder.size() - 2);
                int idx2 = idx1 + 2 + rand.nextInt(inOrder.size() - idx1 - 2);
                swapNodes(inOrder.get(idx1), inOrder.get(idx2));
            }
            testCases.add(createFromTree(root));
        }

        // -------------------------------------------------------------
        // Test Cases 90 - 99: Boundary & Pathological Stress Tests
        // -------------------------------------------------------------
        // 90: Balanced BST N = 4095, min and max nodes swapped (extreme non-adjacent)
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<TreeNode> inOrder = collectInOrderNodes(root);
            swapNodes(inOrder.get(0), inOrder.get(nNodes - 1));
            testCases.add(createFromTree(root));
        }

        // 91: Balanced BST N = 4095, two adjacent middle nodes swapped
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<TreeNode> inOrder = collectInOrderNodes(root);
            swapNodes(inOrder.get(nNodes / 2), inOrder.get(nNodes / 2 + 1));
            testCases.add(createFromTree(root));
        }

        // 92: Balanced BST N = 4095, root swapped with leftmost leaf
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<TreeNode> inOrder = collectInOrderNodes(root);
            swapNodes(inOrder.get(0), root);
            testCases.add(createFromTree(root));
        }

        // 93: Strictly left-skewed chain of 3000 nodes, first and last swapped
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(nNodes);
            TreeNode curr = root;
            for (int k = nNodes - 1; k >= 1; k--) {
                curr.left = new TreeNode(k);
                curr = curr.left;
            }
            List<TreeNode> inOrder = collectInOrderNodes(root);
            swapNodes(inOrder.get(0), inOrder.get(nNodes - 1));
            testCases.add(createFromTree(root));
        }

        // 94: Strictly left-skewed chain of 3000 nodes, adjacent nodes swapped
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(nNodes);
            TreeNode curr = root;
            for (int k = nNodes - 1; k >= 1; k--) {
                curr.left = new TreeNode(k);
                curr = curr.left;
            }
            List<TreeNode> inOrder = collectInOrderNodes(root);
            swapNodes(inOrder.get(100), inOrder.get(101));
            testCases.add(createFromTree(root));
        }

        // 95: Strictly right-skewed chain of 3000 nodes, first and last swapped
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(1);
            TreeNode curr = root;
            for (int k = 2; k <= nNodes; k++) {
                curr.right = new TreeNode(k);
                curr = curr.right;
            }
            List<TreeNode> inOrder = collectInOrderNodes(root);
            swapNodes(inOrder.get(0), inOrder.get(nNodes - 1));
            testCases.add(createFromTree(root));
        }

        // 96: Strictly right-skewed chain of 3000 nodes, adjacent nodes swapped
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(1);
            TreeNode curr = root;
            for (int k = 2; k <= nNodes; k++) {
                curr.right = new TreeNode(k);
                curr = curr.right;
            }
            List<TreeNode> inOrder = collectInOrderNodes(root);
            swapNodes(inOrder.get(500), inOrder.get(501));
            testCases.add(createFromTree(root));
        }

        // 97: Zig-zag tree of 2000 nodes
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
            List<TreeNode> inOrder = collectInOrderNodes(root);
            swapNodes(inOrder.get(10), inOrder.get(1900));
            testCases.add(createFromTree(root));
        }

        // 98: Large balanced BST N = 8191 with values near 10^9
        {
            int nNodes = 8191;
            int[] sorted = new int[nNodes];
            int base = 900000000;
            for (int k = 0; k < nNodes; k++) sorted[k] = base + k * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<TreeNode> inOrder = collectInOrderNodes(root);
            swapNodes(inOrder.get(50), inOrder.get(nNodes - 50));
            testCases.add(createFromTree(root));
        }

        // 99: Maximum boundary test N = 10000 nodes
        {
            int nNodes = 10000;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 5;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<TreeNode> inOrder = collectInOrderNodes(root);
            int idx1 = rand.nextInt(nNodes - 2);
            int idx2 = idx1 + 2 + rand.nextInt(nNodes - idx1 - 2);
            swapNodes(inOrder.get(idx1), inOrder.get(idx2));
            testCases.add(createFromTree(root));
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
                inPw.println(tc.corruptedTree.length);
                for (int j = 0; j < tc.corruptedTree.length; j++) {
                    if (j > 0) inPw.print(" ");
                    inPw.print(tc.corruptedTree[j]);
                }
                inPw.println();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (PrintWriter outPw = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))) {
                for (int j = 0; j < tc.expectedRecovered.size(); j++) {
                    if (j > 0) outPw.print(" ");
                    outPw.print(tc.expectedRecovered.get(j));
                }
                outPw.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Successfully generated all 100 input and output test cases!");
    }

    private static void swapNodes(TreeNode a, TreeNode b) {
        int temp = a.val;
        a.val = b.val;
        b.val = temp;
    }

    private static TestCase createFromRaw(int[] raw) {
        TreeNode root = buildTree(raw);
        recoverTree(root);
        List<Integer> recovered = serializeTree(root);
        return new TestCase(raw, recovered);
    }

    private static TestCase createFromTree(TreeNode root) {
        // Serialized corrupted tree
        List<Integer> corruptedList = serializeTree(root);
        int[] corrupted = new int[corruptedList.size()];
        for (int i = 0; i < corruptedList.size(); i++) {
            corrupted[i] = corruptedList.get(i);
        }

        // Recover tree
        recoverTree(root);
        List<Integer> recovered = serializeTree(root);

        return new TestCase(corrupted, recovered);
    }
}
