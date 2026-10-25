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

    public static void sortKSorted(int[] nums, int k) {
        int n = nums.length;
        if (n <= 1 || k == 0) {
            return;
        }

        int heapCapacity = Math.min(k + 1, n);
        int[] heap = new int[heapCapacity];
        int heapSize = 0;

        int writeIdx = 0;

        for (int i = 0; i < n; i++) {
            if (heapSize < heapCapacity) {
                heap[heapSize] = nums[i];
                siftUp(heap, heapSize);
                heapSize++;
            } else {
                nums[writeIdx++] = heap[0];
                heap[0] = nums[i];
                siftDown(heap, 0, heapSize);
            }
        }

        while (heapSize > 0) {
            nums[writeIdx++] = heap[0];
            heap[0] = heap[heapSize - 1];
            heapSize--;
            if (heapSize > 0) {
                siftDown(heap, 0, heapSize);
            }
        }
    }

    private static void siftUp(int[] heap, int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[i] < heap[parent]) {
                int temp = heap[i];
                heap[i] = heap[parent];
                heap[parent] = temp;
                i = parent;
            } else {
                break;
            }
        }
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

            sortKSorted(nums, k);

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
