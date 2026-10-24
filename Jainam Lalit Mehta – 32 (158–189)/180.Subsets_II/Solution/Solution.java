import java.util.*;

public class Solution {

    public List<List<Integer>> collectUniqueSubsets(int[] items) {

        int[] sorted = items.clone();
        Arrays.sort(sorted);

        List<List<Integer>> result = new ArrayList<>();
        explore(result, new ArrayList<>(), sorted, 0);

        return result;
    }

    private void explore(List<List<Integer>> result, List<Integer> current, int[] sorted, int start) {

        result.add(new ArrayList<>(current));

        for (int i = start; i < sorted.length; i++) {
            // Skip a value equal to the previous one at the same depth,
            // otherwise the same subset would be produced twice.
            if (i > start && sorted[i] == sorted[i - 1]) {
                continue;
            }
            current.add(sorted[i]);
            explore(result, current, sorted, i + 1);
            current.remove(current.size() - 1);
        }
    }

}
