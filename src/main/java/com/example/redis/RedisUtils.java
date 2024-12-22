package com.example.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisUtils {

    private static final String KEY_SPLITTER = ":";

    public static String join(final String prefix, final Object ...args) {
        StringBuilder key = new StringBuilder(prefix);
        for (var arg : args) {
            key.append(KEY_SPLITTER).append(arg);
        }
        return key.toString();
    }
}
