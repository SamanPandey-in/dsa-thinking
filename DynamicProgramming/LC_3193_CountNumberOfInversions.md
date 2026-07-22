# 3193. Count the Number of Inversions ([Link](https://leetcode.com/problems/count-the-number-of-inversions/))

## Description:

You are given an integer n and a 2D array requirements, where requirements[i] = [endi, cnti] represents the end index and the inversion count of each requirement.

A pair of indices (i, j) from an integer array nums is called an inversion if:

i < j and nums[i] > nums[j]
Return the number of permutations perm of [0, 1, 2, ..., n - 1] such that for all requirements[i], perm[0..endi] has exactly cnti inversions.

Since the answer may be very large, return it modulo 109 + 7.

## Solution Overview:

This problem is a classic **Inversion DP**.

The main challenge is deriving the correct recurrence. Once we understand how inversions change when constructing permutations, the DP becomes almost mechanical.

We'll derive the solution step by step:

1. Pure Recursion
2. Memoization
3. Bottom-Up Tabulation
4. Space Optimization

---

# Key Observation

Suppose we already know all permutations of length `len - 1`

Now we want to construct permutations of length `len` by inserting the new largest element `len - 1` into the existing permutation.

Since it is the largest element, it never creates inversions with elements before it.

Instead, every element **after** it contributes exactly one inversion.

If we insert it

| Position                   | New Inversions |
| -------------------------- | -------------: |
| At the end                 |              0 |
| One position from the end  |              1 |
| Two positions from the end |              2 |
| ...                        |            ... |
| At the front               |        len − 1 |

Therefore, the insertion contributes anywhere from `0` to `len − 1` new inversions.

---

# DP State

Let `f(len, inv)` represent

> Number of permutations of length `len` having exactly `inv` inversions.

---

# Transition

Suppose inserting the largest element creates `add` new inversions.

Then the remaining permutation must already contain `inv - add` inversions.

Therefore, `f(len, inv) = Σ f(len-1, inv-add)` where `0 ≤ add ≤ min(inv, len-1)`

This is the fundamental recurrence.

---

# Prefix Requirements

The problem additionally states that some prefixes must contain an exact number of inversions.

A prefix ending at index `i` has length `len = i + 1`

If a requirement exists, then only one inversion count is valid for that prefix.

So after computing a state, we simply reject it whenever `req[len-1] != -1` and `req[len-1] != inv`

This keeps the recurrence unchanged while eliminating invalid states.

---

# Approach 1: Pure Recursion

## State

Define `dfs(len, inv)` as

> Number of valid permutations of length `len` with exactly `inv` inversions while satisfying every prefix requirement.

---

## Base Case

A permutation of length `0` is unique.

It has exactly `0` inversions. `f(0,0)=1` 

All other inversion counts are impossible. `f(0,x)=0`

---

## Recursive Transition

If the current prefix violates its inversion requirement, immediately return `0`

Otherwise, try every possible insertion position of the largest element.

Each insertion contributes `add` new inversions. 

`answer = Σ dfs(len-1, inv-add)`

---

## Recursive Code

```java
class Solution {
    static final int MOD = 1_000_000_007;
    int[] req;

    int dfs(int len, int inv) {
        // impossible
        if (inv < 0) return 0;

        // base case
        if (len == 0) {
            return inv == 0 ? 1 : 0;
        }

        // if this prefix has a requirement, it must match
        if (req[len - 1] != -1 && req[len - 1] != inv) {
            return 0;
        }

        int ans = 0;

        for (int add = 0; add < len && add <= inv; add++) {
            ans = (ans + dfs(len - 1, inv - add)) % MOD;
        }

        return ans;
    }

    public int numberOfPermutations(int n, int[][] requirements) {
        req = new int[n];
        Arrays.fill(req, -1);

        for (int[] r : requirements) {
            req[r[0]] = r[1];
        }

        int ans = 0;
        for (int inv = 0; inv <= 400; inv++) {
            ans = (ans + dfs(n, inv)) % MOD;
        }
        return ans;
    }
}
```

---

## Time Complexity

Each state branches into `len` possible insertion positions.

Without memoization, the same states are recomputed many times.

Overall complexity is exponential.

---

## Space Complexity

Recursion depth is `O(N)`

---

# Why Recursion is Slow?

The same state `dfs(len, inv)` is reached from many different recursive paths.

Since the answer for a state never changes, we repeatedly perform identical computations.

Dynamic Programming removes this redundancy.

---

# Approach 2: Memoization (Top-Down DP)

## Idea

The recursive state is fully determined by `(len, inv)`

Therefore, store `memo[len][inv]` to avoid recomputation.

Before computing a state, simply check whether it already exists.

---

## Memoization Code

```java
class Solution {
    private static final int MOD = 1_000_000_007;

    int[] req;
    Integer[][] memo;

    private int dfs(int len, int inv) {
        if (inv < 0) return 0;

        // Base case
        if (len == 0) {
            return inv == 0 ? 1 : 0;
        }

        // If there is a requirement for this prefix, it must match
        if (req[len - 1] != -1 && req[len - 1] != inv) {
            return 0;
        }

        if (memo[len][inv] != null) {
            return memo[len][inv];
        }

        long ans = 0;
        for (int add = 0; add < len && add <= inv; add++) {
            ans = (ans + dfs(len - 1, inv - add)) % MOD;
        }

        return memo[len][inv] = (int) ans;
    }

    public int numberOfPermutations(int n, int[][] requirements) {
        req = new int[n];
        Arrays.fill(req, -1);

        for (int[] r : requirements) {
            req[r[0]] = r[1];
        }

        memo = new Integer[n + 1][401];

        long ans = 0;
        for (int inv = 0; inv <= 400; inv++) {
            ans = (ans + dfs(n, inv)) % MOD;
        }

        return (int) ans;
    }
}
```

---

## Time Complexity

There are `(N + 1) × 401` states.

Each state loops over at most `len` possible insertion positions.

Hence, `O(N² × 401)` 

Since `401` is fixed by the constraints, this is effectively `O(N²)`

---

## Space Complexity

Memo table: `O(N × 401)`

Recursion stack: `O(N)`

---

# Approach 3: Bottom-Up Tabulation

Instead of solving recursively, we fill the DP table iteratively.

---

# DP Table Formation

Let `dp[len][inv]` represent

> Number of valid permutations of length `len`
> containing exactly `inv` inversions.

Exactly the same state as memoization.

---

## Table Size

`dp[n+1][401]`

Rows represent `len`
Columns represent `inv`

---

## Base Case

There is exactly one empty permutation. `dp[0][0] = 1;`

Every other state remains `0`

---

## Why Fill Increasing Length?

To compute `dp[len][inv]` we only need `dp[len-1][...]`

Therefore, every row depends solely on the previous row.

We iterate `len = 1 → n`

---

## Transition

For every inversion count, try every insertion position.

`dp[len][inv] = Σ dp[len-1][inv-add]` where `0 ≤ add ≤ min(inv, len-1)`

If the current prefix has a required inversion count,

only that column is allowed.

All other columns remain `0`

---

## Tabulation Code

```java
class Solution {
    private static final int MOD = 1_000_000_007;

    public int numberOfPermutations(int n, int[][] requirements) {
        int[] req = new int[n];
        Arrays.fill(req, -1);

        for (int[] r : requirements) {
            req[r[0]] = r[1];
        }

        int[][] dp = new int[n + 1][401];
        dp[0][0] = 1;

        for (int len = 1; len <= n; len++) {
            for (int inv = 0; inv <= 400; inv++) {

                // This inversion count is not allowed for this prefix.
                if (req[len - 1] != -1 && req[len - 1] != inv)
                    continue;

                long ways = 0;

                for (int add = 0; add < len && add <= inv; add++) {
                    ways += dp[len - 1][inv - add];
                    ways %= MOD;
                }

                dp[len][inv] = (int) ways;
            }
        }

        long ans = 0;
        for (int inv = 0; inv <= 400; inv++) {
            ans = (ans + dp[n][inv]) % MOD;
        }

        return (int) ans;
    }
}
```

---

## Time Complexity

There are `N × 401` states.

Each state iterates over at most `len` possible insertion positions.

Therefore, `O(N² × 401)` or effectively `O(N²)` under the given constraints.

---

## Space Complexity

`O(N × 401)`

---

# Approach 4: Space Optimization

Notice that `dp[len][...]` only depends upon `dp[len-1][...]`

No earlier rows are ever used again.

Therefore, instead of storing the complete table, we only keep

* previous row
* current row

---

## Transition

The recurrence remains identical.

`curr[inv] = Σ prev[inv-add]`

After finishing one length, `prev = curr;`

---

## Space Optimized Code

```java
class Solution {
    private static final int MOD = 1_000_000_007;

    public int numberOfPermutations(int n, int[][] requirements) {
        int[] req = new int[n];
        Arrays.fill(req, -1);

        for (int[] r : requirements) {
            req[r[0]] = r[1];
        }

        int[] prev = new int[401];
        prev[0] = 1;

        for (int len = 1; len <= n; len++) {
            int[] curr = new int[401];

            for (int inv = 0; inv <= 400; inv++) {

                // This inversion count is not allowed for this prefix.
                if (req[len - 1] != -1 && req[len - 1] != inv)
                    continue;

                long ways = 0;
                for (int add = 0; add < len && add <= inv; add++) {
                    ways = (ways + prev[inv - add]) % MOD;
                }

                curr[inv] = (int) ways;
            }

            prev = curr;
        }

        long ans = 0;
        for (int inv = 0; inv <= 400; inv++) {
            ans = (ans + prev[inv]) % MOD;
        }

        return (int) ans;
    }
}
```

---

## Time Complexity

Exactly the same as tabulation. `O(N² × 401)`

---

## Space Complexity

Only two rows are stored. `O(401)` which is effectively `O(1)` since `401` is a fixed constant.

---

# Understanding the DP

Consider building permutations incrementally.

### Length = 1

Only one permutation exists.

`[0]`

It contains `0` inversions.

---

### Length = 2

Insert `1` into `[0]`

Two choices exist. 
- `[0,1]` creates `0` new inversions.

- `[1,0]` creates `1` new inversion.

Thus, 
```
dp[2][0] += dp[1][0]

dp[2][1] += dp[1][0]
```

---

### Length = 3

Now insert `2` into every valid permutation of length two.

Each insertion can contribute `0` and `1` or `2` new inversions.

For example, `[0,1]` becomes

```
[0,1,2]   +0 inversions

[0,2,1]   +1 inversion

[2,0,1]   +2 inversions
```

Exactly the recurrence `dp[len][inv] = Σ dp[len-1][inv-add]` captures all these possibilities.

Whenever a prefix requirement exists, only the inversion count matching that requirement is allowed.

All other DP states are discarded.

---

# Complexity Comparison

| Approach        | Time            | Space                           |
| --------------- | --------------- | ------------------------------- |
| Recursion       | Exponential     | **O(N)**                        |
| Memoization     | **O(N² × 401)** | **O(N × 401)** + **O(N)** stack |
| Tabulation      | **O(N² × 401)** | **O(N × 401)**                  |
| Space Optimized | **O(N² × 401)** | **O(401)**                      |

---

# Key Takeaways

* The problem is built on the classic **inversion-count DP** recurrence obtained by inserting the largest element into smaller permutations.
* The DP state is `(length, inversionCount)`, representing the number of valid permutations of a given length with an exact inversion count.
* Prefix constraints do not change the recurrence. They simply invalidate states whose inversion count doesn't match the required value for that prefix.
* Memoization eliminates repeated recursive computations, while tabulation computes states iteratively by increasing permutation length.
* Since each row depends only on the previous row, the DP can be optimized from `O(N × 401)` space to `O(401)` using rolling arrays.
