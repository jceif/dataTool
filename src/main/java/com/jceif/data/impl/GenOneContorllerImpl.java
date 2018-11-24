package com.jceif.data.impl;


import com.jceif.data.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.jceif.data.service.GenCreateOracleService;
import com.jceif.data.service.GenOneContorllerService;
import com.jceif.data.service.TableService;
import com.jceif.data.common.TableUtil;

import java.util.List;


@Repository("genOneContorllerService")
public class GenOneContorllerImpl implements GenOneContorllerService {

    private static final String LINE = "\r\n";
    private static final String TAB = "	";

    @Autowired
    @Qualifier("tableService")
    private TableService tableService;

    @Autowired
    @Qualifier("genCreateOracleService")
    private GenCreateOracleService genCreateOracleService;


    public String geneContorller(String schemaName) throws Exception {
        List<Table> tables = tableService.getTableDefineBySchema(schemaName);
        StringBuffer fileContent = new StringBuffer();

        StringBuffer sbPack = new StringBuffer(TableUtil.controllerPath);
        TableUtil.trimAnyOne(sbPack, ".");
        fileContent.append("package " + sbPack.toString() + " ;");

        fileContent.append(getImport(tables));
        fileContent.append(LINE);
        fileContent.append(TAB + "@Controller");
        fileContent.append(LINE);
        fileContent.append(TAB + "@RequestMapping(\"" + "/OneController"
                + "\")");
        fileContent.append(LINE);
        fileContent.append(TAB + "public class OneController {");
        fileContent.append(LINE);
        fileContent.append(LINE + "public static int pageSize = 1000;");

        fileContent.append(LINE);
        fileContent.append(getAutowired(tables));
        fileContent.append(LINE);

        fileContent.append(getOneDataShift(tables));
        fileContent.append(LINE);
        fileContent.append("}");
        return fileContent.toString();
    }

    private String getImport(List<Table> tables) {
        StringBuffer sb = new StringBuffer();
        sb.append(LINE + "import java.util.ArrayList;");
        sb.append(LINE + "import java.util.List;");
        sb.append(LINE + "import java.util.HashMap;");
        sb.append(LINE + "import java.util.Map;");
        sb.append(LINE + "import java.util.concurrent.ExecutorService;");
        sb.append(LINE + "import java.util.concurrent.Executors;");
        for (int i = 0; i < tables.size(); i++) {

            Table table = tables.get(i);
            String tableName = table.getTableName();
            sb.append("import " + TableUtil.controllerPath
                    + TableUtil.toClassName(tableName).toLowerCase() + "."
                    + TableUtil.toClassName(tableName) + "Controller;");
            sb.append(LINE);
        }
        sb.append(LINE + "import java.io.IOException;");
        sb.append(LINE + "import org.apache.commons.beanutils.BeanUtils;");
        sb.append(LINE + "import org.springframework.beans.factory.annotation.Autowired;");
        sb.append(LINE + "import org.springframework.stereotype.Controller;");
        sb.append(LINE
                        + "import org.springframework.web.bind.annotation.RequestMapping;");
        sb.append(LINE);
        sb.append(LINE
                        + "import org.springframework.web.bind.annotation.ResponseBody;");
        sb.append(LINE);
        return sb.toString();
    }

    private String getOneDataShift(List<Table> tables) {
        StringBuffer sb = new StringBuffer();
        sb.append("@ResponseBody");
        sb.append(LINE + "@RequestMapping(\"" + "/oneDataShift" + "\")");
        sb.append(LINE + "public Map oneDataShift() throws IOException {");
        sb.append(LINE + TAB + "final Map map = new HashMap();");
        sb.append(LINE);
        sb.append(TAB
                + "ExecutorService exec = Executors.newFixedThreadPool(10);");
        sb.append(LINE);
        sb.append(TAB + "Executors.newCachedThreadPool();");

        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
            String tableName = table.getTableName();
            String controllerName = TableUtil.toParameterName(TableUtil.toClassName(tableName))
                    + "Controller";
            sb.append(LINE + TAB + "exec.execute(new Runnable() {");
            sb.append(LINE + TAB + TAB + "public void run() {");
            sb.append(LINE + TAB + "map.put(\"" + tableName + "\","
                    + controllerName + ".dataShift());");
            sb.append(LINE + TAB + TAB + "}");
            sb.append(LINE + TAB + "});");

        }
        sb.append(LINE + TAB + "exec.shutdown();");
        sb.append(LINE + TAB + "return map;");
        sb.append("}");

        return sb.toString();
    }

    private String getAutowired(List<Table> tables) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
            String tableName = table.getTableName();
            sb.append(LINE);
            sb.append(TAB + TAB + "@Autowired");
            sb.append(LINE);
            sb.append(TAB
                    + TAB
                    + TableUtil.toClassName(tableName)
                    + "Controller "
                    + TableUtil.toParameterName(TableUtil.toClassName(tableName)) + "Controller;");
            sb.append(LINE);
        }
        return sb.toString();

    }
}
