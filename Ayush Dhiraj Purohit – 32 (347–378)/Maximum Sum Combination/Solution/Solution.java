import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.util.Arrays;
import java.util.Random;

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

    private static void shuffle(int[] arr) {
        Random rng = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    private static void siftDown(long[] heapSum, int[] heapI, int[] heapJ, int i, int size) {
        while (2 * i + 1 < size) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            if (heapSum[left] > heapSum[largest]) {
                largest = left;
            }
            if (right < size && heapSum[right] > heapSum[largest]) {
                largest = right;
            }

            if (largest != i) {
                long tmpSum = heapSum[i];
                heapSum[i] = heapSum[largest];
                heapSum[largest] = tmpSum;

                int tmpI = heapI[i];
                heapI[i] = heapI[largest];
                heapI[largest] = tmpI;

                int tmpJ = heapJ[i];
                heapJ[i] = heapJ[largest];
                heapJ[largest] = tmpJ;

                i = largest;
            } else {
                break;
            }
        }
    }

    public static long[] maxCombinations(int[] nums1, int[] nums2, int k) {
        int n = nums1.length;
        shuffle(nums1);
        Arrays.sort(nums1);
        shuffle(nums2);
        Arrays.sort(nums2);

        int m = Math.min(n, k);
        long[] heapSum = new long[m];
        int[] heapI = new int[m];
        int[] heapJ = new int[m];

        for (int idx = 0; idx < m; idx++) {
            int i = n - 1 - idx;
            heapSum[idx] = (long) nums1[i] + nums2[n - 1];
            heapI[idx] = i;
            heapJ[idx] = n - 1;
        }

        int size = m;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(heapSum, heapI, heapJ, i, size);
        }

        long[] result = new long[k];
        for (int step = 0; step < k; step++) {
            result[step] = heapSum[0];
            int i = heapI[0];
            int j = heapJ[0];

            if (j > 0) {
                heapSum[0] = (long) nums1[i] + nums2[j - 1];
                heapJ[0] = j - 1;
                siftDown(heapSum, heapI, heapJ, 0, size);
            } else {
                heapSum[0] = heapSum[size - 1];
                heapI[0] = heapI[size - 1];
                heapJ[0] = heapJ[size - 1];
                size--;
                if (size > 0) {
                    siftDown(heapSum, heapI, heapJ, 0, size);
                }
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
            int k = scanner.nextInt();

            int[] nums1 = new int[n];
            for (int i = 0; i < n; i++) {
                nums1[i] = scanner.nextInt();
            }

            int[] nums2 = new int[n];
            for (int i = 0; i < n; i++) {
                nums2[i] = scanner.nextInt();
            }

            long[] result = maxCombinations(nums1, nums2, k);

            for (int i = 0; i < k; i++) {
                if (i > 0) writer.print(" ");
                writer.print(result[i]);
            }
            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
