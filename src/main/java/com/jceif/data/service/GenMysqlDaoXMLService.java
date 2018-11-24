package com.jceif.data.service;

import org.springframework.stereotype.Service;

@Service
public interface GenMysqlDaoXMLService {
	   String geneMybatisXML(String tableName, String schemaName) throws Exception;
}
