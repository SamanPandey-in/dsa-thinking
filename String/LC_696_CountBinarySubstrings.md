# 696. Count Binary Substrings

## Description:  

Given a binary string s, return the number of non-empty substrings that have the same number of 0's and 1's, and all the 0's and all the 1's in these substrings are grouped consecutively.

Substrings that occur multiple times are counted the number of times they occur.

## Step-by-step plan:

- Instead of checking for all possible substrings, we will focus on consectuive group of characters.
- We only care about the length of current group of identical characters and length of previous group.
- When the character changes, the current group becomes the previous group and we start counting the new group.
- Until the current group count is less than or equal to the previous group count, we have found a valid substring.

- Use `curStreak` to count the length of the current group of same characters.
- Use `prevStreak` to store the length of the previous group of same characters.
- Iterate through the string starting from the second character:
    - If the current character is the same as the previous one, increment `curStreak`.
    - If the character changes:
        - Move `curStreak` value to `prevStreak`.
        - Reset `curStreak` to 1.
    - After updating streaks, if `prevStreak >= curStreak`, it means we can form a valid substring (like `0011` where `cur` is 2 and `prev` is 2), so increment `count`.

```java
class Solution {
    public int countBinarySubstrings(String s) {
        int count = 0;
        int curStreak = 1;
        int prevStreak = 0;

        for(int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) curStreak++;
            else {
                prevStreak = curStreak;
                curStreak = 1;
            }
            if (curStreak <= prevStreak) count++;
        }
        return count;
    }
}
```

- Time Complexity: O(n) - same for all

- Space Complexity: O(1)