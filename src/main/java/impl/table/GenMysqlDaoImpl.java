package impl.table;


import org.springframework.stereotype.Repository;
import service.table.GenMysqlDaoService;
import tool.TableUtil;

@Repository("genMysqlDaoService")
public class GenMysqlDaoImpl implements GenMysqlDaoService {

	private static final String LINE = "\r\n";
	private static final String TAB = "	";

	// @Autowired
	// @Qualifier("tableService")
	// private TableService tableService;
	//
	// @Autowired
	// @Qualifier("genCreateOracleService")
	// private GenCreateOracleService genCreateOracleService;


	public String geneMapper(String tableName, String schemaName)
			throws Exception {
		String className = TableUtil.toClassName(tableName);
		StringBuffer fileContent = new StringBuffer();

		fileContent.append("package " + TableUtil.MysqlMapperPath
				+ className.toLowerCase() + " ;");
		fileContent.append(LINE);
		fileContent.append(LINE);
		fileContent.append("import java.util.List;");
		fileContent.append("import java.util.Map;");
		fileContent.append(LINE);
		fileContent.append("import " + TableUtil.MysqlPoPath
				+ className.toLowerCase() + "." + className + ";");
		fileContent.append(LINE);
		fileContent.append(LINE);
		fileContent.append("public interface " + className + "Mapper {");
		fileContent.append(LINE);
		fileContent.append(getInsert(className, true));
		fileContent.append(getDelete(className, true));
		fileContent.append(getDeleteAll(className, true));
		fileContent.append(getUpdate(className, true));
		fileContent.append(getFindById(className, true));
		fileContent.append(getFindList(className, true));
		fileContent.append(genDeleteListSql(className, true));
		fileContent.append(genDeleteListByIdsSql(className, true));
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

	public String getInsert(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		sb.append(TAB + "public void insert" + className + "(" + className
				+ " " + TableUtil.toParameterName(className) + ")");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
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
		sb.append(TAB + "public void update" + className + "(" + className
				+ " " + TableUtil.toParameterName(className) + ")");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
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
		String parName = TableUtil.toParameterName(className);
		sb.append(TAB + "public void delete" + className + "ById(String "
				+ parName + "Id)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
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
		sb.append(TAB + "public int delete" + className + "All()");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
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
		String parName = TableUtil.toParameterName(className);
		sb.append(TAB + "public " + className + " find" + className
				+ "ById(String " + parName + "Id)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
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
		sb.append(TAB + "public List<" + className + "> find" + className
				+ "List()");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 根据id批量删除
	// deleteFnClassificationList String[] ids
	public String genDeleteListByIdsSql(String className, boolean isInterface) {

		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		sb.append(TAB + "public int " + "delete" + className
				+ "ListByIds(String[] " + "Ids)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();

		// return null;
	}

	// 批量删除
	// deleteFnClassificationList String[] ids
	public String genDeleteListSql(String className, boolean isInterface) {

		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		sb.append(TAB + "public int delete" + className + "List" + "("
				+ "List<" + className + ">" + " "
				+ TableUtil.toParameterName(className) + "s)");

		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();

		// return null;
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
		sb.append(TAB + "public int insert" + className + "List" + "("
				+ "List<" + className + ">" + " "
				+ TableUtil.toParameterName(className) + "s)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
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
				+ "List<" + className + ">" + " "
				+ TableUtil.toParameterName(className) + "s)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 条件分页查询
	// findFnClassificationQueryPager
	public String genSelectQuery(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		sb.append(TAB + "public List<" + className + "> find" + className
				+ "Query(Map map)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 条件分页查询
	// findFnClassificationQueryPager
	public String genSelectQueryPage(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		sb.append(TAB + "public List<" + className + "> find" + className
				+ "QueryPage(Map map)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
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
		sb.append(TAB + "public int findCount" + className + "Query(Map map)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

	// 条件分页查询
	// findFnClassificationIncrementQueryPager
	public String genSelectIncrementQueryPage(String className,
			boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		sb.append(TAB + "public List<" + className + "> find" + className
				+ "IncrementQueryPage(Map map)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
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
	public String genCountIncrementQuery(String className, boolean isInterface) {
		StringBuffer sb = new StringBuffer();
		sb.append(LINE);
		if (!isInterface) {
			sb.append(TAB + "@Override");
			sb.append(LINE);
		}
		sb.append(TAB + "public int findCount" + className
				+ "IncrementQuery(Map map)");
		if (!isInterface) {
			sb.append(TAB + "{");
			sb.append(LINE);
			sb.append(TAB + TAB + "return null;");
			sb.append(LINE);
			sb.append(TAB + "}");
		} else {
			sb.append(";");
		}
		sb.append(LINE);
		return sb.toString();
	}

}
