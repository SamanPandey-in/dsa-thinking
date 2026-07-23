# Minimum Path Sum With Grid Switches

**Source and Credits: [Tushar Sharma](https://github.com/TusharSharma77)** 

![Question Image](/OA/assets/MinPathSumWGridSwitches.jpg)

## Description:

You are given two `m × n` grids `grid1` and `grid2`, and a `switchCost`.

You start at `(0,0)` and can move only right or down. At any cell, you may also switch to the other grid by paying `switchCost`.

Find the **minimum path sum** from `(0,0)` to `(m-1, n-1)`, where the path sum includes all visited cell values and any switching costs paid.

---

## Solution Overview:

This is a classic **Two-Layer Grid DP** problem.

Since movement is only right/down, there are no cycles in the graph. Cells can be processed in increasing order of row and column.

The main challenge is handling the switch operation elegantly. Once we realise that each cell has two independent states (standing on grid1 or grid2), the DP becomes mechanical.

We'll derive the solution step by step:

1. Pure Recursion
2. Memoization
3. Bottom-Up Tabulation
4. Space Optimization

---

# Key Observation

Consider the grid as two parallel layers.

At any cell `(i, j)`, you are either:

* walking on **grid1** (top layer)
* walking on **grid2** (bottom layer)

From each layer you can take three actions:

| Action        | Cost                                                 |
| ------------- | ---------------------------------------------------- |
| Move right    | next cell's value (same layer)                       |
| Move down     | next cell's value (same layer)                       |
| Switch layer  | switchCost + next cell's value (opposite layer)      |

Switching does **not** charge the current cell twice. You pay `switchCost` once and continue on the other grid.

This is equivalent to having two DP tables where each cell consults its four possible predecessors:

* top neighbour on the same layer
* left neighbour on the same layer
* top neighbour on the opposite layer (plus switchCost)
* left neighbour on the opposite layer (plus switchCost)

---

# DP State

Let `dp1[i][j]` represent

> Minimum path sum from `(0,0)` to `(i,j)` while currently standing on grid1.

Similarly, `dp2[i][j]` represents

> Minimum path sum from `(0,0)` to `(i,j)` while currently standing on grid2.

The value includes all visited cell costs along the path plus any switching costs paid.

---

# Transition

Suppose we are computing `dp1[i][j]`.

We could have arrived at `(i,j)` on grid1 in four ways:

| Way          | Description                                        |
| ------------ | -------------------------------------------------- |
| Top (same)   | Come from `(i-1, j)` on grid1                      |
| Left (same)  | Come from `(i, j-1)` on grid1                      |
| Top (switch) | Come from `(i-1, j)` on grid2, then switch         |
| Left (switch)| Come from `(i, j-1)` on grid2, then switch         |

Therefore,

```
dp1[i][j] = grid1[i][j] + min(
    dp1[i-1][j],                // top, same layer
    dp1[i][j-1],                // left, same layer
    dp2[i-1][j] + switchCost,   // top, switched from grid2
    dp2[i][j-1] + switchCost    // left, switched from grid2
)
```

Similarly for grid2:

```
dp2[i][j] = grid2[i][j] + min(
    dp2[i-1][j],                // top, same layer
    dp2[i][j-1],                // left, same layer
    dp1[i-1][j] + switchCost,   // top, switched from grid1
    dp1[i][j-1] + switchCost    // left, switched from grid1
)
```

---

# Initialisation

At the starting cell `(0,0)` no previous moves exist.

```
dp1[0][0] = min(grid1[0][0], grid2[0][0] + switchCost)
dp2[0][0] = min(grid2[0][0], grid1[0][0] + switchCost)
```

The `min` captures the option to switch immediately at the start before any movement.

---

# Approach 1: Pure Recursion

## State

Define `dfs(i, j, grid)` as

> Minimum path sum from `(i,j)` to the destination while standing on the given grid.

---

## Base Case

If `(i,j)` is out of bounds:

```
return INF
```

If `(i,j)` is the destination:

```
return grid == 0 ? grid1[i][j] : grid2[i][j]
```

---

## Recursive Transition

At state `(i, j, grid)`:

```
cell = (grid == 0) ? grid1[i][j] : grid2[i][j]

stay  = min(dfs(i+1, j, grid), dfs(i, j+1, grid))

switch = switchCost + min(dfs(i+1, j, 1-grid), dfs(i, j+1, 1-grid))

return cell + min(stay, switch)
```

---

## Recursive Code

```java
public class Solution {

    static int[][] grid1, grid2;
    static int m, n, switchCost;
    static final int INF = (int)1e9;

    static int dfs(int i, int j, int grid) {
        // Out of bounds
        if (i >= m || j >= n)
            return INF;

        // Destination
        if (i == m - 1 && j == n - 1) {
            return (grid == 0) ? grid1[i][j] : grid2[i][j];
        }

        int cell = (grid == 0) ? grid1[i][j] : grid2[i][j];

        // Stay on same grid
        int stay = Math.min(
                dfs(i + 1, j, grid),
                dfs(i, j + 1, grid)
        );

        // Switch grid
        int switchAns = switchCost + Math.min(
                dfs(i + 1, j, 1 - grid),
                dfs(i, j + 1, 1 - grid)
        );

        return cell + Math.min(stay, switchAns);
    }

    public static int minPathSumWithGridSwitches(int[][] g1,
                                                 int[][] g2,
                                                 int cost) {
        grid1 = g1;
        grid2 = g2;
        switchCost = cost;

        m = grid1.length;
        n = grid1[0].length;

        return Math.min(
                dfs(0, 0, 0),
                dfs(0, 0, 1)
        );
    }

    public static void main(String[] args) {
        int[][] g1 = {
                {1, 3},
                {4, 2}
        };
        int[][] g2 = {
                {2, 1},
                {5, 5}
        };
        System.out.println(minPathSumWithGridSwitches(g1, g2, 3));
    }
}
```

---

## Time Complexity

Each state branches into `4` recursive calls.

Without memoization, the call tree size is exponential.

Overall complexity is `O(4^(m+n))`.

---

## Space Complexity

Recursion depth is `O(m + n)`.

---

# Why Recursion is Slow?

The same state `dfs(i, j, grid)` is reached from many different recursive paths.

Since the answer for a state never changes, we repeatedly perform identical computations.

Dynamic Programming removes this redundancy.

---

# Approach 2: Memoization (Top-Down DP)

## Idea

The recursive state is fully determined by `(i, j, grid)`.

Therefore, store `memo[i][j][grid]` to avoid recomputation.

Before computing a state, simply check whether it already exists.

---

## Memoization Code

```java
import java.util.Arrays;

public class Solution {

    static int[][] grid1, grid2;
    static int m, n, switchCost;

    static int[][][] dp;
    static final int INF = (int) 1e9;

    static int dfs(int i, int j, int grid) {

        if (i >= m || j >= n)
            return INF;

        if (i == m - 1 && j == n - 1) {
            return (grid == 0) ? grid1[i][j] : grid2[i][j];
        }

        if (dp[i][j][grid] != -1)
            return dp[i][j][grid];

        int cell = (grid == 0) ? grid1[i][j] : grid2[i][j];

        // Stay on same grid
        int stay = Math.min(
                dfs(i + 1, j, grid),
                dfs(i, j + 1, grid)
        );

        // Switch to other grid
        int sw = switchCost + Math.min(
                dfs(i + 1, j, 1 - grid),
                dfs(i, j + 1, 1 - grid)
        );

        return dp[i][j][grid] = cell + Math.min(stay, sw);
    }

    public static int minPathSumWithGridSwitches(int[][] g1,
                                                 int[][] g2,
                                                 int cost) {
        grid1 = g1;
        grid2 = g2;
        switchCost = cost;

        m = g1.length;
        n = g1[0].length;

        dp = new int[m][n][2];

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                Arrays.fill(dp[i][j], -1);

        return Math.min(
                dfs(0, 0, 0),
                dfs(0, 0, 1)
        );
    }

    public static void main(String[] args) {
        int[][] grid1 = {
                {1, 3},
                {4, 2}
        };
        int[][] grid2 = {
                {2, 1},
                {5, 5}
        };
        int switchCost = 3;

        System.out.println(minPathSumWithGridSwitches(grid1, grid2, switchCost));
    }
}
```

---

## Time Complexity

There are `m × n × 2` unique states.

Each state is computed once.

Time: `O(m × n)`

---

## Space Complexity

Memo table: `O(m × n)`

Recursion stack: `O(m + n)`

---

# Approach 3: Bottom-Up Tabulation

Instead of solving recursively, we fill the DP tables iteratively.

---

# DP Table Formation

Let `dp1[i][j]` and `dp2[i][j]` represent

> Minimum path sum from `(0,0)` to `(i,j)` while ending on grid1 / grid2 respectively.

Exactly the same state as memoization, but computed bottom-up.

---

## Table Size

`dp1[m][n]` and `dp2[m][n]`

---

## Base Case

```
dp1[0][0] = min(grid1[0][0], grid2[0][0] + switchCost)
dp2[0][0] = min(grid2[0][0], grid1[0][0] + switchCost)
```

Every other cell is initialised to INF.

---

## Why Fill Increasing Row and Column?

To compute `dp1[i][j]` we need:

* `dp1[i-1][j]` (previous row, same column)
* `dp1[i][j-1]` (same row, previous column)
* values from the opposite layer at the same neighbours

These are already computed if we iterate `i = 0 → m-1` and `j = 0 → n-1`.

---

## Transition

```
dp1[i][j] = grid1[i][j] + min of the four possible predecessors
dp2[i][j] = grid2[i][j] + min of the four possible predecessors
```

Out-of-bounds cells are treated as INF.

---

## Tabulation Code

```java
import java.util.*;

public class Solution {

    public static int minPathSumWithGridSwitches(int[][] grid1,
                                                 int[][] grid2,
                                                 int switchCost) {
        int m = grid1.length;
        int n = grid1[0].length;

        long INF = Long.MAX_VALUE / 4;

        long[][] dp1 = new long[m][n];
        long[][] dp2 = new long[m][n];

        for (int i = 0; i < m; i++) {
            Arrays.fill(dp1[i], INF);
            Arrays.fill(dp2[i], INF);
        }

        // Start cell
        dp1[0][0] = Math.min(
                grid1[0][0],
                (long)grid2[0][0] + switchCost
        );

        dp2[0][0] = Math.min(
                grid2[0][0],
                (long)grid1[0][0] + switchCost
        );

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (i == 0 && j == 0) continue;

                long best1 = INF;
                long best2 = INF;

                // From top
                if (i > 0) {
                    best1 = Math.min(best1, dp1[i - 1][j]);
                    best1 = Math.min(best1, dp2[i - 1][j] + switchCost);

                    best2 = Math.min(best2, dp2[i - 1][j]);
                    best2 = Math.min(best2, dp1[i - 1][j] + switchCost);
                }

                // From left
                if (j > 0) {
                    best1 = Math.min(best1, dp1[i][j - 1]);
                    best1 = Math.min(best1, dp2[i][j - 1] + switchCost);

                    best2 = Math.min(best2, dp2[i][j - 1]);
                    best2 = Math.min(best2, dp1[i][j - 1] + switchCost);
                }

                dp1[i][j] = best1 + grid1[i][j];
                dp2[i][j] = best2 + grid2[i][j];
            }
        }

        return (int)Math.min(dp1[m - 1][n - 1], dp2[m - 1][n - 1]);
    }

    public static void main(String[] args) {
        int[][] grid1 = {
                {1,3},
                {4,2}
        };
        int[][] grid2 = {
                {2,1},
                {5,5}
        };
        System.out.println(minPathSumWithGridSwitches(grid1, grid2, 3));
    }
}
```

---

## Time Complexity

`O(m × n)` — each cell is processed exactly once.

---

## Space Complexity

`O(m × n)` — two complete DP tables.

---

# Approach 4: Space Optimization

Notice that `dp1[i][j]` and `dp2[i][j]` only depend on:

* previous row, same column (`dpX[i-1][j]`)
* current row, previous column (`dpX[i][j-1]`)

No earlier rows are ever used again.

Therefore, instead of storing the complete table, we only keep:

* `prev1[j]`, `prev2[j]` — minimum cost at column `j` in the **previous row** for grid1 and grid2
* `curr1[j]`, `curr2[j]` — minimum cost at column `j` in the **current row** for grid1 and grid2

---

## Transition

The recurrence remains identical.

After finishing a row, `prev1 = curr1; prev2 = curr2;`

```
curr1[j] = grid1[i][j] + min(
    prev1[j],           // top, same layer
    curr1[j-1],         // left, same layer
    prev2[j] + switchCost,   // top, switched
    curr2[j-1] + switchCost  // left, switched
)

curr2[j] = grid2[i][j] + min(
    prev2[j],           // top, same layer
    curr2[j-1],         // left, same layer
    prev1[j] + switchCost,   // top, switched
    curr1[j-1] + switchCost  // left, switched
)
```

---

## Space Optimised Code

```java
import java.util.Arrays;

public class Solution {

    public static int minPathSumWithGridSwitches(int[][] grid1,
                                                 int[][] grid2,
                                                 int switchCost) {
        int m = grid1.length;
        int n = grid1[0].length;

        long INF = Long.MAX_VALUE / 4;

        long[] prev1 = new long[n];
        long[] prev2 = new long[n];

        Arrays.fill(prev1, INF);
        Arrays.fill(prev2, INF);

        for (int i = 0; i < m; i++) {

            long[] curr1 = new long[n];
            long[] curr2 = new long[n];

            Arrays.fill(curr1, INF);
            Arrays.fill(curr2, INF);

            for (int j = 0; j < n; j++) {

                if (i == 0 && j == 0) {
                    curr1[j] = Math.min(
                            grid1[0][0],
                            (long) grid2[0][0] + switchCost
                    );
                    curr2[j] = Math.min(
                            grid2[0][0],
                            (long) grid1[0][0] + switchCost
                    );
                    continue;
                }

                long best1 = INF;
                long best2 = INF;

                // From top
                if (i > 0) {
                    best1 = Math.min(best1, prev1[j]);
                    best1 = Math.min(best1, prev2[j] + switchCost);

                    best2 = Math.min(best2, prev2[j]);
                    best2 = Math.min(best2, prev1[j] + switchCost);
                }

                // From left
                if (j > 0) {
                    best1 = Math.min(best1, curr1[j - 1]);
                    best1 = Math.min(best1, curr2[j - 1] + switchCost);

                    best2 = Math.min(best2, curr2[j - 1]);
                    best2 = Math.min(best2, curr1[j - 1] + switchCost);
                }

                curr1[j] = best1 + grid1[i][j];
                curr2[j] = best2 + grid2[i][j];
            }

            prev1 = curr1;
            prev2 = curr2;
        }

        return (int) Math.min(prev1[n - 1], prev2[n - 1]);
    }

    public static void main(String[] args) {
        int[][] grid1 = {
                {1, 3},
                {4, 2}
        };
        int[][] grid2 = {
                {2, 1},
                {5, 5}
        };
        int switchCost = 3;

        System.out.println(minPathSumWithGridSwitches(grid1, grid2, switchCost));
    }
}
```

---

## Time Complexity

Exactly the same as tabulation: `O(m × n)`.

---

## Space Complexity

Only two rows per grid are stored.

Space: `O(n)`.

---

# Understanding the DP

Consider building the DP tables cell by cell.

### Example Input

```
grid1 = [[1, 3],
         [4, 2]]

grid2 = [[2, 1],
         [5, 5]]

switchCost = 3
```

---

### Cell (0, 0)

Starting cell. No previous moves.

```
dp1[0][0] = min(1, 2 + 3) = 1
dp2[0][0] = min(2, 1 + 3) = 2
```

We can either start directly on the chosen grid or switch immediately from the other.

---

### Cell (0, 1) — First Row

Only reachable from the left neighbour.

```
Coming to grid1:
  stay (left):    dp1[0][0] = 1
  switch (left):  dp2[0][0] + 3 = 5
  min = 1
dp1[0][1] = 3 + 1 = 4

Coming to grid2:
  stay (left):    dp2[0][0] = 2
  switch (left):  dp1[0][0] + 3 = 4
  min = 2
dp2[0][1] = 1 + 2 = 3
```

---

### Cell (1, 0) — First Column

Only reachable from the top neighbour.

```
Coming to grid1:
  stay (top):     dp1[0][0] = 1
  switch (top):   dp2[0][0] + 3 = 5
  min = 1
dp1[1][0] = 4 + 1 = 5

Coming to grid2:
  stay (top):     dp2[0][0] = 2
  switch (top):   dp1[0][0] + 3 = 4
  min = 2
dp2[1][0] = 5 + 2 = 7
```

---

### Cell (1, 1) — Destination

Reachable from top and left, each with stay or switch.

```
Coming to grid1:
  top (same):      dp1[0][1] = 4
  left (same):     dp1[1][0] = 5
  top (switch):    dp2[0][1] + 3 = 3 + 3 = 6
  left (switch):   dp2[1][0] + 3 = 7 + 3 = 10
  min = 4
dp1[1][1] = 2 + 4 = 6

Coming to grid2:
  top (same):      dp2[0][1] = 3
  left (same):     dp2[1][0] = 7
  top (switch):    dp1[0][1] + 3 = 4 + 3 = 7
  left (switch):   dp1[1][0] + 3 = 5 + 3 = 8
  min = 3
dp2[1][1] = 5 + 3 = 8

Answer = min(6, 8) = 6
```

Exactly the recurrence captures all these possibilities.

---

# Complexity Comparison

| Approach        | Time      | Space                           |
| --------------- | --------- | ------------------------------- |
| Recursion       | O(4^(m+n))| O(m + n)                        |
| Memoization     | O(m × n)  | O(m × n) + O(m + n) stack       |
| Tabulation      | O(m × n)  | O(m × n)                        |
| Space Optimized | O(m × n)  | O(n)                            |

---

# Key Takeaways

* The problem reduces to a **two-layer grid DP** where each cell has two independent states (grid1 / grid2).
* The DP state is `(i, j, grid)` — the minimum cost to reach cell `(i,j)` while standing on the given grid.
* The recurrence considers four possible predecessors: same-layer top/left and switched-layer top/left with added switchCost.
* Memoization eliminates repeated recursive computations, while tabulation computes states iteratively by row.
* Since each cell depends only on the previous row and the current row's left cell, space can be optimised from `O(m × n)` to `O(n)` using rolling arrays.
