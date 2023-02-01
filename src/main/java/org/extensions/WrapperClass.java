package org.extensions;

import org.junit.jupiter.api.Assertions;
public class WrapperClass {

    public static void methodWrapper(String desc, Wrapper method) {
        try {
            method.warp();
        } catch(Exception exception) {
            Assertions.fail(desc + " fails", exception);
        }
    }

}
