import java.util.*;

public class Solution {

    public List<List<Integer>> findGroups(int picks, int goal) {

        List<List<Integer>> result = new ArrayList<>();
        search(result, new ArrayList<>(), picks, goal, 1);

        return result;
    }

    private void search(List<List<Integer>> result, List<Integer> current, int picks, int goal, int from) {

        if (picks == 0) {
            if (goal == 0) {
                result.add(new ArrayList<>(current));
            }
            return;
        }

        for (int value = from; value <= 9; value++) {
            // Values only grow from here, so once one exceeds the remaining goal, stop.
            if (value > goal) {
                break;
            }
            current.add(value);
            search(result, current, picks - 1, goal - value, value + 1);
            current.remove(current.size() - 1);
        }
    }

}
