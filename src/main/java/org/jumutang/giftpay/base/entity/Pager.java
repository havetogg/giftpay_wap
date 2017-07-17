package org.jumutang.giftpay.base.entity;


/**
 * 分页
 * @author Ryj
 *
 */
public class Pager {

	private int pageNum;
	private int pageRows;
	private int pageFirstIndex;
	private int pageEndIndex;
	public Pager(int pageNum, int pageRows) {
		this.pageNum = pageNum;
		this.pageRows = pageRows;
		this.pageFirstIndex = (pageNum-1)*pageRows;
		this.pageEndIndex = pageNum*pageRows;
	}

	public Pager(){

	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageRows() {
		return pageRows;
	}

	public void setPageRows(int pageRows) {
		this.pageRows = pageRows;
	}

	public int getPageFirstIndex() {
		return pageFirstIndex;
	}

	public void setPageFirstIndex(int pageFirstIndex) {
		this.pageFirstIndex = pageFirstIndex;
	}

	public int getPageEndIndex() {
		return pageEndIndex;
	}

	public void setPageEndIndex(int pageEndIndex) {
		this.pageEndIndex = pageEndIndex;
	}
}
