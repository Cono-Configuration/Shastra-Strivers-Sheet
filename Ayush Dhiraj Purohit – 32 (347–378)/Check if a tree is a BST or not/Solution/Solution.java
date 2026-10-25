import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
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
     * Determines whether a binary tree is a valid Binary Search Tree.
     * Uses iterative in-order traversal to verify strictly increasing sequence.
     * Stack-safe O(H) auxiliary space on heap, O(N) time.
     */
    public static boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }

        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;
        TreeNode prev = null;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();

            // Strict BST invariant: strictly increasing in-order sequence
            if (prev != null && curr.val <= prev.val) {
                return false;
            }
            prev = curr;

            curr = curr.right;
        }

        return true;
    }

    /**
     * Builds a Binary Tree from level-order traversal array with -1 representing null.
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

            // Left child
            if (i < arr.length) {
                if (arr[i] != -1) {
                    curr.left = new TreeNode(arr[i]);
                    queue.offer(curr.left);
                }
                i++;
            }

            // Right child
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
        try {
            FastScanner sc = new FastScanner(System.in);
            String firstToken = sc.nextToken();
            if (firstToken == null) return;
            int N = Integer.parseInt(firstToken);

            int[] arr = new int[N];
            for (int i = 0; i < N; i++) {
                arr[i] = sc.nextInt();
            }

            TreeNode root = buildTree(arr);
            boolean result = isValidBST(root);

            PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
            out.println(result);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
