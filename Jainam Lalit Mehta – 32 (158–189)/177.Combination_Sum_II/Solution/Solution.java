import java.util.*;

public class Solution {

    public List<List<Integer>> combinationSum2(int[] pool, int goal) {

        List<List<Integer>> result = new ArrayList<>();

        int[] sorted = pool.clone();
        Arrays.sort(sorted);

        explore(sorted, 0, goal, new ArrayList<>(), result);

        return result;
    }

    private void explore(int[] pool, int start, int remaining, List<Integer> path, List<List<Integer>> result) {

        if (remaining == 0) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int i = start; i < pool.length; i++) {

            if (i > start && pool[i] == pool[i - 1]) {
                continue;
            }

            if (pool[i] > remaining) {
                break;
            }

            path.add(pool[i]);
            explore(pool, i + 1, remaining - pool[i], path, result);
            path.remove(path.size() - 1);
        }
    }

}
