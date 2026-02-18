# 799. Champagne Tower

## Description:

We stack glasses in a pyramid, where the first row has 1 glass, the second row has 2 glasses, and so on until the 100th row.  Each glass holds one cup of champagne.

Then, some champagne is poured into the first glass at the top.  When the topmost glass is full, any excess liquid poured will fall equally to the glass immediately to the left and right of it.  When those glasses become full, any excess champagne will fall equally to the left and right of those glasses, and so on.  (A glass at the bottom row has its excess champagne fall on the floor.)

For example, after one cup of champagne is poured, the top most glass is full.  After two cups of champagne are poured, the two glasses on the second row are half full.  After three cups of champagne are poured, those two cups become full - there are 3 full glasses total now.  After four cups of champagne are poured, the third row has the middle glass half full, and the two outside glasses are a quarter full. 

Now after pouring some non-negative integer cups of champagne, return how full the jth glass in the ith row is (both i and j are 0-indexed.)

Constraints: 0 <= poured <= $10^9$ and 0 <= query_glass <= query_row < 100

## Step-by-step plan:

- One glass poured -> One cup filled. If excess -> starts to spill.
- If a glass at row 'r' and index 'c' is full, it now flows into (r+1, c) and (r+1, c+1)
- Since the max row is 100, we can simulate the process.
- Use a 2D array `tower[r][c]` to store how much champagne each glass holds.
- Start by pouring all champagne into `tower[0][0]`.
- For each glass:
  - If amount > 1.0:
    - Excess = (amount - 1.0) / 2
    - Set current glass to 1.0
    - Add excess to:
      - tower[r+1][c]
      - tower[r+1][c+1]
- Continue row by row until `query_row`.

```java
class Solution {
    public double champagneTower(int poured, int query_row, int query_glass) {
        // 1glass-1cup -> excess spills
        // if a glass at r row and c idx is full. it now flows into (r+1, c) and (r+1, c+1)
        // take the amt of poured in 1st row, calc in 2nd row, then move to 2n drow
        // DP
        // 0 <= query_glass <= query_row < 100
        double[][] tower = new double[102][102];
        tower[0][0] = (double) poured;

        for (int r = 0; r <= query_row; r++) {
            for (int c = 0; c <= r; c++) {
                if (tower[r][c] > 1.0) {
                    double excess = (tower[r][c] - 1.0) / 2.0;
                    tower[r][c] = 1.0;
                    tower[r + 1][c] += excess;
                    tower[r + 1][c + 1] += excess;
                }
            }
        }
        return tower[query_row][query_glass];
    }
}
```

- Time Complexity: O($query\_row^2$) → at most O($100^2$)
- Space Complexity: O(100²)