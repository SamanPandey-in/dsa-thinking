# 796. Rotate String

## Description: 

Given two strings s and goal, return true if and only if s can become goal after some number of shifts on s.

A shift on s consists of moving the leftmost character of s to the rightmost position.

For example, if s = "abcde", then it will be "bcdea" after one shift.

## Step-by-step plan: Naive Approach

1. First, we check if the lengths of s and goal are different. If they are, we can immediately return false, since they cannot be rotations of each other.
2. Next, we concatenate s with itself. This creates a new string that contains all possible rotations of s as contiguous substrings.
3. Finally, we check if goal is a substring of the concatenated string. If it is, then s can be rotated to become goal, and we return true. Otherwise, we return false.

## Code Implementation:
```java
class Solution {
    public boolean rotateString(String s, String goal) {
        if (s.length() != goal.length())
            return false;
        String join = s + s;
        if (join.contains(goal))
            return true;
        return false;
    }
}
```

- Time Complexity: O(n) (where n is the length of the strings, since we check for substring in join.contains)
- Space Complexity: O(1)

## KMP Algorithm Approach for string matching

We can also use the KMP algorithm to check if goal is a substring of s + s in O(n) time.

```java
class Solution {
    private int[] buildLPS(String pattern) {
        int n = pattern.length();
        int[] lps = new int[n];

        int len = 0, i = 1;
        while (i < n) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                lps[i++] = ++len;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }
        }
        return lps;
    }
    public boolean rotateString(String s, String goal) {
        if (s.length() != goal.length()) return false;

        String text = s + s;
        int[] lps = buildLPS(goal);

        int i = 0, j = 0;

        while (i < text.length()) {
            if (text.charAt(i) == goal.charAt(j)) {
                i++;
                j++;
            }

            if (j == goal.length()) {
                return true;
            } else if (i < text.length() && text.charAt(i) != goal.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return false;
    }
}
```

- Time Complexity: O(n) for all cases
- Space Complexity: O(n) for the LPS array