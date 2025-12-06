- Use a method to retrieve fields and methods with "."
    - M = LangUtil.getMethod
    - F = LangUtil.getField
    - obj.method() -> M(obj, "method").apply()
    - obj.field -> F(obj, "field")
    - a.b(args1).c.d(args2).e.f -> F(F(M(F(M(a, "b").apply(args1), "c"), "d").apply(args2), "e"), "f")

    - last dot
        - rhs = [f] or [f, ()]

        - case last tok is expr:
            - id = rhs[:-1] -> [f]
            - args = compileArgs rhs[-1][1:-1]
            - lhs = compileExpr a.b(args1).c.d(args2).e
            - M(lhs, "id").apply(args)
        - else:
            - id = rhs
            - lhs = compileExpr a.b(args1).c.d(args2).e
            - F(lhs, "id")

    - function()
    - 

- Separate statement and expression null checks
    - Null checks currently don't work if the function returns void
- Change this to self (?)
- Add global aliases to precompilation
- Fix java lambdas (->)
    - Fix ones where arguments have no types ((a, b) -> a + b), or there is only one argument (x -> x)
- Add support for implementing and extending classes
- Add single line functions
    Either of:
    - int add(int a, int b) => a + b
    - int add(int a, int b): a + b
- Add more operator overloads
    - string * int
    - char * int
- Add custom operator overloading
    - Use charcode(s) of operator for custom operators ($ => oper36, @ => oper64)
- Add /* */
- F-strings or `` strings (TEMPLATE_STRING token)
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
- Add more operators to overload
    - +, -, *, /, %
- Add "elem in container"
    - in, inside, notin, outside
- Add precompilation and custom regex
    - Allow customisation of the language itself through regex
- Fix operEq not working with null objects
