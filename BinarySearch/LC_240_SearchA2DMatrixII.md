# 240. Search a 2D Matrix II

## Description: 

Write an efficient algorithm that searches for a value target in an m x n integer matrix 'matrix'. 

This matrix has the following properties:

- Integers in each row are sorted in ascending from left to right.
- Integers in each column are sorted in ascending from top to bottom.

## Step-by-step plan:

### 1. Use Bruteforce: O(m*n)

- Check every single element in the matrix until you find the target.
- Worst case: target is not present → you scan all m * n elements.
- Best case: target is the first element → O(1).

```java
public boolean searchMatrix(int[][] matrix, int target) {
    for(int c=0; c<matrix.length; c++){
        for(int r = 0; r<matrix[0].length; r++){
            if(matrix[r][c]==target) return true;
        }
    }    
    return false;
}
```

### 2. Per row linear search: O(m*n)

### 3. Per row binary search: O(m*log n)

- Scan for all the rows and perform binary search in each one of them.

```java
public boolean searchMatrix(int[][] matrix, int target) {
    for(int r =0; r<matrix.length; r++){
        int low = 0, high = matrix[0].length-1;
        while(low<=high){
            int mid = low + (high-low)/2;
            if(matrix[r][mid]==target) return true;
            else if(matrix[r][mid] < target) low = mid+1;
            else if(matrix[r][mid] > target) high = mid-1;
        }
    }
    return false;
}
```

### 4. Staircase Search: O(m+n)
    
- If we start searching from the Top-Left (0,0), moving right increases the value, and moving down also increases the value. We don't have a clear decision to make if the current number is smaller than the target.

- However, if we start from the Top-Right corner (0, n-1):

    - Moving Left ← leads to smaller numbers.
    - Moving Down ↓ leads to larger numbers.
    - This setup works exactly like a Binary Search Tree (BST)! The top-right element is the root; left children are smaller, right children (visually down) are larger. This allows us to eliminate an entire row or column at each step.

- Start Point: Initialize a pointer at the top-right corner: row = 0, col = matrix[0].length - 1.
- Traverse: While the pointer is within the matrix bounds:
- Found: If matrix[row][col] == target, return true.
- Too Big: If matrix[row][col] > target, the current value is too large. Since the column is sorted, every element below this current cell is also larger than the target. We can safely discard this entire column by moving left (col--).
- Too Small: If matrix[row][col] < target, the current value is too small. Since the row is sorted, every element to the left of this current cell is also smaller than the target. We can safely discard this entire row by moving down (row++).
- Not Found: If we exit the loop (indices go out of bounds), the target is not in the matrix. Return false.

```java
public boolean searchMatrix(int[][] matrix, int target) {
    int rows = matrix.length;
    int cols = matrix[0].length;

    int r=0;
    int c=cols-1;

    while(r<rows && c>=0){
        if(matrix[r][c]==target) return true;
        else if(matrix[r][c] > target) c--;
        else r++;
    }
    return false;
}
```