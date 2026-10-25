import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class gen {

    static class Operation {
        String type;
        int arg1;
        int arg2;

        Operation(String type) {
            this.type = type;
        }

        Operation(String type, int arg1) {
            this.type = type;
            this.arg1 = arg1;
        }

        Operation(String type, int arg1, int arg2) {
            this.type = type;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }
    }

    static class MaxHeapSimulator {
        private int[] heap;
        private int size;

        MaxHeapSimulator() {
            this.heap = new int[16];
            this.size = 0;
        }

        void initializeHeap() {
            this.size = 0;
        }

        void insert(int key) {
            ensureCapacity(size + 1);
            heap[size] = key;
            siftUp(size);
            size++;
        }

        void changeKey(int index, int newVal) {
            if (index < 0 || index >= size) {
                return;
            }
            int oldVal = heap[index];
            heap[index] = newVal;
            if (newVal > oldVal) {
                siftUp(index);
            } else if (newVal < oldVal) {
                siftDown(index);
            }
        }

        void extractMax() {
            if (size == 0) {
                return;
            }
            heap[0] = heap[size - 1];
            size--;
            if (size > 0) {
                siftDown(0);
            }
        }

        int isEmpty() {
            return size == 0 ? 1 : 0;
        }

        int getMax() {
            if (size == 0) {
                return -1;
            }
            return heap[0];
        }

        int heapSize() {
            return size;
        }

        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap[i] > heap[parent]) {
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
                int largest = i;

                if (heap[left] > heap[largest]) {
                    largest = left;
                }
                if (right < size && heap[right] > heap[largest]) {
                    largest = right;
                }

                if (largest != i) {
                    swap(i, largest);
                    i = largest;
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

    static List<String> solve(List<Operation> ops) {
        MaxHeapSimulator heap = new MaxHeapSimulator();
        List<String> results = new ArrayList<>(ops.size());

        for (Operation op : ops) {
            if (op.type.equalsIgnoreCase("initializeHeap")) {
                heap.initializeHeap();
                results.add("null");
            } else if (op.type.equalsIgnoreCase("insert")) {
                heap.insert(op.arg1);
                results.add("null");
            } else if (op.type.equalsIgnoreCase("changeKey")) {
                heap.changeKey(op.arg1, op.arg2);
                results.add("null");
            } else if (op.type.equalsIgnoreCase("extractMax")) {
                heap.extractMax();
                results.add("null");
            } else if (op.type.equalsIgnoreCase("isEmpty")) {
                results.add(String.valueOf(heap.isEmpty()));
            } else if (op.type.equalsIgnoreCase("getMax")) {
                results.add(String.valueOf(heap.getMax()));
            } else if (op.type.equalsIgnoreCase("heapSize")) {
                results.add(String.valueOf(heap.heapSize()));
            }
        }
        return results;
    }

    static void writeTestCase(File inFile, File outFile, List<Operation> ops) throws IOException {
        List<String> expected = solve(ops);

        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(ops.size());
            for (Operation op : ops) {
                if (op.type.equalsIgnoreCase("insert")) {
                    inPw.println("insert " + op.arg1);
                } else if (op.type.equalsIgnoreCase("changeKey")) {
                    inPw.println("changeKey " + op.arg1 + " " + op.arg2);
                } else {
                    inPw.println(op.type);
                }
            }
            inPw.flush();
        }

        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            for (int i = 0; i < expected.size(); i++) {
                if (i > 0) outPw.print(" ");
                outPw.print(expected.get(i));
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
        } else if (new File("Ayush Dhiraj Purohit – 32 (347–378)/Implement Max Heap").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit – 32 (347–378)/Implement Max Heap");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit – 32 (347–378)/Implement Max Heap");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(42);

        for (int test = 0; test < 100; test++) {
            List<Operation> ops = new ArrayList<>();
            int currentSize = 0;

            if (test == 0) {
                // Sample 1 from TUF
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("insert", 4));
                ops.add(new Operation("insert", 1));
                ops.add(new Operation("insert", 10));
                ops.add(new Operation("getMax"));
                ops.add(new Operation("heapSize"));
                ops.add(new Operation("isEmpty"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("changeKey", 0, 16));
                ops.add(new Operation("getMax"));
            } else if (test == 1) {
                // Sample 2 from TUF
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("insert", 4));
                ops.add(new Operation("insert", 1));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("getMax"));
                ops.add(new Operation("insert", 1));
                ops.add(new Operation("heapSize"));
                ops.add(new Operation("isEmpty"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("changeKey", 0, 2));
                ops.add(new Operation("getMax"));
            } else if (test == 2) {
                // Minimal edge: single element lifecycle
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("isEmpty"));
                ops.add(new Operation("insert", 42));
                ops.add(new Operation("getMax"));
                ops.add(new Operation("heapSize"));
                ops.add(new Operation("isEmpty"));
            } else if (test == 3) {
                // Negative boundary
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("insert", -1000000000));
                ops.add(new Operation("getMax"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("isEmpty"));
            } else if (test == 4) {
                // Positive boundary & changeKey
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("insert", 1000000000));
                ops.add(new Operation("getMax"));
                ops.add(new Operation("changeKey", 0, -1000000000));
                ops.add(new Operation("getMax"));
            } else if (test == 5) {
                // Two elements ordering check
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("insert", 2));
                ops.add(new Operation("insert", 5));
                ops.add(new Operation("getMax"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("getMax"));
            } else if (test == 6) {
                // Two duplicate elements
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("insert", 7));
                ops.add(new Operation("insert", 7));
                ops.add(new Operation("heapSize"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("getMax"));
                ops.add(new Operation("heapSize"));
            } else if (test == 7) {
                // Initialize heap resetting populated heap
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("insert", 10));
                ops.add(new Operation("insert", 20));
                ops.add(new Operation("insert", 30));
                ops.add(new Operation("heapSize"));
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("heapSize"));
                ops.add(new Operation("isEmpty"));
                ops.add(new Operation("insert", 99));
                ops.add(new Operation("getMax"));
            } else if (test == 8) {
                // Multiple changeKey calls on small heap
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("insert", 50));
                ops.add(new Operation("insert", 40));
                ops.add(new Operation("insert", 30));
                ops.add(new Operation("insert", 20));
                ops.add(new Operation("changeKey", 0, 10)); // root decrease -> siftDown
                ops.add(new Operation("getMax"));
                ops.add(new Operation("changeKey", 3, 60)); // leaf increase -> siftUp
                ops.add(new Operation("getMax"));
            } else if (test == 9) {
                // Strictly increasing insertions (maximum sift-up)
                ops.add(new Operation("initializeHeap"));
                for (int v = 1; v <= 10; v++) {
                    ops.add(new Operation("insert", v));
                }
                ops.add(new Operation("getMax"));
                ops.add(new Operation("heapSize"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("getMax"));
            } else if (test == 10) {
                // Strictly decreasing insertions (no sift-up)
                ops.add(new Operation("initializeHeap"));
                for (int v = 10; v >= 1; v--) {
                    ops.add(new Operation("insert", v));
                }
                ops.add(new Operation("getMax"));
                ops.add(new Operation("heapSize"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("getMax"));
            } else if (test == 11) {
                // Alternating negatives and positives
                ops.add(new Operation("initializeHeap"));
                int[] vals = {-50, 50, -40, 40, -30, 30, -20, 20};
                for (int v : vals) ops.add(new Operation("insert", v));
                ops.add(new Operation("getMax"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("getMax"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("getMax"));
            } else if (test == 12) {
                // Interleaved isEmpty and heapSize calls
                ops.add(new Operation("initializeHeap"));
                for (int v = 1; v <= 5; v++) {
                    ops.add(new Operation("isEmpty"));
                    ops.add(new Operation("heapSize"));
                    ops.add(new Operation("insert", v * 10));
                }
                ops.add(new Operation("getMax"));
            } else if (test == 13) {
                // Extractions until empty, then refill
                ops.add(new Operation("initializeHeap"));
                ops.add(new Operation("insert", 100));
                ops.add(new Operation("insert", 200));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("isEmpty"));
                ops.add(new Operation("insert", 300));
                ops.add(new Operation("getMax"));
            } else if (test == 14) {
                // All identical values
                ops.add(new Operation("initializeHeap"));
                for (int i = 0; i < 12; i++) {
                    ops.add(new Operation("insert", 77));
                }
                ops.add(new Operation("getMax"));
                ops.add(new Operation("heapSize"));
                ops.add(new Operation("extractMax"));
                ops.add(new Operation("getMax"));
            } else if (test >= 15 && test < 35) {
                // Mid-range random stress: Q between 30 and 200
                int targetQ = 30 + rng.nextInt(171);
                ops.add(new Operation("initializeHeap"));
                currentSize = 0;

                while (ops.size() < targetQ) {
                    int choice = rng.nextInt(10);
                    if (currentSize == 0 || choice < 4) {
                        int val = rng.nextInt(200001) - 100000;
                        ops.add(new Operation("insert", val));
                        currentSize++;
                    } else if (choice == 4 || choice == 5) {
                        ops.add(new Operation("extractMax"));
                        currentSize--;
                    } else if (choice == 6) {
                        ops.add(new Operation("getMax"));
                    } else if (choice == 7) {
                        ops.add(new Operation("heapSize"));
                    } else if (choice == 8) {
                        ops.add(new Operation("isEmpty"));
                    } else {
                        int idx = rng.nextInt(currentSize);
                        int newVal = rng.nextInt(200001) - 100000;
                        ops.add(new Operation("changeKey", idx, newVal));
                    }
                }
            } else if (test >= 35 && test < 70) {
                // Medium-to-large operations: Q between 500 and 5000
                int targetQ = 500 + rng.nextInt(4501);
                ops.add(new Operation("initializeHeap"));
                currentSize = 0;

                while (ops.size() < targetQ) {
                    if (currentSize < 10) {
                        int count = 10 + rng.nextInt(20);
                        for (int k = 0; k < count && ops.size() < targetQ; k++) {
                            int val = rng.nextInt(2000001) - 1000000;
                            ops.add(new Operation("insert", val));
                            currentSize++;
                        }
                    } else {
                        int r = rng.nextInt(100);
                        if (r < 35) {
                            int val = rng.nextInt(2000001) - 1000000;
                            ops.add(new Operation("insert", val));
                            currentSize++;
                        } else if (r < 60) {
                            ops.add(new Operation("extractMax"));
                            currentSize--;
                        } else if (r < 75) {
                            int idx = rng.nextInt(currentSize);
                            int newVal = rng.nextInt(2000001) - 1000000;
                            ops.add(new Operation("changeKey", idx, newVal));
                        } else if (r < 85) {
                            ops.add(new Operation("getMax"));
                        } else if (r < 95) {
                            ops.add(new Operation("heapSize"));
                        } else {
                            ops.add(new Operation("isEmpty"));
                        }
                    }
                }
            } else if (test >= 70 && test < 90) {
                // Deep heap stress: Q between 10000 and 40000
                int targetQ = 10000 + (test - 70) * 1500;
                ops.add(new Operation("initializeHeap"));
                currentSize = 0;

                if (test % 3 == 0) {
                    // Deep sift-up stress: insert strictly increasing numbers
                    int start = -100000;
                    for (int k = 0; k < targetQ / 2 && ops.size() < targetQ; k++) {
                        ops.add(new Operation("insert", start + k));
                        currentSize++;
                    }
                    while (ops.size() < targetQ) {
                        if (rng.nextBoolean() && currentSize > 0) {
                            ops.add(new Operation("extractMax"));
                            currentSize--;
                        } else {
                            ops.add(new Operation("getMax"));
                        }
                    }
                } else if (test % 3 == 1) {
                    // Heavy changeKey stress on a deep heap
                    for (int k = 0; k < 5000 && ops.size() < targetQ; k++) {
                        ops.add(new Operation("insert", rng.nextInt(20000001) - 10000000));
                        currentSize++;
                    }
                    while (ops.size() < targetQ) {
                        int r = rng.nextInt(10);
                        if (r < 5 && currentSize > 0) {
                            int idx = rng.nextInt(currentSize);
                            int newVal = rng.nextInt(20000001) - 10000000;
                            ops.add(new Operation("changeKey", idx, newVal));
                        } else if (r < 8 && currentSize > 0) {
                            ops.add(new Operation("getMax"));
                        } else {
                            ops.add(new Operation("heapSize"));
                        }
                    }
                } else {
                    // Large random churning
                    while (ops.size() < targetQ) {
                        if (currentSize == 0 || rng.nextInt(100) < 55) {
                            ops.add(new Operation("insert", rng.nextInt(20000001) - 10000000));
                            currentSize++;
                        } else {
                            ops.add(new Operation("extractMax"));
                            currentSize--;
                        }
                    }
                }
            } else {
                // Boundary maximum constraint stress: Q = 100000
                int targetQ = 100000;
                ops.add(new Operation("initializeHeap"));
                currentSize = 0;

                if (test == 90) {
                    // Maximum operations: 45,000 inserts then 55,000 extractions/queries
                    for (int k = 0; k < 45000; k++) {
                        ops.add(new Operation("insert", rng.nextInt(2000000001) - 1000000000));
                        currentSize++;
                    }
                    while (ops.size() < targetQ) {
                        if (currentSize > 0 && rng.nextInt(100) < 70) {
                            ops.add(new Operation("extractMax"));
                            currentSize--;
                        } else if (currentSize > 0) {
                            ops.add(new Operation("getMax"));
                        } else {
                            ops.add(new Operation("insert", rng.nextInt(2000000001) - 1000000000));
                            currentSize++;
                        }
                    }
                } else if (test == 91) {
                    // 50,000 strictly increasing inserts (worst case sift-up) followed by extracts
                    for (int k = 0; k < 50000 && ops.size() < targetQ; k++) {
                        ops.add(new Operation("insert", k));
                        currentSize++;
                    }
                    while (ops.size() < targetQ) {
                        if (currentSize > 0) {
                            ops.add(new Operation("extractMax"));
                            currentSize--;
                        } else {
                            ops.add(new Operation("isEmpty"));
                        }
                    }
                } else if (test == 92) {
                    // Identical elements stress
                    for (int k = 0; k < 20000; k++) {
                        ops.add(new Operation("insert", 0));
                        currentSize++;
                    }
                    while (ops.size() < targetQ) {
                        int r = rng.nextInt(10);
                        if (r < 4 && currentSize > 0) {
                            int idx = rng.nextInt(currentSize);
                            int newVal = rng.nextBoolean() ? -1000000000 : 1000000000;
                            ops.add(new Operation("changeKey", idx, newVal));
                        } else if (r < 7 && currentSize > 0) {
                            ops.add(new Operation("getMax"));
                        } else if (r < 9 && currentSize > 0) {
                            ops.add(new Operation("extractMax"));
                            currentSize--;
                        } else {
                            ops.add(new Operation("insert", 0));
                            currentSize++;
                        }
                    }
                } else if (test == 93) {
                    // Extreme boundaries alternating -10^9 and 10^9
                    while (ops.size() < targetQ) {
                        if (currentSize == 0 || rng.nextInt(100) < 55) {
                            int val = rng.nextBoolean() ? -1000000000 : 1000000000;
                            ops.add(new Operation("insert", val));
                            currentSize++;
                        } else {
                            ops.add(new Operation("extractMax"));
                            currentSize--;
                        }
                    }
                } else if (test == 94) {
                    // Deep sift-up via changeKey: change leaves to greater than root
                    for (int k = 0; k < 10000; k++) {
                        ops.add(new Operation("insert", -1000000 + k * 10));
                        currentSize++;
                    }
                    while (ops.size() < targetQ) {
                        if (currentSize > 1) {
                            int idx = currentSize - 1 - rng.nextInt(Math.min(currentSize, 50));
                            ops.add(new Operation("changeKey", idx, rng.nextInt(1000000000)));
                            ops.add(new Operation("getMax"));
                        } else {
                            ops.add(new Operation("insert", 100));
                            currentSize++;
                        }
                    }
                } else if (test == 95) {
                    // Repeated initializeHeap cycles
                    while (ops.size() < targetQ) {
                        ops.add(new Operation("initializeHeap"));
                        currentSize = 0;
                        int burst = Math.min(targetQ - ops.size(), 2000);
                        for (int k = 0; k < burst - 5 && ops.size() < targetQ; k++) {
                            ops.add(new Operation("insert", rng.nextInt(2000000001) - 1000000000));
                            currentSize++;
                        }
                        if (currentSize > 0 && ops.size() < targetQ) ops.add(new Operation("getMax"));
                        if (currentSize > 0 && ops.size() < targetQ) ops.add(new Operation("heapSize"));
                    }
                } else if (test == 96) {
                    // Extreme negative numbers domain [-1000000000, -999999900]
                    while (ops.size() < targetQ) {
                        if (currentSize == 0 || rng.nextInt(100) < 55) {
                            int val = -1000000000 + rng.nextInt(101);
                            ops.add(new Operation("insert", val));
                            currentSize++;
                        } else {
                            ops.add(new Operation("extractMax"));
                            currentSize--;
                        }
                    }
                } else if (test == 97) {
                    // Extreme positive numbers domain [999999900, 1000000000]
                    while (ops.size() < targetQ) {
                        if (currentSize == 0 || rng.nextInt(100) < 55) {
                            int val = 999999900 + rng.nextInt(101);
                            ops.add(new Operation("insert", val));
                            currentSize++;
                        } else {
                            ops.add(new Operation("extractMax"));
                            currentSize--;
                        }
                    }
                } else if (test == 98) {
                    // Priority Queue workload simulation: heavy inserts and extractions
                    while (ops.size() < targetQ) {
                        int r = rng.nextInt(100);
                        if (currentSize == 0 || r < 48) {
                            ops.add(new Operation("insert", rng.nextInt(2000000001) - 1000000000));
                            currentSize++;
                        } else if (r < 85) {
                            ops.add(new Operation("extractMax"));
                            currentSize--;
                        } else if (r < 92) {
                            ops.add(new Operation("getMax"));
                        } else if (r < 96) {
                            ops.add(new Operation("heapSize"));
                        } else {
                            ops.add(new Operation("isEmpty"));
                        }
                    }
                } else {
                    // Test 99: Comprehensive multi-pattern test with all 7 operations and Q = 100000
                    while (ops.size() < targetQ) {
                        if (currentSize == 0) {
                            ops.add(new Operation("insert", rng.nextInt(2000000001) - 1000000000));
                            currentSize++;
                        } else {
                            int r = rng.nextInt(100);
                            if (r < 35) {
                                ops.add(new Operation("insert", rng.nextInt(2000000001) - 1000000000));
                                currentSize++;
                            } else if (r < 55) {
                                ops.add(new Operation("extractMax"));
                                currentSize--;
                            } else if (r < 75) {
                                int idx = rng.nextInt(currentSize);
                                int newVal = rng.nextInt(2000000001) - 1000000000;
                                ops.add(new Operation("changeKey", idx, newVal));
                            } else if (r < 85) {
                                ops.add(new Operation("getMax"));
                            } else if (r < 93) {
                                ops.add(new Operation("heapSize"));
                            } else {
                                ops.add(new Operation("isEmpty"));
                            }
                        }
                    }
                }
            }

            // Trim to exactly target if slightly exceeded in loops
            if (ops.size() > 100000) {
                ops = ops.subList(0, 100000);
            }

            String inName = String.format("input%02d.txt", test);
            String outName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inName);
            File outFile = new File(outDir, outName);

            writeTestCase(inFile, outFile, ops);
        }

        System.out.println("Generated exactly 100 test cases successfully.");
    }
}
