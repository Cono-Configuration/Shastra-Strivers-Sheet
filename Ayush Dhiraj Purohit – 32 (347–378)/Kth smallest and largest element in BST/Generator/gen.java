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

    public static int findKthSmallest(TreeNode root, int k) {
        if (root == null || k <= 0) return -1;
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;
        int count = 0;
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            count++;
            if (count == k) return curr.val;
            curr = curr.right;
        }
        return -1;
    }

    public static int findKthLargest(TreeNode root, int k) {
        if (root == null || k <= 0) return -1;
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;
        int count = 0;
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.right;
            }
            curr = stack.pop();
            count++;
            if (count == k) return curr.val;
            curr = curr.left;
        }
        return -1;
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

    private static void writeTestCase(File inDir, File outDir, int index, int[] arr, int K) throws Exception {
        String filename = String.format("%02d.txt", index);
        File inFile = new File(inDir, "input" + filename);
        File outFile = new File(outDir, "output" + filename);

        // Write input file
        try (PrintWriter inPw = new PrintWriter(inFile)) {
            inPw.println(arr.length + " " + K);
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
        int kthSmallest = findKthSmallest(root, K);
        int kthLargest = findKthLargest(root, K);

        // Write output file
        try (PrintWriter outPw = new PrintWriter(outFile)) {
            outPw.println(kthSmallest + " " + kthLargest);
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
                baseDir = new File("e:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Kth smallest and largest element in BST/Testcases");
            }

            File inDir = new File(baseDir, "Input");
            File outDir = new File(baseDir, "Output");
            inDir.mkdirs();
            outDir.mkdirs();

            Random rng = new Random(20261024L);

            // ==================== 00 - 03: Problem Statement Samples ====================
            // 00: Sample 1: root = [3, 1, 4, -1, 2], K = 1 -> output: 1 4
            writeTestCase(inDir, outDir, 0, new int[]{3, 1, 4, -1, 2}, 1);

            // 01: Sample 2: root = [5, 3, 6, 2, -1, -1, -1, 1], K = 3 -> output: 3 3
            writeTestCase(inDir, outDir, 1, new int[]{5, 3, 6, 2, -1, -1, -1, 1}, 3);

            // 02: Sample 3: root = [10], K = 1 -> output: 10 10
            writeTestCase(inDir, outDir, 2, new int[]{10}, 1);

            // 03: Sample 4: root = [40, 20, 60, 10, 30, 50, 70], K = 2 -> output: 20 60
            writeTestCase(inDir, outDir, 3, new int[]{40, 20, 60, 10, 30, 50, 70}, 2);

            // ==================== 04 - 14: Minimal Boundaries & Corner Cases ====================
            // 04: Two nodes [10, 5], K = 1
            writeTestCase(inDir, outDir, 4, new int[]{10, 5}, 1);

            // 05: Two nodes [10, 5], K = 2
            writeTestCase(inDir, outDir, 5, new int[]{10, 5}, 2);

            // 06: Two nodes [10, -1, 20], K = 1
            writeTestCase(inDir, outDir, 6, new int[]{10, -1, 20}, 1);

            // 07: Two nodes [10, -1, 20], K = 2
            writeTestCase(inDir, outDir, 7, new int[]{10, -1, 20}, 2);

            // 08: Three nodes [20, 10, 30], K = 1
            writeTestCase(inDir, outDir, 8, new int[]{20, 10, 30}, 1);

            // 09: Three nodes [20, 10, 30], K = 2
            writeTestCase(inDir, outDir, 9, new int[]{20, 10, 30}, 2);

            // 10: Three nodes [20, 10, 30], K = 3
            writeTestCase(inDir, outDir, 10, new int[]{20, 10, 30}, 3);

            // 11: Right-skewed 5-node BST [10, -1, 20, -1, 30, -1, 40, -1, 50], K = 1
            TreeNode skewR = null;
            for (int v : new int[]{10, 20, 30, 40, 50}) skewR = insertIntoBST(skewR, v);
            writeTestCase(inDir, outDir, 11, toArray(serialize(skewR)), 1);

            // 12: Right-skewed 5-node BST, K = 5 (extreme K)
            writeTestCase(inDir, outDir, 12, toArray(serialize(skewR)), 5);

            // 13: Left-skewed 5-node BST, K = 1
            TreeNode skewL = null;
            for (int v : new int[]{50, 40, 30, 20, 10}) skewL = insertIntoBST(skewL, v);
            writeTestCase(inDir, outDir, 13, toArray(serialize(skewL)), 1);

            // 14: Left-skewed 5-node BST, K = 5 (extreme K)
            writeTestCase(inDir, outDir, 14, toArray(serialize(skewL)), 5);

            // ==================== 15 - 34: Small to Medium Trees (N = 10..50) ====================
            for (int tc = 15; tc <= 34; tc++) {
                int nNodes = 10 + (tc - 15) * 2;
                int[] sorted = new int[nNodes];
                int startVal = 100 + tc * 50;
                for (int i = 0; i < nNodes; i++) sorted[i] = startVal + i * 10;

                int[] arr;
                int K;

                if (tc % 4 == 0) {
                    // Balanced BST, K = 1
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    K = 1;
                } else if (tc % 4 == 1) {
                    // Balanced BST, K = nNodes (maximum)
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    K = nNodes;
                } else if (tc % 4 == 2) {
                    // Balanced BST, K = median
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    K = nNodes / 2;
                } else {
                    // Random BST, random K
                    List<Integer> list = new ArrayList<>();
                    for (int v : sorted) list.add(v);
                    Collections.shuffle(list, rng);
                    TreeNode rRoot = null;
                    for (int v : list) rRoot = insertIntoBST(rRoot, v);
                    arr = toArray(serialize(rRoot));
                    K = 1 + rng.nextInt(nNodes);
                }

                writeTestCase(inDir, outDir, tc, arr, K);
            }

            // ==================== 35 - 69: Large Trees (N = 100..1000) ====================
            for (int tc = 35; tc <= 69; tc++) {
                int nNodes = 100 + (tc - 35) * 25 + rng.nextInt(15);
                int[] sorted = new int[nNodes];
                int startVal = 1000 + tc * 200;
                for (int i = 0; i < nNodes; i++) sorted[i] = startVal + i * 4;

                int[] arr;
                int K;

                if (tc % 3 == 0) {
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    K = 1 + (tc - 35) % nNodes;
                } else if (tc % 3 == 1) {
                    List<Integer> list = new ArrayList<>();
                    for (int v : sorted) list.add(v);
                    Collections.shuffle(list, rng);
                    TreeNode rRoot = null;
                    for (int v : list) rRoot = insertIntoBST(rRoot, v);
                    arr = toArray(serialize(rRoot));
                    K = 1 + rng.nextInt(nNodes);
                } else {
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    K = (tc % 2 == 0) ? 1 : nNodes;
                }

                writeTestCase(inDir, outDir, tc, arr, K);
            }

            // ==================== 70 - 89: Very Large Trees (N = 1500..5000) ====================
            for (int tc = 70; tc <= 89; tc++) {
                int nNodes = 1500 + (tc - 70) * 170 + rng.nextInt(25);
                int[] sorted = new int[nNodes];
                int startVal = 50000 + tc * 1000;
                for (int i = 0; i < nNodes; i++) sorted[i] = startVal + i * 6;

                int[] arr;
                int K;

                if (tc % 3 == 0) {
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    K = nNodes / 2;
                } else if (tc % 3 == 1) {
                    TreeNode bRoot = buildBalanced(sorted, 0, nNodes - 1);
                    arr = toArray(serialize(bRoot));
                    K = (tc % 2 == 0) ? 1 : nNodes;
                } else {
                    List<Integer> list = new ArrayList<>();
                    for (int v : sorted) list.add(v);
                    Collections.shuffle(list, rng);
                    TreeNode rRoot = null;
                    for (int v : list) rRoot = insertIntoBST(rRoot, v);
                    arr = toArray(serialize(rRoot));
                    K = 10 + rng.nextInt(nNodes - 20);
                }

                writeTestCase(inDir, outDir, tc, arr, K);
            }

            // ==================== 90 - 99: Extreme Constraints & Stress Tests (N ≈ 10000) ====================
            // 90: Large Balanced BST (N = 8191), K = 1
            {
                int nNodes = 8191;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root90 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 90, toArray(serialize(root90)), 1);
            }

            // 91: Large Balanced BST (N = 8191), K = 8191 (maximum)
            {
                int nNodes = 8191;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root91 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 91, toArray(serialize(root91)), 8191);
            }

            // 92: Large Balanced BST (N = 8191), K = 4096 (exact median)
            {
                int nNodes = 8191;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root92 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 92, toArray(serialize(root92)), 4096);
            }

            // 93: Strictly right-skewed BST of 10,000 nodes, K = 1
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
                writeTestCase(inDir, outDir, 93, arr93, 1);
            }

            // 94: Strictly right-skewed BST of 10,000 nodes, K = 10000
            {
                int nNodes = 10000;
                int[] arr94 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (i + 1) * 10;
                    if (i == 0) arr94[0] = valNode;
                    else {
                        arr94[2 * i - 1] = -1;
                        arr94[2 * i] = valNode;
                    }
                }
                writeTestCase(inDir, outDir, 94, arr94, 10000);
            }

            // 95: Strictly left-skewed BST of 10,000 nodes, K = 1
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
                writeTestCase(inDir, outDir, 95, arr95, 1);
            }

            // 96: Strictly left-skewed BST of 10,000 nodes, K = 10000
            {
                int nNodes = 10000;
                int[] arr96 = new int[2 * nNodes - 1];
                for (int i = 0; i < nNodes; i++) {
                    int valNode = (nNodes - i) * 10;
                    if (i == 0) arr96[0] = valNode;
                    else {
                        arr96[2 * i - 1] = valNode;
                        arr96[2 * i] = -1;
                    }
                }
                writeTestCase(inDir, outDir, 96, arr96, 10000);
            }

            // 97: Large balanced BST of 10,000 nodes, K = 5000
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = (i + 1) * 10;
                TreeNode root97 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 97, toArray(serialize(root97)), 5000);
            }

            // 98: Large balanced BST of 10,000 nodes, K = 1 (minimum)
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = 10 + i * 10;
                TreeNode root98 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 98, toArray(serialize(root98)), 1);
            }

            // 99: Large balanced BST of 10,000 nodes, K = 10000 (maximum)
            {
                int nNodes = 10000;
                int[] sorted = new int[nNodes];
                for (int i = 0; i < nNodes; i++) sorted[i] = 10 + i * 10;
                TreeNode root99 = buildBalanced(sorted, 0, nNodes - 1);
                writeTestCase(inDir, outDir, 99, toArray(serialize(root99)), 10000);
            }

            System.out.println("SUCCESS: Generated 100 testcases for Kth smallest and largest element in BST.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
