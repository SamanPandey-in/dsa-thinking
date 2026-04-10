# 10. Regular Expression Matching

## Description:

Given an input string s and a pattern p, implement regular expression matching with support for '.' and '*' where:

- '.' Matches any single character.​​​​
- '*' Matches zero or more of the preceding element.
Return a boolean indicating whether the matching covers the entire input string (not partial).

## Intuition

We need to implement a simplified regular expression matcher that supports two special characters: '.' which matches any single character, and '*' which matches zero or more of the preceding element. The matching must cover the entire input string.

**Key Insight:** Instead of trying to build a complex state machine, we can use recursive backtracking to explore all possible ways the pattern could match the string.

## Approach

For each position in both the string and pattern, we check what happens if we match the current characters and move forward, or in the case of *, explore different usage counts.

We use a recursive function that tracks our current position in both the string and pattern. 
- For normal characters and '.', we simply check if they match and move forward. 
- For patterns with '*', we try both skipping the pattern (using 0 repetitions) and using one or more repetitions.

## Step-by-step Solution:

### Step 1: Entry Point

```java
public boolean isMatch(String s, String p) {
    return solve(s, p, 0, 0);  // Start recursion from beginning of both strings
}
```

### Step 2: Base Cases

```java
if (sIdx == s.length() && pIdx == p.length()) {
    return true;  // both strings exhausted - perfect match
}

if (pIdx == p.length()) {
    return false;  // Pattern exhausted but string has characters left - no match
}
```

- First condition: If we've reached the end of both strings simultaneously, we found a complete match
- Second condition: If pattern ends but string still has characters, it cannot be a full match
- Notice we don't check if string ends but pattern continues - because pattern might have * patterns that can match zero characters

### Step 3: Checking for '*' character

```java
char curr = p.charAt(pIdx);  // get current pattern character
boolean isStar = (pIdx + 1 < p.length() && p.charAt(pIdx + 1) == '*');  // check if next char is '*'
```

- We look at the current character in the pattern
- We peek ahead to see if the next character is *
- This tells us if we're dealing with a "character*" pattern or just a normal character

### Step 4: Handling '*' type patterns

```java
if (isStar) {
    if (solve(s, p, sIdx, pIdx + 2)) {  // try skipping the 'char*' pattern (0 repetitions)
        return true;
    }
    
    int i = sIdx;
    while (i < s.length() && (curr == '.' || s.charAt(i) == curr)) {
        if (solve(s, p, i + 1, pIdx + 2)) {  // try using 1+ repetitions, then move past 'char*'
            return true;
        }
        i++;
    }
    return false;
}
```

- First we try skipping the pattern entirely (0 repetitions) by jumping over both the character and `*`
- Then we try using repetitions: we consume as many matching characters as possible, and for each consumption, we recursively check if the rest matches
- The while loop handles multiple repetitions - each iteration tries using one more repetition of the character
- If any path returns true, we immediately return true (short-circuit)
- Example for s = "aaa", p = "a*a":

    - Try skip a* → pattern becomes "a" vs "aaa" - false
    - Try use 1 'a' → "a" consumed, pattern becomes "a" vs "aa" - recursive call
    - Try use 2 'a's → "aa" consumed, pattern becomes "a" vs "a" - true

### Step 5: Handling normal characters and '.'

```java
else {
    if (sIdx < s.length() && (curr == '.' || s.charAt(sIdx) == curr)) {
        return solve(s, p, sIdx + 1, pIdx + 1);  // match current char and move both pointers
    }
    return false;  // current characters don't match
}
```

- For normal characters (no * following), we simply check if current characters match

- `'.'` matches any single character (except if we're at end of string)

- If they match, we move both pointers forward and continue recursively

- If they don't match, we return false immediately

## Final Code:
```java
class Solution {
    public boolean isMatch(String s, String p) {
        return solve(s, p, 0, 0);
    }
    private boolean solve(String s, String p, int sid, int pid) {
        if (s.length() == sid && p.length() == pid) {
            return true;
        }
        if (p.length() == pid) {
            return false;
        }
        char curr = p.charAt(pid);
        boolean isStar = (pid + 1 < p.length() && p.charAt(pid + 1) == '*');

        if (isStar) {
            if (solve(s, p, sid, pid + 2)) {
                return true;
            }
            int i = sid;
            while (i < s.length() && (curr == '.' || s.charAt(i) == curr)) {
                if (solve(s, p, i + 1, pid + 2)) return true;
                i++;
            }
            return false;
        } else {
            if (sid < s.length() && (curr == '.' || s.charAt(sid) == curr)) {
                return solve(s, p, sid + 1, pid + 1);
            }
            return false;
        }
    }
}
```

---

## Time Complexity

* **O(2^(n + m))** in the worst case, where `n` is the length of string `s` and `m` is the length of pattern `p`.
* This is because:
  * At every `'*'` in the pattern, the algorithm branches into multiple recursive calls:
    * One that skips the `'*'` (zero occurrence).
    * Others that try consuming characters from `s` (one or more occurrences).
  * This creates a **recursive tree with exponential branching**, especially for cases like:
    * `s = "aaaaaa..."`, `p = "a*a*a*a*..."`

* Since there is **no memoization**, the same `(sid, pid)` states are recomputed multiple times, leading to exponential complexity.

## Space Complexity

* **O(n + m)** due to the recursion stack.
  * In the worst case, the recursion depth can go up to:
    * `n` (consuming characters from `s`)
    * plus `m` (processing pattern `p`)
* No additional data structures are used apart from recursion.
