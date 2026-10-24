import java.util.*;

public class Solution {

    private static final String[] KEYS = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public List<String> expandPresses(String presses) {

        List<String> result = new ArrayList<>();
        build(result, new StringBuilder(), presses, 0);

        return result;
    }

    private void build(List<String> result, StringBuilder current, String presses, int position) {

        if (position == presses.length()) {
            result.add(current.toString());
            return;
        }

        for (char letter : KEYS[presses.charAt(position) - '0'].toCharArray()) {
            current.append(letter);
            build(result, current, presses, position + 1);
            current.deleteCharAt(current.length() - 1);
        }
    }

}
