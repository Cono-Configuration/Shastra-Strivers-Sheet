public class Solution {

    public long countSumSubsequences(int[] values, int target) {

        // ways[s] = number of subsequences (chosen by index) whose sum is s.
        // The empty subsequence is counted at sum 0 and is never the answer, since target >= 1.
        long[] ways = new long[target + 1];
        ways[0] = 1;

        for (int value : values) {
            // Go downward so each element is used at most once.
            for (int s = target; s >= value; s--) {
                ways[s] += ways[s - value];
            }
        }

        return ways[target];
    }

}
