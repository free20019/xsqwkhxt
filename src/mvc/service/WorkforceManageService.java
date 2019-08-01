package mvc.service;

import java.util.List;

/**
 * @author: xianlehuang
 * @Description:排班管理
 * @date: 2019/06/17
 */
public interface WorkforceManageService {
    String findShiftInstall(String shiftname);

    Integer addShiftInstall(String shiftname, String starttime, String endtime, String ifdoublebreak, String shiftnote, String squadron);

    Integer updateShiftInstall(String shiftid, String shiftname, String starttime, String endtime, String ifdoublebreak, String shiftnote, String squadron);

    Integer deleteShiftInstall(String shiftid);

    String findTemplateInstall(String templatename);

    Integer addTemplateInstall(String templatename, String shiftcycle, String shiftnote, String squadron);

    Integer updateTemplateInstall(String templateid, String templatename, String shiftcycle, String shiftnote, String squadron);

    Integer deleteTemplateInstall(String templateid);

    String findDutySchedulingBrigade(String depname, String servicename, String stime, String etime);

    Integer addDutySchedulingBrigade(String servicename, String servicestartime, String serviceendtime, String serviceaddress, String depcode, List<String> policepost, String policenum, String fpolicenum, String servicenote);

    Integer updateDutySchedulingBrigade(String serviceid, String servicename, String servicestartime, String serviceendtime, String serviceaddress, String depcode, List<String> policepost, String policenum, String fpolicenum, String servicenote);

    Integer deleteDutySchedulingBrigade(String serviceid);

    String findDutySchedulingSquadron(String depname, String jyname, String servicename, String stime, String etime);

    Integer addDutySchedulingSquadron(String serviceid, String servicesquadronid, String depcode, String jycode, List<String> task, String tasknode);

    Integer updateDutySchedulingSquadron(String serviceid, String depcode, String jycode, List<String> task, String tasknode);

    Integer deleteDutySchedulingSquadron(String serviceid, String depcode);

    String findIntelligentScheduling(String date, String jyname);

    Integer addIntelligentScheduling(String date, String jzcode, String jycode, String templateid, String shiftname);

    Integer updateIntelligentScheduling(String schedulingid, String date, String templateid, String shiftname);

    String findIntelligentSchedulingByday(String day);
}
