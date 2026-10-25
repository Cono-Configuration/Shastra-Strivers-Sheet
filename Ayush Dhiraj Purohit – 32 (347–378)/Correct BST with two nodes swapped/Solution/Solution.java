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
     * Recovers a Binary Search Tree with exactly two nodes swapped.
     * 
     * Iterative In-Order Traversal:
     * - O(N) time complexity: Each node is pushed and popped exactly once.
     * - O(H) auxiliary space on heap: Completely stack-safe, avoids StackOverflowError
     *   on skewed trees of depth up to 10^4.
     */
    public static void recoverTree(TreeNode root) {
        if (root == null) return;

        TreeNode first = null;
        TreeNode middle = null;
        TreeNode last = null;
        TreeNode prev = null;

        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }

            curr = stack.pop();

            if (prev != null && prev.val > curr.val) {
                if (first == null) {
                    first = prev;
                    middle = curr;
                } else {
                    last = curr;
                }
            }
            prev = curr;

            curr = curr.right;
        }

        // Fix the swapped nodes
        if (first != null && last != null) {
            // Case 1: Swapped nodes are non-adjacent in in-order sequence
            int temp = first.val;
            first.val = last.val;
            last.val = temp;
        } else if (first != null && middle != null) {
            // Case 2: Swapped nodes are adjacent in in-order sequence
            int temp = first.val;
            first.val = middle.val;
            middle.val = temp;
        }
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

    /**
     * Serializes tree to level-order list of integers (-1 for null), trimming trailing -1s.
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

            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            TreeNode root = buildTree(arr);
            recoverTree(root);
            List<Integer> recovered = serializeLevelOrder(root);

            for (int i = 0; i < recovered.size(); i++) {
                if (i > 0) writer.print(" ");
                writer.print(recovered.get(i));
            }
            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
