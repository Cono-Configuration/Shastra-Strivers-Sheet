import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.util.Arrays;
import java.util.HashMap;
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

    public static boolean isNStraightHand(int[] hand, int groupSize) {
        int n = hand.length;
        if (n % groupSize != 0) {
            return false;
        }
        if (groupSize == 1) {
            return true;
        }

        HashMap<Integer, Integer> count = new HashMap<>(n * 2);
        for (int card : hand) {
            count.put(card, count.getOrDefault(card, 0) + 1);
        }

        shuffle(hand);
        Arrays.sort(hand);

        for (int card : hand) {
            int c = count.get(card);
            if (c == 0) {
                continue;
            }

            for (int i = 0; i < groupSize; i++) {
                int nextCard = card + i;
                int available = count.getOrDefault(nextCard, 0);
                if (available < c) {
                    return false;
                }
                count.put(nextCard, available - c);
            }
        }

        return true;
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
            int groupSize = scanner.nextInt();

            int[] hand = new int[n];
            for (int i = 0; i < n; i++) {
                hand[i] = scanner.nextInt();
            }

            boolean result = isNStraightHand(hand, groupSize);

            writer.println(result ? "true" : "false");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
