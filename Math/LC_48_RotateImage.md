# 48. Rotate Image

## Description: 

You are given an n x n 2D matrix representing an image, rotate the image by 90 degrees (clockwise).

You have to rotate the image in-place, which means you have to modify the input 2D matrix directly. DO NOT allocate another 2D matrix and do the rotation.

## 1. Layer-by-Layer Approach:

We process the matrix in **layers (or rings)**:

- Outer layer → then inner layer → and so on
- Total layers = `n / 2`

## Intution:

The key idea:
- A rotation can be seen as **cyclic swaps of 4 elements at a time**.
- Instead of moving one element at a time, we rotate **groups of 4 corresponding positions**.

For any position `(i, j)`, the 4 elements involved in rotation are:

```
Top        → (i, j)
Left       → (n - j - 1, i)
Bottom     → (n - i - 1, n - j - 1)
Right      → (j, n - i - 1)
```

These positions form a cycle:

```
Left → Top → Right → Bottom → Left
```

So we rotate them like:
```
Top = Left
Left = Bottom
Bottom = Right
Right = Top (saved in temp)
```

## Step-by-Step Breakdown

### 1. Loop through layers
```java
for (int i = 0; i < n / 2; i++)
````

* `i` represents the **current layer**
* Outer layer → `i = 0`
* Move inward as `i` increases

---

### 2. Loop through elements in the current layer

```java
for (int j = i; j < n - i - 1; j++)
```

* `j` moves across the row within the current layer
* Stops before the last element to avoid double rotation

---

### 3. Perform 4-way swap

#### Save top element

```java
int temp = m[i][j];
```

#### Move Left → Top

```java
m[i][j] = m[n - j - 1][i];
```

#### Move Bottom → Left

```java
m[n - j - 1][i] = m[n - i - 1][n - j - 1];
```

#### Move Right → Bottom

```java
m[n - i - 1][n - j - 1] = m[j][n - i - 1];
```

#### Move Top (temp) → Right

```java
m[j][n - i - 1] = temp;
```

## Code Implementation:
```java
class Solution {
    public void rotate(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n / 2; i++) {
            for (int j = i; j < n - i - 1; j++) {
                int temp = m[i][j];
                m[i][j] = m[n - j - 1][i];
                m[n - j - 1][i] = m[n - i - 1][n - j - 1];
                m[n - i - 1][n - j - 1] = m[j][n - i - 1];
                m[j][n - i - 1] = temp;
            }
        }
    }
}
```

## Complexity

* **Time:** O($n^2$) (we visit each element once)
* **Space:** O(1) (in-place rotation)

## Key Takeaways

* Rotation is done via **4-way cyclic swaps**
* Work **layer by layer**
* No extra matrix needed → **in-place optimization**
* Index manipulation is the core trick
