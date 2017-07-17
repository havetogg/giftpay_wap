package org.jumutang.giftpay.base.dao;

import java.util.List;

/**
 * Created by RuanYJ on 2017/1/4.
 */
public interface BaseDaoSupport<T> {


    public int insertModel(T record);
    public int updateModel(T record);
    public int removeModel(T record);
    public List<T> queryModelList(List<T> list);
    public T queryObjectModel(T record);
    public int queryCount(T record);

}
