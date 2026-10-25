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

    public static class KthLargest {
        private final int k;
        private final int[] heap;
        private int size;

        public KthLargest(int k, int[] nums) {
            this.k = k;
            this.heap = new int[k];
            this.size = 0;

            for (int num : nums) {
                addInternal(num);
            }
        }

        private void addInternal(int val) {
            if (size < k) {
                heap[size] = val;
                siftUp(size);
                size++;
            } else if (val > heap[0]) {
                heap[0] = val;
                siftDown(0);
            }
        }

        public int add(int val) {
            addInternal(val);
            return heap[0];
        }

        private void siftUp(int i) {
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

        private void siftDown(int i) {
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
                    int temp = heap[i];
                    heap[i] = heap[smallest];
                    heap[smallest] = temp;
                    i = smallest;
                } else {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            FastScanner scanner = new FastScanner(System.in);
            PrintWriter writer = new PrintWriter(new BufferedOutputStream(System.out));

            String kStr = scanner.nextToken();
            if (kStr == null) {
                writer.flush();
                return;
            }

            int k = Integer.parseInt(kStr);
            int n = scanner.nextInt();

            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = scanner.nextInt();
            }

            KthLargest kthLargest = new KthLargest(k, nums);

            int q = scanner.nextInt();
            for (int i = 0; i < q; i++) {
                int val = scanner.nextInt();
                int ans = kthLargest.add(val);
                if (i > 0) writer.print(" ");
                writer.print(ans);
            }

            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
