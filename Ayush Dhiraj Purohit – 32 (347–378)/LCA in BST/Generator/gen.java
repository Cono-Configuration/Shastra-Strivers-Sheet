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
     * Optimal LCA solver in BST.
     */
    public static TreeNode lowestCommonAncestor(TreeNode root, int p, int q) {
        TreeNode curr = root;
        while (curr != null) {
            if (p < curr.val && q < curr.val) {
                curr = curr.left;
            } else if (p > curr.val && q > curr.val) {
                curr = curr.right;
            } else {
                return curr;
            }
        }
        return null;
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
                break; // duplicate
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
     * Collects all node values in the tree iteratively.
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
        int p;
        int q;
        int expectedLCA;

        TestCase(int[] serializedTree, int p, int q, int expectedLCA) {
            this.serializedTree = serializedTree;
            this.p = p;
            this.q = q;
            this.expectedLCA = expectedLCA;
        }
    }

    private static File findPackageDir() {
        File curr = new File(".").getAbsoluteFile();
        while (curr != null) {
            File testcases = new File(curr, "Testcases");
            if (testcases.exists() && testcases.isDirectory()) {
                return curr;
            }
            File subTarget = new File(curr, "LCA in BST");
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
        // 00: Example 1 (N = 7, p = 2, q = 4, LCA = 3)
        testCases.add(createFromRaw(new int[]{5, 3, 6, 2, 4, -1, 7}, 2, 4));

        // 01: Example 2 (N = 7, p = 2, q = 7, LCA = 5)
        testCases.add(createFromRaw(new int[]{5, 3, 6, 2, 4, -1, 7}, 2, 7));

        // 02: Example 3 (N = 2, p = 2, q = 1, LCA = 2)
        testCases.add(createFromRaw(new int[]{2, 1}, 2, 1));

        // 03: Example 4 (N = 11, p = 7, q = 9, LCA = 8)
        testCases.add(createFromRaw(new int[]{6, 2, 8, 0, 4, 7, 9, -1, -1, 3, 5}, 7, 9));

        // -------------------------------------------------------------
        // Test Cases 04 - 14: Minimal trees, skewed small trees, boundaries
        // -------------------------------------------------------------
        // 04: Two nodes: root and right child, p is root
        testCases.add(createFromRaw(new int[]{10, -1, 20}, 10, 20));

        // 05: Two nodes: root and left child, q is root
        testCases.add(createFromRaw(new int[]{20, 10}, 10, 20));

        // 06: Two nodes: huge values up to 10^9
        testCases.add(createFromRaw(new int[]{1000000000, 0}, 0, 1000000000));

        // 07: Two nodes: huge values up to 10^9 right child
        testCases.add(createFromRaw(new int[]{500000000, -1, 999999999}, 999999999, 500000000));

        // 08: 3 nodes balanced: p and q in opposite subtrees (LCA is root)
        testCases.add(createFromRaw(new int[]{5, 2, 8}, 2, 8));

        // 09: 3 nodes balanced: p is root, q is left child
        testCases.add(createFromRaw(new int[]{5, 2, 8}, 5, 2));

        // 10: 3 nodes balanced: p is right child, q is root
        testCases.add(createFromRaw(new int[]{5, 2, 8}, 8, 5));

        // 11: 3 nodes left-skewed: p=1, q=2 (LCA=2)
        testCases.add(createFromRaw(new int[]{3, 2, -1, 1}, 1, 2));

        // 12: 3 nodes left-skewed: p=1, q=3 (LCA=3, root)
        testCases.add(createFromRaw(new int[]{3, 2, -1, 1}, 1, 3));

        // 13: 3 nodes right-skewed: p=2, q=3 (LCA=2)
        testCases.add(createFromRaw(new int[]{1, -1, 2, -1, 3}, 2, 3));

        // 14: 3 nodes right-skewed: p=1, q=3 (LCA=1, root)
        testCases.add(createFromRaw(new int[]{1, -1, 2, -1, 3}, 1, 3));

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
                // Random BST insertion
                List<Integer> shuffled = new ArrayList<>();
                for (int v : sorted) shuffled.add(v);
                Collections.shuffle(shuffled, rand);
                root = null;
                for (int v : shuffled) root = insertBST(root, v);
            } else {
                // Zig-zag shaped tree
                root = new TreeNode(sorted[sorted.length / 2]);
                for (int j = 0; j < sorted.length; j++) {
                    root = insertBST(root, sorted[j]);
                }
            }

            List<Integer> vals = collectAllValues(root);
            int idx1 = rand.nextInt(vals.size());
            int idx2 = rand.nextInt(vals.size());
            while (idx2 == idx1) {
                idx2 = rand.nextInt(vals.size());
            }
            testCases.add(createFromTree(root, vals.get(idx1), vals.get(idx2)));
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

            List<Integer> vals = collectAllValues(root);
            int idx1 = rand.nextInt(vals.size());
            int idx2 = rand.nextInt(vals.size());
            while (idx2 == idx1) {
                idx2 = rand.nextInt(vals.size());
            }

            // In some cases explicitly test root as one node
            if (i % 5 == 0 && root != null) {
                idx1 = vals.indexOf(root.val);
            }
            testCases.add(createFromTree(root, vals.get(idx1), vals.get(idx2)));
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

            List<Integer> vals = collectAllValues(root);
            int idx1 = rand.nextInt(vals.size());
            int idx2 = rand.nextInt(vals.size());
            while (idx2 == idx1) {
                idx2 = rand.nextInt(vals.size());
            }
            testCases.add(createFromTree(root, vals.get(idx1), vals.get(idx2)));
        }

        // -------------------------------------------------------------
        // Test Cases 90 - 99: Boundary & Pathological Stress Tests
        // -------------------------------------------------------------
        // 90: Balanced BST with N = 4095 (2^12 - 1), p and q in opposite subtrees (LCA is root)
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            testCases.add(createFromTree(root, sorted[0], sorted[nNodes - 1]));
        }

        // 91: Balanced BST with N = 4095, p and q are deep adjacent leaves in left subtree
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            testCases.add(createFromTree(root, sorted[0], sorted[2]));
        }

        // 92: Balanced BST with N = 4095, p is ancestor of q
        {
            int nNodes = 4095;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) sorted[k] = (k + 1) * 10;
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            int rootVal = root.val;
            int leftLeafVal = sorted[0];
            testCases.add(createFromTree(root, rootVal, leftLeafVal));
        }

        // 93: Strictly left-skewed tree of 3000 nodes, p and q at bottom
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(nNodes);
            TreeNode curr = root;
            for (int k = nNodes - 1; k >= 1; k--) {
                curr.left = new TreeNode(k);
                curr = curr.left;
            }
            testCases.add(createFromTree(root, 1, 2));
        }

        // 94: Strictly left-skewed tree of 3000 nodes, p is root, q is leaf
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(nNodes);
            TreeNode curr = root;
            for (int k = nNodes - 1; k >= 1; k--) {
                curr.left = new TreeNode(k);
                curr = curr.left;
            }
            testCases.add(createFromTree(root, nNodes, 1));
        }

        // 95: Strictly right-skewed tree of 3000 nodes, p and q at bottom
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(1);
            TreeNode curr = root;
            for (int k = 2; k <= nNodes; k++) {
                curr.right = new TreeNode(k);
                curr = curr.right;
            }
            testCases.add(createFromTree(root, nNodes - 1, nNodes));
        }

        // 96: Strictly right-skewed tree of 3000 nodes, p is root, q is leaf
        {
            int nNodes = 3000;
            TreeNode root = new TreeNode(1);
            TreeNode curr = root;
            for (int k = 2; k <= nNodes; k++) {
                curr.right = new TreeNode(k);
                curr = curr.right;
            }
            testCases.add(createFromTree(root, 1, nNodes));
        }

        // 97: Zig-zag tree of 2000 nodes
        {
            int nNodes = 2000;
            int low = 1, high = nNodes;
            TreeNode root = new TreeNode(high--);
            TreeNode curr = root;
            boolean leftTurn = true;
            while (low <= high) {
                if (leftTurn) {
                    curr.left = new TreeNode(low++);
                    curr = curr.left;
                } else {
                    curr.right = new TreeNode(high--);
                    curr = curr.right;
                }
                leftTurn = !leftTurn;
            }
            List<Integer> vals = collectAllValues(root);
            testCases.add(createFromTree(root, vals.get(vals.size() - 1), vals.get(vals.size() - 2)));
        }

        // 98: Very large balanced BST (N = 8191) with values near 10^9
        {
            int nNodes = 8191;
            int[] sorted = new int[nNodes];
            int base = 900000000;
            for (int k = 0; k < nNodes; k++) {
                sorted[k] = base + k * 10;
            }
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            testCases.add(createFromTree(root, sorted[100], sorted[nNodes - 100]));
        }

        // 99: Maximum constraint stress test (N = 8191), random p and q
        {
            int nNodes = 8191;
            int[] sorted = new int[nNodes];
            for (int k = 0; k < nNodes; k++) {
                sorted[k] = k + 1;
            }
            TreeNode root = sortedArrayToBST(sorted, 0, nNodes - 1);
            List<Integer> vals = collectAllValues(root);
            int p = vals.get(rand.nextInt(vals.size()));
            int q = vals.get(rand.nextInt(vals.size()));
            while (q == p) {
                q = vals.get(rand.nextInt(vals.size()));
            }
            testCases.add(createFromTree(root, p, q));
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
                inPw.println(tc.serializedTree.length + " " + tc.p + " " + tc.q);
                for (int j = 0; j < tc.serializedTree.length; j++) {
                    if (j > 0) inPw.print(" ");
                    inPw.print(tc.serializedTree[j]);
                }
                inPw.println();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (PrintWriter outPw = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))) {
                outPw.println(tc.expectedLCA);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Successfully generated all 100 input and output test cases!");
    }

    private static TestCase createFromRaw(int[] raw, int p, int q) {
        TreeNode root = buildTree(raw);
        TreeNode lca = lowestCommonAncestor(root, p, q);
        if (lca == null) {
            throw new IllegalArgumentException("LCA not found for p=" + p + ", q=" + q);
        }
        return new TestCase(raw, p, q, lca.val);
    }

    private static TestCase createFromTree(TreeNode root, int p, int q) {
        List<Integer> serialized = serializeTree(root);
        int[] arr = new int[serialized.size()];
        for (int i = 0; i < serialized.size(); i++) {
            arr[i] = serialized.get(i);
        }
        TreeNode lca = lowestCommonAncestor(root, p, q);
        if (lca == null) {
            throw new IllegalArgumentException("LCA not found for p=" + p + ", q=" + q);
        }
        return new TestCase(arr, p, q, lca.val);
    }
}
