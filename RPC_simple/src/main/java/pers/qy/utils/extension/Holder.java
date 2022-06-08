package pers.qy.utils.extension;

/**
 * @author whisper
 * @date 2022/06/08
 **/
public class Holder<T> {
    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}