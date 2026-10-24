public class Solution {

    public long countValidSubstrings(String word) {

        long[] seen = new long[1 << 10];
        seen[0] = 1;

        int mask = 0;
        long total = 0;

        for (int i = 0; i < word.length(); i++) {

            mask ^= 1 << (word.charAt(i) - (char) 97);

            total += seen[mask];

            for (int bit = 0; bit < 10; bit++) {
                total += seen[mask ^ (1 << bit)];
            }

            seen[mask]++;
        }

        return total;
    }

}
