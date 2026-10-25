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

    public static int findKthLargest(int[] nums, int k) {
        int n = nums.length;
        int[] heap = new int[k];
        for (int i = 0; i < k; i++) {
            heap[i] = nums[i];
        }

        // Build min-heap on the first k elements
        for (int i = k / 2 - 1; i >= 0; i--) {
            siftDown(heap, i, k);
        }

        // Process remaining elements
        for (int i = k; i < n; i++) {
            if (nums[i] > heap[0]) {
                heap[0] = nums[i];
                siftDown(heap, 0, k);
            }
        }

        return heap[0];
    }

    private static void siftDown(int[] heap, int i, int n) {
        while (2 * i + 1 < n) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (heap[left] < heap[smallest]) {
                smallest = left;
            }
            if (right < n && heap[right] < heap[smallest]) {
                smallest = right;
            }

            if (smallest != i) {
                int temp = heap[i];
                heap[i] = heap[smallest];
                heap[smallest] = temp;
                i = smallest;
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
            int k = scanner.nextInt();

            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = scanner.nextInt();
            }

            int ans = findKthLargest(nums, k);
            writer.println(ans);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
