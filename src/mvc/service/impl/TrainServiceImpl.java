package mvc.service.impl;

import mvc.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import util.JacksonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: xianlehuang
 * @Description:培训计划发布，培训预约
 * @date: 2019/06/03
 */

@Service("captureDataService")
public class TrainServiceImpl implements TrainService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private Map<String, Object> map;

    private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

    @Override
    public String findRelease(String plateName, String stime, String etime) {
        String tj="";
        if(plateName!=null&&!plateName.isEmpty()&&!plateName.equals("null")&&plateName.length()>0){
            tj +=" and a.plateName like '%"+plateName+"%'";
        }
        if(stime!=null&&!stime.isEmpty()&&!stime.equals("null")&&stime.length()>0){
            tj +=" and a.publishDate >=str_to_date('"+stime+" 00:00:00','%Y-%m-%d %H:%i:%s')";
        }
        if(etime!=null&&!etime.isEmpty()&&!etime.equals("null")&&etime.length()>0){
            tj +=" and a.publishDate <=str_to_date('"+etime+" 23:59:59','%Y-%m-%d %H:%i:%s')";
        }
        String sql="select a.*,b.appointmentSum from px_jhfb a " +
                " left join (select count(stuName) appointmentSum,plateId from px_yyxx where appointmentStatus=0 group by plateId) b on b.plateId=a.id where 1=1";
        sql +=tj;
        sql +=" order by a.publishDate desc";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        if(list.size()>0){
            for (int i=0; i<list.size(); i++){
                list.get(i).put("publishDate",String.valueOf(list.get(i).get("publishDate")).equals("null")?"":String.valueOf(list.get(i).get("publishDate")).substring(0,19));
                list.get(i).put("plateDate",String.valueOf(list.get(i).get("plateDate")).equals("null")?"":String.valueOf(list.get(i).get("plateDate")).substring(0,19));
                list.get(i).put("appointmentSum",String.valueOf(list.get(i).get("appointmentSum")).equals("null")?0:list.get(i).get("appointmentSum"));
            }
        }
        return jacksonUtil.toJson(list);
    }

    @Override
    public Integer addRelease(String plateName, String plateType, String plateNO, String plateDate, String plateSum, String publishUser, String status) {
        int count=0;
        long plateNumber= findMaxId("px_jhfb","plateNO");
        String sql="insert into px_jhfb (plateName, plateType, plateNO, plateDate, plateSum, publishUser, status)" +
                "values ('"+plateName+"','"+plateType+"','"+plateNumber+"',str_to_date('"+plateDate+"','%Y-%m-%d %H:%i:%s'),'"+plateSum+"','"+publishUser+"','"+status+"')";
        count=jdbcTemplate.update(sql);
        return count;
    }

    @Override
    public Integer updateRelease(String id, String plateName, String plateType, String plateNO, String plateDate, String plateSum, String publishUser, String status) {
        int count=0;
        String sql="update px_jhfb set plateName='"+plateName+"',plateType='"+plateType+"'," +
                " plateDate=str_to_date('"+plateDate+"','%Y-%m-%d %H:%i:%s'),plateSum='"+plateSum+"',publishUser='"+publishUser+"'," +
                " status='"+status+"'" +
                " where id='"+id+"'";
        count=jdbcTemplate.update(sql);
        return count;
    }

    @Override
    public Integer auditRelease(String id, String status) {
        int count=0;
        String sql="update px_jhfb set publishStatus=0,publishDate=NOW()" +
                " where id='"+id+"'";
        count=jdbcTemplate.update(sql);
        return count;
    }

    @Override
    public Integer deleteRelease(String id) {
        int count=0;
        String sql="delete from px_jhfb where id in ("+id+")";
        count=jdbcTemplate.update(sql);
        return count;
    }

    @Override
    public String findOrder(String stuName, String stime, String etime) {
        String tj="";
        if(stuName!=null&&!stuName.isEmpty()&&!stuName.equals("null")&&stuName.length()>0){
            tj +=" and a.stuName like '%"+stuName+"%'";
        }
        if(stime!=null&&!stime.isEmpty()&&!stime.equals("null")&&stime.length()>0){
            tj +=" and a.appointmentDate >=str_to_date('"+stime+" 00:00:00','%Y-%m-%d %H:%i:%s')";
        }
        if(etime!=null&&!etime.isEmpty()&&!etime.equals("null")&&etime.length()>0){
            tj +=" and a.appointmentDate <=str_to_date('"+etime+" 23:59:59','%Y-%m-%d %H:%i:%s')";
        }
        String sql="select a.*,b.plateName from px_yyxx a" +
                " left join px_jhfb b on a.plateId=b.id where 1=1";
        sql +=tj;
        sql +=" order by a.appointmentDate desc";
        List<Map<String, Object>>list=jdbcTemplate.queryForList(sql);
        if(list.size()>0){
            for (int i=0; i<list.size(); i++){
                list.get(i).put("appointmentDate",String.valueOf(list.get(i).get("appointmentDate")).equals("null")?"":String.valueOf(list.get(i).get("appointmentDate")).substring(0,19));
            }
        }
        return jacksonUtil.toJson(list);
    }

    public long findMaxId(String table,String id){
        long id1 = 1;
        String sql = "select "+id+" from "+table+" where "+id+" is not null" +
                " and "+id+"> CONCAT(DATE_FORMAT(NOW(),'%Y%m%d'),'0000')   and "+id+"< CONCAT(DATE_FORMAT(NOW(),'%Y%m%d'),'9999') order by cast("+id+" as unsigned int) desc";
        System.out.println("findMaxId sql="+sql);
        List<Map<String, Object>> list = null;
        list = jdbcTemplate.queryForList(sql);
        if(list.size()>0){
            id1 = Long.valueOf(String.valueOf(list.get(0).get(id)))+1;
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            id1 = Long.valueOf(sdf.format(new Date())+"0001");
        }
        return id1;
    }
}
