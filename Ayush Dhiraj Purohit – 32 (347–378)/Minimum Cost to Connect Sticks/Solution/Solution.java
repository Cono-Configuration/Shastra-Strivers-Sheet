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

    private static void siftDown(long[] heap, int i, int size) {
        while (2 * i + 1 < size) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (heap[left] < heap[smallest]) {
                smallest = left;
            }
            if (right < size && heap[right] < heap[smallest]) {
                smallest = right;
            }

            if (smallest != i) {
                long tmp = heap[i];
                heap[i] = heap[smallest];
                heap[smallest] = tmp;
                i = smallest;
            } else {
                break;
            }
        }
    }

    public static long connectSticks(int[] sticks) {
        int n = sticks.length;
        if (n <= 1) {
            return 0L;
        }

        long[] heap = new long[n];
        for (int i = 0; i < n; i++) {
            heap[i] = sticks[i];
        }

        int size = n;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(heap, i, size);
        }

        long totalCost = 0L;
        while (size > 1) {
            long first = heap[0];
            heap[0] = heap[size - 1];
            size--;
            siftDown(heap, 0, size);

            long second = heap[0];
            long cost = first + second;
            totalCost += cost;

            heap[0] = cost;
            siftDown(heap, 0, size);
        }

        return totalCost;
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
            int[] sticks = new int[n];
            for (int i = 0; i < n; i++) {
                sticks[i] = scanner.nextInt();
            }

            long result = connectSticks(sticks);

            writer.println(result);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
