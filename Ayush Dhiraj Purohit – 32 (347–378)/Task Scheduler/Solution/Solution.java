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

    public static int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        int maxFreq = 0;

        for (char c : tasks) {
            freq[c - 'A']++;
            if (freq[c - 'A'] > maxFreq) {
                maxFreq = freq[c - 'A'];
            }
        }

        int countMax = 0;
        for (int i = 0; i < 26; i++) {
            if (freq[i] == maxFreq) {
                countMax++;
            }
        }

        int minIntervals = (maxFreq - 1) * (n + 1) + countMax;
        return Math.max(tasks.length, minIntervals);
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

            int totalTasks = Integer.parseInt(nStr);
            int cooldown = scanner.nextInt();

            char[] tasks = new char[totalTasks];
            int count = 0;
            while (count < totalTasks) {
                String token = scanner.nextToken();
                if (token == null) break;
                for (int i = 0; i < token.length() && count < totalTasks; i++) {
                    char c = token.charAt(i);
                    if (c >= 'A' && c <= 'Z') {
                        tasks[count++] = c;
                    }
                }
            }

            int result = leastInterval(tasks, cooldown);

            writer.println(result);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
