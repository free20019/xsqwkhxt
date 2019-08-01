package mvc.service;

/**
 * @author: xianlehuang
 * @Description:培训计划发布，培训预约
 * @date: 2019/06/03
 */
public interface TrainService {
    String findRelease(String plateName, String stime, String etime);

    Integer addRelease(String plateName, String plateType, String plateNO, String plateDate, String plateSum, String publishUser, String status);

    Integer updateRelease(String id, String plateName, String plateType, String plateNO, String plateDate, String plateSum, String publishUser, String status);

    Integer auditRelease(String id, String status);

    Integer deleteRelease(String id);

    String findOrder(String stuName, String stime, String etime);
}
