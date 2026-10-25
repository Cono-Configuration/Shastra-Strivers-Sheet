import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static int[] topKFrequent(int[] nums, int k) {
        int n = nums.length;
        HashMap<Integer, Integer> freqMap = new HashMap<>(n * 2);
        for (int x : nums) {
            freqMap.put(x, freqMap.getOrDefault(x, 0) + 1);
        }

        List<List<Integer>> buckets = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            buckets.add(null);
        }

        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            int val = entry.getKey();
            int freq = entry.getValue();
            if (buckets.get(freq) == null) {
                buckets.set(freq, new ArrayList<>());
            }
            buckets.get(freq).add(val);
        }

        int[] result = new int[k];
        int count = 0;

        for (int f = n; f >= 1 && count < k; f--) {
            List<Integer> bucket = buckets.get(f);
            if (bucket != null) {
                Collections.sort(bucket);
                for (int val : bucket) {
                    result[count++] = val;
                    if (count == k) break;
                }
            }
        }

        return result;
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
            int k = scanner.nextInt();

            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = scanner.nextInt();
            }

            int[] result = topKFrequent(nums, k);

            for (int i = 0; i < k; i++) {
                if (i > 0) writer.print(" ");
                writer.print(result[i]);
            }
            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
