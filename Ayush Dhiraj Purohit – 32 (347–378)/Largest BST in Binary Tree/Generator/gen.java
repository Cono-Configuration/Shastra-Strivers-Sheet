import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class gen {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    static class NodeInfo {
        boolean isBST;
        int size;
        int maxSize;
        long minVal;
        long maxVal;

        NodeInfo(boolean isBST, int size, int maxSize, long minVal, long maxVal) {
            this.isBST = isBST;
            this.size = size;
            this.maxSize = maxSize;
            this.minVal = minVal;
            this.maxVal = maxVal;
        }
    }

    public static int solve(TreeNode root) {
        if (root == null) {
            return 0;
        }

        List<TreeNode> postOrder = new ArrayList<>();
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            postOrder.add(curr);
            if (curr.left != null) {
                stack.push(curr.left);
            }
            if (curr.right != null) {
                stack.push(curr.right);
            }
        }

        Collections.reverse(postOrder);

        Map<TreeNode, NodeInfo> infoMap = new IdentityHashMap<>();

        for (TreeNode curr : postOrder) {
            NodeInfo leftInfo = curr.left == null
                    ? new NodeInfo(true, 0, 0, Long.MAX_VALUE, Long.MIN_VALUE)
                    : infoMap.get(curr.left);

            NodeInfo rightInfo = curr.right == null
                    ? new NodeInfo(true, 0, 0, Long.MAX_VALUE, Long.MIN_VALUE)
                    : infoMap.get(curr.right);

            boolean isBST = leftInfo.isBST && rightInfo.isBST
                    && (leftInfo.maxVal < (long) curr.val)
                    && ((long) curr.val < rightInfo.minVal);

            if (isBST) {
                int curSize = leftInfo.size + rightInfo.size + 1;
                long curMin = Math.min((long) curr.val, leftInfo.minVal);
                long curMax = Math.max((long) curr.val, rightInfo.maxVal);
                infoMap.put(curr, new NodeInfo(true, curSize, curSize, curMin, curMax));
            } else {
                int curMaxSize = Math.max(leftInfo.maxSize, rightInfo.maxSize);
                infoMap.put(curr, new NodeInfo(false, 0, curMaxSize, Long.MIN_VALUE, Long.MAX_VALUE));
            }
        }

        return infoMap.get(root).maxSize;
    }

    public static List<Integer> serialize(TreeNode root) {
        if (root == null) {
            return Collections.emptyList();
        }

        List<Integer> list = new ArrayList<>();
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

        return list.subList(0, last + 1);
    }

    public static TreeNode deserialize(int[] arr) {
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

    public static TreeNode buildBalancedBST(int[] sorted, int start, int end) {
        if (start > end) {
            return null;
        }
        int mid = (start + end) / 2;
        TreeNode node = new TreeNode(sorted[mid]);
        node.left = buildBalancedBST(sorted, start, mid - 1);
        node.right = buildBalancedBST(sorted, mid + 1, end);
        return node;
    }

    static class TestCase {
        List<Integer> serialized;
        int answer;

        TestCase(List<Integer> serialized, int answer) {
            this.serialized = serialized;
            this.answer = answer;
        }
    }

    public static void main(String[] args) throws IOException {
        String baseDir = "e:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Largest BST in Binary Tree";
        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) {
            inDir.mkdirs();
        }
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        List<TestCase> tests = new ArrayList<>();
        Random rng = new Random(424242L);

        // 00 - 03: Samples from problem description
        tests.add(createFromArr(new int[]{2, 1, 3}));
        tests.add(createFromArr(new int[]{10, -1, 20, -1, 30, -1, 40, -1, 50}));
        tests.add(createFromArr(new int[]{3, 1, 4, -1, -1, 2}));
        tests.add(createFromArr(new int[]{50, 30, 60, 5, 20, 45, 70, -1, -1, -1, -1, -1, -1, 65, 80}));

        // 04 - 14: Minimal & small edge cases
        tests.add(createFromArr(new int[]{42})); // N=1
        tests.add(createFromArr(new int[]{1})); // Min value N=1
        tests.add(createFromArr(new int[]{100000})); // Large value N=1
        tests.add(createFromArr(new int[]{10, 5})); // N=2 valid left
        tests.add(createFromArr(new int[]{10, -1, 15})); // N=2 valid right
        tests.add(createFromArr(new int[]{10, 15})); // N=2 invalid left > root
        tests.add(createFromArr(new int[]{10, -1, 5})); // N=2 invalid right < root
        tests.add(createFromArr(new int[]{10, 10})); // N=2 duplicate
        tests.add(createFromArr(new int[]{5, 5, 5})); // N=3 all duplicates
        tests.add(createFromArr(new int[]{2, 3, 1})); // N=3 fully inverted
        tests.add(createFromArr(new int[]{10, 5, 8})); // N=3 invalid right child

        // 15 - 34: Small-to-Medium Trees (N in [7, 120])
        // 15: Perfect BST of size 7
        tests.add(createFromArr(new int[]{4, 2, 6, 1, 3, 5, 7}));

        // 16: Perfect BST of size 15
        tests.add(createFromArr(new int[]{8, 4, 12, 2, 6, 10, 14, 1, 3, 5, 7, 9, 11, 13, 15}));

        // 17: BST with one violation near root
        tests.add(createFromArr(new int[]{50, 100, 70, 10, 30, 60, 80}));

        // 18: Tree where left is BST size 3, right is BST size 3, root violates
        tests.add(createFromArr(new int[]{10, 5, 20, 3, 7, 15, 8})); // 8 < 20 and 8 < 10

        // 19: Left-skewed strictly decreasing (valid BST of size 6)
        tests.add(createFromArr(new int[]{60, 50, -1, 40, -1, -1, -1, 30, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10}));

        // 20: Right-skewed strictly increasing (valid BST of size 8)
        {
            TreeNode chain = new TreeNode(10);
            TreeNode c = chain;
            for (int v = 20; v <= 80; v += 10) {
                c.right = new TreeNode(v);
                c = c.right;
            }
            tests.add(createFromTree(chain));
        }

        // 21: Right-skewed strictly decreasing (invalid, each node size 1)
        {
            TreeNode chain = new TreeNode(80);
            TreeNode c = chain;
            for (int v = 70; v >= 10; v -= 10) {
                c.right = new TreeNode(v);
                c = c.right;
            }
            tests.add(createFromTree(chain));
        }

        // 22: Left-skewed strictly increasing (invalid, left child > parent)
        {
            TreeNode chain = new TreeNode(10);
            TreeNode c = chain;
            for (int v = 20; v <= 80; v += 10) {
                c.left = new TreeNode(v);
                c = c.left;
            }
            tests.add(createFromTree(chain));
        }

        // 23: Zig-zag tree valid BST (10 -> left 5 -> right 7)
        tests.add(createFromArr(new int[]{10, 5, -1, -1, 7}));

        // 24: Zig-zag tree invalid (10 -> left 5 -> right 12 > 10)
        tests.add(createFromArr(new int[]{10, 5, -1, -1, 12}));

        // 25 - 34: Random structured trees with known BST subtrees
        for (int i = 25; i <= 34; i++) {
            int bstSize = 10 + (i - 25) * 5;
            int[] sorted = new int[bstSize];
            int startVal = 100 + i * 20;
            for (int k = 0; k < bstSize; k++) {
                sorted[k] = startVal + k * 3;
            }
            TreeNode validSubtree = buildBalancedBST(sorted, 0, bstSize - 1);

            TreeNode root = new TreeNode(50); // root value smaller than subtree
            if (i % 2 == 0) {
                root.left = validSubtree; // invalid since all in left > 50
                root.right = new TreeNode(2000);
            } else {
                root.right = validSubtree;
                root.left = new TreeNode(500); // invalid left child > root
            }
            tests.add(createFromTree(root));
        }

        // 35 - 69: Medium-to-Large Trees (N in [100, 2000])
        // 35: Perfect balanced BST of size 31
        {
            int[] arr = new int[31];
            for (int k = 0; k < 31; k++) arr[k] = (k + 1) * 10;
            tests.add(createFromTree(buildBalancedBST(arr, 0, arr.length - 1)));
        }

        // 36: Perfect balanced BST of size 63
        {
            int[] arr = new int[63];
            for (int k = 0; k < 63; k++) arr[k] = (k + 1) * 5;
            tests.add(createFromTree(buildBalancedBST(arr, 0, arr.length - 1)));
        }

        // 37: Large tree with BST of size 127 attached as right child
        {
            int[] arr = new int[127];
            for (int k = 0; k < 127; k++) arr[k] = 1000 + k * 10;
            TreeNode bst = buildBalancedBST(arr, 0, arr.length - 1);
            TreeNode root = new TreeNode(500);
            root.right = bst;
            root.left = new TreeNode(600); // left child 600 > 500 invalidates root
            tests.add(createFromTree(root));
        }

        // 38: Large tree with BST of size 255 attached as left child
        {
            int[] arr = new int[255];
            for (int k = 0; k < 255; k++) arr[k] = 100 + k * 5;
            TreeNode bst = buildBalancedBST(arr, 0, arr.length - 1);
            TreeNode root = new TreeNode(50); // invalid because left subtree values > 50
            root.left = bst;
            root.right = new TreeNode(2000);
            tests.add(createFromTree(root));
        }

        // 39 - 50: Trees with competing BST subtrees (e.g. left size A, right size B)
        for (int i = 39; i <= 50; i++) {
            int leftSize = 50 + (i - 39) * 15;
            int rightSize = 40 + (i - 39) * 20;

            int[] leftArr = new int[leftSize];
            for (int k = 0; k < leftSize; k++) leftArr[k] = 100 + k * 4;

            int[] rightArr = new int[rightSize];
            for (int k = 0; k < rightSize; k++) rightArr[k] = 5000 + k * 4;

            TreeNode leftBST = buildBalancedBST(leftArr, 0, leftSize - 1);
            TreeNode rightBST = buildBalancedBST(rightArr, 0, rightSize - 1);

            // Poison root by setting its value between right subtree values
            TreeNode root = new TreeNode(6000);
            root.left = leftBST;
            root.right = rightBST;
            tests.add(createFromTree(root));
        }

        // 51 - 69: Randomized binary trees with diverse topologies
        for (int i = 51; i <= 69; i++) {
            int count = 100 + rng.nextInt(400);
            TreeNode randTree = generateRandomTree(count, rng, 1, 100000);
            tests.add(createFromTree(randTree));
        }

        // 70 - 89: Pathological & Deep Stress Cases
        // 70: Left-skewed strictly decreasing valid BST (N = 1500)
        {
            TreeNode chain = new TreeNode(150000);
            TreeNode c = chain;
            for (int k = 1499; k >= 1; k--) {
                c.left = new TreeNode(k * 100);
                c = c.left;
            }
            tests.add(createFromTree(chain));
        }

        // 71: Right-skewed strictly increasing valid BST (N = 1500)
        {
            TreeNode chain = new TreeNode(100);
            TreeNode c = chain;
            for (int k = 2; k <= 1500; k++) {
                c.right = new TreeNode(k * 100);
                c = c.right;
            }
            tests.add(createFromTree(chain));
        }

        // 72: Left-skewed strictly increasing (N = 1500, all invalid, answer = 1)
        {
            TreeNode chain = new TreeNode(100);
            TreeNode c = chain;
            for (int k = 2; k <= 1500; k++) {
                c.left = new TreeNode(k * 100);
                c = c.left;
            }
            tests.add(createFromTree(chain));
        }

        // 73: Right-skewed strictly decreasing (N = 1500, all invalid, answer = 1)
        {
            TreeNode chain = new TreeNode(150000);
            TreeNode c = chain;
            for (int k = 1499; k >= 1; k--) {
                c.right = new TreeNode(k * 100);
                c = c.right;
            }
            tests.add(createFromTree(chain));
        }

        // 74: Zig-zag alternating chain of length 1200
        {
            TreeNode root = new TreeNode(1000);
            TreeNode c = root;
            for (int k = 1; k <= 1200; k++) {
                if (k % 2 == 1) {
                    c.left = new TreeNode(1000 + k * 10);
                    c = c.left;
                } else {
                    c.right = new TreeNode(1000 - k * 10);
                    c = c.right;
                }
            }
            tests.add(createFromTree(root));
        }

        // 75: Binary tree with identical values (N = 1000, answer = 1)
        {
            int nNodes = 1000;
            int[] identical = new int[nNodes];
            for (int k = 0; k < nNodes; k++) identical[k] = 777;
            tests.add(createFromArr(identical));
        }

        // 76: Tree with large values up to 10^9
        {
            TreeNode root = new TreeNode(500000000);
            root.left = new TreeNode(200000000);
            root.right = new TreeNode(800000000);
            root.left.left = new TreeNode(100000000);
            root.left.right = new TreeNode(300000000);
            root.right.left = new TreeNode(700000000);
            root.right.right = new TreeNode(999999999);
            tests.add(createFromTree(root));
        }

        // 77 - 89: Large balanced BSTs with partial corruptions
        for (int i = 77; i <= 89; i++) {
            int size = 511; // 2^9 - 1
            int[] arr = new int[size];
            for (int k = 0; k < size; k++) arr[k] = (k + 1) * 10;
            TreeNode bst = buildBalancedBST(arr, 0, size - 1);

            // Intentionally corrupt one leaf or internal node
            if (i % 3 == 0) {
                // Corrupt left-most leaf
                TreeNode curr = bst;
                while (curr.left != null && curr.left.left != null) curr = curr.left;
                curr.left.val = 999999;
            } else if (i % 3 == 1) {
                // Corrupt right-most leaf
                TreeNode curr = bst;
                while (curr.right != null && curr.right.right != null) curr = curr.right;
                curr.right.val = 1;
            } else {
                // Attach a huge invalid branch to root
                TreeNode poison = new TreeNode(1);
                poison.left = bst.left;
                bst.left = poison;
            }
            tests.add(createFromTree(bst));
        }

        // 90 - 99: Boundary Maximum Constraint Cases (N around 5000 - 10000)
        // 90: Valid balanced BST with 4095 nodes (2^12 - 1)
        {
            int size = 4095;
            int[] arr = new int[size];
            for (int k = 0; k < size; k++) arr[k] = (k + 1) * 2;
            tests.add(createFromTree(buildBalancedBST(arr, 0, size - 1)));
        }

        // 91: Deep skewed valid chain of length 3000
        {
            TreeNode root = new TreeNode(300000);
            TreeNode c = root;
            for (int k = 2999; k >= 1; k--) {
                c.left = new TreeNode(k * 100);
                c = c.left;
            }
            tests.add(createFromTree(root));
        }

        // 92: Valid balanced BST of size 2047 on left, valid balanced BST of size 2047 on right, root invalid
        {
            int size = 2047;
            int[] leftArr = new int[size];
            for (int k = 0; k < size; k++) leftArr[k] = 1000 + k * 2;

            int[] rightArr = new int[size];
            for (int k = 0; k < size; k++) rightArr[k] = 20000 + k * 2;

            TreeNode leftBST = buildBalancedBST(leftArr, 0, size - 1);
            TreeNode rightBST = buildBalancedBST(rightArr, 0, size - 1);

            TreeNode root = new TreeNode(500); // smaller than left subtree, invalidates root
            root.left = leftBST;
            root.right = rightBST;
            tests.add(createFromTree(root));
        }

        // 93 - 99: High-stress large trees
        for (int i = 93; i <= 99; i++) {
            int bstSize = 1023;
            int[] bstArr = new int[bstSize];
            for (int k = 0; k < bstSize; k++) bstArr[k] = 10000 + k * 5;
            TreeNode validSub = buildBalancedBST(bstArr, 0, bstSize - 1);

            int noiseCount = 1000 + (i - 93) * 300;
            TreeNode noiseTree = generateRandomTree(noiseCount, rng, 1, 9000);

            TreeNode combined = new TreeNode(5000);
            combined.left = validSub; // left > 5000 invalidates root
            combined.right = noiseTree;
            tests.add(createFromTree(combined));
        }

        System.out.println("Total test cases prepared: " + tests.size());

        for (int i = 0; i < tests.size(); i++) {
            TestCase tc = tests.get(i);
            String testName = String.format("%02d", i);

            File inFile = new File(inDir, "input" + testName + ".txt");
            try (PrintWriter pw = new PrintWriter(new FileWriter(inFile))) {
                pw.println(tc.serialized.size());
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < tc.serialized.size(); j++) {
                    sb.append(tc.serialized.get(j));
                    if (j < tc.serialized.size() - 1) {
                        sb.append(' ');
                    }
                }
                pw.println(sb.toString());
            }

            File outFile = new File(outDir, "output" + testName + ".txt");
            try (PrintWriter pw = new PrintWriter(new FileWriter(outFile))) {
                pw.println(tc.answer);
            }
        }

        System.out.println("Successfully generated all 100 input and output test cases!");
    }

    private static TestCase createFromArr(int[] arr) {
        TreeNode root = deserialize(arr);
        int ans = solve(root);
        List<Integer> list = serialize(root);
        return new TestCase(list, ans);
    }

    private static TestCase createFromTree(TreeNode root) {
        int ans = solve(root);
        List<Integer> list = serialize(root);
        return new TestCase(list, ans);
    }

    private static TreeNode generateRandomTree(int count, Random rng, int minVal, int maxVal) {
        if (count <= 0) return null;
        int rootVal = minVal + rng.nextInt(maxVal - minVal + 1);
        TreeNode root = new TreeNode(rootVal);

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int created = 1;

        while (!queue.isEmpty() && created < count) {
            TreeNode curr = queue.poll();

            if (created < count && rng.nextDouble() > 0.15) {
                int leftVal = minVal + rng.nextInt(maxVal - minVal + 1);
                curr.left = new TreeNode(leftVal);
                queue.offer(curr.left);
                created++;
            }

            if (created < count && rng.nextDouble() > 0.15) {
                int rightVal = minVal + rng.nextInt(maxVal - minVal + 1);
                curr.right = new TreeNode(rightVal);
                queue.offer(curr.right);
                created++;
            }
        }

        return root;
    }
}
