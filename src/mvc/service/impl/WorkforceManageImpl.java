package mvc.service.impl;

import com.alibaba.fastjson.JSON;
import mvc.service.WorkforceManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import util.JacksonUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author: xianlehuang
 * @Description:排班管理
 * @date: 2019/06/17
 */

@Service("workforcemanageService")
public class WorkforceManageImpl implements WorkforceManageService {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    @Override
    public String findShiftInstall(String shiftname) {
        String tj="";
        if(shiftname!=null&&!shiftname.isEmpty()&&!shiftname.equals("null")&&shiftname.length()>0){
            tj +=" and a.shiftname like '%"+shiftname+"%'";
        }
        String sql="select distinct a.*,case when t.shiftcycle <> '' then '关联' else '不关联' end about  from tb_qw_pb_shift a" +
                " left join tb_qw_pb_template t on find_in_set(a.shiftid,t.shiftcycle) where 1=1";
        sql +=tj;
        sql +=" ORDER BY CONVERT(a.shiftname USING gbk) desc ";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    @Override
    public Integer addShiftInstall(String shiftname, String starttime, String endtime, String ifdoublebreak, String shiftnote, String squadron) {
        int count=0;
        String sql="insert into tb_qw_pb_shift (shiftname, starttime, endtime, ifdoublebreak, shiftnote, squadron)" +
                "values ('"+shiftname+"','"+starttime+"','"+endtime+"','"+ifdoublebreak+"','"+shiftnote+"','"+squadron+"')";
        try {
            count=jdbcTemplate.update(sql);
        }catch(DataAccessException e) {
            String cx="select * from tb_qw_pb_shift where shiftname='"+shiftname+"'";
            List<Map<String, Object>> list=jdbcTemplate.queryForList(cx);
            if(list.size()>0){
                count=-1;
                return count;
            }else{
                count=-2;
                return count;
            }
        }
        return count;
    }

    @Override
    public Integer updateShiftInstall(String shiftid, String shiftname, String starttime, String endtime, String ifdoublebreak, String shiftnote, String squadron) {
        String select="select * from tb_qw_pb_template t where find_in_set("+shiftid+",t.shiftcycle)";
        List<Map<String, Object>> selectList=jdbcTemplate.queryForList(select);
        int count=0;
        if(selectList.size()>0){
            count=-3;
            return count;
        }
        String sql="update tb_qw_pb_shift set shiftname='"+shiftname+"',starttime='"+starttime+"'," +
                " endtime='"+endtime+"',ifdoublebreak='"+ifdoublebreak+"',shiftnote='"+shiftnote+"',squadron='"+squadron+"'" +
                " where shiftid='"+shiftid+"'";
        try {
            count=jdbcTemplate.update(sql);
        }catch(DataAccessException e) {
            String cx="select * from tb_qw_pb_shift where shiftname='"+shiftname+"' and shiftid!='"+shiftid+"'";
            List<Map<String, Object>> list=jdbcTemplate.queryForList(cx);
            if(list.size()>0){
                count=-1;
                return count;
            }else{
                count=-2;
                return count;
            }
        }
        return count;
    }

    @Override
    public Integer deleteShiftInstall(String shiftid) {
        int count=0;
        String cx="select * from tb_qw_pb_template where find_in_set('"+shiftid+"',shiftcycle)";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(cx);
        if(list.size()>0){
            count=-3;
            return count;
        }
        String sql="delete from tb_qw_pb_shift where shiftid in ("+shiftid+")";
        count=jdbcTemplate.update(sql);
        return count;
    }

    @Override
    public String findTemplateInstall(String templatename) {
        String tj="";
        if(templatename!=null&&!templatename.isEmpty()&&!templatename.equals("null")&&templatename.length()>0){
            tj +=" and t.templatename like '%"+templatename+"%'";
        }
        String sql="select distinct tt.*,case when s.templateid <> '' then '关联' else '不关联' end about from (select t.*,group_concat(s.shiftname ORDER BY find_in_set(s.shiftid, t.shiftcycle)) shiftcyclename" +
                " from tb_qw_pb_template t, tb_qw_pb_shift s where find_in_set(s.shiftid,t.shiftcycle)";
        sql +=tj;
        sql +=" group by templateId ORDER BY CONVERT(t.templatename USING gbk) desc) tt" +
                " left join tb_qw_pb_scheduling s on tt.templateId=s.templateid";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        return jacksonUtil.toJson(list);
    }

    @Override
    public Integer addTemplateInstall(String templatename, String shiftcycle, String shiftnote, String squadron) {
        int count=0;
        String sql="insert into tb_qw_pb_template (templatename, shiftcycle, shiftnote, squadron)" +
                "values ('"+templatename+"','"+shiftcycle+"','"+shiftnote+"','"+squadron+"')";
        try {
            count=jdbcTemplate.update(sql);
        }catch(DataAccessException e) {
            String cx="select * from tb_qw_pb_template where templatename='"+templatename+"'";
            List<Map<String, Object>> list=jdbcTemplate.queryForList(cx);
            if(list.size()>0){
                count=-1;
                return count;
            }else{
                count=-2;
                return count;
            }
        }
        return count;
    }

    @Override
    public Integer updateTemplateInstall(String templateid, String templatename, String shiftcycle, String shiftnote, String squadron) {
        String select="select * from tb_qw_pb_scheduling where templateid='"+templateid+"'";
        List<Map<String, Object>> selectList=jdbcTemplate.queryForList(select);
        int count=0;
        if(selectList.size()>0){
            count=-3;
            return count;
        }
        String sql="update tb_qw_pb_template set templatename='"+templatename+"',shiftcycle='"+shiftcycle+"'," +
                " shiftnote='"+shiftnote+"',squadron='"+squadron+"'" +
                " where templateid='"+templateid+"'";
        try {
            count=jdbcTemplate.update(sql);
        }catch(DataAccessException e) {
            String cx="select * from tb_qw_pb_template where templatename='"+templatename+"' and templateid!='"+templateid+"'";
            List<Map<String, Object>> list=jdbcTemplate.queryForList(cx);
            if(list.size()>0){
                count=-1;
                return count;
            }else{
                count=-2;
                return count;
            }
        }
        return count;
    }

    @Override
    public Integer deleteTemplateInstall(String templateid) {
        String select="select * from tb_qw_pb_scheduling where templateid='"+templateid+"'";
        List<Map<String, Object>> selectList=jdbcTemplate.queryForList(select);
        int count=0;
        if(selectList.size()>0){
            count=-3;
            return count;
        }
        String sql="delete from tb_qw_pb_template where templateid in ("+templateid+")";
        count=jdbcTemplate.update(sql);
        return count;
    }

    @Override
    public String findDutySchedulingBrigade(String depname, String servicename, String stime, String etime) {
        String tj="";
        if(depname!=null&&!depname.isEmpty()&&!depname.equals("null")&&depname.length()>0){
            tj +=" and d.depname like '%"+depname+"%'";
        }
        if(servicename!=null&&!servicename.isEmpty()&&!servicename.equals("null")&&servicename.length()>0){
            tj +=" and s.servicename like '%"+servicename+"%'";
        }
        if(stime!=null&&!stime.isEmpty()&&!stime.equals("null")&&stime.length()>0){
            tj +=" and s.servicestartime >=str_to_date('"+stime+" 00:00:00','%Y-%m-%d %H:%i:%s')";
        }
        if(etime!=null&&!etime.isEmpty()&&!etime.equals("null")&&etime.length()>0){
            tj +=" and s.servicestartime <=str_to_date('"+etime+" 23:59:59','%Y-%m-%d %H:%i:%s')";
        }
        String sql="select s.*,group_concat(d.depcode order by d.depcode) depcode,group_concat(d.depname order by d.depcode) depname, group_concat(d.policepost  order by d.depcode separator '&d;') policepost" +
                ",group_concat(d.policenum  order by d.depcode) policenum, group_concat(d.fpolicenum  order by d.depcode) fpolicenum,sum(d.policenum+d.fpolicenum) personsum from tb_qw_qw_service s " +
                " left join (select q.*,d.depname from tb_qw_qw_servicesquadron q,tl_p_department d where q.depcode=d.depcode) d" +
                " on d.serviceid=s.serviceid where 1=1";
        sql +=tj;
        sql +=" group by s.serviceid order by s.servicename desc";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        if(list.size()>0){
            for (int i=0; i<list.size(); i++){
                list.get(i).put("servicetime",String.valueOf(list.get(i).get("servicetime")).equals("null")?"":String.valueOf(list.get(i).get("servicetime")).substring(0,19));
            }
        }
        return jacksonUtil.toJson(list);
    }

    @Override
    public Integer addDutySchedulingBrigade(String servicename, String servicestartime, String serviceendtime, String serviceaddress, String depcode, List<String> policepost, String policenum, String fpolicenum, String servicenote) {
        List depcodeList= Arrays.asList(depcode.split(","));
        List policenumList= Arrays.asList(policenum.split(","));
        List fpolicenumList= Arrays.asList(fpolicenum.split(","));
        String serviceagent="";
        int count=0;
        int id = findMaxId("tb_qw_qw_service","serviceid");
        String sql="insert into tb_qw_qw_service (servicename, servicestartime, serviceendtime, serviceaddress, serviceagent, servicenote, servicetime,serviceid)" +
                "values ('"+servicename+"',str_to_date('"+servicestartime+"','%Y-%m-%d'),str_to_date('"+serviceendtime+"','%Y-%m-%d'),'"+serviceaddress+"','"+serviceagent+"','"+servicenote+"',NOW(),'"+id+"')";
        try {
            count=jdbcTemplate.update(sql);
        }catch(DataAccessException e) {
            count=-2;
            return count;
        }
        if(count>0){
//            String cx="select @@IDENTITY  id from tb_qw_qw_service ";
//            List<Map<String, Object>> list=jdbcTemplate.queryForList(cx);
            for(int i=0;i<depcodeList.size();i++){
                String sql2="insert into tb_qw_qw_servicesquadron (serviceid, depcode, policenum, fpolicenum, policepost)" +
                        "values ('"+id+"','"+depcodeList.get(i)+"','"+policenumList.get(i)+"','"+fpolicenumList.get(i)+"','"+policepost.get(i)+"')";
                try {
                    jdbcTemplate.update(sql2);
                }catch(DataAccessException e) {
                    count=-2;
                    return count;
                }
            }
        }
        return count;
    }

    @Override
    public Integer updateDutySchedulingBrigade(String serviceid, String servicename, String servicestartime, String serviceendtime, String serviceaddress, String depcode, List<String> policepost, String policenum, String fpolicenum, String servicenote) {
        List depcodeList= Arrays.asList(depcode.split(","));
        List policenumList= Arrays.asList(policenum.split(","));
        List fpolicenumList= Arrays.asList(fpolicenum.split(","));
        int count=0;
        String sql="update tb_qw_qw_service set servicename='"+servicename+"',servicestartime=str_to_date('"+servicestartime+"','%Y-%m-%d')," +
                " serviceendtime=str_to_date('"+serviceendtime+"','%Y-%m-%d'),serviceaddress='"+serviceaddress+"',servicenote='"+servicenote+"'" +
                " where serviceid='"+serviceid+"'";
        try {
            count=jdbcTemplate.update(sql);
        }catch(DataAccessException e) {
            count=-2;
            return count;
        }
        if(count>0){
            String delete="delete from tb_qw_qw_servicesquadron where serviceid in ("+serviceid+")";
            jdbcTemplate.update(delete);
            String delete2="delete from tb_qw_qw_servicesperson where serviceid= "+serviceid+" and depcode not in("+depcode+")";
            jdbcTemplate.update(delete2);
            for(int i=0;i<depcodeList.size();i++){
                String sql2="insert into tb_qw_qw_servicesquadron (serviceid, depcode, policenum, fpolicenum, policepost)" +
                        "values ('"+serviceid+"','"+depcodeList.get(i)+"','"+policenumList.get(i)+"','"+fpolicenumList.get(i)+"','"+policepost.get(i)+"')";
                try {
                    jdbcTemplate.update(sql2);
                }catch(DataAccessException e) {
                    count=-2;
                    return count;
                }
            }
        }
        return count;
    }

    @Override
    public Integer deleteDutySchedulingBrigade(String serviceid) {
        int count=0;
        String sql="delete from tb_qw_qw_service where serviceid in ("+serviceid+")";
        count=jdbcTemplate.update(sql);
//        String delete="delete from tb_qw_qw_servicesquadron where serviceid in ("+serviceid+")";
//        jdbcTemplate.update(delete);
//        jdbcTemplate.update("delete from tb_qw_qw_servicesperson where serviceid in ("+serviceid+")");
        return count;
    }

    @Override
    public String findDutySchedulingSquadron(String depname, String jyname, String servicename, String stime, String etime) {
        String tj="";
        if(depname!=null&&!depname.isEmpty()&&!depname.equals("null")&&depname.length()>0){
            tj +=" and d.depname like '%"+depname+"%'";
        }
        if(jyname!=null&&!jyname.isEmpty()&&!jyname.equals("null")&&jyname.length()>0&&!jyname.equals(",")){
            tj +=" and d.jyname like '%"+jyname+"%'";
//            tj +=" and find_in_set('"+jyname+"',d.jyname)";
        }
        if(servicename!=null&&!servicename.isEmpty()&&!servicename.equals("null")&&servicename.length()>0){
            tj +=" and se.servicename like '%"+servicename+"%'";
        }
        if(stime!=null&&!stime.isEmpty()&&!stime.equals("null")&&stime.length()>0){
            tj +=" and se.servicestartime >=str_to_date('"+stime+" 00:00:00','%Y-%m-%d %H:%i:%s')";
        }
        if(etime!=null&&!etime.isEmpty()&&!etime.equals("null")&&etime.length()>0){
            tj +=" and se.servicestartime <=str_to_date('"+etime+" 23:59:59','%Y-%m-%d %H:%i:%s')";
        }
        String sql="select se.*,s.policenum,s.fpolicenum,s.servicesquadronid,s.depcode,de.depname,d.* from tb_qw_qw_servicesquadron s " +
                " left join (select d.depcode depcodeextra,s.tasknode,s.serviceid serviceidextra, group_concat(j.jycode order by j.jycode) jycode,group_concat(j.jyname order by j.jycode) jyname,group_concat(s.task order by j.jycode separator '&d;') task" +
                " from tl_p_department d, tl_p_jybmglb dj, tl_p_jyglb j,tb_qw_qw_servicesperson s where d.depcode=dj.bmcode and dj.IDCARD=j.JYCODE and d.depcode=s.depcode and j.JYCODE=s.JYCODE and IS_DELETED='0' group by d.depcode,s.tasknode,s.serviceid) d" +
                "  on d.depcodeextra=s.depcode and d.serviceidextra=s.serviceid" +
                " left join tb_qw_qw_service se on se.serviceid=s.serviceid" +
                " left join tl_p_department de on de.depcode=s.depcode" +
                " where 1=1 and se.serviceid is not null";
        sql +=tj;
        sql +=" order by se.servicename desc, de.depname desc";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        if(list.size()>0){
            for (int i=0; i<list.size(); i++){
                list.get(i).put("servicetime",String.valueOf(list.get(i).get("servicetime")).equals("null")?"":String.valueOf(list.get(i).get("servicetime")).substring(0,19));
            }
        }
        return jacksonUtil.toJson(list);
    }

    @Override
    public Integer addDutySchedulingSquadron(String serviceid, String servicesquadronid, String depcode, String jycode, List<String> task, String tasknode) {
        List jycodeList= Arrays.asList(jycode.split(","));
        String serviceagent="";
        int count=0;
        String update="update tb_qw_qw_servicesquadron set iforder=0,ordertime=NOW() where servicesquadronid='"+servicesquadronid+"'";
        try {
            count=jdbcTemplate.update(update);
        }catch(DataAccessException e) {
            count=-2;
            return count;
        }
        if(count>0){
            for(int i=0;i<jycodeList.size();i++){
                String sql="insert into tb_qw_qw_servicesperson (serviceid, depcode, jycode, task, issuer, tasknode, deliverytime)" +
                        "values ('"+serviceid+"','"+depcode+"','"+jycodeList.get(i)+"','"+task.get(i)+"','"+serviceagent+"','"+tasknode+"',NOW())";
                try {
                    count +=jdbcTemplate.update(sql);
                }catch(DataAccessException e) {
                    count=-2;
                    return count;
                }
            }
        }
        return count;
    }

    @Override
    public Integer updateDutySchedulingSquadron(String serviceid, String depcode, String jycode, List<String> task, String tasknode) {
        List jycodeList= Arrays.asList(jycode.split(","));
        String serviceagent="";
        int count=0;
        String delete="delete from tb_qw_qw_servicesperson where serviceid= "+serviceid+" and depcode ='"+depcode+"'";
        jdbcTemplate.update(delete);
        for(int i=0;i<jycodeList.size();i++){
            String sql="insert into tb_qw_qw_servicesperson (serviceid, depcode, jycode, task, issuer, tasknode, deliverytime)" +
                    "values ('"+serviceid+"','"+depcode+"','"+jycodeList.get(i)+"','"+task.get(i)+"','"+serviceagent+"','"+tasknode+"',NOW())";
            try {
                count +=jdbcTemplate.update(sql);
            }catch(DataAccessException e) {
                count=-2;
                return count;
            }
        }
        return count;
    }

    @Override
    public Integer deleteDutySchedulingSquadron(String serviceid, String depcode) {
        List serviceidList= Arrays.asList(serviceid.split(","));
        List depcodeList= Arrays.asList(depcode.split(","));
        int count=0;
        for(int i=0;i<serviceidList.size();i++){
            String sql="delete from tb_qw_qw_servicesperson where serviceid ='"+serviceidList.get(i)+"' and depcode= '"+depcodeList.get(i)+"'";
            count +=jdbcTemplate.update(sql);
        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        return count;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }

    @Override
    public String findIntelligentScheduling(String date, String jyname) {
        int maxDate = 0;
        String sql = "";
        String tj="";
        if(jyname!=null&&!jyname.isEmpty()&&!jyname.equals("null")&&jyname.length()>0){
            tj +=" and j.jyname = '"+jyname+"'";
        }
        if(date!=null&&!date.isEmpty()&&!date.equals("null")&&date.length()>0){
            String[] year_month=date.split("-");
            Calendar cal = Calendar.getInstance();
            int year=Integer.parseInt(year_month[0]);
            int month=Integer.parseInt(year_month[1])-1;
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH,month);
            cal.set(Calendar.DATE, 1);
            cal.roll(Calendar.DATE, -1);
            maxDate = cal.get(Calendar.DATE);
            sql += "select t.*,se.task from (select u.*, group_concat(c.shiftname order by c.day) shiftname, group_concat(sh.starttime,'~',sh.endtime ORDER BY c. DAY) time from (select distinct s.*, j.jyname, d.depname, t.templatename" +
                    " from tb_qw_pb_scheduling s,tl_p_department d, tl_p_jyglb j,tb_qw_pb_template t" +
                    " where s.jycode=j.JYCODE and s.jzcode=d.DEPCODE and s.templateid=t.templateid";
            sql +=" and s.month='"+date+"'";
            sql +=tj;
            sql += " ) u ,tb_qw_pb_calendarshift c, tb_qw_pb_shift sh where u.schedulingid=c.schedulingid and sh.shiftname=c.shiftname";
            sql +=" and c.day >=str_to_date('"+date+"-01','%Y-%m-%d') and c.day <=str_to_date('"+date+"-"+maxDate+"','%Y-%m-%d')";
            sql +=" group by u.schedulingid,u.jyname,u.depname,u.jycode" +
                    ")t left join tb_qw_qw_servicesperson se on t.jycode=se.jycode order by convert(t.depname USING gbk) ,convert(t.jyname USING gbk)";
        }
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        if(list.size()>0){
            for (int i=0; i<list.size(); i++){
                list.get(i).put("creattime",String.valueOf(list.get(i).get("creattime")).equals("null")?"":String.valueOf(list.get(i).get("creattime")).substring(0,19));
                list.get(i).put("settime",String.valueOf(list.get(i).get("settime")).equals("null")?"":String.valueOf(list.get(i).get("settime")).substring(0,19));
            }
        }
        return jacksonUtil.toJson(list);
    }

    @Override
    public Integer addIntelligentScheduling(String date, String jzcode, String jycode, String templateid, String shiftname) {
        List jzcodeList= Arrays.asList(jzcode.split(","));
        List jycodeList= Arrays.asList(jycode.split(","));
        List templateidList= Arrays.asList(templateid.split(","));
        List<String> shiftnameList= JSON.parseArray(shiftname,String.class);
        String operator="";
        int count=0;
        if(date!=null&&!date.isEmpty()&&!date.equals("null")&&date.length()>0){
            String[] year_month=date.split("-");
            Calendar cal = Calendar.getInstance();
            int year=Integer.parseInt(year_month[0]);
            int month=Integer.parseInt(year_month[1])-1;
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH,month);
            cal.set(Calendar.DATE, 1);
            cal.roll(Calendar.DATE, -1);
            int maxDate = cal.get(Calendar.DATE);
            //删除原有的排班
//            String delete1="delete from tb_qw_pb_calendarshift where day >=str_to_date('"+date+"-01','%Y-%m-%d') and day <=str_to_date('"+date+"-"+maxDate+"','%Y-%m-%d')";
            String delete2="delete from tb_qw_pb_scheduling where month='"+date+"'";
            try {
//                jdbcTemplate.update(delete1);
                jdbcTemplate.update(delete2);
            }catch(DataAccessException e) {
                count=-2;
                return count;
            }
            for(int i=0;i<jycodeList.size();i++){
                int id = findMaxId("tb_qw_pb_scheduling","schedulingid");
                String sql="insert into tb_qw_pb_scheduling (jzcode, jycode, templateid, month, creattime, settime, operator, schedulingid)" +
                        "values ('"+jzcodeList.get(i)+"','"+jycodeList.get(i)+"','"+templateidList.get(i)+"','"+date+"', NOW(), NOW(),'"+operator+"','"+id+"')";
                try {
                    count +=jdbcTemplate.update(sql);
                }catch(DataAccessException e) {
                    count=-2;
                    return count;
                }
                List shiftnameListSon= Arrays.asList(shiftnameList.get(i).split(","));
                for(int j=0;j<maxDate;j++){
                    String insert="insert into tb_qw_pb_calendarshift (schedulingid, day, shiftname, jzcode)" +
                            "values ('"+id+"',str_to_date('"+date+"-"+(j+1)+"','%Y-%m-%d'),'"+shiftnameListSon.get(j)+"','"+jzcodeList.get(i)+"')";
                    try {
                        jdbcTemplate.update(insert);
                    }catch(DataAccessException e) {
                        count=-2;
                        return count;
                    }
                }
            }
        }
        return count;
    }

    @Override
    public Integer updateIntelligentScheduling(String schedulingid, String time, String templateid, String shiftname) {
        String select ="select * from tb_qw_pb_scheduling where schedulingid="+schedulingid+"";
        List<Map<String, Object>> selectlist =jdbcTemplate.queryForList(select);
        String date= String.valueOf(selectlist.get(0).get("month"));
        String jzcode= String.valueOf(selectlist.get(0).get("jzcode"));
        int count=0;
        if(date.length()>0){
            String[] year_month=date.split("-");
            Calendar cal = Calendar.getInstance();
            int year=Integer.parseInt(year_month[0]);
            int month=Integer.parseInt(year_month[1])-1;
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH,month);
            cal.set(Calendar.DATE, 1);
            cal.roll(Calendar.DATE, -1);
            int maxDate = cal.get(Calendar.DATE);
            String sql="update  tb_qw_pb_scheduling set templateid ='"+templateid+"' where schedulingid="+schedulingid;
            try {
                count +=jdbcTemplate.update(sql);
            }catch(DataAccessException e) {
                count=-2;
                return count;
            }
            List shiftnameList= Arrays.asList(shiftname.split(","));
            String delete="delete from tb_qw_pb_calendarshift where schedulingid="+schedulingid;
            try {
                jdbcTemplate.update(delete);
            }catch(DataAccessException e) {
                count=-2;
                return count;
            }
            for(int j=0;j<maxDate;j++){
                String insert="insert into tb_qw_pb_calendarshift (schedulingid, day, shiftname, jzcode)" +
                        "values ('"+schedulingid+"',str_to_date('"+date+"-"+(j+1)+"','%Y-%m-%d'),'"+shiftnameList.get(j)+"','"+jzcode+"')";
                try {
                    jdbcTemplate.update(insert);
                }catch(DataAccessException e) {
                    count=-2;
                    return count;
                }
            }
        }
        return count;
    }

    @Override
    public String findIntelligentSchedulingByday(String day) {
        int maxDate = 0;
        String sql = "";
        if(day!=null&&!day.isEmpty()&&!day.equals("null")&&day.length()>0){
            sql += "select t.*,se.task from (select u.*, c.shiftname, c.day, CONCAT(sh.starttime,'~',sh.endtime) time from (select distinct s.*, j.jyname, d.depname, t.templatename" +
                    " from tb_qw_pb_scheduling s,tl_p_department d, tl_p_jyglb j,tb_qw_pb_template t" +
                    " where s.jycode=j.JYCODE and s.jzcode=d.DEPCODE and s.templateid=t.templateid";
            sql += " and s.month='"+day.substring(0,7)+"'";
            sql += " ) u ,tb_qw_pb_calendarshift c, tb_qw_pb_shift sh where u.schedulingid=c.schedulingid and sh.shiftname=c.shiftname";
            sql += " and c.day =str_to_date('"+day+"','%Y-%m-%d')";
            sql += " )t left join tb_qw_qw_servicesperson se on t.jycode=se.jycode order by convert(t.depname USING gbk) ,convert(t.jyname USING gbk)";
        }
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        if(list.size()>0){
            for (int i=0; i<list.size(); i++){
                list.get(i).put("creattime",String.valueOf(list.get(i).get("creattime")).equals("null")?"":String.valueOf(list.get(i).get("creattime")).substring(0,19));
                list.get(i).put("settime",String.valueOf(list.get(i).get("settime")).equals("null")?"":String.valueOf(list.get(i).get("settime")).substring(0,19));
            }
        }
        return jacksonUtil.toJson(list);
    }

    /**
    * 查询表中id最大的
    */
    public int findMaxId(String table,String ids){
        int id = 1;
        String sql = "select "+ids+" from "+table +"  order by cast("+ids+" as unsigned int) desc";
        System.out.println(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        if(list.size()>0){
            id = Integer.parseInt(list.get(0).get(ids).toString())+1;
        }
        return id;
    }

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                }
