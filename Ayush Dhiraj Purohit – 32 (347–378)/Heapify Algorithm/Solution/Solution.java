import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Solution {

    public static void heapify(int[] nums, int ind, int val) {
        int n = nums.length;
        if (ind < 0 || ind >= n) {
            return;
        }

        nums[ind] = val;

        // If the new value is smaller than its parent, sift up
        if (ind > 0 && nums[ind] < nums[(ind - 1) / 2]) {
            siftUp(nums, ind);
        } else {
            // Otherwise, sift down
            siftDown(nums, ind, n);
        }
    }

    private static void siftUp(int[] nums, int ind) {
        while (ind > 0) {
            int parent = (ind - 1) / 2;
            if (nums[ind] < nums[parent]) {
                swap(nums, ind, parent);
                ind = parent;
            } else {
                break;
            }
        }
    }

    private static void siftDown(int[] nums, int ind, int n) {
        while (true) {
            int left = 2 * ind + 1;
            int right = 2 * ind + 2;
            int smallest = ind;

            if (left < n && nums[left] < nums[smallest]) {
                smallest = left;
            }
            if (right < n && nums[right] < nums[smallest]) {
                smallest = right;
            }

            if (smallest != ind) {
                swap(nums, ind, smallest);
                ind = smallest;
            } else {
                break;
            }
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        int n = scanner.nextInt();
        if (n <= 0) {
            writer.flush();
            return;
        }

        int ind = scanner.nextInt();
        int val = scanner.nextInt();

        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = scanner.nextInt();
        }

        heapify(nums, ind, val);

        for (int i = 0; i < n; i++) {
            writer.print(nums[i]);
            if (i < n - 1) {
                writer.print(' ');
            }
        }
        writer.println();
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
