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
     * Optimal BST construction from preorder traversal (iterative monotonic stack).
     */
    public static TreeNode bstFromPreorder(int[] preorder) {
        if (preorder == null || preorder.length == 0) return null;

        TreeNode root = new TreeNode(preorder[0]);
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        for (int i = 1; i < preorder.length; i++) {
            TreeNode node = new TreeNode(preorder[i]);

            if (preorder[i] < stack.peek().val) {
                stack.peek().left = node;
            } else {
                TreeNode parent = stack.peek();
                while (!stack.isEmpty() && stack.peek().val < preorder[i]) {
                    parent = stack.pop();
                }
                parent.right = node;
            }

            stack.push(node);
        }

        return root;
    }

    /**
     * Serializes tree to level-order list of integers (-1 for null), trimming trailing -1s.
     */
    public static List<Integer> serializeLevelOrder(TreeNode root) {
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
     * Collects preorder traversal iteratively.
     */
    public static List<Integer> getPreorder(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            res.add(curr.val);
            if (curr.right != null) stack.push(curr.right);
            if (curr.left != null) stack.push(curr.left);
        }
        return res;
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

    static class TestCase {
        int[] preorder;
        List<Integer> expectedLevelOrder;

        TestCase(int[] preorder, List<Integer> expectedLevelOrder) {
            this.preorder = preorder;
            this.expectedLevelOrder = expectedLevelOrder;
        }
    }

    private static File findPackageDir() {
        File curr = new File(".").getAbsoluteFile();
        while (curr != null) {
            File testcases = new File(curr, "Testcases");
            if (testcases.exists() && testcases.isDirectory()) {
                return curr;
            }
            File subTarget = new File(curr, "Construct a BST from a preorder traversal");
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
        // 00: Example 1 [8, 5, 1, 7, 10, 12]
        testCases.add(createFromPreorder(new int[]{8, 5, 1, 7, 10, 12}));

        // 01: Example 2 [1, 3]
        testCases.add(createFromPreorder(new int[]{1, 3}));

        // 02: Example 3 [2, 1, 3]
        testCases.add(createFromPreorder(new int[]{2, 1, 3}));

        // 03: Example 4 [5, 4, 3, 2, 1]
        testCases.add(createFromPreorder(new int[]{5, 4, 3, 2, 1}));

        // -------------------------------------------------------------
        // Test Cases 04 - 14: Minimal trees, small edges, boundaries
        // -------------------------------------------------------------
        // 04: Single node N = 1
        testCases.add(createFromPreorder(new int[]{1}));

        // 05: Single node with large value
        testCases.add(createFromPreorder(new int[]{1000000000}));

        // 06: Single node with value 0
        testCases.add(createFromPreorder(new int[]{0}));

        // 07: Two nodes: [2, 1] (root and left child)
        testCases.add(createFromPreorder(new int[]{2, 1}));

        // 08: Two nodes with extreme difference
        testCases.add(createFromPreorder(new int[]{0, 1000000000}));

        // 09: Three nodes: [1, 2, 3] (strictly right-skewed)
        testCases.add(createFromPreorder(new int[]{1, 2, 3}));

        // 10: Three nodes: [3, 2, 1] (strictly left-skewed)
        testCases.add(createFromPreorder(new int[]{3, 2, 1}));

        // 11: Three nodes: [3, 1, 2] (left child has right child)
        testCases.add(createFromPreorder(new int[]{3, 1, 2}));

        // 12: Three nodes: [1, 3, 2] (right child has left child)
        testCases.add(createFromPreorder(new int[]{1, 3, 2}));

        // 13: Four nodes balanced
        testCases.add(createFromPreorder(new int[]{3, 1, 2, 4}));

        // 14: Four nodes zig-zag [4, 1, 3, 2]
        testCases.add(createFromPreorder(new int[]{4, 1, 3, 2}));

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
                // Zig-zag tree
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
            testCases.add(createFromTree(root));
        }

        // -------------------------------------------------------------
        // Test Cases 35 - 69: Medium-to-large BSTs (N = 50 .. 1000)
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
            testCases.add(createFromTree(root));
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
            testCases.add(createFromTree(root));
        }

        // -------------------------------------------------------------
        // Test Cases 90 - 99: Boundary & Pathological Stress Tests
        // -------------------------------------------------------------
        // 90: Balanced BST of N = 4095 (2^12 - 1)
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            testCases.add(createFromTree(root));
        }

        // 91: Balanced BST of N = 4095 with huge values near 10^9
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            int base = 950000000;
            for (int k = 0; k < nNodes; k++) sorted[k] = base + k * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            testCases.add(createFromTree(root));
        }

        // 92: Strictly left-skewed chain of 3000 nodes
        {
            int nNodes = 3000;
            int[] preorder = new int[nNodes];
            for (int k = 0; k < nNodes; k++) preorder[k] = nNodes - k;
            testCases.add(createFromPreorder(preorder));
        }

        // 93: Strictly right-skewed chain of 3000 nodes
        {
            int nNodes = 3000;
            int[] preorder = new int[nNodes];
            for (int k = 0; k < nNodes; k++) preorder[k] = k + 1;
            testCases.add(createFromPreorder(preorder));
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
            testCases.add(createFromTree(root));
        }

        // 95: Left-heavy tree of 4000 nodes
        {
            int nNodes = 4000;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = k + 1;
            // Root at 3500 (87% nodes in left subtree)
            TreeNode root = new TreeNode(sorted[3500]);
            root.left = sortedArrayToBST(sorted, 0, 3499);
            root.right = sortedArrayToBST(sorted, 3501, nNodes - 1);
            testCases.add(createFromTree(root));
        }

        // 96: Right-heavy tree of 4000 nodes
        {
            int nNodes = 4000;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = k + 1;
            // Root at 500 (87% nodes in right subtree)
            TreeNode root = new TreeNode(sorted[500]);
            root.left = sortedArrayToBST(sorted, 0, 499);
            root.right = sortedArrayToBST(sorted, 501, nNodes - 1);
            testCases.add(createFromTree(root));
        }

        // 97: Balanced BST with N = 7000 nodes
        {
            int nNodes = 7000;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 3;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            testCases.add(createFromTree(root));
        }

        // 98: Large balanced BST with N = 8191 nodes
        {
            int nNodes = 8191;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = k + 1;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            testCases.add(createFromTree(root));
        }

        // 99: Maximum constraint test N = 10000 nodes
        {
            int nNodes = 10000;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 5;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
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
                inPw.println(tc.preorder.length);
                for (int j = 0; j < tc.preorder.length; j++) {
                    if (j > 0) inPw.print(" ");
                    inPw.print(tc.preorder[j]);
                }
                inPw.println();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (PrintWriter outPw = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))) {
                for (int j = 0; j < tc.expectedLevelOrder.size(); j++) {
                    if (j > 0) outPw.print(" ");
                    outPw.print(tc.expectedLevelOrder.get(j));
                }
                outPw.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Successfully generated all 100 input and output test cases!");
    }

    private static TestCase createFromPreorder(int[] preorder) {
        TreeNode root = bstFromPreorder(preorder);
        List<Integer> levelOrder = serializeLevelOrder(root);
        return new TestCase(preorder, levelOrder);
    }

    private static TestCase createFromTree(TreeNode root) {
        List<Integer> pre = getPreorder(root);
        int[] preorder = new int[pre.size()];
        for (int i = 0; i < pre.size(); i++) {
            preorder[i] = pre.get(i);
        }
        List<Integer> levelOrder = serializeLevelOrder(root);
        return new TestCase(preorder, levelOrder);
    }
}
