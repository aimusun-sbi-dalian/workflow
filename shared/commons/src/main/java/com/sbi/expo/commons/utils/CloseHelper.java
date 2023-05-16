package com.sbi.expo.commons.utils;

import java.io.Closeable;

public class CloseHelper {
    private CloseHelper() {}

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // silence close
            }
        }
    }

    public static void close(AutoCloseable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // silence close
            }
        }
    }
}
