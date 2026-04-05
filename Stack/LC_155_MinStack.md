# 155. Min Stack

## Description:

Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.

Implement the MinStack class:

- MinStack() initializes the stack object.
- void push(int val) pushes the element val onto the stack.
- void pop() removes the element on the top of the stack.
- int top() gets the top element of the stack.
- int getMin() retrieves the minimum element in the stack.

You must implement a solution with O(1) time complexity for each function.

## Approach 1 - Bruteforce:

### Intuition:

To find the minimum value, we check all elements in the stack.

A normal stack does not keep track of the minimum value, so:
- We remove each element one by one,
- Keep track of the smallest value,
- Then put all elements back into the stack.

This method is simple to understand, but slow because every `getMin` call goes through the entire stack.

```java
class MinStack {

    private Stack<Integer> stack;

    public MinStack() {
        stack = new Stack<>();
    }

    public void push(int val) {
        stack.push(val);
    }

    public void pop() {
        stack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        Stack<Integer> tmp = new Stack<>();
        int mini = stack.peek();

        while (!stack.isEmpty()) {
            mini = Math.min(mini, stack.peek());
            tmp.push(stack.pop());
        }

        while (!tmp.isEmpty()) {
            stack.push(tmp.pop());
        }

        return mini;
    }
}
```

- Time complexity: O(n) for getMin() and O(1) for other operations.
- Space Complexity: O(n) for getMin() and O(1) for other operations.


## Approach 2 - Two Stacks:

### Intuition:

Instead of checking the whole stack every time to find the minimum, we use an extra stack to track it.

- We keep a second stack (`minStack`) that stores the minimum value at each step.
- When we add a new value, we compare it with the current minimum and push the smaller value onto `minStack`.

This way, the top of `minStack` always holds the minimum value of the main stack,  
so `getMin()` can return the answer in constant time.

### Algorithm:

1. Maintain two stacks:
    - stack → stores all pushed values.
    - minStack → stores the minimum so far at each level.
2. On push(val):
    - Push val onto stack.
    - Compute the new minimum (between val and the current minimum on minStack).
    - Push this minimum onto minStack.
3. On pop():
    - Pop from both stack and minStack to keep them aligned.
4. On top():
    - Return the top of stack.
5. On getMin():
    - Return the top of minStack, which is the current minimum.


```java
class MinStack {

    Stack<Integer> stack, min;

    public MinStack() {
        stack = new Stack<>();
        min = new Stack<>();    
    }
    
    public void push(int val) {
        stack.push(val);
        if (min.empty()) min.push(val);
        else if (min.peek() >= val) min.push(val);
    }
    
    public void pop() {
        int popped = stack.pop();
        if(!min.empty() && min.peek() == popped) {
            min.pop();
        }
    }
    
    public int top() {
        return stack.peek();
    }
    
    public int getMin() {
        return min.peek();
    }
}
```

- Time Complexity: O(1) for all operations
- Space Complexity: O(n)

## Approach 3 - One Stack:

### Intuition:

This method uses only one stack, but stores modified (encoded) values instead of the actual numbers.

- We store the difference between the current value and the minimum value.
- If the value is smaller than the current minimum, the stored difference becomes negative.
  - This tells us that the minimum has changed.

- When removing elements:
  - If we see a negative value, we know it represents a previous minimum,
  - So we decode it to restore the old minimum.

This way, we don’t need a second stack.  
The stack itself keeps track of minimum changes, allowing all operations to run in constant time with less space.

### Algorithm:

1. Maintain:
    - stack → stores encoded values (not the actual numbers)
    - min → stores the current minimum in the stack
2. Push(val):
    - If the stack is empty:
        - Push 0 (difference) and set min = val.
    - Otherwise:
        - Push val - min.
        - If val is the new minimum, update min.
3. Pop():
    - Pop the encoded value.
    - If this value is negative, it means the popped element was the minimum.
        - Restore the previous minimum using the encoded difference.
4. Top():
    - If the encoded value is positive, return encoded + min.
    - If it's negative, the top actual value is simply min.
5. getMin():
    - Return the current min.


```java
class MinStack {
    long min;
    Stack<Long> stack;

    public MinStack() {
        stack = new Stack<>();
    }

    public void push(int val) {
        if (stack.isEmpty()) {
            stack.push(0L);
            min = val;
        } else {
            stack.push(val - min);
            if (val < min) min = val;
        }
    }

    public void pop() {
        if (stack.isEmpty()) return;

        long pop = stack.pop();

        if (pop < 0) min = min - pop;
    }

    public int top() {
        long top = stack.peek();
        if (top > 0) {
            return (int) (top + min);
        } else {
            return (int) min;
        }
    }

    public int getMin() {
        return (int) min;
    }
}
```

- Time Complexity: O(1) for all operations
- Space Complexity: O(n)
