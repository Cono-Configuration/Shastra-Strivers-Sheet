import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution {

    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[32768];
        private int head = 0;
        private int tail = 0;

        public FastScanner(InputStream in) {
            this.in = in;
        }

        private int read() throws IOException {
            if (head >= tail) {
                head = 0;
                tail = in.read(buffer, 0, buffer.length);
                if (tail <= 0) return -1;
            }
            return buffer[head++];
        }

        public String nextToken() throws IOException {
            int c = read();
            while (c <= 32) {
                if (c == -1) return null;
                c = read();
            }
            StringBuilder sb = new StringBuilder();
            while (c > 32) {
                sb.append((char) c);
                c = read();
            }
            return sb.toString();
        }

        public int nextInt() throws IOException {
            String s = nextToken();
            if (s == null) return -1;
            return Integer.parseInt(s);
        }
    }

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * Constructs a Binary Search Tree (BST) from its preorder traversal array.
     * 
     * Iterative Monotonic Stack Approach:
     * - O(N) time complexity: Each element is pushed and popped at most once.
     * - O(H) auxiliary space on heap: Completely stack-safe, avoids call stack overflow
     *   on deep/skewed trees of depth up to 10^4.
     */
    public static TreeNode bstFromPreorder(int[] preorder) {
        if (preorder == null || preorder.length == 0) {
            return null;
        }

        TreeNode root = new TreeNode(preorder[0]);
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        for (int i = 1; i < preorder.length; i++) {
            TreeNode node = new TreeNode(preorder[i]);

            if (preorder[i] < stack.peek().val) {
                // Preorder invariant: smaller element immediately following is the left child
                stack.peek().left = node;
            } else {
                // Pop all nodes that are smaller than the current element to find the parent
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
     * Serializes a binary tree into level-order list of integers (-1 for null),
     * with trailing nulls omitted.
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

    public static void main(String[] args) {
        try {
            FastScanner scanner = new FastScanner(System.in);
            PrintWriter writer = new PrintWriter(new BufferedOutputStream(System.out));

            String nStr = scanner.nextToken();
            if (nStr == null) {
                writer.flush();
                return;
            }
            int n = Integer.parseInt(nStr);
            if (n == 0) {
                writer.println();
                writer.flush();
                return;
            }

            int[] preorder = new int[n];
            for (int i = 0; i < n; i++) {
                preorder[i] = scanner.nextInt();
            }

            TreeNode root = bstFromPreorder(preorder);
            List<Integer> serialized = serializeLevelOrder(root);

            for (int i = 0; i < serialized.size(); i++) {
                if (i > 0) writer.print(" ");
                writer.print(serialized.get(i));
            }
            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
