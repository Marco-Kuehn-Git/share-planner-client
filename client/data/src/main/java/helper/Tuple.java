package helper;

public class Tuple<X, Y> {
    public final X key;
    public final Y value;

    public Tuple(X key, Y value) {
        this.key = key;
        this.value = value;
    }

    public X getKey() {
        return key;
    }

    public Y getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
