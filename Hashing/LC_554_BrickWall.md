# 554. Brick Wall

## Description: 

There is a rectangular brick wall in front of you with n rows of bricks. The ith row has some number of bricks each of the same height (i.e., one unit) but they can be of different widths. The total width of each row is the same.

Draw a vertical line from the top to the bottom and cross the least bricks. If your line goes through the edge of a brick, then the brick is not considered as crossed. You cannot draw a line just along one of the two vertical edges of the wall, in which case the line will obviously cross no bricks.

Given the 2D array wall that contains the information about the wall, return the minimum number of crossed bricks after drawing such a vertical line.

## Step-by-step plan:

- We want to maximize the number of gaps we pass through, because each gap means we avoid cutting a brick. 
- If we count how many times each gap position appears across all rows, the position with the most gaps is the best place to draw our line. 
- The answer is then total rows minus the maximum gap count.

- Use a hash map to count the frequency of each gap position across all rows. (Key: gap no. Value: count of gap)
- For each row, compute cumulative brick widths (excluding the last brick to avoid counting the wall edge).
- For each cumulative width, increment its count in the hash map.
- Find the maximum count in the hash map (the position with the most aligned gaps).
- Return the total number of rows minus this maximum count.

## Code: 
```java
class Solution {
    // Bruteforce- go through every single posn
    // Optimize: count gaps -> then maximize it
    // key = gap no., value = count of gap
    public int leastBricks(List<List<Integer>> wall) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);

        for (List<Integer> row : wall) {
            int total = 0;
            for (int i = 0; i < row.size() - 1; i++) {
                total += row.get(i);
                map.put(total, map.getOrDefault(total, 0) + 1);
            }
        }

        int maxGaps = 0;
        for (int count : map.values()) {
            maxGaps = Math.max(maxGaps, count);
        }
        return wall.size() - maxGaps;
    }
}
```

- Time Complexity: O(n) - Same for all cases

- Space Complexity: O(g)
Where n is the total number of bricks in the wall and g is the total number of gaps in all the rows.