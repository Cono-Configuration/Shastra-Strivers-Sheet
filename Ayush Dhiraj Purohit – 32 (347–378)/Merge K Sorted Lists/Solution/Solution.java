import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;

public class Solution {

    public static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

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

    static class MinHeap {
        private final ListNode[] heap;
        private int size;

        public MinHeap(int capacity) {
            this.heap = new ListNode[capacity];
            this.size = 0;
        }

        public void add(ListNode node) {
            heap[size] = node;
            siftUp(size);
            size++;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public ListNode pollAndAdvance() {
            if (size == 0) return null;
            ListNode minNode = heap[0];
            if (minNode.next != null) {
                heap[0] = minNode.next;
                siftDown(0);
            } else {
                heap[0] = heap[size - 1];
                heap[size - 1] = null;
                size--;
                if (size > 0) {
                    siftDown(0);
                }
            }
            return minNode;
        }

        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap[i].val < heap[parent].val) {
                    ListNode tmp = heap[i];
                    heap[i] = heap[parent];
                    heap[parent] = tmp;
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

                if (heap[left].val < heap[smallest].val) {
                    smallest = left;
                }
                if (right < size && heap[right].val < heap[smallest].val) {
                    smallest = right;
                }

                if (smallest != i) {
                    ListNode tmp = heap[i];
                    heap[i] = heap[smallest];
                    heap[smallest] = tmp;
                    i = smallest;
                } else {
                    break;
                }
            }
        }
    }

    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }

        int k = lists.length;
        int nonNullCount = 0;
        for (int i = 0; i < k; i++) {
            if (lists[i] != null) {
                nonNullCount++;
            }
        }

        if (nonNullCount == 0) {
            return null;
        }

        MinHeap pq = new MinHeap(nonNullCount);
        for (int i = 0; i < k; i++) {
            if (lists[i] != null) {
                pq.add(lists[i]);
            }
        }

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        while (!pq.isEmpty()) {
            ListNode node = pq.pollAndAdvance();
            tail.next = node;
            tail = node;
        }
        tail.next = null;

        return dummy.next;
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
            ListNode[] lists = new ListNode[k];

            for (int i = 0; i < k; i++) {
                int m = scanner.nextInt();
                ListNode head = null;
                ListNode tail = null;
                for (int j = 0; j < m; j++) {
                    int val = scanner.nextInt();
                    ListNode node = new ListNode(val);
                    if (head == null) {
                        head = node;
                        tail = node;
                    } else {
                        tail.next = node;
                        tail = node;
                    }
                }
                lists[i] = head;
            }

            ListNode merged = mergeKLists(lists);
            ListNode curr = merged;
            boolean first = true;
            while (curr != null) {
                if (!first) writer.print(" ");
                writer.print(curr.val);
                first = false;
                curr = curr.next;
            }
            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
