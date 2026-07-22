# 516. Longest Palindromic Subsequence

One of the best and most effective ways to understand Dynamic Programming is to first solve the problem recursively, identify the overlapping subproblems, optimize it using memoization, and finally convert it into a bottom-up tabulation solution.

In this article, we'll go through all three approaches step by step.

---

## Intuition

For every substring `s[l...r]`, there are only two possibilities:

### Case 1: Characters at both ends are equal

If

```text
s[l] == s[r]
```

then both characters can be part of the palindrome.

Therefore,

```text
LPS(l, r) = 2 + LPS(l+1, r-1)
```

---

### Case 2: Characters are different

If

```text
s[l] != s[r]
```

then one of them cannot belong to the same palindrome.

We try both possibilities:

* Ignore left character
* Ignore right character

```text
LPS(l, r) =
max(
    LPS(l+1, r),
    LPS(l, r-1)
)
```

These two observations completely solve the problem.

---

# Approach 1: Pure Recursion

## Idea

Define a recursive function

```java
solve(l, r)
```

that returns the length of the longest palindromic subsequence inside the substring

```text
s[l...r]
```

### Base Cases

If

```text
l > r
```

there are no characters left.

Return

```text
0
```

If

```text
l == r
```

there is exactly one character.

A single character itself is a palindrome.

Return

```text
1
```

### Recursive Transition

If both end characters are equal

```java
if(s.charAt(l)==s.charAt(r))
```

we include both.

Otherwise,

we recursively compute the answer by excluding one end at a time.

---

## Recursive Code

```java
class Solution {
    String s;

    public int longestPalindromeSubseq(String s) {
        this.s = s;
        return solve(0, s.length() - 1);
    }

    private int solve(int l, int r) {

        if (l > r) return 0;

        if (l == r) return 1;

        if (s.charAt(l) == s.charAt(r)) {
            return 2 + solve(l + 1, r - 1);
        }

        return Math.max(
                solve(l + 1, r),
                solve(l, r - 1)
        );
    }
}
```

---

## Time Complexity

At every mismatch, the recursion branches into two calls.

The recursion tree grows exponentially.

**Time Complexity:**

```text
O(2^N)
```

---

## Space Complexity

Maximum recursion depth is

```text
O(N)
```

---

# Why is Recursion Slow?

Notice the repeated computation.

Example:

```text
solve(0,4)

├── solve(1,4)
│      ├── solve(2,4)
│      └── solve(1,3)
│
└── solve(0,3)
       ├── solve(1,3)   <-- computed again
       └── solve(0,2)
```

The same states are solved multiple times.

This is exactly where Dynamic Programming helps.

---

# Approach 2: Memoization (Top-Down DP)

## Idea

Since every state is uniquely identified by

```text
(l,r)
```

we store its answer in

```java
dp[l][r]
```

Before solving a state, simply check whether it has already been computed.

If yes,

return it immediately.

This removes all repeated recursive work.

---

## Memoized Code

```java
class Solution {
    String s;
    int[][] dp;

    public int longestPalindromeSubseq(String s) {
        this.s = s;

        int n = s.length();

        dp = new int[n][n];

        for (int[] row : dp)
            Arrays.fill(row, -1);

        return solve(0, n - 1);
    }

    private int solve(int l, int r) {

        if (l > r) return 0;

        if (l == r) return 1;

        if (dp[l][r] != -1)
            return dp[l][r];

        if (s.charAt(l) == s.charAt(r)) {
            dp[l][r] = 2 + solve(l + 1, r - 1);
        } else {
            dp[l][r] = Math.max(
                    solve(l + 1, r),
                    solve(l, r - 1)
            );
        }

        return dp[l][r];
    }
}
```

---

## Time Complexity

There are

```text
N × N
```

possible states.

Each state is computed only once.

**Time Complexity**

```text
O(N²)
```

---

## Space Complexity

DP table:

```text
O(N²)
```

Recursive stack:

```text
O(N)
```

Total:

```text
O(N²)
```

---

# Approach 3: Bottom-Up Tabulation

Memoization computes states recursively.

Instead, we can fill the DP table iteratively.

The transition is

```text
dp[i][j]
```

depends upon

```text
dp[i+1][j]
dp[i][j-1]
dp[i+1][j-1]
```

So before computing a larger substring, all smaller substrings must already be available.

That means we process substrings in increasing order of their length.

---

## DP Definition

Let

```text
dp[i][j]
```

represent the longest palindromic subsequence inside

```text
s[i...j]
```

---

## Base Case

Every single character is a palindrome.

```java
dp[i][i]=1;
```

---

## Transition

If

```text
s[i]==s[j]
```

```text
dp[i][j]=2+dp[i+1][j-1]
```

Otherwise

```text
dp[i][j]=max(
    dp[i+1][j],
    dp[i][j-1]
)
```

---

# Understanding the DP Table

Let us look at a concrete example using the string

```text
s = "bbbab"
```

The length of the string is **5**, so we create a **5 × 5** DP table.

* Rows represent the starting index `i`.
* Columns represent the ending index `j`.
* We only fill the upper-right half of the table because a substring cannot end before it starts (`j < i` is invalid).

---

## 1. Filling Process

### Step 1: Substrings of Length 1

Every individual character is a palindrome of length **1**, so we initialize the diagonal.

```
dp[i][i] = 1
```

---

### Step 2: Substrings of Length 2

We evaluate:

```
"bb"
"bb"
"ba"
"ab"
```

If both characters match:

```
dp[i][j] = 2
```

Otherwise:

```
dp[i][j] = max(dp[i+1][j], dp[i][j-1]) = 1
```

since both neighboring values are already `1`.

---

### Step 3: Substrings of Length 3, 4 and 5

Now every required smaller substring has already been computed.

For the full string:

```
"bbbab"
```

```
i = 0
j = 4
```

Both outer characters are `'b'`.

So,

```
dp[0][4]
=
dp[1][3] + 2
```

Since

```
dp[1][3] = 2
```

we obtain

```
dp[0][4] = 4
```

---

## 2. Final DP Table

Empty cells (`0`) correspond to invalid states where `i > j`.

| i \ j       | 0 ('b') | 1 ('b') | 2 ('b') | 3 ('a') | 4 ('b') |
| ----------- | ------- | ------- | ------- | ------- | ------- |
| **0 ('b')** | 1       | 2       | 3       | 3       | **4**   |
| **1 ('b')** | 0       | 1       | 2       | 2       | 3       |
| **2 ('b')** | 0       | 0       | 1       | 1       | 2       |
| **3 ('a')** | 0       | 0       | 0       | 1       | 1       |
| **4 ('b')** | 0       | 0       | 0       | 0       | 1       |

---

## How to Read the Answer

The answer for the complete string is stored in:

```
dp[0][n-1]
```

For this example,

```
dp[0][4] = 4
```

Hence the longest palindromic subsequence has length **4**.

One valid subsequence is:

```text
bbbb
```

(using indices **0, 1, 2, 4**)

---

## Tabulation Code

```java
class Solution {
    public int longestPalindromeSubseq(String s) {

        int n = s.length();

        int[][] dp = new int[n][n];

        for (int i = 0; i < n; i++)
            dp[i][i] = 1;

        for (int len = 2; len <= n; len++) {

            for (int i = 0; i < n - len + 1; i++) {

                int j = i + len - 1;

                if (s.charAt(i) == s.charAt(j)) {

                    if (len == 2)
                        dp[i][j] = 2;
                    else
                        dp[i][j] = dp[i + 1][j - 1] + 2;

                } else {

                    dp[i][j] = Math.max(
                            dp[i + 1][j],
                            dp[i][j - 1]
                    );
                }
            }
        }

        return dp[0][n - 1];
    }
}
```

---

## Time Complexity

We fill every cell of the DP table exactly once.

There are

```text
N²
```

cells.

**Time Complexity**

```text
O(N²)
```

---

## Space Complexity

Only one DP table is used.

```text
O(N²)
```

---

# Complexity Comparison

| Approach    | Time       | Space                                |
| ----------- | ---------- | ------------------------------------ |
| Recursion   | **O(2^N)** | **O(N)**                             |
| Memoization | **O(N²)**  | **O(N²)** + **O(N)** recursion stack |
| Tabulation  | **O(N²)**  | **O(N²)**                            |

---

# Key Takeaways

* Pure recursion is straightforward but repeatedly solves the same subproblems, leading to exponential time.
* Memoization stores each `(l, r)` state, reducing the complexity to `O(N²)`.
* Tabulation eliminates recursion entirely by filling the DP table from smaller substrings to larger ones.
* The final answer is always found at `dp[0][n-1]`, representing the longest palindromic subsequence of the entire string.

This progression from **Recursion → Memoization → Tabulation** is a common Dynamic Programming pattern and can be applied to many interval DP problems.
