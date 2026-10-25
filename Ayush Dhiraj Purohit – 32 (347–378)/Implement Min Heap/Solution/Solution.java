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

    public static class MinHeap {
        private int[] heap;
        private int size;

        public MinHeap() {
            this.heap = new int[16];
            this.size = 0;
        }

        public void initializeHeap() {
            this.size = 0;
        }

        public void insert(int key) {
            ensureCapacity(size + 1);
            heap[size] = key;
            siftUp(size);
            size++;
        }

        public void changeKey(int index, int newVal) {
            if (index < 0 || index >= size) {
                return;
            }
            int oldVal = heap[index];
            heap[index] = newVal;
            if (newVal < oldVal) {
                siftUp(index);
            } else if (newVal > oldVal) {
                siftDown(index);
            }
        }

        public void extractMin() {
            if (size == 0) {
                return;
            }
            heap[0] = heap[size - 1];
            size--;
            if (size > 0) {
                siftDown(0);
            }
        }

        public int isEmpty() {
            return size == 0 ? 1 : 0;
        }

        public int getMin() {
            if (size == 0) {
                return -1;
            }
            return heap[0];
        }

        public int heapSize() {
            return size;
        }

        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap[i] < heap[parent]) {
                    swap(i, parent);
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
                    swap(i, smallest);
                    i = smallest;
                } else {
                    break;
                }
            }
        }

        private void swap(int i, int j) {
            int temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
        }

        private void ensureCapacity(int minCapacity) {
            if (minCapacity > heap.length) {
                int newCap = Math.max(heap.length * 2, minCapacity);
                int[] newHeap = new int[newCap];
                System.arraycopy(heap, 0, newHeap, 0, size);
                heap = newHeap;
            }
        }
    }

    public static void main(String[] args) {
        try {
            FastScanner scanner = new FastScanner(System.in);
            PrintWriter writer = new PrintWriter(new BufferedOutputStream(System.out));

            String qStr = scanner.nextToken();
            if (qStr == null) {
                writer.flush();
                return;
            }

            int q = Integer.parseInt(qStr);
            MinHeap minHeap = new MinHeap();

            for (int i = 0; i < q; i++) {
                String op = scanner.nextToken();
                if (op == null) break;
                if (i > 0) writer.print(" ");

                if (op.equalsIgnoreCase("initializeHeap")) {
                    minHeap.initializeHeap();
                    writer.print("null");
                } else if (op.equalsIgnoreCase("insert")) {
                    int key = scanner.nextInt();
                    minHeap.insert(key);
                    writer.print("null");
                } else if (op.equalsIgnoreCase("changeKey")) {
                    int index = scanner.nextInt();
                    int newVal = scanner.nextInt();
                    minHeap.changeKey(index, newVal);
                    writer.print("null");
                } else if (op.equalsIgnoreCase("extractMin")) {
                    minHeap.extractMin();
                    writer.print("null");
                } else if (op.equalsIgnoreCase("isEmpty")) {
                    writer.print(minHeap.isEmpty());
                } else if (op.equalsIgnoreCase("getMin")) {
                    writer.print(minHeap.getMin());
                } else if (op.equalsIgnoreCase("heapSize")) {
                    writer.print(minHeap.heapSize());
                }
            }

            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
