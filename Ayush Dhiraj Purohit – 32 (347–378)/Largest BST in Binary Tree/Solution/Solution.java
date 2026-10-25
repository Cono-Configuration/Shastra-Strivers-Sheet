import java.io.InputStream;
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

public class Solution {

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

    public static int largestBST(TreeNode root) {
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

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        int n = scanner.nextInt();
        if (n <= 0) {
            writer.println(0);
            writer.flush();
            return;
        }

        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        TreeNode root = buildTree(arr);
        int ans = largestBST(root);
        writer.println(ans);
        writer.flush();
    }

    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0;
        private int len = 0;

        public FastScanner(InputStream in) {
            this.in = in;
        }

        private int readByte() {
            if (ptr >= len) {
                ptr = 0;
                try {
                    len = in.read(buffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (len <= 0) {
                    return -1;
                }
            }
            return buffer[ptr++];
        }

        public int nextInt() {
            int c = readByte();
            while (c <= ' ' && c != -1) {
                c = readByte();
            }
            if (c == -1) {
                return -1;
            }

            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = readByte();
            }

            int res = 0;
            while (c >= '0' && c <= '9') {
                res = res * 10 + (c - '0');
                c = readByte();
            }
            return res * sign;
        }
    }
}
