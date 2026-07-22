# 3995. Minimum Cost to Convert String III ([Link](https://leetcode.com/problems/minimum-cost-to-convert-string-iii/))

## Description:

You are given two strings, source and target.

You are also given a 2D string array rules, where rules[i] = [patterni, replacementi], and an integer array costs, where costs[i] is the base cost of applying rules[i]. Both arrays have the same length. Additionally, patterni and replacementi have the same length.

You may apply any rule any number of times. Each rule application works as follows:

Choose an index l such that the range of positions from l to l + patterni.length - 1 exists in the current string and none of these positions has been used in a previous rule application.
For each index j, the character patterni[j] must either be equal to the current character at position l + j, or be '*'.
Replace the characters in this range with replacementi. The replacement is used exactly as given and does not contain wildcards.
The cost of this rule application is costs[i] plus the number of '*' characters in patterni.
Once a character position has been used in a rule application, it cannot be used in any later rule application.
Since every patterni and replacementi have the same length, character positions are preserved after every rule application.

Return the minimum total cost required to transform source into target. If it is impossible, return -1.

---

## Solution: 

This problem combines **Dynamic Programming** with **string pattern matching**.

We need to transform the source string into the target string using the given transformation rules while minimizing the total cost.

At every position, we either:

* move ahead if the current characters already match, or
* apply one of the valid transformation rules.

This naturally leads to a DP on the current index.

We'll derive the solution step by step.

1. Pure Recursion
2. Memoization
3. Bottom-Up Tabulation

---

# Intuition

Suppose we are currently processing position `i` in the source and target strings.

Our task is to transform `source[i...]` into `target[i...]` with minimum possible cost.

At every position, there are only two possibilities.

## Case 1: Characters already match

If `source[i] == target[i]`, then there is no need to spend any cost.

Simply continue with the remaining suffix.

`cost(i) = cost(i+1)`

---

## Case 2: Apply a Rule

For every rule `pattern → replacement`

we check whether

* the pattern matches the source starting from index `i`
* the replacement exactly matches the target starting from index `i`

If both conditions hold, we pay `ruleCost + wildcardCount` and continue after the transformed substring.

`cost(i) = ruleCost + wildcards + cost(i+patternLength)`

Among all valid choices, we take the minimum.

---

# State Definition

Define `solve(i)` as the minimum cost required to transform `source[i...]` into `target[i...]`

---

# Approach 1: Pure Recursion

## Base Case

When `i == n` the entire string has already been processed. No more cost is required.

Return `0`

---

## Recursive Choices

At every index,

### Option 1

If the current characters already match, skip them.

`solve(i+1)`

---

### Option 2

Try every transformation rule.

If a rule is applicable, its total cost becomes `cost(rule) + wildcards + solve(i+patternLength)`

The minimum among all choices is the answer.

---

## Recursive Code

```java
class Solution {

    static final long INF = (long) 1e18;

    String s, t;
    List<List<String>> rules;
    int[] costs;
    int n;

    public int minCost(String source, String target,
                       List<List<String>> rules,
                       int[] costs) {

        this.s = source;
        this.t = target;
        this.rules = rules;
        this.costs = costs;
        this.n = source.length();

        long ans = solve(0);

        return ans == INF ? -1 : (int) ans;
    }

    private long solve(int i) {

        if (i == n)
            return 0;

        long ans = INF;

        if (s.charAt(i) == t.charAt(i))
            ans = solve(i + 1);

        for (int r = 0; r < rules.size(); r++) {

            String patt = rules.get(r).get(0);
            String repl = rules.get(r).get(1);

            int len = patt.length();

            if (i + len > n)
                continue;

            boolean ok = true;
            int stars = 0;

            for (int j = 0; j < len; j++) {

                char p = patt.charAt(j);

                if (p == '*')
                    stars++;
                else if (p != s.charAt(i + j)) {
                    ok = false;
                    break;
                }

                if (repl.charAt(j) != t.charAt(i + j)) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                ans = Math.min(
                        ans,
                        costs[r] + stars + solve(i + len)
                );
            }
        }

        return ans;
    }
}
```

---

## Time Complexity

Let

* `N` = length of the string
* `R` = number of rules
* `L` = maximum rule length

Each recursive call checks every rule and compares at most `L` characters.

Since many suffixes are recomputed,

**Time Complexity:** `Exponential`

---

**Space Complexity:** `O(N)` (recursion stack)

---

# Why is Recursion Slow?

The same suffix is solved repeatedly.

For example, `solve(7)` may be reached from multiple earlier recursive paths.

Every time, the entire remaining string is recomputed. This is the overlapping subproblem that Dynamic Programming removes.

---

# Approach 2: Memoization (Top-Down DP)

## Idea

Notice that the answer depends only on `i`

Therefore, store `dp[i]` where 

> `dp[i]` = minimum cost required to transform `source[i...]` into `target[i...]`.

Before solving, check whether `dp[i]` has already been computed.

If yes, return it immediately.

---

## Memoization Code

```java
class Solution {

    static final long INF = (long) 1e18;

    String s, t;
    List<List<String>> rules;
    int[] costs;
    int n;

    Long[] dp;

    public int minCost(String source, String target,
                       List<List<String>> rules,
                       int[] costs) {

        this.s = source;
        this.t = target;
        this.rules = rules;
        this.costs = costs;
        this.n = source.length();

        dp = new Long[n + 1];

        long ans = solve(0);

        return ans == INF ? -1 : (int) ans;
    }

    private long solve(int i) {

        if (i == n)
            return 0;

        if (dp[i] != null)
            return dp[i];

        long ans = INF;

        if (s.charAt(i) == t.charAt(i))
            ans = solve(i + 1);

        for (int r = 0; r < rules.size(); r++) {

            String patt = rules.get(r).get(0);
            String repl = rules.get(r).get(1);

            int len = patt.length();

            if (i + len > n)
                continue;

            boolean ok = true;
            int stars = 0;

            for (int j = 0; j < len; j++) {

                char p = patt.charAt(j);

                if (p == '*')
                    stars++;
                else if (p != s.charAt(i + j)) {
                    ok = false;
                    break;
                }

                if (repl.charAt(j) != t.charAt(i + j)) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                ans = Math.min(
                        ans,
                        costs[r] + stars + solve(i + len)
                );
            }
        }

        return dp[i] = ans;
    }
}
```

---

## Time Complexity

There are only `N` DP states.

Each state checks
* all `R` rules
* up to `L` characters per rule

Therefore, `O(N × R × L)`

---

## Space Complexity

DP array: `O(N)`

Recursion stack: `O(N)`

Total: `O(N)`

---

# Approach 3: Bottom-Up Tabulation

Instead of solving suffixes recursively, we compute them iteratively from the end of the string toward the beginning.

---

# DP Table Formation

Let `dp[i]` represent 

> Minimum cost required to transform `source[i...]` into `target[i...]`

This is exactly the same state used in memoization.

---

## Table Size

We only need one dimension.

`dp[n + 1]`

where

* `dp[i]` stores the answer for suffix `i`
* `dp[n]` represents the empty suffix

---

## Base Case

When `i == n` both strings have been completely processed. No additional cost is needed.

`dp[n] = 0;`

Every other state is initially unreachable.

`Arrays.fill(dp, INF);`

---

## Why Iterate Backwards?

Every transition depends on `dp[i + 1]` or `dp[i + patternLength]`

Both represent suffixes that start **after** the current index.

Therefore, before computing `dp[i]`, all later suffixes must already be known.

Hence we iterate `i = n - 1 ... 0`

---

## Transition

### Option 1

If the current characters already match, `dp[i] = dp[i+1]`

### Option 2

For every valid rule,

```java
dp[i] = min(
    dp[i],
    ruleCost
    +
    wildcardCount
    +
    dp[i+patternLength]
);
```

---

## Tabulation Code

```java
class Solution {

    static final long INF = (long) 1e18;

    public int minCost(String source, String target,
                       List<List<String>> rules,
                       int[] costs) {

        int n = source.length();

        long[] dp = new long[n + 1];
        Arrays.fill(dp, INF);

        dp[n] = 0;

        for (int i = n - 1; i >= 0; i--) {

            if (source.charAt(i) == target.charAt(i))
                dp[i] = dp[i + 1];

            for (int r = 0; r < rules.size(); r++) {

                String patt = rules.get(r).get(0);
                String repl = rules.get(r).get(1);

                int len = patt.length();

                if (i + len > n)
                    continue;

                boolean ok = true;
                int stars = 0;

                for (int j = 0; j < len; j++) {

                    char p = patt.charAt(j);

                    if (p == '*')
                        stars++;
                    else if (p != source.charAt(i + j)) {
                        ok = false;
                        break;
                    }

                    if (repl.charAt(j) != target.charAt(i + j)) {
                        ok = false;
                        break;
                    }
                }

                if (ok && dp[i + len] != INF) {
                    dp[i] = Math.min(
                            dp[i],
                            costs[r] + stars + dp[i + len]
                    );
                }
            }
        }

        return dp[0] == INF ? -1 : (int) dp[0];
    }
}
```

---

# Understanding the DP

Suppose `source = "abcd"target = "abxy"`

Initially, `dp[4] = 0` because the empty suffix requires no transformations.

Now we process indices from right to left.

At index `3`, we check whether `'d'` already matches `'y'`.

If not, we try every rule that can transform the suffix beginning at index `3`.

Next, we compute `dp[2]`, using already computed values like `dp[3]` or `dp[4]`.

Eventually, `dp[0]` stores the minimum cost required to transform the entire source string into the target string.

---

# Complexity Comparison

| Approach    | Time             | Space    |
| ----------- | ---------------- | -------- |
| Recursion   | Exponential      | **O(N)** |
| Memoization | **O(N × R × L)** | **O(N)** |
| Tabulation  | **O(N × R × L)** | **O(N)** |

---

# Key Takeaways

* This is a **1D Dynamic Programming** problem because each state depends only on the current suffix index.
* At every position, either continue for free if the characters already match or apply a valid transformation rule.
* Memoization eliminates repeated computation of the same suffix, reducing the complexity from exponential to `O(N × R × L)`.
* Tabulation uses the same state definition and transitions, filling the DP array from right to left.
* The final answer is stored in `dp[0]`, representing the minimum cost to transform the entire source string into the target string.
