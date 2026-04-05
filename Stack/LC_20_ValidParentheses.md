# 20. Valid Parentheses

## Description:

Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.

An input string is valid if:

Open brackets must be closed by the same type of brackets.
Open brackets must be closed in the correct order.
Every close bracket has a corresponding open bracket of the same type.

## Approach 1 - Stack but hard-coding:

### Intuition:

We use a stack-like structure to keep track of opening brackets.

- When we see an opening bracket `(`, `[`, or `{`, we store it.
- When we see a closing bracket `)`, `]`, or `}`, we check the most recent opening bracket.
  - If there is no opening bracket, the string is invalid.
  - If the types don’t match (e.g., `(` with `]`), it is invalid.
- At the end, if no unmatched opening brackets remain, the string is valid.

This works because brackets must close in the correct order (last opened, first closed).

### Algorithm:

1. Create an empty list (used like a stack).
2. Traverse each character in the string:
   - If it is an opening bracket, add it to the list.
   - If it is a closing bracket:
     - If the list is empty, return `false`.
     - Remove the last element from the list.
     - Check if it matches the current closing bracket:
       - If not, return `false`.
3. After the loop, check:
   - If the list is empty → return `true`.
   - Otherwise → return `false`.

```java
class Solution {
    public boolean isValid(String s) {
        ArrayList<Character> st=new ArrayList<>();
        for(int i=0; i<s.length(); i++){
            char c=s.charAt(i);
            if(c=='('||c=='['||c=='{'){
                st.add(c);
            }
            else if(c==')'||c==']'||c=='}'){
                if(st.isEmpty()){
                    return false;
                }else{
                    char temp=st.remove(st.size()-1);
                    if( (c==')' && temp!='(') || (c==']' && temp!='[') || (c=='}' && temp!='{') ){
                        return false;
                    }
                }
            }
        }
        return st.isEmpty();
    }
}
```

- Time Complexity: O(n)
- Space Complexity: O(n)


## Approach 2 - Stack with HashMap:

### Intuition:

Instead of hardcoding the conditions, we can use a hashmap to store the brackets and their match. 
And while checking them, simply use the hashmap. 

This improves the code readability and maintainabililty.

### Algorithm:

1. Create a stack to store opening brackets.
2. For each character c in the string:
    - If it is an opening bracket, push it onto the stack.
    - If it is a closing bracket:
        - Check if the stack is not empty and its top matches the corresponding opening bracket.
        - If yes, pop the stack.
        - Otherwise, return false.
3. After processing all characters:
    - If the stack is empty, return true.
    - Otherwise, return false.


```java
class Solution {
    public boolean isValid(String s) {
        Stack<Character> st = new Stack<>();
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');

        for (char c : s.toCharArray()) {
            if (map.containsKey(c)) {
                if (!st.isEmpty() && st.peek() == map.get(c)) {
                    st.pop();
                } else return false;
            } else st.push(c);
        }
        return st.isEmpty();
    }
}
```

- Time Complexity: O(n)
- Space Complexity: O(n)
