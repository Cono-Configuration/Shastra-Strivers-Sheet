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
     * Finds the Lowest Common Ancestor (LCA) of nodes p and q in a Binary Search Tree.
     * 
     * Iterative, O(1) auxiliary space, stack-safe approach leveraging the BST invariant:
     * - If both p and q are strictly less than curr.val, LCA lies in the left subtree.
     * - If both p and q are strictly greater than curr.val, LCA lies in the right subtree.
     * - Otherwise, curr is the split point (or one of the nodes itself), which is the LCA.
     */
    public static TreeNode lowestCommonAncestor(TreeNode root, int p, int q) {
        TreeNode curr = root;
        while (curr != null) {
            if (p < curr.val && q < curr.val) {
                curr = curr.left;
            } else if (p > curr.val && q > curr.val) {
                curr = curr.right;
            } else {
                return curr; // Split point or matching node found
            }
        }
        return null;
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
            int p = scanner.nextInt();
            int q = scanner.nextInt();

            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            TreeNode root = buildTree(arr);
            TreeNode lcaNode = lowestCommonAncestor(root, p, q);

            if (lcaNode != null) {
                writer.println(lcaNode.val);
            } else {
                writer.println(-1);
            }

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
