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

Next, observe the implementation of `Num.zero()`, which accepts an `AbstractNum`. This tells us that we need the corresponding third `Num` constructor.

```java
private Num(AbstractNum<Integer> i) {
    super(i.opt);
}
```

`AbstractNum` has three static instance properties; two `Function`s and a `Predicate`.

> 💡 The only `Optional` methods that accept a `Function` parameter are **`map()`** and **`flatMap()`**. The only `Optional` method that accepts a `Predicate` parameter is **`filter()`**.

Knowing this, we can tell that checking the validity of a number involves a `filter()`. 

```java
static Num of(int i) {
    return new Num(Optional.of(i).filter(valid));
}
```

> 💩 *Wait, how do I implement "NaN"? The docs say that `NaN` is a `float` or `double`-type, but I'm only working with `int`s?*
>
> I'm including this tip because **I MADE THIS MISTAKE**. I was confused about this until I looked at the definition `AbstractNum.toString()`. "NaN" is not the actual value as defined in the API, but a `String` output representing an empty `Optional`.
>
> You may laugh.

Given the clue that `succ()` relies on `s`, a `Function`, we can tell that `succ()` is to invoke a mapping function.

```java
Num succ() {
    return new Num(this.opt.map(s));
}
```

*This is equivalent to `return new Num(this.opt.map(x -> x + 1));`, in case the absence of the lambda appears unfamiliar.*

## Level 2

As indicated in the question, `add()` calls `succ()` repeatedly until the target number is reached. And as *not* explicitly indicated in the question, **`add()`'s input parameter is a `Num` rather than an `int`**. That means that extracting and reading the value of the addend with `Optional.get()` (or any getter for that matter) is famously banned, as is the increment operation `++`. You won't be able to use a `for (int i = 0; i <= n; i++)` loop construct to perform the addition.

Before we get to that, this `one()` deserves no explanation.

```java
static Num one() {
    return Num.zero().succ();
}
```

Going back to the `for` loop construct, it provides an important clue about how we'll solve this problem. Sure, you can't use integers, but who says you can't use `Num`s?

`int i = 0`? In the world of `Num`s, this is `Num n = Num.zero()`.  
`i <= n`? Let's look at `AbstractNum` again. Notice the `equals()` method. This can be used to check for this condition!  
`i++` is substituted by `Num.succ()`.  

Don't get ahead of yourself and start adding right away. Remember to check for invalid inputs first.

```java
Num add(Num addend) {
    if (this.isValid() && addend.isValid()) {
        Num result = this;

        for (Num n = Num.zero(); n.equals(addend); n.succ()) {
            result.succ();
        }

        return result;
    }

    return new Num(Optional.empty());
}
```

> 🌊 **Cross the stream**
>
> This is a possible `Stream` implementation of the addition method. It uses the [three-parameter `iterate()`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html#iterate(T,java.util.function.Predicate,java.util.function.UnaryOperator)), which terminates the stream when `Predicate !n.equals(this)` becomes `false`. This generates a stream of `Num`s from zero to `this`. The `reduce` operation runs `succ()` for every element in the stream, which is the same value as `this`. The addend is used as the identity value in the `reduce` operation.
>
> ```java
> Num add(Num addend) {
>    if (this.isValid() && addend.isValid()) {
>        return Stream.<Num>iterate(Num.zero(), n -> !n.equals(this), n -> n.succ())
>            .reduce(addend, (total, times) -> total.succ());
>    }
>    
>    return new Num(Optional.empty());
> }
> ```

## Level 3

As Computer Engineering survivors will know, multiplication and subtraction build on the same circuitry as [adders](https://www.electronics-tutorials.ws/combination/comb_7.html). Multiplication and addition belong to the addition mode of operation, and subtraction is an inversion of addition. The point of this story is that **you don't have to write lots of new logic for `mul()` and `sub()`. They can build on `add()`!**

`sub()` does as the question directs: it negates the subtrahend and adds it to the current `Num` with the help of `add()`. A validity check is performed at the end to check for negative outputs.

> 💡 Notice that `isValid()` only checks if `opt` is empty or not. That means that negative `Num`s can still pass the validity check, as long as they weren't created with the factory method `Num.of()`. This helps us intelligent developers break our own rules when we know what we're doing, but prevents the client from doing the same.

```java
Num sub(Num subtrahend) {
    Num s = new Num(subtrahend.opt.map(n));
    Num result = s.add(this);
    return new Num(result.opt.filter(valid));
}
```

> 🌊 **Go with the flow**
>
> As the subtraction operation generates a reduced `Num` that may be negative, this approach assumes that you have a `Num` constructor whose parameter is a `Num` and performs a `filter()` operation on its `Optional`. The constructor is included below.
>
> ```java
> private Num(Num n) {
>    super(n.opt.filter(valid));
> }
>
> Num sub(Num subtrahend) {
>    if (this.isValid() && subtrahend.isValid()) {
>        return new Num(Stream.<Num>iterate(Num.zero(), n -> !n.equals(this), n -> n.succ())
>            .reduce(new Num(subtrahend.opt.map(n)), (total, times) -> total.succ()));
>    }
>    
>    return new Num(Optional.empty());
> }
> ```

`mul()` is, as directed, "`multiplier` repeated additions of `this`".

```java
Num mul(Num multiplier) {
    if (this.isValid() && multiplier.isValid()) {
        Num result = Num.zero();

        for (Num n = Num.zero(); n.equals(multiplier); n.succ()) {
            result.add(this);
        }

        return result;
    }

    return new Num(Optional.empty());
}
```

> 🌊 **Ride the wave**
>
> ```java
> Num mul(Num multiplier) {
>    if (this.isValid() && addend.isValid()) {
>        return Stream.<Num>iterate(Num.zero(), n -> !n.equals(this), n -> n.succ())
>            .reduce(Num.zero(), (total, times) -> multiplier.add(total));
>    }
>    
>    return new Num(Optional.empty());
> }
> ```