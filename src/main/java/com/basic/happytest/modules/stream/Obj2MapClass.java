package com.basic.happytest.modules.stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 对象转map类
 * @author : lhf
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Obj2MapClass implements Obj2MapInterface<String, Obj2MapClass> {

    private String name;

    private String str;

    @Override
    public String obtainKey() {
        return name;
    }

    @Override
    public Obj2MapClass obtainValue() {
        return this;
    }
}
