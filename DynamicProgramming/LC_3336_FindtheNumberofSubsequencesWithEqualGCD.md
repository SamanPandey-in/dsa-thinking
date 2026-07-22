# 3336. Find the Number of Subsequences With Equal GCD ([Link](https://leetcode.com/problems/find-the-number-of-subsequences-with-equal-gcd/))

## Description:
You are given an integer array nums.

Your task is to find the number of pairs of non-empty subsequences (seq1, seq2) of nums that satisfy the following conditions:

The subsequences seq1 and seq2 are disjoint, meaning no index of nums is common between them.
The GCD of the elements of seq1 is equal to the GCD of the elements of seq2.
Return the total number of such pairs.

Since the answer may be very large, return it modulo 109 + 7.

This problem looks intimidating at first because we're simultaneously building **two subsequences** while tracking the **GCD of each**.

The key observation is that at every index, we only have **three possible choices**, making it a natural Dynamic Programming problem.

We'll derive the solution step by step:

1. Pure Recursion
2. Memoization
3. Bottom-Up Tabulation

---

# Intuition

We need to build **two subsequences** from the given array such that:

* both subsequences are non-empty
* their GCDs are equal

Every element has exactly **three choices**:

* Ignore it.
* Put it into subsequence 1.
* Put it into subsequence 2.

Therefore, our recursive state only needs to know:

* which index we're processing
* current GCD of subsequence 1
* current GCD of subsequence 2

---

# State Definition

Let

```text
solve(i, g1, g2)
```

represent

> **The number of valid ways considering elements from index `i` onward, where**
>
> * `g1` = current GCD of subsequence 1
> * `g2` = current GCD of subsequence 2

Initially,

```text
solve(0,0,0)
```

because both subsequences are empty.

---

# Why do we start GCD with 0?

Normally,

```text
gcd(a,b)
```

requires two numbers.

Here we initialize the GCD as

```text
0
```

because

```text
gcd(0,x)=x
```

This neatly handles the first element inserted into a subsequence.

Example:

```text
gcd(0,12)=12
```

After that,

```text
gcd(12,18)=6
```

and so on.

Thus, `0` acts as an "empty subsequence" marker.

---

# Approach 1: Pure Recursion

## Base Case

When we've processed every element,

```text
i == n
```

we check whether both subsequences are valid.

If either GCD is still

```text
0
```

then that subsequence was never chosen.

Hence,

```text
return 0
```

Otherwise,

if

```text
g1 == g2
```

we found one valid pair.

Return

```text
1
```

Else,

return

```text
0
```

---

## Recursive Choices

For every element,

there are exactly three possibilities.

### 1. Skip it

```text
solve(i+1,g1,g2)
```

---

### 2. Put it into subsequence 1

Its GCD changes.

```text
solve(i+1,gcd(g1,nums[i]),g2)
```

---

### 3. Put it into subsequence 2

```text
solve(i+1,g1,gcd(g2,nums[i]))
```

---

The answer is simply the sum of all three choices.

---

## Recursive Code (TLE)

```
class Solution {
    final int MOD = 1_000_000_007;
    private int n;
    private int[] nums;
    public int subsequencePairCount(int[] nums) {
        this.n = nums.length;
        this.nums = nums;

        // recursion
        return rec(0, 0, 0);
    }
    private int rec(int i, int g1, int g2) {
        if (i == n) {
            if (g1 == 0 || g2 == 0) return 0;
            return (g1 == g2) ? 1 : 0;
        }

        long ans = 0;
        
        //skip
        int skip = rec(i + 1, g1, g2);

        // take im seq1 & then 2
        int seq1 = rec(i + 1, gcd(g1, nums[i]), g2);
        int seq2 = rec(i + 1, g1, gcd(g2, nums[i]));

        return (((skip + seq1 ) % MOD) + seq2) % MOD;
    }
    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}
```

---

## Time Complexity

Every index branches into **3 choices**.

Therefore,

```text
Time = O(3^N)
```

which quickly becomes infeasible.

---

## Space Complexity

Only recursion stack.

```text
O(N)
```

---

# Why Recursion is Slow

Many states repeat.

For example,

```text
solve(4,2,6)
```

may be reached through several different sequences of choices.

Every time it is recomputed from scratch.

This is exactly the overlapping subproblem property required for Dynamic Programming.

---

# Approach 2: Memoization (Top-Down DP)

## Idea

The recursive state is completely determined by

```text
(i,g1,g2)
```

Since

* `i` ranges from `0...n`
* every GCD is at most `200`

we store

```text
dp[i][g1][g2]
```

Before solving a state,

check if it has already been computed.

If yes,

return it immediately.

---

## Memoization Code

```
class Solution {
    final int MOD = 1_000_000_007;
    private int n;
    private int[] nums;
    private int[][][] dp;
    public int subsequencePairCount(int[] nums) {
        this.n = nums.length;
        this.nums = nums;

        // recursion
        // return rec(0, 0, 0);

        // memo
        this.dp = new int[n][201][201];
        for (int[][] twoD : dp) {
            for (int[] oneD : twoD) {
                Arrays.fill(oneD, -1);
            }
        }
        return memo(0, 0, 0);
    }
    private int memo(int i, int g1, int g2) {
        if (i == n) {
            if (g1 == 0 || g2 == 0) return 0;
            return (g1 == g2) ? 1 : 0;
        }

        if (dp[i][g1][g2] != -1) return dp[i][g1][g2];
        
        //skip
        int skip = memo(i + 1, g1, g2);

        // take im seq1 & then 2
        int seq1 = memo(i + 1, gcd(g1, nums[i]), g2);
        int seq2 = memo(i + 1, g1, gcd(g2, nums[i]));

        dp[i][g1][g2] = (((skip + seq1 ) % MOD) + seq2) % MOD;
        return dp[i][g1][g2];
    }
    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}
```

---

## Time Complexity

There are

```text
N × 201 × 201
```

possible states.

Each state is computed only once.

Therefore,

```text
Time = O(N × 201²)
```

Since `201` is a fixed constant,

this is effectively

```text
O(N)
```

with a relatively large constant factor.

---

## Space Complexity

DP table

```text
O(N × 201²)
```

Recursion stack

```text
O(N)
```

Total

```text
O(N × 201²)
```

---

# Approach 3: Bottom-Up Tabulation

Instead of solving states recursively,

we fill the DP table from the end toward the beginning.

---

# DP Table Formation

Let

```text
dp[i][g1][g2]
```

represent

> Number of valid ways considering elements starting from index `i`,
> when the current GCDs are `g1` and `g2`.

Exactly the same meaning as memoization.

---

## Table Dimensions

```
dp[n+1][201][201]
```

* `i` → array index
* `g1` → current GCD of subsequence 1
* `g2` → current GCD of subsequence 2

---

## Base Case

When

```text
i == n
```

there are no elements left.

Only states satisfying

```text
g1 == g2
```

and

```text
g1 != 0
```

are valid.

So,

```java
for (int g = 1; g <= 200; g++)
    dp[n][g][g] = 1;
```

Every other state remains `0`.

---

## Why Iterate Backwards?

Every transition depends upon

```text
dp[i+1][...][...]
```

Therefore,

before computing

```text
dp[i]
```

the entire layer

```text
dp[i+1]
```

must already exist.

Hence,

we iterate

```text
i = n-1 ... 0
```

---

## Transition

For every state,

we perform the same three choices as recursion.

### Skip

```text
dp[i+1][g1][g2]
```

---

### Put into subsequence 1

```text
dp[i+1][gcd(g1,x)][g2]
```

---

### Put into subsequence 2

```text
dp[i+1][g1][gcd(g2,x)]
```

The answer becomes

```text
skip
+
seq1
+
seq2
```

under modulo arithmetic.

---


# Understanding the DP State

Suppose

```text
nums = [2,4]
```

Initially,

```
dp starts from

(i=0,g1=0,g2=0)
```

At index `0` (`2`), we have three choices:

```
                (0,0,0)
              /    |     \
          Skip   Seq1   Seq2
          (1,0,0)(1,2,0)(1,0,2)
```

From each of these states, we again have three choices for `4`.

For example,

placing `4` into subsequence 1 after already placing `2` there gives

```
gcd(2,4)=2
```

so the state becomes

```
(i=2,g1=2,g2=0)
```

Similarly,

if subsequence 2 already had `4`

and we now add `2`

```
gcd(4,2)=2
```

Every path in this state graph represents one unique assignment of elements to:

* neither subsequence
* subsequence 1
* subsequence 2

The DP simply counts all assignments that end with

```
g1 == g2 != 0
```

---

## Tabulation Code (TLE)

```
class Solution {
    final int MOD = 1_000_000_007;
    private int n;
    private int[][][] dp;
    public int subsequencePairCount(int[] nums) {
        this.n = nums.length;

        // tabulation
        return tab(nums);
    }
    private int tab(int[] nums) {
        dp = new int[n + 1][201][201];

        // base case
        for (int g = 1; g <= 200; g++) {
            dp[n][g][g] = 1;
        }

        for (int i = n - 1; i >= 0; i--) {
            int x = nums[i];
            for (int g1 = 0; g1 < 201; g1++) {
                for (int g2 = 0; g2 < 201; g2++) {
                    long ans = dp[i + 1][g1][g2];
                    ans += dp[i + 1][gcd(g1, x)][g2];
                    ans += dp[i + 1][g1][gcd(g2, x)];
                    dp[i][g1][g2] = (int) (ans % MOD);
                }
            }
        }
        return dp[0][0][0];
    }
    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}
```

---

## Time Complexity

We visit

```
N × 201 × 201
```

states.

Each transition performs only constant work.

Therefore,

```text
O(N × 201²)
```

---

## Space Complexity

Only the DP table.

```text
O(N × 201²)
```

---

# Complexity Comparison

| Approach    | Time            | Space           |
| ----------- | --------------- | --------------- |
| Recursion   | **O(3ᴺ)**       | **O(N)**        |
| Memoization | **O(N × 201²)** | **O(N × 201²)** |
| Tabulation  | **O(N × 201²)** | **O(N × 201²)** |

---

# Key Takeaways

* Every element has exactly **three choices**: skip it, add it to subsequence 1, or add it to subsequence 2.
* The DP state is fully described by **(index, gcd₁, gcd₂)**.
* Using `gcd(0, x) = x` elegantly represents an empty subsequence without any special-case logic.
* Memoization avoids recomputing identical `(i, g1, g2)` states, reducing the exponential recursion to a polynomial number of states.
* Tabulation uses the same state transitions in reverse index order because each state depends only on the next layer `i + 1`.
* The final answer is `dp[0][0][0]`, which represents all valid assignments starting with two empty subsequences.
