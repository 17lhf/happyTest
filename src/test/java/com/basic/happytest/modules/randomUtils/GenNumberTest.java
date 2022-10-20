package com.basic.happytest.modules.randomUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenNumberTest {

    @Test
    void genDoubleNumber() {
        for (int i = 0; i < 10; i++) {
            System.out.println(GenNumber.genDoubleNumber(10.0, false, 20.0, false));
            System.out.println(GenNumber.genDoubleNumber(10.0, true, 20.0, false));
            System.out.println(GenNumber.genDoubleNumber(10.0, true, 20.0, true));
            System.out.println(GenNumber.genDoubleNumber(10.0, false, 20.0, true));
            System.out.println();
        }
    }

    @Test
    void getIntegerNumber() {
        for (int i = 0; i < 10; i++) {
            System.out.println(GenNumber.getIntegerNumber(10, false, 20, false));
            System.out.println(GenNumber.getIntegerNumber(10, true, 20, false));
            System.out.println(GenNumber.getIntegerNumber(10, true, 20, true));
            System.out.println(GenNumber.getIntegerNumber(10, false, 20, true));
            System.out.println();
        }
    }
}