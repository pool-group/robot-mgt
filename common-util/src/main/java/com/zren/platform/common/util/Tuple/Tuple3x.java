package com.zren.platform.common.util.tuple;

import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import java.io.Serializable;
import java.util.*;

/**
 * Tuple3x Meta component
 *
 * @author k.y
 * @version Id: Tuple2x.java, v 0.1 2019年08月01日 下午16:00 k.y Exp $
 */
public class Tuple3x<T1, T2, T3> implements Iterable<Object>, Serializable {
    private static final long serialVersionUID = -3518082018884860684L;
    @NonNull
    final T1 t1;
    @NonNull
    final T2 t2;
    @NonNull
    final T3 t3;

    public Tuple3x(T1 t1, T2 t2, T3 t3) {
        this.t1 = Objects.requireNonNull(t1, "t1");
        this.t2 = Objects.requireNonNull(t2, "t2");
        this.t3 = Objects.requireNonNull(t3, "t3");
    }

    public Optional<T1> _1 () {
        return Optional.of(t1);
    }

    public Optional<T2> _2 () {
        return Optional.of(t2);
    }

    public Optional<T3> _3 () {
        return Optional.of(t3);
    }

    @Nullable
    public Object get(int index) {
        switch(index) {
            case 0:
                return this.t1;
            case 1:
                return this.t2;
            case 2:
                return this.t2;
            default:
                return null;
        }
    }

    public List<Object> toList() {
        return Arrays.asList(this.toArray());
    }

    public Object[] toArray() {
        return new Object[]{this.t1, this.t2, this.t3};
    }

    public Iterator<Object> iterator() {
        return Collections.unmodifiableList(this.toList()).iterator();
    }

    public int size() {
        return 3;
    }


}