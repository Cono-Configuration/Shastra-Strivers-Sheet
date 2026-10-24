import java.util.*;

public class Solution {

    public List<String> buildExpressions(String numeral, int desired) {

        List<String> expressions = new ArrayList<>();
        extend(expressions, numeral, desired, 0, 0, 0, new StringBuilder());

        Collections.sort(expressions);
        return expressions;
    }

    // total: value of the expression so far, excluding the last term
    // last:  the last term added, so '*' can replace it correctly
    private void extend(List<String> expressions, String numeral, long desired, int position,
                        long total, long last, StringBuilder path) {

        if (position == numeral.length()) {
            if (total == desired) {
                expressions.add(path.toString());
            }
            return;
        }

        for (int end = position + 1; end <= numeral.length(); end++) {

            // An operand may not have a leading zero, except the single digit 0.
            if (end > position + 1 && numeral.charAt(position) == '0') {
                break;
            }

            long operand = Long.parseLong(numeral.substring(position, end));
            int length = path.length();

            if (position == 0) {
                path.append(operand);
                extend(expressions, numeral, desired, end, operand, operand, path);
                path.setLength(length);
            } else {
                // '+'
                path.append('+').append(operand);
                extend(expressions, numeral, desired, end, total + operand, operand, path);
                path.setLength(length);

                // '-'
                path.append('-').append(operand);
                extend(expressions, numeral, desired, end, total - operand, -operand, path);
                path.setLength(length);

                // '*': undo the last term and apply multiplication to it
                long product = last * operand;
                path.append('*').append(operand);
                extend(expressions, numeral, desired, end, total - last + product, product, path);
                path.setLength(length);
            }
        }
    }

}
