import java.util.*;

public class Solution {

    public List<String> generateParenthesis(int pairs) {

        List<String> result = new ArrayList<>();
        build(result, new StringBuilder(), 0, 0, pairs);

        return result;
    }

    private void build(List<String> result, StringBuilder current, int opened, int closed, int pairs) {

        if (current.length() == 2 * pairs) {
            result.add(current.toString());
            return;
        }

        if (opened < pairs) {
            current.append('(');
            build(result, current, opened + 1, closed, pairs);
            current.deleteCharAt(current.length() - 1);
        }

        if (closed < opened) {
            current.append(')');
            build(result, current, opened, closed + 1, pairs);
            current.deleteCharAt(current.length() - 1);
        }
    }

}
