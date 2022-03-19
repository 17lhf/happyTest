package com.basic.happytest.modules.lombokExplore.model;

/**
 * 寻常的入参实体
 * get和set方法由idea自动生成，符合java bean的规范
 * @author lhf
 */

public class NormalDto {
    public Integer getaInt() {
        return aInt;
    }

    public void setaInt(Integer aInt) {
        this.aInt = aInt;
    }

    public Integer getOneInt() {
        return oneInt;
    }

    public void setOneInt(Integer oneInt) {
        this.oneInt = oneInt;
    }

    public String getaStr() {
        return aStr;
    }

    public void setaStr(String aStr) {
        this.aStr = aStr;
    }

    public String getOneStr() {
        return oneStr;
    }

    public void setOneStr(String oneStr) {
        this.oneStr = oneStr;
    }

    /**
     * 首字母小写，第二个字母大写的变量
     */
    private Integer aInt;

    private Integer oneInt;

    /**
     * 首字母小写，第二个字母大写的变量
     */
    private String aStr;

    private String oneStr;
}
