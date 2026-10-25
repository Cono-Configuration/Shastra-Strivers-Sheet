import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Random;

public class gen {

    // Optimal Two-Heap MedianFinder
    static class OptimalMedianFinder {
        private final PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        private final PriorityQueue<Integer> minHeap = new PriorityQueue<>();

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

    // Brute-force verification using ArrayList and Collections.sort
    static class BruteForceMedianFinder {
        private final List<Integer> list = new ArrayList<>();

        public void addNum(int num) {
            list.add(num);
        }

        public double findMedian() {
            List<Integer> copy = new ArrayList<>(list);
            Collections.sort(copy);
            int n = copy.size();
            if (n % 2 == 1) {
                return copy.get(n / 2);
            }
            long sum = (long) copy.get(n / 2 - 1) + copy.get(n / 2);
            double med = sum / 2.0;
            if (Math.abs(med) < 1e-9) {
                med = 0.0;
            }
            return med;
        }
    }

    // Binary search insertion verification
    static class BinarySearchMedianFinder {
        private final List<Integer> list = new ArrayList<>();

        public void addNum(int num) {
            int idx = Collections.binarySearch(list, num);
            if (idx < 0) {
                idx = -(idx + 1);
            }
            list.add(idx, num);
        }

        public double findMedian() {
            int n = list.size();
            if (n % 2 == 1) {
                return list.get(n / 2);
            }
            long sum = (long) list.get(n / 2 - 1) + list.get(n / 2);
            double med = sum / 2.0;
            if (Math.abs(med) < 1e-9) {
                med = 0.0;
            }
            return med;
        }
    }

    static class Operation {
        String type; // "addNum" or "findMedian"
        int val;

        Operation(String type, int val) {
            this.type = type;
            this.val = val;
        }

        Operation(String type) {
            this.type = type;
            this.val = 0;
        }
    }

    private static void writeTestCase(File inDir, File outDir, int testIdx, List<Operation> ops) throws IOException {
        String baseName = String.format("input%02d.txt", testIdx);
        String outName = String.format("output%02d.txt", testIdx);
        File inFile = new File(inDir, baseName);
        File outFile = new File(outDir, outName);

        OptimalMedianFinder opt = new OptimalMedianFinder();
        BruteForceMedianFinder brute = (ops.size() <= 2000) ? new BruteForceMedianFinder() : null;
        BinarySearchMedianFinder bs = (ops.size() <= 5000) ? new BinarySearchMedianFinder() : null;

        List<String> results = new ArrayList<>(ops.size());

        for (Operation op : ops) {
            if (op.type.equals("addNum")) {
                opt.addNum(op.val);
                if (brute != null) brute.addNum(op.val);
                if (bs != null) bs.addNum(op.val);
                results.add("null");
            } else {
                double medOpt = opt.findMedian();
                String resOpt = String.format(Locale.US, "%.1f", medOpt);

                if (brute != null) {
                    double medBrute = brute.findMedian();
                    String resBrute = String.format(Locale.US, "%.1f", medBrute);
                    if (!resOpt.equals(resBrute)) {
                        throw new RuntimeException("Mismatch with brute force on test " + testIdx + ": opt=" + resOpt + " brute=" + resBrute);
                    }
                }

                if (bs != null) {
                    double medBs = bs.findMedian();
                    String resBs = String.format(Locale.US, "%.1f", medBs);
                    if (!resOpt.equals(resBs)) {
                        throw new RuntimeException("Mismatch with binary search on test " + testIdx + ": opt=" + resOpt + " bs=" + resBs);
                    }
                }

                results.add(resOpt);
            }
        }

        // Write input
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(ops.size());
            for (Operation op : ops) {
                if (op.type.equals("addNum")) {
                    inPw.println("addNum " + op.val);
                } else {
                    inPw.println("findMedian");
                }
            }
            inPw.flush();
        }

        // Write output
        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            for (int i = 0; i < results.size(); i++) {
                if (i > 0) outPw.print(" ");
                outPw.print(results.get(i));
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
        } else if (new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Find Median from Data Stream").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Find Median from Data Stream");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Find Median from Data Stream");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(20261025);

        for (int test = 0; test < 100; test++) {
            List<Operation> ops = new ArrayList<>();

            if (test == 0) {
                // TUF Example 1
                ops.add(new Operation("addNum", 1));
                ops.add(new Operation("addNum", 2));
                ops.add(new Operation("findMedian"));
                ops.add(new Operation("addNum", 3));
                ops.add(new Operation("findMedian"));
            } else if (test == 1) {
                // TUF Example 2 (strictly negative)
                ops.add(new Operation("addNum", -1));
                ops.add(new Operation("findMedian"));
                ops.add(new Operation("addNum", -2));
                ops.add(new Operation("findMedian"));
                ops.add(new Operation("addNum", -3));
                ops.add(new Operation("findMedian"));
            } else if (test == 2) {
                // Example 3
                ops.add(new Operation("addNum", 10));
                ops.add(new Operation("addNum", 20));
                ops.add(new Operation("addNum", 30));
                ops.add(new Operation("findMedian"));
                ops.add(new Operation("addNum", 5));
                ops.add(new Operation("addNum", 15));
                ops.add(new Operation("findMedian"));
            } else if (test == 3) {
                // Example 4 (extreme values canceling out)
                ops.add(new Operation("addNum", 1000000000));
                ops.add(new Operation("addNum", -1000000000));
                ops.add(new Operation("findMedian"));
                ops.add(new Operation("addNum", 0));
            } else if (test == 4) {
                // Single element query
                ops.add(new Operation("addNum", 42));
                ops.add(new Operation("findMedian"));
            } else if (test == 5) {
                // All identical elements
                for (int i = 0; i < 15; i++) {
                    ops.add(new Operation("addNum", 7));
                    if (i % 2 == 1) ops.add(new Operation("findMedian"));
                }
            } else if (test == 6) {
                // Zero crossing with alternating positive and negative
                for (int i = 1; i <= 10; i++) {
                    ops.add(new Operation("addNum", i * 10));
                    ops.add(new Operation("addNum", -i * 10));
                    ops.add(new Operation("findMedian"));
                }
            } else if (test == 7) {
                // Strictly increasing stream
                for (int i = 1; i <= 25; i++) {
                    ops.add(new Operation("addNum", i));
                    ops.add(new Operation("findMedian"));
                }
            } else if (test == 8) {
                // Strictly decreasing stream
                for (int i = 25; i >= 1; i--) {
                    ops.add(new Operation("addNum", i));
                    ops.add(new Operation("findMedian"));
                }
            } else if (test == 9) {
                // Many additions then burst of medians
                for (int i = 0; i < 20; i++) {
                    ops.add(new Operation("addNum", rng.nextInt(100) - 50));
                }
                for (int i = 0; i < 10; i++) {
                    ops.add(new Operation("findMedian"));
                }
            } else if (test < 30) {
                // Small cases: Q in [10, 50]
                int q = 10 + rng.nextInt(41);
                int countAdded = 0;
                int mode = test % 5;

                for (int i = 0; i < q; i++) {
                    // Must have countAdded > 0 to findMedian
                    if (countAdded == 0 || (countAdded < q / 2 && rng.nextInt(3) != 0)) {
                        int val;
                        if (mode == 0) val = rng.nextInt(21) - 10;
                        else if (mode == 1) val = rng.nextInt(2) == 0 ? 5 : 10;
                        else if (mode == 2) val = (i + 1) * 3;
                        else if (mode == 3) val = -(i + 1) * 3;
                        else val = rng.nextInt(2000000001) - 1000000000;

                        ops.add(new Operation("addNum", val));
                        countAdded++;
                    } else {
                        ops.add(new Operation("findMedian"));
                    }
                }
            } else if (test < 60) {
                // Medium cases: Q in [100, 2000]
                int q = 100 + rng.nextInt(1901);
                int countAdded = 0;
                int mode = test % 5;

                for (int i = 0; i < q; i++) {
                    if (countAdded == 0 || rng.nextInt(10) < 6) {
                        int val;
                        if (mode == 0) {
                            val = rng.nextInt(20001) - 10000;
                        } else if (mode == 1) {
                            val = rng.nextInt(10); // Many duplicates
                        } else if (mode == 2) {
                            val = (rng.nextBoolean() ? 1000000000 - rng.nextInt(100) : -1000000000 + rng.nextInt(100));
                        } else if (mode == 3) {
                            val = (int) (rng.nextGaussian() * 1000); // Normal distribution
                        } else {
                            val = rng.nextInt(2000000001) - 1000000000;
                        }

                        ops.add(new Operation("addNum", val));
                        countAdded++;
                    } else {
                        ops.add(new Operation("findMedian"));
                    }
                }
            } else if (test < 80) {
                // Large cases: Q in [5000, 20000]
                int q = 5000 + rng.nextInt(15001);
                int countAdded = 0;
                int mode = test % 4;

                for (int i = 0; i < q; i++) {
                    if (countAdded == 0 || rng.nextInt(10) < 7) {
                        int val;
                        if (mode == 0) {
                            val = (int) (rng.nextLong() % 1000000001L);
                        } else if (mode == 1) {
                            val = rng.nextInt(50);
                        } else if (mode == 2) {
                            val = -1 - (int) (Math.abs(rng.nextLong()) % 1000000000L);
                        } else {
                            val = 1 + (int) (Math.abs(rng.nextLong()) % 1000000000L);
                        }

                        ops.add(new Operation("addNum", val));
                        countAdded++;
                    } else {
                        ops.add(new Operation("findMedian"));
                    }
                }
            } else {
                // Max constraint cases: Q in [50000, 100000]
                int q;
                if (test == 80) q = 50000;
                else if (test == 85) q = 75000;
                else if (test == 90 || test == 99) q = 100000;
                else q = 50000 + rng.nextInt(50001);

                int countAdded = 0;
                int mode = test % 5;

                for (int i = 0; i < q; i++) {
                    if (countAdded == 0 || rng.nextInt(10) < 7) {
                        int val;
                        if (mode == 0) {
                            // Uniform full-range
                            val = (int) (rng.nextLong() % 1000000001L);
                        } else if (mode == 1) {
                            // Identical constant stream
                            val = 555555555;
                        } else if (mode == 2) {
                            // Monotonically increasing
                            val = i * 2;
                        } else if (mode == 3) {
                            // Monotonically decreasing
                            val = (q - i) * 2;
                        } else {
                            // Bipolar extremes
                            val = rng.nextBoolean() ? 1000000000 : -1000000000;
                        }

                        ops.add(new Operation("addNum", val));
                        countAdded++;
                    } else {
                        ops.add(new Operation("findMedian"));
                    }
                }
            }

            writeTestCase(inDir, outDir, test, ops);
        }

        System.out.println("Generated 100 test cases successfully.");
    }
}
