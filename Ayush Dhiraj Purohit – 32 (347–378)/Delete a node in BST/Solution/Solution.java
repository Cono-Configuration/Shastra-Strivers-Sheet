import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.util.LinkedList;
import java.util.ArrayList;
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
     * Deletes a node with the given key from the BST iteratively.
     * Uses in-order successor replacement for nodes with two children.
     * Stack-safe O(1) auxiliary space approach.
     */
    public static TreeNode deleteNode(TreeNode root, int key) {
        TreeNode curr = root;
        TreeNode parent = null;

        // Search for the node to delete
        while (curr != null && curr.val != key) {
            parent = curr;
            if (key < curr.val) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        // Node not found in the BST
        if (curr == null) {
            return root;
        }

        // Case 1 & Case 2: Node has 0 or 1 child
        if (curr.left == null || curr.right == null) {
            TreeNode child = (curr.left != null) ? curr.left : curr.right;
            if (parent == null) {
                return child; // Root is being deleted
            }
            if (parent.left == curr) {
                parent.left = child;
            } else {
                parent.right = child;
            }
            return root;
        }

        // Case 3: Node has two children
        // Find inorder successor (minimum element in the right subtree)
        TreeNode succParent = curr;
        TreeNode succ = curr.right;
        while (succ.left != null) {
            succParent = succ;
            succ = succ.left;
        }

        // Replace the target node's value with the successor's value
        curr.val = succ.val;

        // Delete the successor node (successor has at most a right child)
        if (succParent != curr) {
            succParent.left = succ.right;
        } else {
            succParent.right = succ.right;
        }

        return root;
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

    /**
     * Serializes a Binary Tree to level-order list of integers (-1 for null),
     * trimming all trailing -1 elements.
     */
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

        // Trim trailing -1s
        int lastIndex = result.size() - 1;
        while (lastIndex >= 0 && result.get(lastIndex) == -1) {
            lastIndex--;
        }

        return result.subList(0, lastIndex + 1);
    }

    public static void main(String[] args) {
        try {
            FastScanner sc = new FastScanner(System.in);
            String firstToken = sc.nextToken();
            if (firstToken == null) return;
            int N = Integer.parseInt(firstToken);
            int key = sc.nextInt();

            int[] arr = new int[N];
            for (int i = 0; i < N; i++) {
                arr[i] = sc.nextInt();
            }

            TreeNode root = buildTree(arr);
            root = deleteNode(root, key);
            List<Integer> outputList = serialize(root);

            PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
            for (int i = 0; i < outputList.size(); i++) {
                out.print(outputList.get(i));
                if (i + 1 < outputList.size()) {
                    out.print(" ");
                }
            }
            out.println();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
