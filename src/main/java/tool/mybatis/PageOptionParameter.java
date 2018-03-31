package tool.mybatis;

/**
 *  可选的分页参数 
 * @author hugo
 *
 */
public class PageOptionParameter {

    // 排序字段
    private String orderBy;
    

    // where 语句
    private String condition;

    public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	


	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
    
    
}
