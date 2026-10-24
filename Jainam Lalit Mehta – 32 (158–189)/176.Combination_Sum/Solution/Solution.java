import java.util.*;

public class Solution {

    public List<List<Integer>> combinationSum(int[] vals, int goal) {

        List<List<Integer>> result = new ArrayList<>();

        int[] sorted = vals.clone();
        Arrays.sort(sorted);

        explore(sorted, 0, goal, new ArrayList<>(), result);

        return result;
    }

    private void explore(int[] vals, int start, int remaining, List<Integer> path, List<List<Integer>> result) {

        if (remaining == 0) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int i = start; i < vals.length; i++) {

            if (vals[i] > remaining) {
                break;
            }

            path.add(vals[i]);
            explore(vals, i, remaining - vals[i], path, result);
            path.remove(path.size() - 1);
        }
    }

}
