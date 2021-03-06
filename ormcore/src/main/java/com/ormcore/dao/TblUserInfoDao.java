package com.ormcore.dao;

import com.ormcore.model.TblUserInfo;

import java.util.List;
import java.util.Map;

public interface TblUserInfoDao {
	
    public List<TblUserInfo> findUserInfoByPhone(String account);


    public List<TblUserInfo> findUserInfoByCard(String account);

    public List<TblUserInfo> findUserInfoById(int accountId);

    public List<TblUserInfo> findCardUserById(int accountId);

    public List<TblUserInfo> findThirdUserById(int accountId);

    public List<TblUserInfo> findOrgUserById(int accountId);

    public List<TblUserInfo> findUserInfoByOrg(int orgNo);

    public String executeSql(String sql);

    public int updateBalance(TblUserInfo info);
    
    public int subCost(TblUserInfo info);
    
    public int addCost(TblUserInfo info);
    
    public int insertUser(Map<String, Object> map);
    
    public int insertNormalUser(Map<String, Object> map);

    public int subElectric(Map<String, Object> map);

    public int addElectric(Map<String, Object> map);

    public int queryElectricRemain(int id);

    public int insertElectricDetail(Map<String, Object> map);
}
