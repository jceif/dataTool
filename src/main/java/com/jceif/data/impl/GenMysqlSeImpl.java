package com.jceif.data.impl;


import org.springframework.stereotype.Repository;
import com.jceif.data.service.GenMysqlSeService;
import com.jceif.data.common.TableUtil;

@Repository("genMysqlSeService")
public class GenMysqlSeImpl implements GenMysqlSeService {

	private static final String LINE = "\r\n";
	private static final String TAB = "	";

	// @Autowired
	// @Qualifier("tableService")
	// private TableService tableService;
	//
	// @Autowired
	// @Qualifier("genCreateOracleService")
	// private GenCreateOracleService genCreateOracleService;


	public String geneService(String tableName, String schemaName)
			throws Exception {
		String className = TableUtil.toClassName(tableName);
		StringBuffer fileContent = new StringBuffer();
		fileContent.append("package " + TableUtil.MysqlServicePath
				+ className.toLowerCase() + " ;");
		fileContent.append(LINE);
		fileContent.append(LINE);
		fileContent.append("import java.util.List;");
		fileContent.append(LINE);
		fileContent.append("import java.util.Map;");
		fileContent.append(LINE);
		fileContent.append("import " + TableUtil.pageUtilPath);
		fileContent.append(LINE);
		fileContent.append("import " + TableUtil.MysqlPoPath
				+ className.toLowerCase() + "." + className + ";");
		fileContent.append(LINE);
		fileContent.append(LINE);
		fileContent.append("public interface " + className + "Service {");
		fileContent.append(LINE);
		fileContent.append(getInsert(className, true));
		fileContent.append(getDelete(className, true));
		fileContent.append(getDeleteAll(className, true));
		fileContent.append(getUpdate(className, true));
		fileContent.append(getFindById(className, true));
		fileContent.append(getFindList(className, true));
		fileContent.append(genDeleteListByIdsSql(className, true));
		fileContent.append(genDeleteListSql(className, true));
		fileContent.append(genInsertListSql(className, true));
		fileContent.append(genUpdateListSql(className, true));
		fileContent.append(genSelectQuery(className, true));
		fileContent.append(genSelectQueryPage(className, true));
		fileContent.append(genCountQuery(className, true));

		if (TableUtil.incrementMap.get(tableName) != null) {
			// 增量条件分页查询
			fileContent.append(genSelectIncrementQueryPage(className, true));
			// 增量条件查询总条数
			fileContent.append(genCountIncrementQuery(className, true));

		}

		fileContent.append(LINE);
		fileContent.append("}");
		return fileContent.toString();
	}


	public String geneServiceImpl(String tableName, String schemaName)
			throws Exception {
		String className = TableUtil.toClassName(tableName);
		StringBuffer fileContent = new StringBuffer();
		fileContent.append("package " + TableUtil.MysqlServicePath
				+ className.toLowerCase() + ".com.jceif.data.impl" + " ;");
		fileContent.append(LINE);
		fileContent.append(LINE);
		fileContent.append("import java.util.Map;");
		fileContent.append(LINE);
		fileContent.append("import java.util.List;");
		fileContent.append(LINE);

		fileContent.append("import " + TableUtil.pageUtilPath);
		fileContent.append(LINE);
		fileContent.append("import " + TableUtil.MysqlPoPath
				+ className.toLowerCase() + "." + className + ";");
		fileContent.append(LINE);
		fileContent.append("import " + TableUtil.MysqlMapperPath
				+ className.toLowerCase() + "." + className + "Mapper;");
		fileContent.append(LINE);
		fileContent.append("import " + TableUtil.MysqlServicePath
				+ className.toLowerCase() + "." + className + "Service;");

		fileContent.append(LINE);
		fileContent
				.append("import org.springframework.beans.factory.annotation.Autowired;");
		fileContent.append(LINE);
		fileContent.append("import org.springframework.stereotype.Service;");

		fileContent.append(LINE);
		fileContent.append("@Service(\"" + TableUtil.toParameterName(className)
				+ "Service" + "\")");
		fileContent.append(LINE);
		fileContent.append("public class " + className);
		fileContent.append("ServiceImpl implements " + className + "Service {");
		fileContent.append(LINE);
		fileContent.append(getMapperInfo(className));
		fileContent.append(getInsert(className, false));
		fileContent.append(getDelete(className, false));
		fileContent.append(getDeleteAll(className, false));
		fileContent.append(getUpdate(className, false));
		fileContent.append(getFindById(className, false));
		fileContent.append(getFindList(className, false));
		fileContent.append(genDeleteListByIdsSql(className, false));
		fileContent.append(genDeleteListSql(className, false));
		fileContent.append(genInsertListSql(className, false));
		fileContent.append(genUpdateListSql(className, false));
		fileContent.append(genSelectQuery(className, false));
		fileContent.append(genSelectQueryPage(className, false));
		fileContent.append(genCountQuery(className, false));

		if (TableUtil.incrementMap.get(tableName) != null) {
			// 增量条件分页查询
			fileContent.append(genSelectIncrementQueryPage(className, false));
			// 增量条件查询总条数
			fileContent.append(genCountIncrementQuery(className, false));
		}

		fileContent.append(LINE);
		fileContent.append("}");
		return fileContent.toString();
	}

	public String getMapperInfo(String className) {
		String auto = TAB + "@Autowired";
		String field = TAB + "private " + className + "Mapper "
				+ getParameterName(className) + "Mapper;";
		return LINE + auto + LINE + field + LINE;
	}

	public String getParameterName(String className) {
		return className.substring(0, 1).toLowerCase()
				+ className.substring(1, className.length());
	}

	public String getInsert(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public void insert" + className + "(" + className
				+ " " + parName + ")");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "this." + parName + "Mapper.insert"
					+ className + "(" + parName + ");");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	public String getUpdate(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public void update" + className + "(" + className
				+ " " + parName + ")");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "this." + parName + "Mapper.update"
					+ className + "(" + parName + ");");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	public String getDelete(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public void delete" + className + "ById(String "
				+ parName + "Id)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "this." + parName + "Mapper.delete"
					+ className + "ById(" + parName + "Id);");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	public String getDeleteAll(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public int delete" + className + "All()");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return this." + parName + "Mapper.delete"
					+ className + "All();");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	public String getFindById(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public " + className + " find" + className
				+ "ById(String " + parName + "Id)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return this." + parName + "Mapper.find"
					+ className + "ById(" + parName + "Id);");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	public String getFindList(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public List<" + className + "> find" + className
				+ "List()");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return this." + parName + "Mapper.find"
					+ className + "List();");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 根据ids批量删除
	// deleteFnClassificationList
	public String genDeleteListByIdsSql(String className, boolean isInterface) {

		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public int " + "delete" + className
				+ "ListByIds(String[] " + "Ids)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return this." + parName + "Mapper.delete"
					+ className + "ListByIds(Ids);");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 根据ids批量删除
	// deleteFnClassificationList
	public String genDeleteListSql(String className, boolean isInterface) {

		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public int " + "delete" + className + "List" + "("
				+ "List<" + className + ">" + " " + parName + "s)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return this." + parName + "Mapper.delete"
					+ className + "List" + "(" + parName + "s);");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 批量插入
	// insertFnClassificationList
	public String genInsertListSql(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		sb.append(TAB + "public void insert" + className + "List" + "("
				+ "List<" + className + ">" + " " + getParameterName(className)
				+ "s)");
		String parName = getParameterName(className);
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + " this." + parName + "Mapper.insert"
					+ className + "List" + "(" + parName + "s);");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 批量更新
	// updateFnClassificationList
	public String genUpdateListSql(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		sb.append(TAB + "public void update" + className + "List" + "("
				+ "List<" + className + ">" + " " + getParameterName(className)
				+ "s)");
		String parName = getParameterName(className);
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + " this." + parName + "Mapper.update"
					+ className + "List" + "(" + parName + "s);");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 条件分页查询
	// findFnClassificationQueryPage
	public String genSelectQuery(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public List<" + className + "> find" + className
				+ "Query(Map map)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return this." + parName + "Mapper.find"
					+ className + "Query(map);");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 条件分页查询
	// findFnClassificationQueryPage
	public String genSelectQueryPage(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		// String parName = getParameterName(className);
		// sb.append(TAB + "public PageUtil<" + className + "> find" + className
		// + "QueryPage(Map map,String pageNo,String pageSize)");
		// if (!isInterface) {
		// sb.append(TAB + "{");
		// sb.append(LINE);
		// sb.append(TAB + TAB + "return this." + parName + "Mapper.find"
		// + className + "QueryPage(map);");
		// sb.append(LINE);
		// sb.append(TAB + "}");

		String parName = getParameterName(className);
		sb.append(TAB + "public PageUtil<" + className + "> find" + className
				+ "QueryPage(Map map,String pageNo,String pageSize)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);

			// int totalCount = bsCustomerMapper.findCountBsCustomerQuery(map);
			// PageUtil pageUtil = new PageUtil(pageNo, totalCount, pageSize);
			// if (totalCount != 0) {
			// map.put("startIndex",pageUtil.getStartRecord());
			// map.put("limit",pageUtil.getPageSize());
			// List<BsCustomer> bsCustomerList =
			// bsCustomerMapper.findBsCustomerQueryPage(map);
			// pageUtil.setRecords(bsCustomerList);
			// }
			// return pageUtil;

			sb.append(TAB + TAB + "int totalCount = " + parName
					+ "Mapper.findCount" + className + "Query(map);");
			sb.append(LINE);
			sb
					.append(TAB
							+ TAB
							+ "PageUtil pageUtil = new PageUtil(pageNo, totalCount, pageSize);");
			sb.append(LINE);
			sb.append(TAB + TAB + "if (totalCount != 0) {");
			sb.append(LINE);
			sb.append(TAB + TAB + TAB + TAB + "map.put(\"" + "startIndex"
					+ "\",pageUtil.getStartRecord());");
			sb.append(LINE);
			sb.append(TAB + TAB + TAB + TAB + "map.put(\"" + "limit"
					+ "\",pageUtil.getPageSize());");
			sb.append(LINE);
			sb.append(TAB + TAB + TAB + TAB + "List<" + className + ">"
					+ parName + "List = ");

			sb.append("this." + parName + "Mapper.find" + className
					+ "QueryPage(map);");
			sb.append(LINE);
			sb.append(TAB + TAB + TAB + TAB +"pageUtil.setRecords(" + parName + "List);");
			sb.append(LINE);
			sb.append(TAB + TAB + "}");
			sb.append(LINE);
			sb.append(TAB + TAB + "return pageUtil;");

			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 条件查询总条数
	// findCountFnClassificationQuery
	public String genCountQuery(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public int findCount" + className + "Query(Map map)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return this." + parName + "Mapper.findCount"
					+ className + "Query(map);");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 增量条件分页查询
	// findFnClassificationIncrementQueryPage
	public String genSelectIncrementQueryPage(String className,
			boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}

		String parName = getParameterName(className);
		sb.append(TAB + "public PageUtil<" + className + "> find" + className
				+ "IncrementQueryPage(Map map,String pageNo,String pageSize)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "int totalCount = " + parName
					+ "Mapper.findCount" + className + "IncrementQuery(map);");
			sb.append(LINE);
			sb
					.append(TAB
							+ TAB
							+ "PageUtil pageUtil = new PageUtil(pageNo, totalCount, pageSize);");
			sb.append(LINE);
			sb.append(TAB + TAB + "if (totalCount != 0) {");
			sb.append(LINE);
			sb.append(TAB + TAB + TAB + TAB + "map.put(\"" + "startIndex"
					+ "\",pageUtil.getStartRecord());");
			sb.append(LINE);
			sb.append(TAB + TAB + TAB + TAB + "map.put(\"" + "limit"
					+ "\",pageUtil.getPageSize());");
			sb.append(LINE);
			sb.append(TAB + TAB + TAB + TAB + "List<" + className + ">"
					+ parName + "List = ");

			sb.append("this." + parName + "Mapper.find" + className
					+ "IncrementQueryPage(map);");
			sb.append(LINE);
			sb.append(TAB + TAB + TAB + TAB + "pageUtil.setRecords(" + parName + "List);");
			sb.append(LINE);
			sb.append(TAB + TAB + "}");
			sb.append(LINE);
			sb.append(TAB + TAB + "return pageUtil;");

			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 增量条件查询总条数
	// findCountFnClassificationIncrementQuery
	public String genCountIncrementQuery(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		String parName = getParameterName(className);
		sb.append(TAB + "public int findCount" + className
				+ "IncrementQuery(Map map)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return this." + parName + "Mapper.findCount"
					+ className + "IncrementQuery(map);");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

}
