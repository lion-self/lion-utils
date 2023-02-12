package com.lion.utils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class UnitTestUtil {
    private static final String PROTOCOL_FILE = "file";
    private static final String USER_DIR_PROPERTY = "user.dir";
    private static final String MAIN_SOURCE_PATH = "/src/main/java/";
    private static final String TEST_SOURCE_PATH = "/src/test/java/";
    private static final String MOCK_FIRE_PREFIX = "MOCK";
    private static final String ENTER_NEW_LINE = "\r\n";

    /**
     * 生成 Mock 实现类
     * @param sourcePackageNm 类源包地址
     * @param targetPackageNm 类目地包地址
     * @param getChild  是否遍历子包
     */
    public static void createMockImpl(String sourcePackageNm, String targetPackageNm, boolean getChild) {
        // 通过包名获取包内所有类
        List<Class<?>> interfaceList = getClasses(sourcePackageNm, getChild);
        // 在目的包下创建类的 Mock 实现类
        try {
            createMockImpl(interfaceList, targetPackageNm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过包名中获取所有的Class
     * @param packageName 包名
     * @param getChild 是否遍历子包
     */
    private static List<Class<?>> getClasses(String packageName, boolean getChild) {
        List<Class<?>> resultClasses = new ArrayList<Class<?>>();
        // 获取包的名字
        String packageDirName = packageName.replace('.', '/');
        URL url = Thread.currentThread().getContextClassLoader().getResource(packageDirName);
        // 得到协议的名称
        if (url == null || !PROTOCOL_FILE.equals(url.getProtocol())) {
            return new ArrayList<>();
        }
        // 获取包的物理路径
        String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
        // 以文件的方式扫描整个包下的文件 并添加到集合中
        findAndAddClassesInPackageByFile(packageName, filePath, getChild, resultClasses);
        return resultClasses;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean getChild, List<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] dirFiles = dir.listFiles(file -> (getChild && file.isDirectory()) || (file.getName().endsWith(".class")));
        if (dirFiles == null) {
            return;
        }

        for (File file : dirFiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), getChild, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().split("\\.")[0];
                try {
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // LOGGER
                }
            }
        }
    }

    /**
     * 在指定包路径下创建创建文件
     */
    private static void createMockImpl(List<Class<?>> interfaceList, String targetPackageNm) throws IOException {
        String packageDirName = targetPackageNm.replace('.', '/');
        String targetDir = System.getProperty(USER_DIR_PROPERTY) + TEST_SOURCE_PATH + packageDirName;
        // 创建测试目录
        File file=new File(targetDir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                // TODO:
            }
        }
        for (Class<?> interfaceZ : interfaceList) {
            implClassItem(file, interfaceZ, targetPackageNm);
        }


    }

    private static void implClassItem(File file, Class<?> interfaceZ, String targetPackageNm) throws IOException {
        String fileName = MOCK_FIRE_PREFIX + interfaceZ.getName();
        interfaceZ.getMethods();
        File mockFile = File.createTempFile(MOCK_FIRE_PREFIX, interfaceZ.getName(), file);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(mockFile, true);//true代表追加写入内容
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String contentLine = "";
            bufferedWriter.write("package " + targetPackageNm + ENTER_NEW_LINE);
            bufferedWriter.write(ENTER_NEW_LINE);
            bufferedWriter.write("import " + interfaceZ.getTypeName() + ENTER_NEW_LINE);
            bufferedWriter.write(ENTER_NEW_LINE);
            bufferedWriter.write("public class " + fileName + "implements " + interfaceZ.getName() + " {" + ENTER_NEW_LINE);
            bufferedWriter.write(ENTER_NEW_LINE);

            //记得flush不然文件不完整
            bufferedWriter.flush();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
