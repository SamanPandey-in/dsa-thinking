# 1190. Reverse Substrings Between Each Pair of Parentheses

## Description:

You are given a string s that consists of lower case English letters and brackets.

Reverse the strings in each pair of matching parentheses, starting from the innermost one.

Your result should not contain any brackets.

---

## Intuition

The problem asks us to reverse substrings enclosed within parentheses, starting from the innermost pair and moving outward. This naturally suggests a **Last-In-First-Out (LIFO)** behavior, which is exactly what a **stack** provides.

Think of it like this:

* As we traverse the string, we keep pushing characters onto the stack.
* When we encounter a closing parenthesis `)`, we know we've reached the end of a segment that needs to be reversed.
* Since the most recent characters are on top of the stack, we can easily pop them to form the reversed substring.

This ensures that **inner parentheses are processed before outer ones**, which is key to solving the problem correctly.

---

## Approach

1. **Initialize a stack** to store characters.

2. **Traverse the string character by character:**

   * If the character is a letter or `'('`, push it onto the stack.
   * If the character is `')'`:

     * Start popping characters from the stack until `'('` is found.
     * While popping, build a temporary string (`temp`).
     * This `temp` string is already reversed due to stack popping order.
     * Remove the `'('` from the stack.
     * Push all characters of `temp` back onto the stack.

3. **Build the final result:**

   * Pop all characters from the stack and append them to a result string.
   * Since the stack reverses order, reverse the final string before returning.

---

## Key Observations

* The stack automatically helps reverse substrings due to its LIFO nature.
* Each pair of parentheses is handled independently, starting from the innermost.
* No need for explicit recursion or complex parsing.

---

```java
class Solution {
    public int minOperations(String[] logs) {
        Stack<String> st = new Stack<>();
        for (String log : logs) {
            if (log.equals("../")) {
                if (!st.isEmpty()) {
                    st.pop();
                }
            } else if (!log.equals("./")) {
                st.push(log);
            }
        }
        return st.size();
    }
}
```

---

## Time Complexity

* **O(n²)** in the worst case (due to repeated string building and pushing back characters).
* Can be optimized further, but this approach is intuitive and works well for typical constraints.

---

## Space Complexity

* **O(n)** for the stack and temporary storage.

---

## Summary

* Use a stack to simulate nested reversal.
* Reverse substrings when encountering `')'`.
* Build the result from the stack at the end.

This approach keeps the solution simple while correctly handling nested parentheses.
