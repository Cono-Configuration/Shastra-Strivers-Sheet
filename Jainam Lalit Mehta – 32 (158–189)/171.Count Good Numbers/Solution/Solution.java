public class Solution {

    private static final long MOD = 1_000_000_007L;

    public int countValidStrings(long length) {

        long evenSlots = (length + 1) / 2;
        long oddSlots = length / 2;

        long result = power(5, evenSlots) * power(4, oddSlots) % MOD;

        return (int) result;
    }

    private long power(long base, long exp) {

        long result = 1;
        base %= MOD;

        while (exp > 0) {

            if ((exp & 1) == 1) {
                result = result * base % MOD;
            }

            base = base * base % MOD;
            exp >>= 1;
        }

        return result;
    }

}
