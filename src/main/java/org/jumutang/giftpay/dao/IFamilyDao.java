package org.jumutang.giftpay.dao;

import org.jumutang.giftpay.model.liftPaytment.FamilyModel;

import java.util.List;

/**
 * Created by RuanYJ on 2017/4/8.
 */
public interface IFamilyDao {
    List<FamilyModel> queryFamilyList(FamilyModel familyModel);
    int addFamilyModel(FamilyModel familyModel);
    int updateFamilyModel(FamilyModel familyModel);
    int removeFamilyModel(FamilyModel familyModel);
}
