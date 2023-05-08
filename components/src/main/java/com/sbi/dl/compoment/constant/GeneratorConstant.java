package com.sbi.dl.compoment.constant;

public class GeneratorConstant {
    private GeneratorConstant() {}

    public static final String TEMPLATE = "template";

    // be template name
    public static final String MODEL = "Model";
    public static final String DTO = "DTO";
    public static final String MAPPER = "Mapper";
    public static final String CONTROLLER = "Controller";
    public static final String QUERY_CRITERIA = "QueryCriteria";
    public static final String SERVICE = "Service";
    public static final String SERVICE_IMPL = "ServiceImpl";
    public static final String REPOSITORY = "Repository";

    // fe template name
    public static final String INDEX = "index";
    public static final String HOOKS_INDEX = "hooks/index";
    public static final String COMPS_INDEX = "comps/index";
    public static final String COMPS_DETAIL_DIALOG = "comps/DetailDialog";
    public static final String COMPS_DETAIL_FORM = "comps/DetailForm";

    // file name and file path
    public static final String BE_TEMPLATE_PATH = "generator/admin/";
    public static final String FE_TEMPLATE_PATH = "generator/front/";
    public static final String FREEMARKER_SUFFIX = ".ftl";
    public static final String JAVA_SUFFIX = ".java";
    public static final String JSX_SUFFIX = ".tsx";
    public static final String JS_SUFFIX = ".ts";
    public static final String DTO_PACKAGE = "dto";
    public static final String SERVICE_PACKAGE = "service";
    public static final String SERVICE_IMPL_PACKAGE = "impl";
    public static final String CONTENT = "content";
    public static final String NAME = "name";
    public static final String TEMP_ROOT_PATH = "BF-temp";
}
