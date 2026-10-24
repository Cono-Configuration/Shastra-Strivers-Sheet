import java.util.*;

public class Solution {

    public List<String> generateStrings(int length) {

        List<String> result = new ArrayList<>();

        build(new StringBuilder(), length, result);

        return result;
    }

    private void build(StringBuilder current, int length, List<String> result) {

        if (current.length() == length) {
            result.add(current.toString());
            return;
        }

        // place '0' first so the strings come out in increasing lexicographic order
        current.append('0');
        build(current, length, result);
        current.deleteCharAt(current.length() - 1);

        // place '1' only when the previous character is not '1'
        boolean previousIsOne = current.length() > 0 && current.charAt(current.length() - 1) == '1';

        if (!previousIsOne) {
            current.append('1');
            build(current, length, result);
            current.deleteCharAt(current.length() - 1);
        }
    }

}
