package mvc.service.impl;

import mvc.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import util.JacksonUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xianlehuang
 * @Description:下拉栏等
 * @date: 2019/06/17
 */

@Service("commonService")
public class CommonServiceImpl implements CommonService {
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

	@Override
	public String shift() {
		String sql = "select distinct shiftid, shiftname from tb_qw_pb_shift order by convert(shiftname USING gbk)";
		return jacksonUtil.toJson(jdbcTemplate.queryForList(sql));
	}

	@Override
	public String template() {
//		String sql = "select distinct templateid, templatename from tb_qw_pb_template order by convert(templatename USING gbk)";
		String sql="select t.templateid,t.templatename,group_concat(s.shiftname ORDER BY find_in_set(s.shiftid, t.shiftcycle)) shiftcyclename,t.shiftcycle" +
				" from tb_qw_pb_template t, tb_qw_pb_shift s where find_in_set(s.shiftid,t.shiftcycle) group by templateId ORDER BY CONVERT(t.templatename USING gbk) desc ";
		return jacksonUtil.toJson(jdbcTemplate.queryForList(sql));
	}

	@Override
	public String scheduling() {
		String sql = "select distinct serviceid, servicename from tb_qw_qw_service order by convert(servicename USING gbk)";
		return jacksonUtil.toJson(jdbcTemplate.queryForList(sql));
	}

	@Override
	public String squadron() {
		String sql = "select distinct id, pid, depcode, depname from tl_p_department order by convert(depname USING gbk)";
		return jacksonUtil.toJson(jdbcTemplate.queryForList(sql));
	}

	@Override
	public void getFile(HttpServletRequest request, HttpServletResponse response, String key) {
		try {
//			InputStream in = commonDao.getImage(key);
//			if(in == null){
				String path = "E:/hk/hk_pic/"+key;
				File pf = new File(path);
				FileInputStream fin = new FileInputStream(pf);
				ServletOutputStream fout =  response.getOutputStream();
				byte bts [] = new byte[fin.available()];
				fin.read(bts);
				fout.write(bts);
				fin.close();
				fout.close();

//				URL url = new URL("http://192.168.0.102:9000/"+key);
//				InputStream ism=url.openStream();
//				ServletOutputStream fout =  response.getOutputStream();
//				byte bts [] = new byte[ism.available()];
//				ism.read(bts);
//				fout.write(bts);
//				ism.close();
//				fout.close();
//			}else {
//				ServletOutputStream fout =  response.getOutputStream();
//				byte[] content = new byte[1024];
//				int length = 0;
//				if (in != null) {
//					while ((length = in.read(content)) != -1) {
//						fout.write(content, 0, length);
//					}
//				}
//				in.close();
//				fout.close();
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String people(String depcode) {
		String tj="";
		if(depcode!=null&&!depcode.isEmpty()&&!depcode.equals("null")&&depcode.length()>0){
			tj +=" and d.depcode = '"+depcode+"'";
		}
		String sql = "select distinct d.depcode, d.depname, j.jycode, j.jyname from tl_p_department d" +
				" left join (select j.jycode, j.jyname, dj.bmcode from tl_p_jybmglb dj, tl_p_jyglb j where dj.IDCARD=j.JYCODE and j.IS_DELETED='0') j on  d.depcode=j.bmcode where 1=1";
		sql +=tj;
		sql +=" order by convert(d.depname USING gbk) ,convert(j.jyname USING gbk)";
//		String sql = "select distinct d.depcode, d.depname, j.jycode, j.jyname from tl_p_department d, tl_p_jybmglb dj, tl_p_jyglb j" +
//				" where d.depcode=dj.bmcode and dj.IDCARD=j.JYCODE and IS_DELETED='0' order by convert(depname USING gbk)";
		return jacksonUtil.toJson(jdbcTemplate.queryForList(sql));
	}
}
