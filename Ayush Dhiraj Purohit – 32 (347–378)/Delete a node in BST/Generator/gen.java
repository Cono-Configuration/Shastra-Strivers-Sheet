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

    public static TreeNode deleteNode(TreeNode root, int key) {
        TreeNode curr = root;
        TreeNode parent = null;

        while (curr != null && curr.val != key) {
            parent = curr;
            if (key < curr.val) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        if (curr == null) {
            return root;
        }

        if (curr.left == null || curr.right == null) {
            TreeNode child = (curr.left != null) ? curr.left : curr.right;
            if (parent == null) {
                return child;
            }
            if (parent.left == curr) {
                parent.left = child;
            } else {
                parent.right = child;
            }
            return root;
        }

        TreeNode succParent = curr;
        TreeNode succ = curr.right;
        while (succ.left != null) {
            succParent = succ;
            succ = succ.left;
        }

        curr.val = succ.val;

        if (succParent != curr) {
            succParent.left = succ.right;
        } else {
            succParent.right = succ.right;
        }

        return root;
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
        if (root == null) {
            return result;
        }

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

    private static void writeTestCase(File inDir, File outDir, int index, int[] arr, int key) throws Exception {
        String filename = String.format("%02d.txt", index);
        File inFile = new File(inDir, "input" + filename);
        File outFile = new File(outDir, "output" + filename);

        // Write input file
        try (PrintWriter inPw = new PrintWriter(inFile)) {
            inPw.println(arr.length + " " + key);
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
        root = deleteNode(root, key);
        List<Integer> outList = serialize(root);

        // Write output file
        try (PrintWriter outPw = new PrintWriter(outFile)) {
            for (int i = 0; i < outList.size(); i++) {
                outPw.print(outList.get(i));
                if (i + 1 < outList.size()) outPw.print(" ");
            }
            outPw.println();
        }
    }

    // Helper: Build balanced BST from sorted array
    private static TreeNode buildBalanced(int[] sortedArr, int start, int end) {
        if (start > end) return null;
        int mid = (start + end) / 2;
        TreeNode node = new TreeNode(sortedArr[mid]);
        node.left = buildBalanced(sortedArr, start, mid - 1);
        node.right = buildBalanced(sortedArr, mid + 1, end);
        return node;
    }

    // Helper: Convert List<Integer> to int[]
    private static int[] toArray(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static void main(String[] args) {
        try {
            File baseDir = new File("../Testcases");
            if (!baseDir.exists()) {
                baseDir = new File("Testcases");
            }
            if (!baseDir.exists()) {
                baseDir = new File("e:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Delete a node in BST/Testcases");
            }

            File inDir = new File(baseDir, "Input");
            File outDir = new File(baseDir, "Output");
            inDir.mkdirs();
            outDir.mkdirs();

            Random rng = new Random(20261024L);

            // ==================== 00 - 04: Problem Statement Samples ====================
            // 00: Sample 1: root = [5, 3, 6, 2, 4, -1, 7], key = 3
            writeTestCase(inDir, outDir, 0, new int[]{5, 3, 6, 2, 4, -1, 7}, 3);

            // 01: Sample 2: root = [5, 3, 6, 2, 4, -1, 7], key = 0 (not found)
            writeTestCase(inDir, outDir, 1, new int[]{5, 3, 6, 2, 4, -1, 7}, 0);

            // 02: Sample 3: empty tree, key = 0
            writeTestCase(inDir, outDir, 2, new int[0], 0);

            // 03: Sample 4: single node [5], key = 5
            writeTestCase(inDir, outDir, 3, new int[]{5}, 5);

            // 04: Sample 5: [2, 1, 3], key = 2 (root with 2 children)
            writeTestCase(inDir, outDir, 4, new int[]{2, 1, 3}, 2);

            // ==================== 05 - 14: Minimal Boundaries & Corner Cases ====================
            // 05: Single node [10], key = 5 (not found)
            writeTestCase(inDir, outDir, 5, new int[]{10}, 5);

            // 06: Two nodes [10, 5], delete leaf child 5
            writeTestCase(inDir, outDir, 6, new int[]{10, 5}, 5);

            // 07: Two nodes [10, 5], delete root 10 (has only left child)
            writeTestCase(inDir, outDir, 7, new int[]{10, 5}, 10);

            // 08: Two nodes [10, -1, 20], delete leaf child 20
            writeTestCase(inDir, outDir, 8, new int[]{10, -1, 20}, 20);

            // 09: Two nodes [10, -1, 20], delete root 10 (has only right child)
            writeTestCase(inDir, outDir, 9, new int[]{10, -1, 20}, 10);

            // 10: Right-skewed 5-node BST [10, -1, 20, -1, 30, -1, 40, -1, 50], delete root 10
            TreeNode skewR = null;
            for (int v : new int[]{10, 20, 30, 40, 50}) skewR = insertIntoBST(skewR, v);
            writeTestCase(inDir, outDir, 10, toArray(serialize(skewR)), 10);

            // 11: Right-skewed 5-node BST, delete tail 50
            writeTestCase(inDir, outDir, 11, toArray(serialize(skewR)), 50);

            // 12: Right-skewed 5-node BST, delete middle node 30
            writeTestCase(inDir, outDir, 12, toArray(serialize(skewR)), 30);

            // 13: Left-skewed 5-node BST [50, 40, -1, 30, -1, 20, -1, 10], delete root 50
            TreeNode skewL = null;
            for (int v : new int[]{50, 40, 30, 20, 10}) skewL = insertIntoBST(skewL, v);
            writeTestCase(inDir, outDir, 13, toArray(serialize(skewL)), 50);

            // 14: Left-skewed 5-node BST, delete tail 10
            writeTestCase(inDir, outDir, 14, toArray(serialize(skewL)), 10);

            // ==================== 15 - 34: Small to Medium Trees (N = 10..50) ====================
            for (int tc = 15; tc <= 34; tc++) {
                int nNodes = 10 + (tc - 15) * 2;
                int[] sorted = new int[nNodes];
                int startVal = 100 + tc * 50;
                for (int i = 0; i < nNodes; i++) sorted[i] = startVal + i * 10;

                int[] arr;
                int key;

                if (tc % 4 == 0) {
                    // Balanced tree, delete root
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    key = bRoot.val;
                } else if (tc % 4 == 1) {
                    // Balanced tree, delete leaf
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    key = sorted[0]; // Leftmost leaf
                } else if (tc % 4 == 2) {
                    // Balanced tree, key not found
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    key = startVal + 5; // Gap
                } else {
                    // Random BST, delete internal node
                    TreeNode rRoot = null;
                    List<Integer> list = new ArrayList<>();
                    for (int v : sorted) list.add(v);
                    Collections.shuffle(list, rng);
                    for (int v : list) rRoot = insertIntoBST(rRoot, v);
                    arr = toArray(serialize(rRoot));
                    key = list.get(nNodes / 2);
                }

                writeTestCase(inDir, outDir, tc, arr, key);
            }

            // ==================== 35 - 69: Large Trees (N = 100..1000) ====================
            for (int tc = 35; tc <= 69; tc++) {
                int nNodes = 100 + (tc - 35) * 25 + rng.nextInt(15);
                int[] sorted = new int[nNodes];
                int startVal = 1000 + tc * 200;
                for (int i = 0; i < nNodes; i++) sorted[i] = startVal + i * 4;

                int[] arr;
                int key;

                if (tc % 3 == 0) {
                    // Balanced tree, delete node with 2 children
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    key = sorted[nNodes / 4];
                } else if (tc % 3 == 1) {
                    // Random BST, delete random existing node
                    List<Integer> list = new ArrayList<>();
                    for (int v : sorted) list.add(v);
                    Collections.shuffle(list, rng);
                    TreeNode rRoot = null;
                    for (int v : list) rRoot = insertIntoBST(rRoot, v);
                    arr = toArray(serialize(rRoot));
                    key = list.get(rng.nextInt(nNodes));
                } else {
                    // Random BST, delete non-existent key
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    key = (tc % 2 == 0) ? (startVal - 3) : (startVal + nNodes * 4 + 5);
                }

                writeTestCase(inDir, outDir, tc, arr, key);
            }

            // ==================== 70 - 89: Very Large Trees (N = 1500..5000) ====================
            for (int tc = 70; tc <= 89; tc++) {
                int nNodes = 1500 + (tc - 70) * 170 + rng.nextInt(25);
                int[] sorted = new int[nNodes];
                int startVal = 50000 + tc * 1000;
                for (int i = 0; i < nNodes; i++) sorted[i] = startVal + i * 6;

                int[] arr;
                int key;

                if (tc % 3 == 0) {
                    // Large Balanced Tree, delete root
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    key = bRoot.val;
                } else if (tc % 3 == 1) {
                    // Large Balanced Tree, delete deep leaf
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    key = sorted[0];
                } else {
                    // Large Balanced Tree, key not found
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    key = startVal - 5;
                }

                writeTestCase(inDir, outDir, tc, arr, key);
            }

            // ==================== 90 - 99: Extreme Constraints & Stress Tests (N ≈ 10000) ====================
            // 90: Large Balanced BST (N = 8191), delete root
            {
                int nNodes = 8191;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root90 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 90, toArray(serialize(root90)), root90.val);
            }

            // 91: Large Balanced BST (N = 8191), delete deepest leaf
            {
                int nNodes = 8191;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root91 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 91, toArray(serialize(root91)), sorted[0]);
            }

            // 92: Strictly right-skewed BST of 10,000 nodes, delete root
            {
                int nNodes = 10000;
                int[] arr92 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (i + 1) * 10;
                    if (i == 0) {
                        arr92[0] = valNode;
                    } else {
                        arr92[2 * i - 1] = -1;
                        arr92[2 * i] = valNode;
                    }
                }
                writeTestCase(inDir, outDir, 92, arr92, 10); // delete root 10
            }

            // 93: Strictly right-skewed BST of 10,000 nodes, delete tail
            {
                int nNodes = 10000;
                int[] arr93 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (i + 1) * 10;
                    if (i == 0) {
                        arr93[0] = valNode;
                    } else {
                        arr93[2 * i - 1] = -1;
                        arr93[2 * i] = valNode;
                    }
                }
                writeTestCase(inDir, outDir, 93, arr93, 100000); // delete tail
            }

            // 94: Strictly left-skewed BST of 10,000 nodes, delete root
            {
                int nNodes = 10000;
                int[] arr94 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (nNodes - i) * 10;
                    if (i == 0) {
                        arr94[0] = valNode;
                    } else {
                        arr94[2 * i - 1] = valNode;
                        arr94[2 * i] = -1;
                    }
                }
                writeTestCase(inDir, outDir, 94, arr94, 100000); // delete root
            }

            // 95: Strictly left-skewed BST of 10,000 nodes, delete tail
            {
                int nNodes = 10000;
                int[] arr95 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (nNodes - i) * 10;
                    if (i == 0) {
                        arr95[0] = valNode;
                    } else {
                        arr95[2 * i - 1] = valNode;
                        arr95[2 * i] = -1;
                    }
                }
                writeTestCase(inDir, outDir, 95, arr95, 10); // delete tail
            }

            // 96: Large Balanced Tree of N = 10,000 nodes, delete median
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root96 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 96, toArray(serialize(root96)), sorted[nNodes / 2]);
            }

            // 97: Large Balanced Tree of N = 10,000 nodes, delete non-existent key 0
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = 10 + i * 10;
                TreeNode root97 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 97, toArray(serialize(root97)), 0);
            }

            // 98: Large Balanced Tree of N = 10,000 nodes, delete non-existent key 1,000,000,000
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = 10 + i * 10;
                TreeNode root98 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 98, toArray(serialize(root98)), 1000000000);
            }

            // 99: Alternating zig-zag tree of 5,000 nodes, delete root
            {
                int nNodes = 5000;
                int low = 1000000;
                int high = 2000000;
                TreeNode root99 = null;
                int rootVal = 0;
                for (int i = 0; i < nNodes; i++) {
                    int v = (i % 2 == 0) ? (low += 10) : (high -= 10);
                    if (i == 0) rootVal = v;
                    root99 = insertIntoBST(root99, v);
                }
                writeTestCase(inDir, outDir, 99, toArray(serialize(root99)), rootVal);
            }

            System.out.println("SUCCESS: Generated 100 testcases for Delete a node in BST.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
