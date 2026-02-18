# 327. Count of Range Sum  

## Description:  

Given an integer array nums and two integers lower and upper, return the number of range sums that lie in [lower, upper] inclusive.

Range sum S(i, j) is defined as the sum of the elements in nums between indices i and j inclusive, where i <= j.

## Step-by-step plan:

### 1. Naive Bruteforce (Three Nested Loops): O(n³)

- A range sum means:
  - Pick starting index `i`
  - Pick ending index `j`
  - Compute sum of elements from `i` to `j`

- Steps:
  1. Fix starting index `i`
  2. Fix ending index `j`
  3. Compute sum using another loop from `i` to `j`
  4. If sum lies in [lower, upper], increment count

- Time Complexity:
  - Outer loop → O(n)
  - Middle loop → O(n)
  - Inner sum loop → O(n)
  - Overall → **O(n³)**

- Space Complexity: O(1)

- This solution works but will cause TLE for large inputs.

```java
class Solution {
    public int countRangeSum(int[] nums, int lower, int upper) {
        // range sum = sum of elements in range (i, j) inclusive
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i; j < nums.length; j++) {
                long sum = 0;
                for (int k = i; k <= j; k++) {
                    sum += nums[k];
                }
                if (sum >= lower && sum <= upper)
                    count++;
            }
        }
        return count;
    }
}
```

### 2. Optimized Bruteforce (Two Nested Loops): O(n²)

- Instead of recalculating the sum from scratch for every (i, j),
  we maintain a running sum.

- Idea:
  1. Fix starting index `i`
  2. Initialize `sum = 0`
  3. Expand ending index `j` from `i` to end
  4. Keep adding `nums[j]` to `sum`
  5. Check if sum lies in [lower, upper]

- Why better?
  - We remove the third loop.
  - No repeated summation of the same elements.

- Time Complexity:
  - Outer loop → O(n)
  - Inner loop → O(n)
  - Overall → **O(n²)**

- Space Complexity: O(1)

- Still may cause TLE for very large inputs,
  but much better than O(n³).

```java
class Solution {
    public int countRangeSum(int[] nums, int lower, int upper) {
        // range sum = sum of elements in range (i, j) inclusive
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            long sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                if (sum >= lower && sum <= upper)
                    count++;
            }
        }
        return count;
    }
}
```

### 3. Divide and Conquer (Merge Sort + Prefix Sum): O(n log n)

- Key Observation:

  Range sum from i to j:
      sum(i, j) = prefix[j + 1] - prefix[i]

- So the condition:
      lower ≤ sum(i, j) ≤ upper

  becomes:
      lower ≤ prefix[j] - prefix[i] ≤ upper

  Rearranging:
      prefix[j] must lie in:
      [prefix[i] + lower, prefix[i] + upper]

### Step 1: Build Prefix Sum Array

- prefix[0] = 0
- prefix[i+1] = prefix[i] + nums[i]
- This converts the problem into counting valid pairs (i, j)
  in the prefix array.

### Step 2: Use Merge Sort (Divide and Conquer)

Why merge sort?

- During merge sort:
  - Left half is sorted
  - Right half is sorted
- We can efficiently count valid pairs across halves
  using two pointers.

For each prefix[i] in left half:
  - Move pointer k to find first prefix[k] such that:
        prefix[k] - prefix[i] ≥ lower
  - Move pointer j to find first prefix[j] such that:
        prefix[j] - prefix[i] > upper
  - Number of valid values = (j - k)

Because both halves are sorted,
we can move pointers only forward → O(n) per level.

### Complexity:

- Each level of merge sort → O(n)
- Total levels → O(log n)
- Overall Time Complexity → O(n log n)
- Space Complexity → O(n)

```java
class Solution {
    // lower ≤ prefix[j] - prefix[i] ≤ upper
    public int countRangeSum(int[] nums, int lower, int upper) {
        // Step 1: Build prefix sum
        long[] prefix = new long[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        return mergeSort(prefix, 0, prefix.length - 1, lower, upper);
    }

    private int mergeSort(long[] arr, int l, int r, int lower, int upper) {
        if (l >= r) return 0;
        int mid = l + (r - l) / 2;
        int count = 0;

        count += mergeSort(arr, l, mid, lower, upper);
        count += mergeSort(arr, mid + 1, r, lower, upper);

        int j = mid + 1;
        int k = mid + 1;

        // Count valid cross-boundary pairs
        for (int i = l; i <= mid; i++) {
            while (k <= r && arr[k] - arr[i] < lower) k++;
            while (j <= r && arr[j] - arr[i] <= upper) j++;
            count += (j - k);
        }
        merge(arr, l, mid, r);
        return count;
    }
    private void merge(long[] arr, int l, int mid, int r) {
        long[] temp = new long[r - l + 1];
        int i = l, j = mid + 1, t = 0;
        while (i <= mid && j <= r) {
            if (arr[i] <= arr[j]) {
                temp[t++] = arr[i++];
            } else {
                temp[t++] = arr[j++];
            }
        }

        while (i <= mid) temp[t++] = arr[i++];
        while (j <= r) temp[t++] = arr[j++];

        System.arraycopy(temp, 0, arr, l, temp.length);
    }
}
```