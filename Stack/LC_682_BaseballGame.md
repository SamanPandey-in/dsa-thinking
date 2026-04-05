# 682. Baseball Game

## Description:

The Leetcode file system keeps a log each time some user performs a change folder operation.

The operations are described below:

- "../" : Move to the parent folder of the current folder. (If you are already in the main folder, remain in the same folder).
- "./" : Remain in the same folder.
- "x/" : Move to the child folder named x (This folder is guaranteed to always exist).
You are given a list of strings logs where logs[i] is the operation performed by the user at the ith step.

The file system starts in the main folder, then the operations in logs are performed.

Return the minimum number of operations needed to go back to the main folder after the change folder operations.

## Approach 1 - Two loops:

### Intution:

A stack works well here because every operation depends on the most recent scores.

- When we see `"+"`, we use the **last two scores**.
- When we see `"D"`, we use the **last score**.
- When we see `"C"`, we **remove the last score**.

A stack makes it easy to quickly access and update these recent values.

### Algorithm:

1. Initialize an empty stack to store valid scores.
2. For each operation:
    - If it's +, add the sum of the top two elements to the stack.
    - If it's D, add double the top element to the stack.
    - If it's C, pop the top element.
    - Otherwise, it's a number, so push it onto the stack.
3. Return the sum of all elements in the stack.

```java
class Solution {
    public int calPoints(String[] operations) {
        Stack<Integer> st = new Stack<>();
        for(String op : operations){
            switch(op){
                case "+":
                    int top = st.pop();
                    int newTop = top+st.peek();
                    st.push(top);
                    st.push(newTop);
                    break;
                case "D":
                    st.push(2*st.peek());
                    break;
                case "C":
                    st.pop();
                    break;
                default:
                    st.push(Integer.parseInt(op));
                    break;
            }
        }
        int score = 0;
        while(!st.isEmpty()){
            score+=st.pop();
        }
        return score;
    }
}
```

- Time Complexity: O(n)
- Space Complexity: O(n)

## Approach 2 - Only one loop:

### Intution:

This method is similar to the first one, but we keep track of the total score as we go.

- When we add a score, we **add it to the total**.
- When we remove a score using `"C"`, we **subtract it from the total**.

This way, we get the final answer without needing to go through the stack again at the end.

### Algorithm:

1. Initialize an empty stack and a result variable set to 0.
2. For each operation:
    - If it's +, calculate the sum of the top two elements, push it, and add to result.
    - If it's D, calculate double the top element, push it, and add to result.
    - If it's C, pop the top element and subtract it from result.
    - Otherwise, parse the number, push it, and add to result.
3. Return the result.

```java
class Solution {
    public int calPoints(String[] operations) {
        Stack<Integer> st = new Stack<>();
        int score = 0;
        for(String op : operations){
            switch(op){
                case "+":
                    int top = st.pop();
                    int newTop = top+st.peek();
                    st.push(top);
                    st.push(newTop);
                    score+= newTop;
                    break;
                case "D":
                    st.push(2*st.peek());
                    score+=st.peek();
                    break;
                case "C":
                    score-=st.pop();
                    break;
                default:
                    st.push(Integer.parseInt(op));
                    score+=st.peek();
                    break;
            }
        }
        return score;
    }
}
```

- Time Complexity: O(n)
- Space Complexity: O(n)