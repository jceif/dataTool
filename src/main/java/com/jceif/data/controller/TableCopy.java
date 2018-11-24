package com.jceif.data.controller;

import com.jceif.data.model.Table;
import com.jceif.data.model.TableColumn;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jceif.data.service.*;
import com.jceif.data.common.TableUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/tableCopy")
public class TableCopy extends BaseController {
    private static String filePath = "ss//";

    @Autowired
    @Qualifier("tableService")
    private TableService tableService;

    @Autowired
    @Qualifier("genCreateOracleService")
    private GenCreateOracleService genCreateOracleService;

    @Autowired
    @Qualifier("genMysqlPoService")
    private GenMysqlPoService genMysqlPoService;

    @Autowired
    @Qualifier("genMysqlDaoXMLService")
    private GenMysqlDaoXMLService genMysqlDaoXMLService;

    @Autowired
    @Qualifier("genMysqlDaoService")
    private GenMysqlDaoService genMysqlDaoService;

    @Autowired
    @Qualifier("genMysqlSeService")
    private GenMysqlSeService genMysqlSeService;

    @Autowired
    @Qualifier("genOraclePoService")
    private GenOraclePoService genOraclePoService;

    @Autowired
    @Qualifier("genOracleDaoXMLService")
    private GenOracleDaoXMLService genOracleDaoXMLService;

    @Autowired
    @Qualifier("genOracleDaoService")
    private GenOracleDaoService genOracleDaoService;

    @Autowired
    @Qualifier("genOracleSeService")
    private GenOracleSeService genOracleSeService;

    @Autowired
    @Qualifier("genContorllerService")
    private GenContorllerService genContorllerService;

    @Autowired
    @Qualifier("genOneContorllerService")
    private GenOneContorllerService genOneContorllerService;

    @Autowired
    @Qualifier("genDubboXMLService")
    private GenDubboXMLService genDubboXMLService;

    @RequestMapping(value = "/getTableSingle")
    @ResponseBody
    public Table getTableSingle() {
        String tableName = "user_info";
        String schemaName = "minxindai_2";
        return tableService.getTableDefine(tableName, schemaName);
    }

    @RequestMapping(value = "/getTableColumnInfo")
    @ResponseBody
    public List<TableColumn> getTableColumnInfo() {
        String schemaName = "minxindai_2";
        String tableName = "user_info";
        List<TableColumn> lstTableColumn = tableService.getMysqlTableColumnInfo(tableName, schemaName);
        return lstTableColumn;
    }

    @RequestMapping(value = "/getTableSingleBySchema")
    @ResponseBody
    public List<Table> getTableSingleBySchema(String schemaName) {
        if (schemaName == null) {
            schemaName = "test";
        }
        String strFileName = "f:/createTable.sql";
        File file = new File(strFileName);
        file.delete();
        List<Table> lstTable = tableService.getTableDefineBySchema(schemaName);
        FileWriter fw = null;
        try {
            fw = new FileWriter(strFileName, true);
            String strTmp = "";
            for (int i = 0; i < lstTable.size(); i++) {
                String tableName = lstTable.get(i).getTableName();
                System.out.println(tableName);
                strTmp = genCreateOracleService.generateCreateTableSpecial(tableName, schemaName);
                strTmp += "\n";
                fw.write(strTmp);
                strTmp = "";
            }
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lstTable;
    }

    public String getCreateTable(String tableName, String schemaName) {
        String strRtn = genCreateOracleService.generateCreateTableSpecial(tableName, schemaName);
        return strRtn;
    }


    /**
     * 生成一个controller
     */
    @RequestMapping(value = "/getOneController")
    @ResponseBody
    public String getOneController(String schemaName) throws Exception {
        System.out.println("schemaName=" + schemaName);
        if (schemaName == null) {
            schemaName = "minxindai_2";
        }
        String onrContorllerRe = genOneContorllerService.geneContorller(schemaName);
        String oneContorllerFileName = "OneController.java";
        TableUtil.buildFile(TableUtil.controllerPath, oneContorllerFileName, onrContorllerRe);
        return "success";
    }

    /**
     * 生成controller
     */
    @RequestMapping(value = "/getController")
    @ResponseBody
    public String getController(String tableName, String schemaName)
            throws Exception {
        System.out.println("tableName=" + tableName + "schemaName="
                + schemaName);
        if (tableName == null) {
            tableName = "BS_CUSTOMER";
        }
        if (schemaName == null) {
            schemaName = "minxindai_2";
        }
        String contorllerRe = genContorllerService.geneContorller(tableName, schemaName,"");
        String contorllerFileName = TableUtil.toClassName(tableName)
                + "Controller.java";
        String controllerFolderName = TableUtil.toParameterName(TableUtil.toClassName(tableName)).toLowerCase();
        TableUtil.buildFile(TableUtil.controllerPath + "."
                + controllerFolderName, contorllerFileName, contorllerRe);
        return "success";
    }

    /**
     * 生成 mysql 全部
     */
    @RequestMapping(value = "/getMySqlAll")
    @ResponseBody
    public String getMySqlAll(String tableName, String schemaName)
            throws Exception {
        System.out.println("tableName=" + tableName + "schemaName="
                + schemaName);
        if (tableName == null) {
            tableName = "BS_CUSTOMER";
        }
        if (schemaName == null) {
            schemaName = "minxindai_2";
        }
        String poRe = genMysqlPoService.geneJavaBean(tableName, schemaName);
        String poFileName = TableUtil.toClassName(tableName) + ".java";
        String poFolderName = TableUtil.toParameterName(TableUtil.toClassName(tableName)).toLowerCase();
        TableUtil.buildFile(TableUtil.MysqlPoPath + "." + poFolderName, poFileName, poRe);
        String xmlRe = genMysqlDaoXMLService.geneMybatisXML(tableName, schemaName);
        String xmlFileName = TableUtil.toClassName(tableName) + "Mapper.xml";
        String xmlFolderName = TableUtil.toParameterName(TableUtil.toClassName(tableName)).toLowerCase();
        TableUtil.buildFile(TableUtil.MysqlMapperPath + "." + xmlFolderName, xmlFileName, xmlRe);
        String mapperRe = genMysqlDaoService.geneMapper(tableName, schemaName);
        String mapperFileName = TableUtil.toClassName(tableName)
                + "Mapper.java";
        String mapperFolderName = TableUtil.toParameterName(TableUtil.toClassName(tableName)).toLowerCase();
        TableUtil.buildFile(TableUtil.MysqlMapperPath + "." + mapperFolderName, mapperFileName, mapperRe);
        String serviceRe = genMysqlSeService.geneService(tableName, schemaName);
        String serviceFileName = TableUtil.toClassName(tableName)
                + "Service.java";
        String serviceFolderName = TableUtil.toParameterName(TableUtil.toClassName(tableName)).toLowerCase();
        TableUtil.buildFile(TableUtil.MysqlServicePath + "."
                + serviceFolderName, serviceFileName, serviceRe);
        String serviceImplRe = genMysqlSeService.geneServiceImpl(tableName, schemaName);
        String serviceImplFileName = TableUtil.toClassName(tableName)
                + "ServiceImpl.java";
        TableUtil.buildFile(TableUtil.MysqlServicePath + "." + serviceFolderName + ".com.jceif.data.impl", serviceImplFileName, serviceImplRe);
        return "success";
    }

    /**
     * 生成 Oracle 全部
     */
    @RequestMapping(value = "/getOracleAll")
    @ResponseBody
    public String getOracleAll(String tableName, String schemaName)
            throws Exception {
        System.out.println("tableName=" + tableName + "schemaName="
                + schemaName);
        if (tableName == null) {
            tableName = "BS_CUSTOMER";
        }
        if (schemaName == null) {
            schemaName = "minxindai_2";
        }
        String poRe = genOraclePoService.geneJavaBean(tableName, schemaName);
        String poFileName = TableUtil.toOracleClassName(tableName) + ".java";
        String poFolderName = TableUtil.toParameterName(
                TableUtil.toClassName(tableName)).toLowerCase();
        TableUtil.buildFile(TableUtil.OraclePoPath + "." + poFolderName,
                poFileName, poRe);
        //
        String xmlRe = genOracleDaoXMLService
                .geneMybatisXML(tableName, schemaName);
        String xmlFileName = TableUtil.toOracleClassName(tableName)
                + "Mapper.xml";
        String xmlFolderName = TableUtil.toParameterName(
                TableUtil.toClassName(tableName)).toLowerCase();
        TableUtil.buildFile(TableUtil.OracleMapperPath + "." + xmlFolderName,
                xmlFileName, xmlRe);
        //
        String mapperRe = genOracleDaoService.geneMapper(tableName,
                schemaName);
        String mapperFileName = TableUtil.toOracleClassName(tableName)
                + "Mapper.java";
        String MapperFolderName = TableUtil.toParameterName(
                TableUtil.toClassName(tableName)).toLowerCase();
        TableUtil.buildFile(
                TableUtil.OracleMapperPath + "." + MapperFolderName,
                mapperFileName, mapperRe);
        //
        String serviceRe = genOracleSeService
                .geneService(tableName, schemaName);
        String serviceFileName = TableUtil.toOracleClassName(tableName)
                + "Service.java";

        String serviceFolderName = TableUtil.toParameterName(
                TableUtil.toClassName(tableName)).toLowerCase();
        TableUtil.buildFile(TableUtil.OracleServicePath + "."
                + serviceFolderName, serviceFileName, serviceRe);
        //
        String serviceImplRe = genOracleSeService.geneServiceImpl(tableName,
                schemaName);
        String serviceImplFileName = TableUtil.toOracleClassName(tableName)
                + "ServiceImpl.java";
        TableUtil.buildFile(TableUtil.OracleServicePath + "."
                        + serviceFolderName + ".com.jceif.data.impl", serviceImplFileName,
                serviceImplRe);
        return "success";
    }

    /**
     * 执行一次全部生成
     */
    @RequestMapping(value = "/getAll")
    @ResponseBody
    public Map getAll(String schemaName) throws Exception {
        System.out.println("schemaName=" + schemaName);
        if (schemaName == null) {
            schemaName = "minxindai_2";
        }
        List<Table> tables = tableService.getTableDefineBySchema(schemaName);
        Map<String, String> map = new HashMap<String, String>();
        map.put("OneController", this.getOneController(schemaName));
        for (int i = 0; i < tables.size(); i++) {
            Table table = tables.get(i);
            String tableName = table.getTableName();
            map.put(tableName + "Controller", this.getController(tableName, schemaName));
            map.put(tableName, this.getMySqlAll(tableName, schemaName));
            map.put(tableName, this.getOracleAll(tableName, schemaName));
        }
        String json="";
        json=JSONObject.valueToString(map);
        return map;
    }

    @RequestMapping(value = "/getDubboCustomer")
    @ResponseBody
    public String getDubboCustomer(String schemaName, String target) throws Exception {
        if (schemaName == null) {
            schemaName = "minxindai_2";
        }
        if (target == null) {
            target = TableUtil.databaseTypeMysql;
        }
        String re = genDubboXMLService.geneCustomerXML(schemaName, target);
        String fileName = "spring-dubbo-consumer.xml";
        TableUtil.buildFile(TableUtil.controllerPath, fileName, re);

        return "success";
    }

    @RequestMapping(value = "/getDubboProducer")
    @ResponseBody
    public String getDubboProducer(String schemaName, String target) throws Exception {
        if (schemaName == null) {
            schemaName = "minxindai_2";
        }
        if (target == null) {
            target = TableUtil.databaseTypeMysql;
        }
        String re = genDubboXMLService.geneProducerXML(schemaName, target);
        String fileName = "spring-dubbo-provider.xml";
        TableUtil.buildFile(TableUtil.controllerPath, fileName, re);
        return "success";
    }


}
