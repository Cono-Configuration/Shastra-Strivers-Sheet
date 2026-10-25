import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class gen {

    static boolean isNStraightHand(int[] hand, int groupSize) {
        int n = hand.length;
        if (n % groupSize != 0) return false;
        if (groupSize == 1) return true;

        HashMap<Integer, Integer> count = new HashMap<>(n * 2);
        for (int card : hand) {
            count.put(card, count.getOrDefault(card, 0) + 1);
        }

        int[] copy = hand.clone();
        shuffle(copy);
        Arrays.sort(copy);

        for (int card : copy) {
            int c = count.get(card);
            if (c == 0) continue;

            for (int i = 0; i < groupSize; i++) {
                int nextCard = card + i;
                int available = count.getOrDefault(nextCard, 0);
                if (available < c) return false;
                count.put(nextCard, available - c);
            }
        }

        return true;
    }

    static boolean isNStraightHandHeap(int[] hand, int groupSize) {
        int n = hand.length;
        if (n % groupSize != 0) return false;
        if (groupSize == 1) return true;

        HashMap<Integer, Integer> count = new HashMap<>(n * 2);
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int card : hand) {
            count.put(card, count.getOrDefault(card, 0) + 1);
        }

        for (int key : count.keySet()) {
            pq.add(key);
        }

        while (!pq.isEmpty()) {
            int minCard = pq.peek();
            int c = count.get(minCard);
            if (c == 0) {
                pq.poll();
                continue;
            }

            for (int i = 0; i < groupSize; i++) {
                int nextCard = minCard + i;
                int available = count.getOrDefault(nextCard, 0);
                if (available < c) return false;
                count.put(nextCard, available - c);
            }
            pq.poll();
        }

        return true;
    }

    static void shuffle(int[] arr) {
        Random rng = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    static void writeTestCase(File inFile, File outFile, int[] hand, int groupSize) throws IOException {
        // Write input
        try (PrintWriter inPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(inFile)))) {
            inPw.println(hand.length + " " + groupSize);
            for (int i = 0; i < hand.length; i++) {
                if (i > 0) inPw.print(" ");
                inPw.print(hand[i]);
            }
            inPw.println();
            inPw.flush();
        }

        // Solve and dual verify
        boolean resSort = isNStraightHand(hand, groupSize);
        boolean resHeap = isNStraightHandHeap(hand, groupSize);

        if (resSort != resHeap) {
            throw new IllegalStateException("Verification mismatch: resSort (" + resSort + ") != resHeap (" + resHeap + ")");
        }

        // Write output
        try (PrintWriter outPw = new PrintWriter(new BufferedOutputStream(new FileOutputStream(outFile)))) {
            outPw.println(resSort ? "true" : "false");
            outPw.flush();
        }
    }

    static int[] generateValidHand(int numGroups, int groupSize, Random rng, int minVal, int maxVal) {
        int[] hand = new int[numGroups * groupSize];
        int idx = 0;
        int maxStart = maxVal - groupSize;
        long range = (long) maxStart - minVal + 1;

        for (int g = 0; g < numGroups; g++) {
            int start = minVal + (int) (Math.abs(rng.nextLong()) % Math.max(1, range));
            for (int i = 0; i < groupSize; i++) {
                hand[idx++] = start + i;
            }
        }
        shuffle(hand);
        return hand;
    }

    public static void main(String[] args) throws IOException {
        File cur = new File(".");
        File baseDir;
        if (new File(cur, "Testcases").exists() || new File(cur, "Generator").exists() || new File(cur, "Problem.md").exists()) {
            baseDir = cur;
        } else if (new File(cur, "../Testcases").exists() || new File(cur, "../Problem.md").exists()) {
            baseDir = new File(cur, "..");
        } else if (new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Hand of Straights").exists()) {
            baseDir = new File("Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Hand of Straights");
        } else {
            baseDir = new File("E:/Shastra Striver/Shastra-Strivers-Sheet/Ayush Dhiraj Purohit \u2013 32 (347\u2013378)/Hand of Straights");
        }

        File inDir = new File(baseDir, "Testcases/Input");
        File outDir = new File(baseDir, "Testcases/Output");

        if (!inDir.exists()) inDir.mkdirs();
        if (!outDir.exists()) outDir.mkdirs();

        Random rng = new Random(20261025);

        for (int test = 0; test < 100; test++) {
            int[] hand;
            int groupSize;

            if (test == 0) {
                // TUF Example 1
                hand = new int[]{1, 2, 3, 6, 2, 3, 4, 7, 8};
                groupSize = 3;
            } else if (test == 1) {
                // TUF Example 2
                hand = new int[]{1, 2, 3, 4, 5};
                groupSize = 4;
            } else if (test == 2) {
                // TUF Example 3
                hand = new int[]{1, 2, 3, 4, 2, 3, 4, 5};
                groupSize = 4;
            } else if (test == 3) {
                // TUF Example 4
                hand = new int[]{1, 1, 2, 2, 3, 3};
                groupSize = 3;
            } else if (test == 4) {
                // Single card
                hand = new int[]{42};
                groupSize = 1;
            } else if (test == 5) {
                // Single card extreme
                hand = new int[]{1000000000};
                groupSize = 1;
            } else if (test == 6) {
                // N=3, groupSize=3, gap
                hand = new int[]{1, 2, 4};
                groupSize = 3;
            } else if (test == 7) {
                // N=3, groupSize=3, all same
                hand = new int[]{5, 5, 5};
                groupSize = 3;
            } else if (test == 8) {
                // N=4, groupSize=2, two identical pairs
                hand = new int[]{10, 10, 11, 11};
                groupSize = 2;
            } else if (test == 9) {
                // N=6, groupSize=2, large disjoint numbers
                hand = new int[]{100, 101, 50000, 50001, 999999998, 999999999};
                groupSize = 2;
            } else if (test < 30) {
                // Small cases: N in [4, 50], groupSize in [1, 5]
                groupSize = 1 + rng.nextInt(5);
                int numGroups = 1 + (test - 10);
                if (test % 3 == 0) {
                    // Valid
                    hand = generateValidHand(numGroups, groupSize, rng, 0, 50);
                } else if (test % 3 == 1) {
                    // Invalid by perturbation
                    hand = generateValidHand(numGroups, groupSize, rng, 0, 50);
                    if (groupSize > 1) {
                        hand[rng.nextInt(hand.length)] += 2;
                    }
                } else {
                    // Invalid by size
                    hand = generateValidHand(numGroups, groupSize, rng, 0, 50);
                    if (hand.length > 1 && groupSize > 1) {
                        int[] shorter = new int[hand.length - 1];
                        System.arraycopy(hand, 0, shorter, 0, shorter.length);
                        hand = shorter;
                    }
                }
            } else if (test < 60) {
                // Medium cases: N in [60, 2000], groupSize in [2, 20]
                groupSize = 2 + rng.nextInt(15);
                int numGroups = (100 + (test - 30) * 60) / groupSize;
                if (numGroups < 1) numGroups = 1;

                if (test % 2 == 0) {
                    hand = generateValidHand(numGroups, groupSize, rng, 0, 1000000);
                } else {
                    hand = generateValidHand(numGroups, groupSize, rng, 0, 1000000);
                    hand[rng.nextInt(hand.length)] += 3;
                }
            } else if (test < 80) {
                // Large cases: N in [5000, 30000], groupSize in [2, 100]
                groupSize = 2 + rng.nextInt(30);
                int targetN = 5000 + (test - 60) * 1250;
                int numGroups = targetN / groupSize;
                if (numGroups < 1) numGroups = 1;

                if (test % 2 == 0) {
                    hand = generateValidHand(numGroups, groupSize, rng, 0, 1000000000);
                } else {
                    hand = generateValidHand(numGroups, groupSize, rng, 0, 1000000000);
                    hand[rng.nextInt(hand.length)] += 5;
                }
            } else {
                // Max constraint cases: N in [50000, 100000]
                int targetN;
                if (test < 88) {
                    targetN = 50000 + (test - 80) * 6000;
                } else {
                    targetN = 100000;
                }

                if (test == 80) {
                    groupSize = 1;
                    hand = new int[targetN];
                    for (int i = 0; i < targetN; i++) hand[i] = rng.nextInt(1000000);
                } else if (test == 81) {
                    // Single massive valid group
                    groupSize = 50000;
                    hand = generateValidHand(1, groupSize, rng, 100, 500000000);
                } else if (test == 87) {
                    // Size indivisible
                    groupSize = 10;
                    hand = new int[99999];
                    for (int i = 0; i < hand.length; i++) hand[i] = i;
                } else if (test == 88) {
                    // 50,000 pairs of consecutive cards
                    groupSize = 2;
                    hand = generateValidHand(50000, groupSize, rng, 0, 900000000);
                } else if (test == 89) {
                    // 50,000 pairs with gap
                    groupSize = 2;
                    hand = generateValidHand(50000, groupSize, rng, 0, 900000000);
                    hand[0] = 999999999;
                } else if (test == 90) {
                    groupSize = 5;
                    hand = generateValidHand(20000, groupSize, rng, 0, 900000000);
                } else if (test == 91) {
                    groupSize = 5;
                    hand = generateValidHand(20000, groupSize, rng, 0, 900000000);
                    hand[100] += 2;
                } else if (test == 92) {
                    groupSize = 10;
                    hand = generateValidHand(10000, groupSize, rng, 0, 900000000);
                } else if (test == 93) {
                    groupSize = 10;
                    hand = generateValidHand(10000, groupSize, rng, 0, 900000000);
                    hand[500] += 4;
                } else if (test == 94) {
                    groupSize = 20;
                    hand = generateValidHand(5000, groupSize, rng, 0, 900000000);
                } else if (test == 95) {
                    groupSize = 50;
                    hand = generateValidHand(2000, groupSize, rng, 0, 900000000);
                } else if (test == 96) {
                    groupSize = 100;
                    hand = generateValidHand(1000, groupSize, rng, 0, 900000000);
                } else if (test == 97) {
                    groupSize = 500;
                    hand = generateValidHand(200, groupSize, rng, 0, 900000000);
                } else if (test == 98) {
                    groupSize = 1000;
                    hand = generateValidHand(100, groupSize, rng, 0, 900000000);
                } else if (test == 99) {
                    groupSize = 100;
                    hand = generateValidHand(1000, groupSize, rng, 0, 900000000);
                    hand[42] += 2; // invalid gap
                } else {
                    groupSize = 4;
                    int numG = targetN / groupSize;
                    hand = generateValidHand(numG, groupSize, rng, 0, 1000000000);
                }
            }

            String inFileName = String.format("input%02d.txt", test);
            String outFileName = String.format("output%02d.txt", test);

            File inFile = new File(inDir, inFileName);
            File outFile = new File(outDir, outFileName);

            writeTestCase(inFile, outFile, hand, groupSize);
        }

        System.out.println("Generated 100 testcases successfully.");
    }
}
