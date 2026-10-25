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

    private static void siftDown(int[] heapVal, int[] heapIdx, int i, int n) {
        while (2 * i + 1 < n) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (heapVal[left] < heapVal[smallest]) {
                smallest = left;
            }
            if (right < n && heapVal[right] < heapVal[smallest]) {
                smallest = right;
            }

            if (smallest != i) {
                int tmpVal = heapVal[i];
                heapVal[i] = heapVal[smallest];
                heapVal[smallest] = tmpVal;

                int tmpIdx = heapIdx[i];
                heapIdx[i] = heapIdx[smallest];
                heapIdx[smallest] = tmpIdx;

                i = smallest;
            } else {
                break;
            }
        }
    }

    public static int[] replaceWithRank(int[] arr) {
        int n = arr.length;
        if (n == 0) return new int[0];

        int[] heapVal = new int[n];
        int[] heapIdx = new int[n];

        for (int i = 0; i < n; i++) {
            heapVal[i] = arr[i];
            heapIdx[i] = i;
        }

        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(heapVal, heapIdx, i, n);
        }

        int[] result = new int[n];
        int rank = 0;
        long prevVal = Long.MIN_VALUE;
        int heapSize = n;

        while (heapSize > 0) {
            int val = heapVal[0];
            int idx = heapIdx[0];

            if (val != prevVal) {
                rank++;
                prevVal = val;
            }

            result[idx] = rank;

            heapVal[0] = heapVal[heapSize - 1];
            heapIdx[0] = heapIdx[heapSize - 1];
            heapSize--;

            if (heapSize > 0) {
                siftDown(heapVal, heapIdx, 0, heapSize);
            }
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

            int[] ranks = replaceWithRank(arr);

            for (int i = 0; i < n; i++) {
                if (i > 0) writer.print(" ");
                writer.print(ranks[i]);
            }
            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
