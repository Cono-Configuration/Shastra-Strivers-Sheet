import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class gen {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
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

    static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;

        int k = lists.length;
        int nonNullCount = 0;
        for (int i = 0; i < k; i++) {
            if (lists[i] != null) nonNullCount++;
        }
        if (nonNullCount == 0) return null;

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

    static int[] generateSortedArray(int m, Random rng, int minVal, int maxVal) {
        int[] arr = new int[m];
        long range = (long) maxVal - minVal + 1;
        for (int i = 0; i < m; i++) {
            arr[i] = minVal + (int) (Math.abs(rng.nextLong()) % range);
        }
        Arrays.sort(arr);
        return arr;
    }

    static void writeTestCase(File inFile, File outFile, List<int[]> listsData) throws IOException {
        int k = listsData.size();

        // Write input
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(k);
            for (int i = 0; i < k; i++) {
                int[] arr = listsData.get(i);
                inPw.print(arr.length);
                for (int val : arr) {
                    inPw.print(" ");
                    inPw.print(val);
                }
                inPw.println();
            }
            inPw.flush();
        }

        // Build Linked Lists for solver
        ListNode[] lists = new ListNode[k];
        List<Integer> allVals = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            int[] arr = listsData.get(i);
            ListNode head = null;
            ListNode tail = null;
            for (int val : arr) {
                allVals.add(val);
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

        // Verification against Arrays.sort of all elements
        int[] expectedArr = new int[allVals.size()];
        for (int i = 0; i < allVals.size(); i++) {
            expectedArr[i] = allVals.get(i);
        }
        Arrays.sort(expectedArr);

        List<Integer> actualVals = new ArrayList<>();
        ListNode curr = merged;
        while (curr != null) {
            actualVals.add(curr.val);
            curr = curr.next;
        }

        if (actualVals.size() != expectedArr.length) {
            throw new IllegalStateException("Size mismatch between merged list (" + actualVals.size() + ") and expected (" + expectedArr.length + ")");
        }
        for (int i = 0; i < expectedArr.length; i++) {
            if (actualVals.get(i) != expectedArr[i]) {
                throw new IllegalStateException("Value mismatch at index " + i + ": got " + actualVals.get(i) + ", expected " + expectedArr[i]);
            }
        }

        // Write output
        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            for (int i = 0; i < actualVals.size(); i++) {
                if (i > 0) outPw.print(" ");
                outPw.print(actualVals.get(i));
            }
            outPw.println();
            outPw.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        File cur = new File(".");
        File baseDir;
        if (new File(cur, "Testcases").exists() || new File(cur, "Generator").exists() || new File(cur, "Problem.md").exists()) {
            baseDir = cur;
        } else if (new File(cur, "../Testcases").exists() || new File(cur, "../Problem.md").exists()) {
            baseDir = new File(cur, "..");
        } else if (new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Merge K Sorted Lists").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Merge K Sorted Lists");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Merge K Sorted Lists");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(20261025);

        for (int test = 0; test < 100; test++) {
            List<int[]> listsData = new ArrayList<>();

            if (test == 0) {
                // TUF Example 1: [[1, 4, 5], [1, 3, 4], [2, 6]]
                listsData.add(new int[]{1, 4, 5});
                listsData.add(new int[]{1, 3, 4});
                listsData.add(new int[]{2, 6});
            } else if (test == 1) {
                // TUF Example 2: k = 0
                // listsData is empty
            } else if (test == 2) {
                // TUF Example 3: k = 1, []
                listsData.add(new int[0]);
            } else if (test == 3) {
                // TUF Example 4: k = 3, [], [-5, 0, 7], []
                listsData.add(new int[0]);
                listsData.add(new int[]{-5, 0, 7});
                listsData.add(new int[0]);
            } else if (test == 4) {
                // Single element positive
                listsData.add(new int[]{42});
            } else if (test == 5) {
                // Single element minimum constraint
                listsData.add(new int[]{-1000000000});
            } else if (test == 6) {
                // k = 5, all empty lists
                for (int i = 0; i < 5; i++) listsData.add(new int[0]);
            } else if (test == 7) {
                // k = 2, [10], [5]
                listsData.add(new int[]{10});
                listsData.add(new int[]{5});
            } else if (test == 8) {
                // k = 4, identical elements
                for (int i = 0; i < 4; i++) {
                    listsData.add(new int[]{7, 7, 7});
                }
            } else if (test == 9) {
                // k = 3, negative elements
                listsData.add(new int[]{-20, -10});
                listsData.add(new int[]{-15, -5});
                listsData.add(new int[]{-25, -1});
            } else if (test < 30) {
                // Small cases: k in [2, 10], N in [5, 50]
                int k = 2 + rng.nextInt(9);
                int totalN = 5 + rng.nextInt(45);
                int remN = totalN;
                for (int i = 0; i < k; i++) {
                    int len;
                    if (i == k - 1) {
                        len = remN;
                    } else {
                        len = remN > 0 ? rng.nextInt(Math.min(remN + 1, 15)) : 0;
                        remN -= len;
                    }
                    listsData.add(generateSortedArray(len, rng, -100, 100));
                }
            } else if (test < 60) {
                // Medium cases: k in [10, 100], N in [100, 2000]
                int k = 10 + rng.nextInt(90);
                int totalN = 100 + (test - 30) * 60;
                int remN = totalN;
                for (int i = 0; i < k; i++) {
                    int len;
                    if (i == k - 1) {
                        len = remN;
                    } else {
                        int avg = Math.max(1, remN / (k - i));
                        len = remN > 0 ? rng.nextInt(Math.min(remN + 1, avg * 2 + 1)) : 0;
                        remN -= len;
                    }
                    listsData.add(generateSortedArray(len, rng, -100000, 100000));
                }
            } else if (test < 80) {
                // Large cases: k in [50, 500], N in [5000, 30000]
                int k = 50 + rng.nextInt(450);
                int totalN = 5000 + (test - 60) * 1250;
                int remN = totalN;
                for (int i = 0; i < k; i++) {
                    int len;
                    if (i == k - 1) {
                        len = remN;
                    } else {
                        int avg = Math.max(1, remN / (k - i));
                        len = remN > 0 ? rng.nextInt(Math.min(remN + 1, avg * 2 + 1)) : 0;
                        remN -= len;
                    }
                    listsData.add(generateSortedArray(len, rng, -1000000000, 1000000000));
                }
            } else {
                // Max constraint cases: N in [50000, 100000], k up to 10000
                int k;
                int totalN;

                if (test == 80) {
                    // One giant list
                    k = 1;
                    totalN = 100000;
                } else if (test == 81) {
                    // Two large lists
                    k = 2;
                    totalN = 100000;
                } else if (test == 82) {
                    k = 10;
                    totalN = 100000;
                } else if (test == 83) {
                    k = 50;
                    totalN = 100000;
                } else if (test == 84) {
                    k = 100;
                    totalN = 100000;
                } else if (test == 85) {
                    k = 500;
                    totalN = 100000;
                } else if (test == 86) {
                    k = 1000;
                    totalN = 100000;
                } else if (test == 87) {
                    k = 5000;
                    totalN = 100000;
                } else if (test == 88) {
                    // k = 10000, each list of size 1 (N = 10000)
                    k = 10000;
                    totalN = 10000;
                } else if (test == 89) {
                    // Max K = 10000 and Max N = 100000
                    k = 10000;
                    totalN = 100000;
                } else if (test == 90) {
                    // k = 5000, 2000 empty lists, 3000 non-empty
                    k = 5000;
                    totalN = 75000;
                } else if (test == 91) {
                    // Skewed lengths: 1 list gets 80%, remaining 99 lists get 20%
                    k = 100;
                    totalN = 80000;
                } else if (test == 92) {
                    // Heavy duplicates
                    k = 200;
                    totalN = 90000;
                } else if (test == 93) {
                    // Extreme range
                    k = 500;
                    totalN = 95000;
                } else if (test == 94) {
                    // All negative
                    k = 1000;
                    totalN = 100000;
                } else if (test == 95) {
                    k = 2000;
                    totalN = 100000;
                } else if (test == 96) {
                    k = 5000;
                    totalN = 100000;
                } else if (test == 97) {
                    k = 10000;
                    totalN = 100000;
                } else if (test == 98) {
                    k = 10000;
                    totalN = 100000;
                } else {
                    // test == 99: Full scale max test
                    k = 10000;
                    totalN = 100000;
                }

                int remN = totalN;
                int minV = -1000000000;
                int maxV = 1000000000;

                if (test == 92 || test == 96) {
                    minV = -10;
                    maxV = 10;
                } else if (test == 94) {
                    minV = -1000000000;
                    maxV = -1;
                }

                if (test == 88) {
                    for (int i = 0; i < k; i++) {
                        listsData.add(generateSortedArray(1, rng, minV, maxV));
                    }
                } else if (test == 90) {
                    // 2000 empty, 3000 non-empty
                    int active = 3000;
                    int remActive = totalN;
                    for (int i = 0; i < k; i++) {
                        if (i < 2000) {
                            listsData.add(new int[0]);
                        } else {
                            int idx = i - 2000;
                            int len = (idx == active - 1) ? remActive : (remActive / (active - idx));
                            remActive -= len;
                            listsData.add(generateSortedArray(len, rng, minV, maxV));
                        }
                    }
                } else if (test == 91) {
                    // Skewed: 1 list of 80%, remaining share 20%
                    int firstLen = (int) (totalN * 0.8);
                    listsData.add(generateSortedArray(firstLen, rng, minV, maxV));
                    remN -= firstLen;
                    for (int i = 1; i < k; i++) {
                        int len = (i == k - 1) ? remN : (remN / (k - i));
                        remN -= len;
                        listsData.add(generateSortedArray(len, rng, minV, maxV));
                    }
                } else {
                    for (int i = 0; i < k; i++) {
                        int len;
                        if (i == k - 1) {
                            len = remN;
                        } else {
                            int avg = Math.max(1, remN / (k - i));
                            len = remN > 0 ? rng.nextInt(Math.min(remN + 1, avg * 2 + 1)) : 0;
                            remN -= len;
                        }
                        listsData.add(generateSortedArray(len, rng, minV, maxV));
                    }
                }
            }

            String inFileName = String.format("input%02d.txt", test);
            String outFileName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inFileName);
            File outFile = new File(outDir, outFileName);

            writeTestCase(inFile, outFile, listsData);
        }

        System.out.println("Generated 100 testcases successfully.");
    }
}
