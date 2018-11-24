package com.jceif.data.common.mybatis;

public class PageAllInfo {
	// 分页参数
	private PageParameter page;

	// 可选分页参数
	private PageOptionParameter optionParam;
	
	
	public PageOptionParameter getOptionParam() {
		return optionParam;
	}

	public void setOptionParam(PageOptionParameter optionParam) {
		this.optionParam = optionParam;
	}

	public PageParameter getPage() {
		return page;
	}

	public void setPage(PageParameter page) {
		this.page = page;
	}
	
    
}
