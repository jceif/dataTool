package com.jceif.data.controller;

import com.jceif.data.model.Table;
import com.jceif.data.model.TableColumn;
import com.jceif.data.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jceif.data.common.TableUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/mysql")
public class MySqlController extends BaseController {
    private static String filePath = "ss//";

    @Autowired
    private TableService tableService;

    @Autowired
    private GenMysqlPoService genMysqlPoService;

    @Autowired
    private GenMysqlDaoXMLService genMysqlDaoXMLService;

    @Autowired
    private GenMysqlDaoService genMysqlDaoService;

    @Autowired
    private GenMysqlSeService genMysqlSeService;

    @Autowired
    private GenContorllerService genContorllerService;

    @Autowired
    private GenOneContorllerService genOneContorllerService;

    @Autowired
    private GenDubboXMLService genDubboXMLService;

    @RequestMapping(value = "/getTableSingle")
    @ResponseBody
    public Table getTableSingle() {
        String tableName = "user_main";
        String schemaName = "stsyd-develop";
        return tableService.getTableDefine(tableName, schemaName);
    }

    @RequestMapping(value = "/getTableColumnInfo")
    @ResponseBody
    public List<TableColumn> getTableColumnInfo() {
        String schemaName = "stsyd-develop";
        String tableName = "user_main";
        List<TableColumn> lstTableColumn = tableService.getMysqlTableColumnInfo(tableName, schemaName);
        return lstTableColumn;
    }



    /**
     * 生成一个controller
     */
    @RequestMapping(value = "/getOneController")
    @ResponseBody
    public String getOneController(String schemaName) throws Exception {
        System.out.println("schemaName=" + schemaName);
         if (schemaName == null) {
            return  "请输入表名称";
        }else {
            String onrContorllerRe = genOneContorllerService.geneContorller(schemaName);
            String oneContorllerFileName = "OneController.java";
            TableUtil.buildFile(TableUtil.controllerPath, oneContorllerFileName, onrContorllerRe);
            return "success";
        }
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
            return  "请输入表名称";
        }
        else if (schemaName == null) {
            return  "请输入数据库名称";
        }else {
            String contorllerRe = genContorllerService.geneContorller(tableName, schemaName,"mysql");
            String contorllerFileName = TableUtil.toClassName(tableName)
                    + "Controller.java";
            String controllerFolderName = TableUtil.toParameterName(TableUtil.toClassName(tableName)).toLowerCase();
            TableUtil.buildFile(TableUtil.controllerPath + "."
                    + controllerFolderName, contorllerFileName, contorllerRe);
            return "success";
        }
    }

    /**
     * 生成 mysql 全部
     */
    @RequestMapping(value = "/getMySqlAll")
    @ResponseBody
    public String getMySqlAll(String tableName, String schemaName) {
        System.out.println("tableName=" + tableName + "schemaName="
                + schemaName);
        if (tableName == null) {
            return  "请输入表名称";
        }
       else if (schemaName == null) {
            return  "请输入数据库名称";
        }else {
            try {
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
                TableUtil.buildFile(TableUtil.MysqlServicePath + "." + serviceFolderName + ".impl", serviceImplFileName, serviceImplRe);
            } catch (Exception ex) {
                return  ex.getMessage();
            }
            return "success";
        }

    }



    /**
     * 执行一次全部生成
     */
    @RequestMapping(value = "/getAll")
    @ResponseBody
    public String getAll(String schemaName) throws Exception {
        System.out.println("schemaName=" + schemaName);
        Map<String, String> map = new HashMap<String, String>();
        if (schemaName == null) {
            System.out.println("请输入数据库名称");
           return  JSONObject.valueToString(map);
        }else {
            List<Table> tables = tableService.getTableDefineBySchema(schemaName);
            map.put("OneController", this.getOneController(schemaName));
            for (int i = 0; i < tables.size(); i++) {
                Table table = tables.get(i);
                String tableName = table.getTableName();
                map.put(tableName + "Controller", this.getController(tableName, schemaName));
                map.put(tableName, this.getMySqlAll(tableName, schemaName));

            }
            String json = JSONObject.valueToString(map);
            //System.out.println(json);
        }
        return JSONObject.valueToString(map);
    }

    @RequestMapping(value = "/getDubboCustomer")
    @ResponseBody
    public String getDubboCustomer(String schemaName, String target) throws Exception {
        if (schemaName == null) {
            return "请输入数据库名称";
        }else {
            String re = genDubboXMLService.geneCustomerXML(schemaName, TableUtil.databaseTypeMysql);
            String fileName = "spring-dubbo-consumer.xml";
            TableUtil.buildFile(TableUtil.controllerPath, fileName, re);
            return "success";
        }
    }

    @RequestMapping(value = "/getDubboProducer")
    @ResponseBody
    public String getDubboProducer(String schemaName) throws Exception {
        if (schemaName == null) {
           return "请输入数据库名称";
        }else {
            String re = genDubboXMLService.geneProducerXML(schemaName, TableUtil.databaseTypeMysql);
            String fileName = "spring-dubbo-provider.xml";
            TableUtil.buildFile(TableUtil.controllerPath, fileName, re);
            return "success";
        }
    }


}
