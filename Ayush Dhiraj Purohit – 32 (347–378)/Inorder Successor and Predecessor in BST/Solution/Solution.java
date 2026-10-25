import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
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
     * Finds the Inorder Predecessor of key in a BST.
     * Iterative, O(H) time, O(1) auxiliary space, stack-safe.
     * 
     * Invariant:
     * - If curr.val < key, curr is a potential predecessor; we record curr.val
     *   and move right to seek an even larger candidate smaller than key.
     * - If curr.val >= key, curr cannot be a predecessor; move left.
     */
    public static int findPredecessor(TreeNode root, int key) {
        int predecessor = -1;
        TreeNode curr = root;
        while (curr != null) {
            if (curr.val < key) {
                predecessor = curr.val;
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }
        return predecessor;
    }

    /**
     * Finds the Inorder Successor of key in a BST.
     * Iterative, O(H) time, O(1) auxiliary space, stack-safe.
     * 
     * Invariant:
     * - If curr.val > key, curr is a potential successor; we record curr.val
     *   and move left to seek an even smaller candidate greater than key.
     * - If curr.val <= key, curr cannot be a successor; move right.
     */
    public static int findSuccessor(TreeNode root, int key) {
        int successor = -1;
        TreeNode curr = root;
        while (curr != null) {
            if (curr.val > key) {
                successor = curr.val;
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return successor;
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
        try {
            FastScanner scanner = new FastScanner(System.in);
            PrintWriter writer = new PrintWriter(new BufferedOutputStream(System.out));

            String nStr = scanner.nextToken();
            if (nStr == null) {
                writer.flush();
                return;
            }
            int n = Integer.parseInt(nStr);
            int key = scanner.nextInt();

            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            TreeNode root = buildTree(arr);

            int predecessor = findPredecessor(root, key);
            int successor = findSuccessor(root, key);

            writer.println(predecessor + " " + successor);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
