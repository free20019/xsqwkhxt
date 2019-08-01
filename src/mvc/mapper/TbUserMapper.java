package mvc.mapper;

import mvc.pojo.TbUser;

public interface TbUserMapper {

    int deleteByPrimaryKey(String userId);

    int insert(TbUser record);

    int insertSelective(TbUser record);


    TbUser selectByPrimaryKey(String userId);


    int updateByPrimaryKeySelective(TbUser record);

    int updateByPrimaryKey(TbUser record);
}