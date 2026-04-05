# 1598. Crawler Log Folder

## Description:

The Leetcode file system keeps a log each time some user performs a change folder operation.

The operations are described below:

- "../" : Move to the parent folder of the current folder. (If you are already in the main folder, remain in the same folder).
- "./" : Remain in the same folder.
- "x/" : Move to the child folder named x (This folder is guaranteed to always exist).
You are given a list of strings logs where logs[i] is the operation performed by the user at the ith step.

The file system starts in the main folder, then the operations in logs are performed.

Return the minimum number of operations needed to go back to the main folder after the change folder operations.

## Intution:

We can think of a file path like a stack.

- When we go into a folder, we **add (push)** it to the stack.
- When we go back to the parent folder, we **remove (pop)** from the stack.
- The command `"./"` means **stay in the same place**, so we do nothing.

At the end, the number of items in the stack tells us how deep we are inside folders.  
This number is also the **minimum steps needed to go back to the main folder**.

```java
class Solution {
    public int minOperations(String[] logs) {
        Stack<String> st = new Stack<>();
        for (String log : logs) {
            if (log.equals("../")) {
                if (!st.isEmpty()) {
                    st.pop();
                }
            } else if (!log.equals("./")) {
                st.push(log);
            }
        }
        return st.size();
    }
}
```

- Time Complexity: O(n)
- Space Complexity: O(n)