package com.basic.happytest.modules.regexSamples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : lhf
 */
class RegexSamplesTest {

    @Test
    void eachNumCharInsertStr() {
        String originalStr = "01234567890";
        String insertStr = "a";
        System.out.println(RegexSamples.eachNumCharInsertStr(originalStr, 4, insertStr));
    }
}