# Mock PA2 Walkthrough

> 💡 **Protected** variables and methods can be accessed by subclasses of a class.
## Level 1

`Num` is a subclass of `AbstractNum<Integer>`. It is not allowed to define its own instance variables, so the only variable it can use is its parent's `opt`. This tells us that `Num`'s constructor **should call its parent's constructors with the `super()` keyword.**

Observe `AbstractNum`'s constructors. One of them takes a variable of generic type `T` and packages it into an `Optional`, while the other takes an `Optional`. We can guess that `Num` will utilize its parent's constructors similarly. 

The first `Num` constructor invokes the `AbstractNum` constructor that accepts the raw variable and packages it into an `Optional`.

```java
private Num(int i) {
    super(i);
}
```

The second `Num` constructor invokes the `AbstractNum` constructor that accepts an `Optional`.

```java
private Num(Optional<Integer> i) {
    super(i);
}
```