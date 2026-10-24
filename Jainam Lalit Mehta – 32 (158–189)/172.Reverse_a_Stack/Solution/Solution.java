import java.util.*;

public class Solution {

    public void reverseStack(Stack<Integer> pile) {

        if (pile.isEmpty()) {
            return;
        }

        int topValue = pile.pop();

        reverseStack(pile);

        insertAtBottom(pile, topValue);
    }

    private void insertAtBottom(Stack<Integer> pile, int value) {

        if (pile.isEmpty()) {
            pile.push(value);
            return;
        }

        int topValue = pile.pop();

        insertAtBottom(pile, value);

        pile.push(topValue);
    }

}
