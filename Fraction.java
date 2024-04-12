import java.util.Optional;

public class Fraction extends AbstractNum<Frac> {

    private Fraction(Optional<Frac> opt) {
        super(opt);
    }

    /**
     * This is the static method to new a fraciton.
     * @param n nominator
     * @param d denominator
     * @return fraction
     */
    public static Fraction of(int n, int d) {
        Num nNum = Num.of(n);
        Num dNum = Num.of(d);
        if (dNum.equals(Num.zero()) || !dNum.isValid() || !nNum.isValid()) {
            return new Fraction(Optional.empty());
        }
        Frac frac = Frac.of(nNum, dNum);
        return new Fraction(Optional.of(frac));
    }

    /**
     * This is a dumb function.
     * @param n n
     * @param d d
     * @return new fraction
     */
    public static Fraction of(Optional<Num> n, Optional<Num> d) {
        if (n.map(x -> x.isValid()).equals(Optional.of(false))) {
            return new Fraction(Optional.empty());
        }
        Optional<Frac> output = n.flatMap(x -> d.map(y -> Frac.of(x, y)));
        return new Fraction(output);
    }

    /**
     * An add emthod to add two fraction.
     * @param otherFraction the otherfraction.
     * @return a new Fraction.
     */
    public Fraction add(Fraction otherFraction) {
        Optional<Num> a = this.opt.map(x -> x.first());
        Optional<Num> b = this.opt.map(x -> x.second());
        Optional<Num> c = otherFraction.opt.map(x -> x.first());
        Optional<Num> d = otherFraction.opt.map(x -> x.second());

        Optional<Num> ad = a.flatMap(x -> d.map(y -> y.mul(x)));
        Optional<Num> bc = b.flatMap(x -> c.map(y -> y.mul(x)));
        Optional<Num> nominator = ad.flatMap(x -> bc.map(y -> y.add(x)));
        Optional<Num> denominator = b.flatMap(x -> d.map(y -> y.mul(x)));

        return Fraction.of(nominator, denominator);
    }

    /**
     * An add emthod to add two fraction.
     * @param otherFraction the otherfraction.
     * @return a new Fraction.
     */
    public Fraction sub(Fraction otherFraction) {
        Optional<Num> a = this.opt.map(x -> x.first());
        Optional<Num> b = this.opt.map(x -> x.second());
        Optional<Num> c = otherFraction.opt.map(x -> x.first());
        Optional<Num> d = otherFraction.opt.map(x -> x.second());

        Optional<Num> ad = a.flatMap(x -> d.map(y -> y.mul(x)));
        Optional<Num> bc = b.flatMap(x -> c.map(y -> y.mul(x)));
        Optional<Num> nominator = bc.flatMap(x -> ad.map(y -> y.sub(x)));
        Optional<Num> denominator = b.flatMap(x -> d.map(y -> y.mul(x)));

        return Fraction.of(nominator, denominator);
    }

    /**
     * An add method to add two fraction.
     * @param otherFraction the other fraction.
     * @return a new Fraction.
     */
    public Fraction mul(Fraction otherFraction) {
        Optional<Num> a = this.opt.map(x -> x.first());
        Optional<Num> b = this.opt.map(x -> x.second());
        Optional<Num> c = otherFraction.opt.map(x -> x.first());
        Optional<Num> d = otherFraction.opt.map(x -> x.second());

        Optional<Num> nominator = a.flatMap(x -> c.map(y -> y.mul(x)));
        Optional<Num> denominator = b.flatMap(x -> d.map(y -> y.mul(x)));

        return Fraction.of(nominator, denominator);
    }
        
}