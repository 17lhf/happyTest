package com.basic.happytest.modules.cryptology;

import com.basic.happytest.modules.cryptology.enums.ProviderEnums;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.security.provider.Sun;

import java.security.Provider;
import java.security.Security;

/**
 * 一些通用的内容
 * @author lhf
 */

public class CommonUtils {

    // 解决报错：no such provider: BC
    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取BC库支持的算法
     */
    public static void keyAloSupportedInBCLibrary() {
        Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        for (Provider.Service service : provider.getServices()) {
            System.out.println(service.getType() + ": " + service.getAlgorithm());
        }
    }

    /**
     * 获取JAVA默认支持的算法(SUN库) <br />
     * 需要注意的是，后续JAVA版本 Sun库 就不能这样直接用了
     */
    public static void keyAloSupportedInSunLibrary() {
        Provider provider = new Sun();
        for (Provider.Service service : provider.getServices()) {
            System.out.println(service.getType() + ": " + service.getAlgorithm());
        }
    }

    /**
     * 展示当前所有的算法提供者和信息
     */
    public static void showProviders() {
        System.out.println("-------当前有这些算法提供者-------");
        for (Provider provider : Security.getProviders()) {
            System.out.println("算法提供者名：" + provider.getName());
            System.out.println("算法提供者的版本：" + provider.getVersion());
            System.out.println("算法提供者的信息：" + provider.getInfo());
            if(ProviderEnums.SUN.getProvider().equals(provider.getName())){
                for (Provider.Service service : provider.getServices()) {
                    System.out.println(service.getType() + ": " + service.getAlgorithm());
                }
            }
        }
        System.out.println("------------展示完毕------------");
    }

}
