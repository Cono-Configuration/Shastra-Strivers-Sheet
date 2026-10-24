public class Solution {

    public boolean canReachTarget(int[] values, int target) {

        boolean[] reachable = new boolean[target + 1];
        reachable[0] = true;

        for (int value : values) {

            for (int sum = target; sum >= value; sum--) {
                if (reachable[sum - value]) {
                    reachable[sum] = true;
                }
            }
        }

        return reachable[target];
    }

}
