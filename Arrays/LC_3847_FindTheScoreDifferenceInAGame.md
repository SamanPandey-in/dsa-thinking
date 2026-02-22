# 3847. Find the Score Difference in a Game

## Description:

You are given an integer array nums, where nums[i] represents the points scored in the ith game.

There are exactly two players. Initially, the first player is active and the second player is inactive.

The following rules apply sequentially for each game i:

- If nums[i] is odd, the active and inactive players swap roles.
- In every 6th game (that is, game indices 5, 11, 17, ...), the active and inactive players swap roles.
- The active player plays the ith game and gains nums[i] points.
- Return the score difference, defined as the first player's total score minus the second player's total score.

## Step-by-step plan:

- We'll just follow what's given in the description.
- To keep context of which player is inactive We keep our boolean variable `isFirstActive`.  
- If it is true, then the first is active; else if it is false, then the second player is active. 
- Then, according to the player active, we will increase their scores and then return their difference. 

```java
class Solution {
    public int scoreDifference(int[] nums) {
        //odd-> swap roles <- eevry 6th game
        //ans= first-second
        int firstScore = 0, secondScore = 0;

        boolean isFirstActive = true;

        for (int i = 0; i < nums.length; i++) {
            //odd
            if (nums[i] % 2 != 0) isFirstActive = !isFirstActive;

            // 6th game
            if (i % 6 == 5) isFirstActive = !isFirstActive;

            if (isFirstActive) firstScore += nums[i];
            else secondScore += nums[i];
        }
        return firstScore - secondScore;
    }
}
```

- Time Complexity: O(n) - Same for all cases
- Space Complexity: O(1)