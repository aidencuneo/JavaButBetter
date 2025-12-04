- Add "elem in container"
- Fix java lambdas (->)
- Add support for implementing and extending classes
- Add single line functions
    Either of:
    - int add(int a, int b) => a + b
    - int add(int a, int b): a + b
- Add more operator overloads
    - string * int
    - char * int
- Add more operators to overload
    - !, +, -, *, /, %
- Add operator overloading
    - Use charcode(s) of operator for custom operators ($ => oper36, @ => oper64)
- Add /* */
- F-strings or `` strings
- Mutable for loops
    - for i to arr.length => for (var i = 0; i < (arr.length); i += 1)
    - for i to 0..length
    - for i to 0..length..1
    - for i to length
    - Allow for (let i = 10; i; --i) (?)
- set/get
    - get name: => public (type of name) _get_name() {}
    - set name: => public void _set_name((type of name) value) {}
- Add lambdas (?)
    - str = (s => s.toUpperCase())("hello")
    - Define functions called _anon0, _anon1, etc. in the current class and replace the lambda with it
    - Upon further consideration, I don't think this will work very well



Done:
- Add null conditional operator
    - name?.operation()?.niceness => ???
- Fix automatic var insertion
    - Delete locals every time scope decreases
- Add @Override (!)
- Add decorators
- Automatic "new" insertion for pascal case function calls
