import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.util.Collections;
import java.util.Locale;
import java.util.PriorityQueue;

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

    public static class MedianFinder {
        private final PriorityQueue<Integer> maxHeap; // Smaller half
        private final PriorityQueue<Integer> minHeap; // Larger half

        public MedianFinder() {
            this.maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            this.minHeap = new PriorityQueue<>();
        }

        public void addNum(int num) {
            maxHeap.offer(num);
            minHeap.offer(maxHeap.poll());

            if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }

        public double findMedian() {
            if (maxHeap.size() > minHeap.size()) {
                return (double) maxHeap.peek();
            }
            long sum = (long) maxHeap.peek() + minHeap.peek();
            double med = sum / 2.0;
            if (Math.abs(med) < 1e-9) {
                med = 0.0;
            }
            return med;
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
            MedianFinder finder = new MedianFinder();

            boolean first = true;
            for (int i = 0; i < q; i++) {
                String op = scanner.nextToken();
                if (op == null) break;

                if (!first) {
                    writer.print(" ");
                }
                first = false;

                if (op.equals("addNum") || op.equals("add")) {
                    int val = scanner.nextInt();
                    finder.addNum(val);
                    writer.print("null");
                } else if (op.equals("findMedian") || op.equals("getMedian")) {
                    double median = finder.findMedian();
                    writer.print(String.format(Locale.US, "%.1f", median));
                } else if (op.equals("MedianFinder")) {
                    finder = new MedianFinder();
                    writer.print("null");
                }
            }

            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
