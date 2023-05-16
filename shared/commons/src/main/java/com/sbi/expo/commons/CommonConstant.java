package com.sbi.expo.commons;

import java.io.File;
import java.text.DecimalFormat;

/**
 * CommonConstant
 *
 * @author Ming.G
 * @date 2022-05-24
 */
public class CommonConstant {
    private CommonConstant() {}

    public static final String COMMA = ",";
    public static final String SINGLE_QUOTES = "'";

    public static final String ID = "id";
    public static final String DELETED = "deleted";
    public static final String SORT = "sort";
    public static final char SEPARATOR = '_';

    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;
    public static final int GB = 1024 * 1024 * 1024;
    public static final int MB = 1024 * 1024;
    public static final int KB = 1024;

    public static final DecimalFormat DF = new DecimalFormat("0.00");

    public static final String IMAGE = "image";
    public static final String TXT = "document";
    public static final String MUSIC = "music";
    public static final String VIDEO = "video";
    public static final String OTHER = "other";

    public static final String SWAGGER_PATH = "/swagger-ui/index.html";
}
