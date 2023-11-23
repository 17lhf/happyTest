package com.basic.happytest.modules.property.customProperty;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.Vector;

/**
 * 自定义配置的加载和更新
 * @author lhf
 */

public class CustomConfig {

    /**
     * 默认的处于内部的配置文件的路径
     */
    private static final String CONFIG_FILE_NAME = "/properties/customConfig.properties";

    /**
     * 加载的配置（注意，该类继承自Hashtable）
     */
    private static Properties properties;

    /**
     * 负责记录传入的外部配置文件的绝对路径（不准备输出配置数据到配置文件中的话可以删除掉这个变量）
     */
    private static String extPropFilePath;

    /**
     * 初始化加载（一般会配置到启动加载项，项目启动时直接加载）
     * @param extPropFilePathVar 外部配置文件的绝对路径，null则表示没有配置外部配置文件
     */
    public static void init(final String extPropFilePathVar) {
        try (
                InputStream inputStream = CustomConfig.class.getResourceAsStream(CONFIG_FILE_NAME);
                // inputStream、inputStreamReader等未实现mark、reset方法，所以需要用到BufferedInputStream等实现了该方法的类
                BufferedInputStream bufferedInputStream = new BufferedInputStream(Objects.requireNonNull(inputStream));
                InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream)) {

            // 标记当前位置（此时实际是流的起始位置），让等下reset的时候可以直接回到这个位置
            // 配置文件内容大小不要超过这里的readLimit，否则有可能会导致mark失效，到时候reset就会抛出异常：Resetting to invalid mark
            bufferedInputStream.mark(2048);

            properties = new Properties();
            // 有中文的话，不能直接使用inputStream去加载，因为是ISO 8859-1格式，读取中文会乱码。所以这里用的是Reader，解决中文乱码问题。
            // 内部是一行一行去加载，读取一个键值对，就去put一次设置值
            System.out.println("开始加载内部的自定义配置");
            properties.load(inputStreamReader);

            // 配置读取完以后，输入流到达末尾
            System.out.println("inputStream是否已经到达文件末尾：" + (inputStream.read() == -1));
            System.out.println("inputStreamReader是否已经到达文件末尾：" + (inputStreamReader.read() == -1));
            System.out.println("bufferedInputStream是否已经到达文件末尾：" + (bufferedInputStream.read() == -1));

            // 让输入流回到起始位置，方便等下可以重复读取数据
            bufferedInputStream.reset();

            // 外部配置文件路径不为空，说明要加载外部配置文件
            if (StringUtils.isNotBlank(extPropFilePathVar)) {
                loadExtProps(bufferedInputStream, extPropFilePathVar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载外部配置文件
     * @param innerInputStream 内部配置文件的输入流
     * @param extPropFilePathVar 外部配置文件路径
     */
    private static void loadExtProps(InputStream innerInputStream, String extPropFilePathVar) {
        File extPropFile = new File(extPropFilePathVar);
        // 如果外部配置文件不存在，则直接结束
        if (!extPropFile.exists()) {
            System.out.println("外部配置文件不存在，直接结束加载外部配置文件");
            return;
        }
        InputStream extInputStream;
        try {
            extInputStream = Files.newInputStream(Paths.get(extPropFilePathVar));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("外部配置文件加载异常，直接结束加载外部配置文件");
            return;
        }
        // 内外部文件流拼接
        Vector<InputStream> inputStreamVector = new Vector<>(3);
        inputStreamVector.add(innerInputStream);
        // 补充换行，放置无法正常连接两个Properties数据流，默认连接后没有换行
        inputStreamVector.add(new ByteArrayInputStream("\n".getBytes(StandardCharsets.UTF_8)));
        inputStreamVector.add(extInputStream);
        try (SequenceInputStream sequenceInputStream = new SequenceInputStream(inputStreamVector.elements())){
            properties = new Properties();
            InputStreamReader inputStreamReader = new InputStreamReader(sequenceInputStream);
            // 内部是一行一行去加载，读取一个键值对，就去put一次设置值
            // 所以，外部配置文件流放后面，出现的键值对因为key和内部的配置文件的一样，所以会产生覆盖（即外部配置文件优先级更高）
            properties.load(inputStreamReader);
            extPropFilePath = extPropFilePathVar;
            System.out.println("加载外部配置文件成功，配置以外部的优先");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                extInputStream.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * 更新配置并输出到配置文件（使用外部配置文件时才允许修改）
     * @param name 名称（具体的配置项value）
     * @param age 年龄（具体的配置项value）
     */
    public static void updProps(String name, String age) {
        // 深度复制一份旧的配置，以备后面如果输出到文件失败的话，可以恢复
        Properties originalProps = SerializationUtils.clone(properties);
        // 记录是否修改了原配置
        boolean isModified = false;
        // 更新内存中的配置
        if (!properties.getProperty(CustomConfigEnum.NAME.getKeyName()).equals(name)) {
            properties.setProperty(CustomConfigEnum.NAME.getKeyName(), name);
            isModified = true;
        }
        if (!properties.getProperty(CustomConfigEnum.AGE.getKeyName()).equals(String.valueOf(age))) {
            properties.setProperty(CustomConfigEnum.AGE.getKeyName(), String.valueOf(age));
            isModified = true;
        }
        // 修改了配置，才需要尝试将新的配置输出到配置文件
        if (isModified) {
            System.out.println("内存中的配置发生了变化，开始将更新后的配置数据输出到配置文件");
            // 此处是：当前使用哪里的配置文件，就更新哪个配置文件
            String propsFilePath;
            if (StringUtils.isNotBlank(extPropFilePath)) {
                propsFilePath = extPropFilePath;
            } else {
                System.out.println("使用内部配置，不允许修改配置，修改内容不生效");
                properties = SerializationUtils.clone(originalProps);
                return;
                /*
                try {
                    // IDE中执行时，修改的其实是这个文件夹里的对应配置文件 /target/classes/properties
                    // 如果是打包后执行jar包，这里的输出是会有问题的，实际运行会报错：java.io.FileNotFoundException
                    URL fileURL = FileIO.class.getResource(CONFIG_FILE_NAME);
                    if (fileURL == null) {
                        System.out.println("定位文件路径失败！！！");
                        throw new IOException();
                    }
                    propsFilePath = fileURL.getPath();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                */
            }
            // jdk11时可以指定FileWriter输出时的编码方式，低版本则不支持，只能输出时默认使用系统的编码方式（例如win经常时GBK）,这样会导致容易出现中文乱码的情况，所以低版本Jdk的话，只能换其他的输出流
            try (FileOutputStream fileOutputStream = new FileOutputStream(propsFilePath);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8)){
                // 注意，这里整个文件的内容都会更新
                // comments中文会变成 \u8FD9 之类，没招，除非自己继承Properties去实现输出方法，特别是这个方法writeComments
                properties.store(outputStreamWriter, "这个内容将会输出到配置文件中第一行作为注释，不想设置可以为null");
                System.out.println("输出到配置文件成功");
            } catch (Exception e) {
                e.printStackTrace();
                // 输出到文件失败，则会恢复到原来的配置
                properties = SerializationUtils.clone(originalProps);
            }
        }
    }

    /**
     * 获取配置
     * @param key 配置项的key
     * @return 配置值
     */
    public static String getProp(String key) {
        return properties.getProperty(key);
    }
}
