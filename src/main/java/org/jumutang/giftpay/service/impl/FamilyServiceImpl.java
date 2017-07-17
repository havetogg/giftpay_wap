package org.jumutang.giftpay.service.impl;

import org.jumutang.giftpay.dao.IFamilyDao;
import org.jumutang.giftpay.model.liftPaytment.FamilyModel;
import org.jumutang.giftpay.service.IFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by RuanYJ on 2017/4/8.
 */
@Service("familyService")
public class FamilyServiceImpl implements IFamilyService {

    @Autowired
    private IFamilyDao familyDaoImpl;

    @Override
    public List<FamilyModel> queryFamilyList(FamilyModel familyModel) {
        if(StringUtils.isEmpty(familyModel.getStatus())){
            familyModel.setStatus("0");
        }
        return familyDaoImpl.queryFamilyList(familyModel);
    }

    @Override
    public int addFamilyModel(FamilyModel familyModel) {
        return familyDaoImpl.addFamilyModel(familyModel);
    }

    @Override
    public int updateFamilyModel(FamilyModel familyModel) {
        return familyDaoImpl.updateFamilyModel(familyModel);
    }

    @Override
    public int removeFamilyModel(FamilyModel familyModel) {
        return familyDaoImpl.removeFamilyModel(familyModel);
    }
}
