package com.neshan.resturantrest.util;

import java.util.Collection;

public class Utils {
    public static <T> T random(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for (T t : coll) if (--num < 0) return t;
        throw new AssertionError();
    }
}
