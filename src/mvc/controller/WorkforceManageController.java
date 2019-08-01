package mvc.controller;

import com.alibaba.fastjson.JSON;
import mvc.service.WorkforceManageService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import util.DownloadAct;
import util.JacksonUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: xianlehuang
 * @Description:排班管理
 * @date: 2019/06/17
 */

@Controller
@RequestMapping("/workforcemanage")
public class WorkforceManageController {
    @Autowired
    private WorkforceManageService workforcemanageservice;
    private DownloadAct downloadAct = new DownloadAct();
    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    //班次设置查询
    @RequestMapping("/findShiftInstall")
    @ResponseBody
    public String findShiftInstall(String shiftname){
        return workforcemanageservice.findShiftInstall(shiftname);
    }

    //班次设置添加
    @RequestMapping("/addShiftInstall")
    @ResponseBody
    public Integer addShiftInstall(String shiftname, String starttime, String endtime, String ifdoublebreak, String shiftnote, String squadron){
        return workforcemanageservice.addShiftInstall(shiftname, starttime, endtime, ifdoublebreak, shiftnote, squadron);
    }

    //班次设置修改
    @RequestMapping("/updateShiftInstall")
    @ResponseBody
    public Integer updateShiftInstall(String shiftid,String shiftname, String starttime, String endtime, String ifdoublebreak, String shiftnote, String squadron){
        return workforcemanageservice.updateShiftInstall(shiftid, shiftname, starttime, endtime, ifdoublebreak, shiftnote, squadron);
    }

    //班次设置删除
    @RequestMapping("/deleteShiftInstall")
    @ResponseBody
    public Integer deleteRecruitment(String shiftid){
        return workforcemanageservice.deleteShiftInstall(shiftid);
    }

    //模板设置查询
    @RequestMapping("/findTemplateInstall")
    @ResponseBody
    public String findTemplateInstall(String templatename){
        return workforcemanageservice.findTemplateInstall(templatename);
    }

    //模板设置添加
    @RequestMapping("/addTemplateInstall")
    @ResponseBody
    public Integer addTemplateInstall(String templatename, String shiftcycle, String shiftnote, String squadron){
        return workforcemanageservice.addTemplateInstall(templatename, shiftcycle, shiftnote, squadron);
    }

    //模板设置修改
    @RequestMapping("/updateTemplateInstall")
    @ResponseBody
    public Integer updateTemplateInstall(String templateid, String templatename, String shiftcycle, String shiftnote, String squadron){
        return workforcemanageservice.updateTemplateInstall(templateid, templatename, shiftcycle, shiftnote, squadron);
    }

    //模板设置删除
    @RequestMapping("/deleteTemplateInstall")
    @ResponseBody
    public Integer deleteTemplateInstall(String templateid){
        return workforcemanageservice.deleteTemplateInstall(templateid);
    }

    //勤务查询(大队)
    @RequestMapping("/findDutySchedulingBrigade")
    @ResponseBody
    public String findDutySchedulingBrigade(HttpServletRequest request,String depname, String servicename, String stime, String etime){
        return workforcemanageservice.findDutySchedulingBrigade(depname, servicename, stime, etime);
    }

    //勤务添加(大队)
    @RequestMapping("/addDutySchedulingBrigade")
    @ResponseBody
    public Integer addDutySchedulingBrigade(HttpServletRequest request,String servicename, String servicestartime, String serviceendtime, String serviceaddress, String depcode, String policepost, String policenum, String fpolicenum, String servicenote){
        List<String> policepost1=JSON.parseArray(policepost,String.class);
        return workforcemanageservice.addDutySchedulingBrigade(servicename, servicestartime, serviceendtime, serviceaddress, depcode, policepost1, policenum, fpolicenum, servicenote);
    }

    //勤务修改(大队)
    @RequestMapping("/updateDutySchedulingBrigade")
    @ResponseBody
    public Integer updateDutySchedulingBrigade(String serviceid, String servicename, String servicestartime, String serviceendtime, String serviceaddress, String depcode, String policepost, String policenum, String fpolicenum, String servicenote){
        List<String> policepost1=JSON.parseArray(policepost,String.class);
        return workforcemanageservice.updateDutySchedulingBrigade(serviceid, servicename, servicestartime, serviceendtime, serviceaddress, depcode, policepost1, policenum, fpolicenum, servicenote);
    }

    //勤务删除(大队)
    @RequestMapping("/deleteDutySchedulingBrigade")
    @ResponseBody
    public Integer deleteDutySchedulingBrigade(String serviceid){
        return workforcemanageservice.deleteDutySchedulingBrigade(serviceid);
    }

    //勤务查询(中队)
    @RequestMapping("/findDutySchedulingSquadron")
    @ResponseBody
    public String findDutySchedulingSquadron(String depname, String jyname, String servicename, String stime, String etime){
        return workforcemanageservice.findDutySchedulingSquadron(depname, jyname, servicename, stime, etime);
    }

    //勤务添加(中队)
    @RequestMapping("/addDutySchedulingSquadron")
    @ResponseBody
    public Integer addDutySchedulingSquadron(String serviceid,String servicesquadronid, String depcode, String jycode, String task, String tasknode){
        List<String> task1=JSON.parseArray(task,String.class);
        return workforcemanageservice.addDutySchedulingSquadron(serviceid, servicesquadronid, depcode, jycode, task1, tasknode);
    }

    //勤务修改(中队)
    @RequestMapping("/updateDutySchedulingSquadron")
    @ResponseBody
    public Integer updateDutySchedulingSquadron(String serviceid, String depcode, String jycode, String task, String tasknode){
        List<String> task1=JSON.parseArray(task,String.class);
        return workforcemanageservice.updateDutySchedulingSquadron(serviceid, depcode, jycode, task1, tasknode);
    }

    //勤务删除(中队)
    @RequestMapping("/deleteDutySchedulingSquadron")
    @ResponseBody
    public Integer deleteDutySchedulingSquadron(String serviceid,String depcode){
        return workforcemanageservice.deleteDutySchedulingSquadron(serviceid, depcode);
    }

    //智能排班查询
    @RequestMapping("/findIntelligentScheduling")
    @ResponseBody
    public String findIntelligentScheduling(String date, String jyname){
        return workforcemanageservice.findIntelligentScheduling(date, jyname);
    }

    //智能排班添加
    @RequestMapping("/addIntelligentScheduling")
    @ResponseBody
    public Integer addIntelligentScheduling(String date, String jzcode, String jycode, String templateid, String shiftname){
        return workforcemanageservice.addIntelligentScheduling(date, jzcode, jycode, templateid, shiftname);
    }

    //排班单条修改
    @RequestMapping("/updateIntelligentScheduling")
    @ResponseBody
    public Integer updateIntelligentScheduling(String schedulingid, String date, String templateid, String shiftname){
        return workforcemanageservice.updateIntelligentScheduling(schedulingid, date, templateid, shiftname);
    }

    //智能排班查询(按照日期查询)
    @RequestMapping("/findIntelligentSchedulingByday")
    @ResponseBody
    public String findIntelligentSchedulingByday(String day){
        return workforcemanageservice.findIntelligentSchedulingByday(day);
    }
}
