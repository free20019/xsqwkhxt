package mvc.controller;

import mvc.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import util.DownloadAct;
import util.JacksonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: xianlehuang
 * @Description:培训计划发布，培训预约
 * @date: 2019/06/03
 */

@Controller
@RequestMapping("/train")
public class TrainController {
    @Autowired
    private TrainService trainService;
    private DownloadAct downloadAct = new DownloadAct();
    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    //培训计划发布查询
    @RequestMapping("/findRelease")
    @ResponseBody
    public String findRelease(String plateName,String stime,String etime){
        return trainService.findRelease(plateName, stime, etime);
    }

    //培训计划发布导出
    @RequestMapping("/findReleaseExcel")
    @ResponseBody
    public String findRecruitmentExcel(HttpServletRequest request, HttpServletResponse response, String plateName,String stime,String etime) throws IOException {
        String a[] = {"计划名称","计划类型","计划编号","培训日期","培训计划人数","发布人的账户","状态","创建时间"};//导出列明
        String b[] = {"plateName", "plateType", "plateNO", "plateDate", "plateSum", "publishUser", "status", "publishDate"};//导出map中的key
        String gzb = "培训计划发布";//导出sheet名和导出的文件名
        String msg = trainService.findRelease(plateName, stime, etime);
        List<Map<String, Object>> list = downloadAct.strlist(msg);
        if(list.size()>0){
            for (int i=0; i<list.size(); i++){
                if(String.valueOf(list.get(i).get("plateType")).equals("1")){
                    list.get(i).put("plateType","培训");
                }else if(String.valueOf(list.get(i).get("plateType")).equals("2")){
                    list.get(i).put("plateType","复训");
                }
                if(String.valueOf(list.get(i).get("status")).equals("0")){
                    list.get(i).put("status","有效");
                }else if(String.valueOf(list.get(i).get("status")).equals("1")){
                    list.get(i).put("status","无效");
                }
            }
        }
        downloadAct.download(request,response,a,b,gzb,list);
        return null;
    }

    //培训计划发布添加
    @RequestMapping("/addRelease")
    @ResponseBody
    public Integer addRelease(String plateName, String plateType, String plateNO, String plateDate, String plateSum, String publishUser, String status){
        return trainService.addRelease(plateName, plateType, plateNO, plateDate, plateSum, publishUser, status);
    }

    //培训计划发布修改
    @RequestMapping("/updateRelease")
    @ResponseBody
    public Integer updateRelease(String id,String plateName, String plateType, String plateNO, String plateDate, String plateSum, String publishUser, String status){
        return trainService.updateRelease(id, plateName, plateType, plateNO, plateDate, plateSum, publishUser, status);
    }

    //培训计划发布
    @RequestMapping("/auditRelease")
    @ResponseBody
    public Integer auditRelease(String id, String status){
        return trainService.auditRelease(id, status);
    }

    //培训计划发布删除
    @RequestMapping("/deleteRelease")
    @ResponseBody
    public Integer deleteRelease(String id){
        return trainService.deleteRelease(id);
    }

    //培训预约查询
    @RequestMapping("/findOrder")
    @ResponseBody
    public String findOrder(String stuName,String stime,String etime){
        return trainService.findOrder(stuName, stime, etime);
    }

    //培训预约导出
    @RequestMapping("/findOrderExcel")
    @ResponseBody
    public String findOrderExcel(HttpServletRequest request, HttpServletResponse response, String stuName,String stime,String etime) throws IOException {
        String a[] = {"预约人账户ID","从业资格证号","手机号码","姓名","所在单位","预约状态","创建时间"};//导出列明
        String b[] = {"ownerId", "certificateNumber", "stuPhone", "stuName", "stuComp",  "appointmentStatus", "appointmentDate"};//导出map中的key
        String gzb = "培训预约";//导出sheet名和导出的文件名
        String msg = trainService.findOrder(stuName, stime, etime);
        List<Map<String, Object>> list = downloadAct.strlist(msg);
        if(list.size()>0){
            for (int i=0; i<list.size(); i++){
                if(String.valueOf(list.get(i).get("appointmentStatus")).equals("0")){
                    list.get(i).put("appointmentStatus","预约成功");
                }else if(String.valueOf(list.get(i).get("appointmentStatus")).equals("1")){
                    list.get(i).put("appointmentStatus","取消预约");
                }
            }
        }
        downloadAct.download(request,response,a,b,gzb,list);
        return null;
    }

}
