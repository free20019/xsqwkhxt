package mvc.mapper;

import mvc.pojo.TbMdtState;

public interface TbMdtStateMapper {
    int deleteByPrimaryKey(String msId);

    int insert(TbMdtState record);

    int insertSelective(TbMdtState record);

    TbMdtState selectByPrimaryKey(String msId);

    int updateByPrimaryKeySelective(TbMdtState record);

    int updateByPrimaryKey(TbMdtState record);
}