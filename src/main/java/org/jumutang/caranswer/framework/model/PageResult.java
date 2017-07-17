package org.jumutang.caranswer.framework.model;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author YuanYu
 *
 * @param <T>
 */
public class PageResult<T> implements Serializable
{

	private static final long serialVersionUID = -346604219202345145L;

	private Integer currPage; // 当前页
	private Integer totalPage; // 总页数
	private Integer pageSize;	//每页显示的数量
	private Long totalCount; // 总数量
	private List<T> queryResult; // 查询结果集合

	public Integer getPageSize()
	{
		return pageSize;
	}
	
	public PageResult<T> setPageSize(Integer pageSize)
	{
		this.pageSize = pageSize;
		if (null != this.pageSize && null != this.totalCount)
		{
			this.totalPage = (int) (this.totalCount % this.pageSize == 0 ?
					this.totalCount / this.pageSize : this.totalCount / this.pageSize + 1);
		}
		return this;
	}

	public Integer getCurrPage()
	{
		return currPage;
	}

	public PageResult<T> setCurrPage(Integer currPage)
	{
		this.currPage = currPage;
		return this;
	}

	public Integer getTotalPage()
	{
		return totalPage;
	}

	public PageResult<T> setTotalPage(Integer totalPage)
	{
		this.totalPage = totalPage;
		return this;
	}

	public Long getTotalCount()
	{
		return totalCount;
	}

	public PageResult<T> setTotalCount(Long totalCount)
	{
		this.totalCount = totalCount;
		if (null != this.totalPage && null != this.totalCount)
		{
			this.totalPage = (int) (this.totalCount % this.pageSize == 0 ? 
					this.totalCount / this.pageSize : this.totalCount / this.pageSize + 1);
		}
		return this;
	}

	public List<T> getQueryResult()
	{
		return queryResult;
	}

	public PageResult<T> setQueryResult(List<T> queryResult)
	{
		this.queryResult = queryResult;
		return this;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

}
