# 3713. Longest Balanced Substring I

## Description: 

You are given a string 's' consisting of lowercase English letters.

A substring of s is called balacned if all distinct characters in the substrign appear the same number of times.

Return the length of the longest balanced substring of s.

## Step-by-step plan:

### Use Bruteforce: O($n^2$)

- Balanced substring means that all the different characters in the substring must have the same freq.

- Thus, sliding window becomes a bit tricky here as for every new/existing element, the frequency balance gets disturbed. Thus the easiest way to solve this is to make substring from each index and keep checking if balanced.

- For every index i in string:
    1. Maintain a frequency array which will store each character's count.
    2. Now keep a count "distinct" for the number of distinct characters appearing.
    3. Now for each incoming character c, track it's maxFreq.
        - Check and update maxFreq as the frequency increases.
        - Final check:
            - if maxFreq*distinct == length of substring means that all characters have the same frequency as (maxFreq * dist == length) can happen only if every character frequency equals maxFreq -> update the ans for maxLength.

```java
class Solution {
    public int longestBalanced(String s) {
        int n = s.length();
        int ans = 0;

        for (int i = 0; i < n; i++) {
            int[] freq = new int[26];
            int maxFreq = 0;
            int dist = 0;
            for (int j = i; j < n; j++) {
                char c = s.charAt(j);
                int idx = c - 'a';
                if (freq[idx] == 0) {
                    dist++;
                }
                freq[idx] += 1;
                maxFreq = Math.max(maxFreq, freq[idx]);
                if (maxFreq * dist == j - i + 1) {
                    ans = Math.max(ans, j - i + 1);
                }
            }
        }
        return ans;
    }
}
```

- Time Complexity: O($n^2$) - Same for all cases

    - The outer loop runs n times

    - For each i, the inner loop can run up to n - i times

    - All possible substrings are explored

    - No early termination is possible because: A longer balanced substring might appear later

    - Worst-case scenario: Any input (even all identical numbers) still forces full traversal.

- Space Complexity: O(1) - we are using only a freq[26] array.
