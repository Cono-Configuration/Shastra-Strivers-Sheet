import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;

public class Solution {

    public static class FastScanner {
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
            int c = read();
            while (c <= 32) {
                if (c == -1) return 0;
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            while (c >= '0' && c <= '9') {
                res = res * 10 + (c - '0');
                c = read();
            }
            return res * sgn;
        }
    }

    public static void heapSort(int[] nums) {
        int n = nums.length;
        if (n <= 1) {
            return;
        }

        // Phase 1: Build max heap (bottom-up)
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(nums, i, n);
        }

        // Phase 2: In-place extraction and sorting
        for (int i = n - 1; i > 0; i--) {
            // Swap root (maximum remaining element) with element at index i
            int temp = nums[0];
            nums[0] = nums[i];
            nums[i] = temp;

            // Restore max-heap property on reduced heap of size i
            siftDown(nums, 0, i);
        }
    }

    private static void siftDown(int[] nums, int i, int n) {
        while (2 * i + 1 < n) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            if (nums[left] > nums[largest]) {
                largest = left;
            }
            if (right < n && nums[right] > nums[largest]) {
                largest = right;
            }

            if (largest != i) {
                int temp = nums[i];
                nums[i] = nums[largest];
                nums[largest] = temp;
                i = largest;
            } else {
                break;
            }
        }
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
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = scanner.nextInt();
            }

            heapSort(nums);

            for (int i = 0; i < n; i++) {
                if (i > 0) writer.print(" ");
                writer.print(nums[i]);
            }
            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
