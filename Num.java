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

    public Num add(Num addend) {
        if (this.isValid() && addend.isValid()) {
            Num result = this;
    
            for (Num n = Num.zero(); n.equals(addend); n.succ()) {
                result.succ();
            }
    
            return result;
        }
    
        return Num.of(-1);
    }

    public Num sub(Num subtrahend) {
        Num s = new Num(subtrahend.opt.map(n));
        Num result = s.add(this);
        return new Num(result.opt.filter(valid));
    }

    public Num mul(Num multiplier) {
        if (this.isValid() && multiplier.isValid()) {
            Num result = Num.zero();
            
            for (Num n = Num.zero(); n.equals(multiplier); n.succ()) {
                result.add(this);
            }
    
            return result;
        }
    
        return Num.of(-1);
    }

}