package com.jceif.data.impl;


import com.jceif.data.dao.mapper.TableMapper;
import com.jceif.data.model.Table;
import com.jceif.data.model.TableColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jceif.data.service.TableService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tableService")
public class TableServiceImpl implements TableService {
    @Autowired
    private TableMapper tableMapper;

    public Table getTableDefine(String tableName, String schemaName){
        Map<String,Object> map=new HashMap();
        map.put("TABLE_NAME",tableName);
        map.put("TABLE_SCHEMA",schemaName);
        return tableMapper.getTableDefine(tableName, schemaName);
    }
    public List<Table> getTableDefineBySchema(String schemaName){
        return tableMapper.getTableDefineBySchema(schemaName);
    }

    public List<TableColumn> getMysqlTableColumnInfo(String tableName, String schemaName) {
        Map<String,Object> map=new HashMap();
        map.put("TABLE_NAME",tableName);
        map.put("TABLE_SCHEMA",schemaName);
        List<TableColumn> lstTableColumn=tableMapper.getTableColumnInfo(map);
        for(int i=0;i<lstTableColumn.size();i++){
            TableColumn tableColumn = lstTableColumn.get(i);
            String strComment=tableColumn.getColumnComment();
            strComment=strComment.replaceAll("\r\n", "");
            tableColumn.setColumnComment(strComment);
        }
        return lstTableColumn;
    }




}