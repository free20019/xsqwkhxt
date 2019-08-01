package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class DownloadAct {
	/**
	 * 
	 * json字符串转换成List<Map>集合   
	 * jsonString字符串的JSON格式为[{a:1,b:2}]
	 * @param jsonString 
	 * @return
	 */
    public static List<Map<String, Object>> strlist(String jsonString) {
    	List<Map<String, Object>> list = null;
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
	 * 
	 * json字符串转换成List<Map>集合   
	 * jsonString字符串的JSON格式为{datas:[{a:1,b:2}]}
	 * @param jsonString 
	 * @return
	 */
	public static List<Map<String, Object>> strlist2(String jsonString) {
		String a=jsonString.substring(9,jsonString.lastIndexOf("}"));
    	List<Map<String, Object>> list = null;
        try {
            list = JSON.parseObject(a, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
   	 * 
   	 * json字符串转换成List<Map>集合   
   	 * jsonString字符串的JSON格式为{datas:[{a:1,b:2}],count:1}
   	 * @param jsonString 
   	 * @return
   	 */
   	public static List<Map<String, Object>> str2list(String jsonString) {
   		String a=jsonString.substring(9, jsonString.lastIndexOf(","));
       	List<Map<String, Object>> list = null;
           try {
               list = JSON.parseObject(a, new TypeReference<List<Map<String, Object>>>() {});
           } catch (Exception e) {
               e.printStackTrace();
           }
           return list;
   	}
    
	/**
	 * 
	 * json字符串转换成List<Map>集合   
	 * jsonString字符串的JSON格式为{"data":[{a:1,b:2}]}
	 * @param jsonString 
	 * @return
	 */
	public static List<Map<String, Object>> str2list1(String jsonString) {
		String a=jsonString.substring(8,jsonString.lastIndexOf("}"));
    	List<Map<String, Object>> list = null;
        try {
            list = JSON.parseObject(a, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
	/**
	 * 
	 * json字符串转换成List<Map>集合   
	 * jsonString字符串的JSON格式为{"datas":{datas:[{a:1,b:2}]},count:1}
	 * @param jsonString 
	 * @return
	 */
	public static List<Map<String, Object>> str2list2(String jsonString) {
		String a=jsonString.substring(18, jsonString.lastIndexOf(",")-1);
    	List<Map<String, Object>> list = null;
        try {
            list = JSON.parseObject(a, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
	
	
	/**
	 * 
	 * 将JSON格式为{"DATA":[{a:1,b:2}]}转换为
	 * list<map>方便导出
	 * @param jsonString 
	 * @return
	 */
	public static List<Map<String, Object>> str2list3(String jsonString) {
		String a=jsonString.substring(8,jsonString.lastIndexOf("}"));
    	List<Map<String, Object>> list = null;
        try {
            list = JSON.parseObject(a, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
	
    /**
     * 返回http响应
     * @param request
     * @param response
     * @param a
     * @param b
     * @param gzb
     * @param list
     * @return
     * @throws IOException
     */
    public String download(HttpServletRequest request,
    		HttpServletResponse response, 
    		String[] a,String[] b,String gzb,List<Map<String,Object>> list) throws IOException{
        String fileName=gzb;//excle文件名
        //填充projects数据
        String columnNames[]=a;//列名
        String keys[]    =     b;//map中的key
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ExcelUtil.createWorkBook(list,keys,columnNames,gzb).write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[1024];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        return null;
    }
}
