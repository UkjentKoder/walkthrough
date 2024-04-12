import java.util.Optional;

class Num extends AbstractNum<Integer> {
    
    private Num(int i) {
        super(i);
    }

    private Num(AbstractNum<Integer> i) {
        super(i.opt);
    }

    private Num(Optional<Integer> i) {
        super(i);
    }

    public static Num of(int i) {
        return new Num(Optional.of(i).filter(valid));
    }
    
    public static Num zero() {
        return new Num(AbstractNum.zero());
    }

    public static Num one() {
        return Num.zero().succ();
    }

    public Num succ() {
        return new Num(this.opt.map(s));
    }

    public Num add(Num otherNum) {
        if (!otherNum.isValid() || !this.isValid()) {
            return new Num(Optional.empty());
        }
        Num count = Num.zero();
        Num outputNum = this;
        while (!count.equals(otherNum)) {
            outputNum = outputNum.succ();
            count = count.succ();
        }
        return outputNum;
    }

    public Num sub(Num otherNum) {
        if (!otherNum.isValid() || !this.isValid()) {
            return new Num(Optional.empty());
        }
        Num count = Num.zero();
        Num outputNum = new Num(this.opt.map(n));
        while (!count.equals(otherNum)) {
            if (outputNum.equals(Num.zero())) {
                return new Num(Optional.empty());
            }
            outputNum = outputNum.succ();
            count = count.succ();
        }
        outputNum = new Num(outputNum.opt.map(n));
        return outputNum;
    }

    public Num mul(Num otherNum) {
        if (!otherNum.isValid() || !this.isValid()) {
            return new Num(Optional.empty());
        }
        if (this.equals(Num.zero()) || otherNum.equals((Num.zero()))) {
            return Num.zero();
        }
        Num count = Num.one();
        Num outputNum = this;
        while (!count.equals(otherNum)) {
            outputNum = outputNum.add(this);
            count = count.succ();
        }
        return outputNum;
    }

}