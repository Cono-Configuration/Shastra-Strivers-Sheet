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

    public static TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }

        TreeNode curr = root;
        while (true) {
            if (val < curr.val) {
                if (curr.left != null) {
                    curr = curr.left;
                } else {
                    curr.left = new TreeNode(val);
                    break;
                }
            } else {
                if (curr.right != null) {
                    curr = curr.right;
                } else {
                    curr.right = new TreeNode(val);
                    break;
                }
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

    private static void writeTestCase(File inDir, File outDir, int index, int[] arr, int val) throws Exception {
        String filename = String.format("%02d.txt", index);
        File inFile = new File(inDir, "input" + filename);
        File outFile = new File(outDir, "output" + filename);

        // Write input file
        try (PrintWriter inPw = new PrintWriter(inFile)) {
            inPw.println(arr.length + " " + val);
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
        root = insertIntoBST(root, val);
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

    // Helper: Generate random BST with numNodes nodes
    private static int[] generateRandomBST(int numNodes, int minVal, int maxVal, Random rng, Set<Integer> used) {
        if (numNodes == 0) return new int[0];
        TreeNode root = null;
        int attempts = 0;
        int added = 0;
        while (added < numNodes && attempts < numNodes * 50) {
            attempts++;
            int v = minVal + rng.nextInt(maxVal - minVal + 1);
            if (!used.contains(v)) {
                used.add(v);
                root = insertIntoBST(root, v);
                added++;
            }
        }
        return toArray(serialize(root));
    }

    public static void main(String[] args) {
        try {
            File baseDir = new File("../Testcases");
            if (!baseDir.exists()) {
                baseDir = new File("Testcases");
            }
            if (!baseDir.exists()) {
                baseDir = new File("e:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Insert a given node in BST/Testcases");
            }

            File inDir = new File(baseDir, "Input");
            File outDir = new File(baseDir, "Output");
            inDir.mkdirs();
            outDir.mkdirs();

            Random rng = new Random(20261024L);

            // ==================== 00 - 03: Problem Statement Samples ====================
            // 00: Sample 1: root = [4, 2, 7, 1, 3], val = 5
            writeTestCase(inDir, outDir, 0, new int[]{4, 2, 7, 1, 3}, 5);

            // 01: Sample 2: root = [40, 20, 60, 10, 30, 50, 70], val = 25
            writeTestCase(inDir, outDir, 1, new int[]{40, 20, 60, 10, 30, 50, 70}, 25);

            // 02: Sample 3: empty tree, val = 5
            writeTestCase(inDir, outDir, 2, new int[0], 5);

            // 03: Sample 4: root = [4, 2, 7, 1, 3], val = 8
            writeTestCase(inDir, outDir, 3, new int[]{4, 2, 7, 1, 3}, 8);

            // ==================== 04 - 14: Minimal Boundaries & Corner Cases ====================
            // 04: Single node, insert smaller (left child)
            writeTestCase(inDir, outDir, 4, new int[]{10}, 5);

            // 05: Single node, insert larger (right child)
            writeTestCase(inDir, outDir, 5, new int[]{10}, 15);

            // 06: Two nodes [10, 5], insert 2 (left of left)
            writeTestCase(inDir, outDir, 6, new int[]{10, 5}, 2);

            // 07: Two nodes [10, 5], insert 8 (right of left)
            writeTestCase(inDir, outDir, 7, new int[]{10, 5}, 8);

            // 08: Two nodes [10, -1, 20], insert 15 (left of right)
            writeTestCase(inDir, outDir, 8, new int[]{10, -1, 20}, 15);

            // 09: Two nodes [10, -1, 20], insert 25 (right of right)
            writeTestCase(inDir, outDir, 9, new int[]{10, -1, 20}, 25);

            // 10: Right-skewed 5-node BST, insert smaller than root
            TreeNode skewR = null;
            for (int v : new int[]{10, 20, 30, 40, 50}) skewR = insertIntoBST(skewR, v);
            writeTestCase(inDir, outDir, 10, toArray(serialize(skewR)), 5);

            // 11: Right-skewed 5-node BST, insert larger than tail
            writeTestCase(inDir, outDir, 11, toArray(serialize(skewR)), 60);

            // 12: Left-skewed 5-node BST, insert smaller than tail
            TreeNode skewL = null;
            for (int v : new int[]{50, 40, 30, 20, 10}) skewL = insertIntoBST(skewL, v);
            writeTestCase(inDir, outDir, 12, toArray(serialize(skewL)), 5);

            // 13: Left-skewed 5-node BST, insert larger than root
            writeTestCase(inDir, outDir, 13, toArray(serialize(skewL)), 60);

            // 14: Zig-zag small BST [20, 10, 30, -1, 15, 25, -1]
            TreeNode zigzag = null;
            for (int v : new int[]{20, 10, 30, 15, 25}) zigzag = insertIntoBST(zigzag, v);
            writeTestCase(inDir, outDir, 14, toArray(serialize(zigzag)), 12);

            // ==================== 15 - 34: Small to Medium Trees (N = 10..50) ====================
            for (int tc = 15; tc <= 34; tc++) {
                int nNodes = 10 + (tc - 15) * 2;
                Set<Integer> used = new HashSet<>();
                int[] arr;
                int val;

                if (tc % 3 == 0) {
                    // Balanced tree
                    int[] sorted = new int[nNodes];
                    int startVal = 100 + tc * 50;
                    for (int i = 0; i < nNodes; i++) {
                        sorted[i] = startVal + i * 10;
                        used.add(sorted[i]);
                    }
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    val = startVal + 5; // Insert in gap
                } else if (tc % 3 == 1) {
                    // Random BST
                    arr = generateRandomBST(nNodes, 10, 5000, rng, used);
                    do {
                        val = 10 + rng.nextInt(5000);
                    } while (used.contains(val));
                } else {
                    // Skewed tree
                    TreeNode sTree = null;
                    int cur = 50;
                    for (int i = 0; i < nNodes; i++) {
                        cur += 1 + rng.nextInt(10);
                        sTree = insertIntoBST(sTree, cur);
                        used.add(cur);
                    }
                    arr = toArray(serialize(sTree));
                    val = cur + 5;
                }

                writeTestCase(inDir, outDir, tc, arr, val);
            }

            // ==================== 35 - 69: Large Trees (N = 100..1000) ====================
            for (int tc = 35; tc <= 69; tc++) {
                int nNodes = 100 + (tc - 35) * 25 + rng.nextInt(15);
                Set<Integer> used = new HashSet<>();
                int[] arr;
                int val;

                if (tc % 4 == 0) {
                    // Balanced tree
                    int[] sorted = new int[nNodes];
                    int startVal = 1000 + tc * 200;
                    for (int i = 0; i < nNodes; i++) {
                        sorted[i] = startVal + i * 4;
                        used.add(sorted[i]);
                    }
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    val = (tc % 2 == 0) ? (startVal - 2) : (startVal + nNodes * 4 + 2);
                } else {
                    // Random BST
                    arr = generateRandomBST(nNodes, 1, 1000000, rng, used);
                    do {
                        val = 1 + rng.nextInt(1000000);
                    } while (used.contains(val));
                }

                writeTestCase(inDir, outDir, tc, arr, val);
            }

            // ==================== 70 - 89: Very Large Trees (N = 1500..5000) ====================
            for (int tc = 70; tc <= 89; tc++) {
                int nNodes = 1500 + (tc - 70) * 170 + rng.nextInt(25);
                Set<Integer> used = new HashSet<>();
                int[] arr;
                int val;

                if (tc % 3 == 0) {
                    // Large Balanced Tree
                    int[] sorted = new int[nNodes];
                    int startVal = 50000 + tc * 1000;
                    for (int i = 0; i < nNodes; i++) {
                        sorted[i] = startVal + i * 6;
                        used.add(sorted[i]);
                    }
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    val = startVal + (nNodes / 2) * 6 + 3; // Median gap insertion
                } else {
                    // Large random BST
                    int[] sorted = new int[nNodes];
                    for (int i = 0; i < nNodes; i++) {
                        sorted[i] = (i + 1) * 100;
                        used.add(sorted[i]);
                    }
                    // Shuffle order of insertion to create varied heights
                    for (int i = nNodes - 1; i > 0; i--) {
                        int j = rng.nextInt(i + 1);
                        int temp = sorted[i];
                        sorted[i] = sorted[j];
                        sorted[j] = temp;
                    }
                    TreeNode rRoot = null;
                    for (int v : sorted) {
                        rRoot = insertIntoBST(rRoot, v);
                    }
                    arr = toArray(serialize(rRoot));
                    val = (tc % 2 == 0) ? 50 : 10000000;
                }

                writeTestCase(inDir, outDir, tc, arr, val);
            }

            // ==================== 90 - 99: Extreme Constraints & Stress Tests (N ≈ 10000) ====================
            // 90: Large Balanced BST (N = 8191), insert deep leaf gap
            {
                int nNodes = 8191;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root90 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 90, toArray(serialize(root90)), 25);
            }

            // 91: Strictly right-skewed BST with 10,000 nodes, insert val = 1 (becomes root.left)
            {
                int nNodes = 10000;
                // Representation of right-skewed tree in level-order:
                // node 1: left = null (-1), right = node 2
                // node 2: left = null (-1), right = node 3
                // Total elements in level-order array: 2 * nNodes - 1
                int[] arr91 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (i + 1) * 10;
                    if (i == 0) {
                        arr91[0] = valNode;
                    } else {
                        arr91[2 * i - 1] = -1;
                        arr91[2 * i] = valNode;
                    }
                }
                writeTestCase(inDir, outDir, 91, arr91, 1);
            }

            // 92: Strictly right-skewed BST with 10,000 nodes, insert maximum value (extends tail)
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
                writeTestCase(inDir, outDir, 92, arr92, 1000000000);
            }

            // 93: Strictly left-skewed BST with 10,000 nodes, insert minimum value (extends tail)
            {
                int nNodes = 10000;
                int[] arr93 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (nNodes - i) * 10;
                    if (i == 0) {
                        arr93[0] = valNode;
                    } else {
                        arr93[2 * i - 1] = valNode;
                        arr93[2 * i] = -1;
                    }
                }
                writeTestCase(inDir, outDir, 93, arr93, 1);
            }

            // 94: Strictly left-skewed BST with 10,000 nodes, insert maximum value (becomes root.right)
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
                writeTestCase(inDir, outDir, 94, arr94, 1000000000);
            }

            // 95: Alternating zig-zag tree of 5,000 nodes
            {
                int nNodes = 5000;
                int low = 1000000;
                int high = 2000000;
                TreeNode root95 = null;
                for (int i = 0; i < nNodes; i++) {
                    int v = (i % 2 == 0) ? (low += 10) : (high -= 10);
                    root95 = insertIntoBST(root95, v);
                }
                writeTestCase(inDir, outDir, 95, toArray(serialize(root95)), 1500000);
            }

            // 96: Balanced tree with N = 10000 nodes, insert random element
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 20;
                TreeNode root96 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 96, toArray(serialize(root96)), 54321);
            }

            // 97: Balanced tree with N = 10000 nodes, insert value = 1 (global minimum)
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = 10 + i * 10;
                TreeNode root97 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 97, toArray(serialize(root97)), 1);
            }

            // 98: Balanced tree with N = 10000 nodes, insert value = 1,000,000,000 (global maximum)
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = 10 + i * 10;
                TreeNode root98 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 98, toArray(serialize(root98)), 1000000000);
            }

            // 99: Balanced tree with N = 10000 nodes, insert median gap value
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root99 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 99, toArray(serialize(root99)), 50005);
            }

            System.out.println("SUCCESS: Generated 100 testcases for Insert a given node in BST.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
