# 761. Special Binary String

## Description: 

Special binary strings are binary strings with the following two properties:

The number of 0's is equal to the number of 1's.
Every prefix of the binary string has at least as many 1's as 0's.
You are given a special binary string s.

A move consists of choosing two consecutive, non-empty, special substrings of s, and swapping them. Two strings are consecutive if the last character of the first string is exactly one index before the first character of the second string.

Return the lexicographically largest resulting string possible after applying the mentioned operations on the string.

## Idea:

- Goal is to swap and make the lexicographically larget special string.
- if 1 -> increase count; else if 0 -> decrease count. When count = 0 -> Valid special string. (Just like valid paranthesis).

## Step-by-step plan:

- Maintain a counter `count`
    - If 1, `count++`; if 0 `count--`
- Break the string into small valid parts.
- Do the same thing in each part recursively.
- Whenever count == 0, a valid special substring is found
    - process inner substring of the valid from start + 1 to i recursively
    - Then add `1 + inner + 0` to the list
- Sort all parts in descending order.
- Join all for answer

## Code: 
```java
class Solution {
    public String makeLargestSpecial(String s) {
        List<String> list = new ArrayList<>();

        int start = 0, count = 0;
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '1') count++;
            else count--;

            if (count == 0) {
                String inner = makeLargestSpecial(s.substring(start + 1, i));
                list.add("1" + inner + "0");
                start = i + 1;
            }
        }

        Collections.sort(list, Collections.reverseOrder());

        StringBuilder ans = new StringBuilder();
        for (String str : list) ans.append(str);
        return ans.toString();
    }
}
```

- Time Complexity: O($n^2$) - Recursion + sorting + substring

- Space Complexity: O(n) - Recursion stack + list
