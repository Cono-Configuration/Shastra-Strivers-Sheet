import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class gen {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    public static boolean isValidBST(TreeNode root) {
        if (root == null) return true;

        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;
        TreeNode prev = null;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();

            if (prev != null && curr.val <= prev.val) {
                return false;
            }
            prev = curr;

            curr = curr.right;
        }

        return true;
    }

    public static TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);
        TreeNode curr = root;
        while (true) {
            if (val < curr.val) {
                if (curr.left != null) curr = curr.left;
                else { curr.left = new TreeNode(val); break; }
            } else {
                if (curr.right != null) curr = curr.right;
                else { curr.right = new TreeNode(val); break; }
            }
        }
        return root;
    }

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

    public static List<Integer> serialize(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            if (curr != null) {
                result.add(curr.val);
                queue.offer(curr.left);
                queue.offer(curr.right);
            } else {
                result.add(-1);
            }
        }

        int lastIndex = result.size() - 1;
        while (lastIndex >= 0 && result.get(lastIndex) == -1) {
            lastIndex--;
        }

        return result.subList(0, lastIndex + 1);
    }

    private static void writeTestCase(File inDir, File outDir, int index, int[] arr) throws Exception {
        String filename = String.format("%02d.txt", index);
        File inFile = new File(inDir, "input" + filename);
        File outFile = new File(outDir, "output" + filename);

        // Write input file
        try (PrintWriter inPw = new PrintWriter(inFile)) {
            inPw.println(arr.length);
            if (arr.length > 0) {
                for (int i = 0; i < arr.length; i++) {
                    inPw.print(arr[i]);
                    if (i + 1 < arr.length) inPw.print(" ");
                }
                inPw.println();
            }
        }

        // Compute expected output
        TreeNode root = buildTree(arr);
        boolean valid = isValidBST(root);

        // Write output file
        try (PrintWriter outPw = new PrintWriter(outFile)) {
            outPw.println(valid);
        }
    }

    private static TreeNode buildBalanced(int[] sortedArr, int start, int end) {
        if (start > end) return null;
        int mid = (start + end) / 2;
        TreeNode node = new TreeNode(sortedArr[mid]);
        node.left = buildBalanced(sortedArr, start, mid - 1);
        node.right = buildBalanced(sortedArr, mid + 1, end);
        return node;
    }

    private static int[] toArray(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    public static void main(String[] args) {
        try {
            File baseDir = new File("../Testcases");
            if (!baseDir.exists()) baseDir = new File("Testcases");
            if (!baseDir.exists()) {
                baseDir = new File("e:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Check if a tree is a BST or not/Testcases");
            }

            File inDir = new File(baseDir, "Input");
            File outDir = new File(baseDir, "Output");
            inDir.mkdirs();
            outDir.mkdirs();

            Random rng = new Random(20261024L);

            // ==================== 00 - 04: Problem Statement Samples ====================
            // 00: Sample 1: root = [5, 3, 6, 2, 4, -1, 7] -> true
            writeTestCase(inDir, outDir, 0, new int[]{5, 3, 6, 2, 4, -1, 7});

            // 01: Sample 2: root = [5, 3, 6, 4, 2, -1, 7] -> false
            writeTestCase(inDir, outDir, 1, new int[]{5, 3, 6, 4, 2, -1, 7});

            // 02: Sample 3: root = [5, 1, 4, -1, -1, 3, 6] -> false
            writeTestCase(inDir, outDir, 2, new int[]{5, 1, 4, -1, -1, 3, 6});

            // 03: Sample 4: root = [10] -> true
            writeTestCase(inDir, outDir, 3, new int[]{10});

            // 04: Sample 5: empty tree [] -> true
            writeTestCase(inDir, outDir, 4, new int[0]);

            // ==================== 05 - 19: Minimal Boundaries & Corner Cases ====================
            // 05: Two nodes [10, 5] -> true
            writeTestCase(inDir, outDir, 5, new int[]{10, 5});

            // 06: Two nodes [10, 15] (left child > root) -> false
            writeTestCase(inDir, outDir, 6, new int[]{10, 15});

            // 07: Two nodes [10, -1, 20] -> true
            writeTestCase(inDir, outDir, 7, new int[]{10, -1, 20});

            // 08: Two nodes [10, -1, 5] (right child < root) -> false
            writeTestCase(inDir, outDir, 8, new int[]{10, -1, 5});

            // 09: Duplicate values [2, 2, 2] -> false
            writeTestCase(inDir, outDir, 9, new int[]{2, 2, 2});

            // 10: Three nodes [2, 1, 3] -> true
            writeTestCase(inDir, outDir, 10, new int[]{2, 1, 3});

            // 11: Three nodes [2, 3, 1] -> false
            writeTestCase(inDir, outDir, 11, new int[]{2, 3, 1});

            // 12: Right-skewed 5-node BST [10, -1, 20, -1, 30, -1, 40, -1, 50] -> true
            TreeNode skewR = null;
            for (int v : new int[]{10, 20, 30, 40, 50}) skewR = insertIntoBST(skewR, v);
            writeTestCase(inDir, outDir, 12, toArray(serialize(skewR)));

            // 13: Right-skewed invalid (tail decreased) -> false
            int[] arr13 = toArray(serialize(skewR));
            arr13[arr13.length - 1] = 5;
            writeTestCase(inDir, outDir, 13, arr13);

            // 14: Left-skewed 5-node BST [50, 40, -1, 30, -1, 20, -1, 10] -> true
            TreeNode skewL = null;
            for (int v : new int[]{50, 40, 30, 20, 10}) skewL = insertIntoBST(skewL, v);
            writeTestCase(inDir, outDir, 14, toArray(serialize(skewL)));

            // 15: Left-skewed invalid (tail increased) -> false
            int[] arr15 = toArray(serialize(skewL));
            arr15[arr15.length - 1] = 100;
            writeTestCase(inDir, outDir, 15, arr15);

            // 16: Ancestor violation: [10, 5, 15, -1, -1, 6, 20] (6 in right subtree of 10) -> false
            writeTestCase(inDir, outDir, 16, new int[]{10, 5, 15, -1, -1, 6, 20});

            // 17: Ancestor violation: [10, 5, 15, 2, 12, -1, -1] (12 in left subtree of 10) -> false
            writeTestCase(inDir, outDir, 17, new int[]{10, 5, 15, 2, 12, -1, -1});

            // 18: Duplicate value equal to root [10, 5, 15, -1, 10, -1, -1] -> false
            writeTestCase(inDir, outDir, 18, new int[]{10, 5, 15, -1, 10, -1, -1});

            // 19: Valid extreme values [0, -1, 1000000000] -> true
            writeTestCase(inDir, outDir, 19, new int[]{0, -1, 1000000000});

            // ==================== 20 - 39: Small to Medium Trees (N = 10..50) ====================
            for (int tc = 20; tc <= 39; tc++) {
                int nNodes = 10 + (tc - 20) * 2;
                int[] sorted = new int[nNodes];
                int startVal = 100 + tc * 20;
                for (int i = 0; i < nNodes; i++) sorted[i] = startVal + i * 10;

                TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                int[] arr = toArray(serialize(bRoot));

                if (tc % 2 == 1) {
                    // Inject violation: swap two adjacent non-null elements or invert one
                    int idx = 1 + rng.nextInt(Math.max(1, arr.length - 2));
                    while (arr[idx] == -1) idx = (idx + 1) % arr.length;
                    arr[idx] = sorted[nNodes - 1] + 500; // Violates ancestor upper bound
                }

                writeTestCase(inDir, outDir, tc, arr);
            }

            // ==================== 40 - 69: Large Trees (N = 100..1000) ====================
            for (int tc = 40; tc <= 69; tc++) {
                int nNodes = 100 + (tc - 40) * 25 + rng.nextInt(15);
                int[] sorted = new int[nNodes];
                int startVal = 1000 + tc * 100;
                for (int i = 0; i < nNodes; i++) sorted[i] = startVal + i * 4;

                int[] arr;
                if (tc % 4 == 0) {
                    // Valid balanced BST
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                } else if (tc % 4 == 1) {
                    // Valid random BST
                    List<Integer> list = new ArrayList<>();
                    for (int v : sorted) list.add(v);
                    Collections.shuffle(list, rng);
                    TreeNode rRoot = null;
                    for (int v : list) rRoot = insertIntoBST(rRoot, v);
                    arr = toArray(serialize(rRoot));
                } else if (tc % 4 == 2) {
                    // Invalid tree: ancestor violation deep in tree
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    int targetIdx = arr.length / 2;
                    while (targetIdx < arr.length && arr[targetIdx] == -1) targetIdx++;
                    if (targetIdx < arr.length) arr[targetIdx] = startVal - 50; // Violates lower bound
                } else {
                    // Invalid tree: duplicate value
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    int targetIdx = arr.length - 1;
                    while (targetIdx > 0 && arr[targetIdx] == -1) targetIdx--;
                    arr[targetIdx] = arr[0]; // duplicate root value at leaf
                }

                writeTestCase(inDir, outDir, tc, arr);
            }

            // ==================== 70 - 89: Very Large Trees (N = 1500..5000) ====================
            for (int tc = 70; tc <= 89; tc++) {
                int nNodes = 1500 + (tc - 70) * 170 + rng.nextInt(25);
                int[] sorted = new int[nNodes];
                int startVal = 50000 + tc * 1000;
                for (int i = 0; i < nNodes; i++) sorted[i] = startVal + i * 6;

                int[] arr;
                if (tc % 2 == 0) {
                    // Valid Large Balanced BST
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                } else {
                    // Invalid Large Tree: single leaf violation
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    int last = arr.length - 1;
                    while (last >= 0 && arr[last] == -1) last--;
                    arr[last] = (tc % 4 == 1) ? 0 : 2000000000;
                }

                writeTestCase(inDir, outDir, tc, arr);
            }

            // ==================== 90 - 99: Extreme Constraints & Stress Tests (N ≈ 10000) ====================
            // 90: Large Balanced BST (N = 8191), completely valid -> true
            {
                int nNodes = 8191;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root90 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 90, toArray(serialize(root90)));
            }

            // 91: Large Balanced BST (N = 8191), leaf violation -> false
            {
                int nNodes = 8191;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root91 = buildBalanced(sorted, 0, nNodes - 1);
                int[] arr91 = toArray(serialize(root91));
                arr91[arr91.length - 1] = 0; // Violated
                writeTestCase(inDir, outDir, 91, arr91);
            }

            // 92: Strictly right-skewed BST of 10,000 nodes, strictly increasing -> true
            {
                int nNodes = 10000;
                int[] arr92 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (i + 1) * 10;
                    if (i == 0) arr92[0] = valNode;
                    else {
                        arr92[2 * i - 1] = -1;
                        arr92[2 * i] = valNode;
                    }
                }
                writeTestCase(inDir, outDir, 92, arr92);
            }

            // 93: Strictly right-skewed tree of 10,000 nodes with tail violated -> false
            {
                int nNodes = 10000;
                int[] arr93 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (i + 1) * 10;
                    if (i == 0) arr93[0] = valNode;
                    else {
                        arr93[2 * i - 1] = -1;
                        arr93[2 * i] = valNode;
                    }
                }
                arr93[arr93.length - 1] = 5; // tail smaller than root -> false
                writeTestCase(inDir, outDir, 93, arr93);
            }

            // 94: Strictly left-skewed BST of 10,000 nodes, strictly decreasing -> true
            {
                int nNodes = 10000;
                int[] arr94 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (nNodes - i) * 10;
                    if (i == 0) arr94[0] = valNode;
                    else {
                        arr94[2 * i - 1] = valNode;
                        arr94[2 * i] = -1;
                    }
                }
                writeTestCase(inDir, outDir, 94, arr94);
            }

            // 95: Strictly left-skewed tree of 10,000 nodes with tail violated -> false
            {
                int nNodes = 10000;
                int[] arr95 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (nNodes - i) * 10;
                    if (i == 0) arr95[0] = valNode;
                    else {
                        arr95[2 * i - 1] = valNode;
                        arr95[2 * i] = -1;
                    }
                }
                arr95[arr95.length - 2] = 200000; // tail larger than root -> false
                writeTestCase(inDir, outDir, 95, arr95);
            }

            // 96: Large random valid BST with 10,000 nodes -> true
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root96 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 96, toArray(serialize(root96)));
            }

            // 97: Large tree with duplicate values at N = 10000 -> false
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root97 = buildBalanced(sorted, 0, nNodes - 1);
                int[] arr97 = toArray(serialize(root97));
                arr97[arr97.length / 2] = arr97[0]; // duplicate root value
                writeTestCase(inDir, outDir, 97, arr97);
            }

            // 98: Large tree with subtle ancestor violation in right subtree (N = 10000) -> false
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = 10 + i * 10;
                TreeNode root98 = buildBalanced(sorted, 0, nNodes - 1);
                int[] arr98 = toArray(serialize(root98));
                // Find node in right subtree of root and set it smaller than root
                int rootVal = arr98[0];
                int rightChildIdx = 2; // Right child index in BFS
                if (rightChildIdx < arr98.length && arr98[rightChildIdx] != -1) {
                    arr98[rightChildIdx] = rootVal - 1; // Direct violation
                }
                writeTestCase(inDir, outDir, 98, arr98);
            }

            // 99: Full balanced binary tree of 10,000 nodes, strictly valid -> true
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 20;
                TreeNode root99 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 99, toArray(serialize(root99)));
            }

            System.out.println("SUCCESS: Generated 100 testcases for Check if a tree is a BST or not.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
