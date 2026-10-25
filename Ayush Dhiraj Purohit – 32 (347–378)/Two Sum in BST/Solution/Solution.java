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

        public long nextLong() throws IOException {
            String s = nextToken();
            if (s == null) return -1;
            return Long.parseLong(s);
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
     * Dual-direction BST Iterator:
     * - reverse = false: in-order traversal (smallest to largest)
     * - reverse = true: reverse in-order traversal (largest to smallest)
     * 
     * O(H) auxiliary space on heap, amortized O(1) time per next() step.
     */
    public static class BSTIterator {
        private final Deque<TreeNode> stack;
        private final boolean reverse;

        public BSTIterator(TreeNode root, boolean reverse) {
            this.stack = new ArrayDeque<>();
            this.reverse = reverse;
            pushAll(root);
        }

        private void pushAll(TreeNode node) {
            while (node != null) {
                stack.push(node);
                node = reverse ? node.right : node.left;
            }
        }

        public int next() {
            TreeNode node = stack.pop();
            if (reverse) {
                pushAll(node.left);
            } else {
                pushAll(node.right);
            }
            return node.val;
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }
    }

    /**
     * Determines whether two distinct nodes in the BST sum to k using a
     * two-pointer technique simulated by two BST iterators.
     * 
     * Time Complexity: O(N)
     * Space Complexity: O(H) auxiliary space on heap (completely stack-safe).
     */
    public static boolean findTarget(TreeNode root, long k) {
        if (root == null) return false;

        BSTIterator leftIter = new BSTIterator(root, false);
        BSTIterator rightIter = new BSTIterator(root, true);

        if (!leftIter.hasNext() || !rightIter.hasNext()) return false;

        int leftVal = leftIter.next();
        int rightVal = rightIter.next();

        while (leftVal < rightVal) {
            long sum = (long) leftVal + rightVal;
            if (sum == k) {
                return true;
            } else if (sum < k) {
                if (leftIter.hasNext()) {
                    leftVal = leftIter.next();
                } else {
                    break;
                }
            } else {
                if (rightIter.hasNext()) {
                    rightVal = rightIter.next();
                } else {
                    break;
                }
            }
        }

        return false;
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
            long k = scanner.nextLong();

            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            TreeNode root = buildTree(arr);
            boolean found = findTarget(root, k);

            writer.println(found ? "true" : "false");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
