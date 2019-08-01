package mvc.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: xianlehuang
 * @Description:下拉栏等
 * @date: 2019/06/17
 */

public interface CommonService  {

    String shift();

    String template();

    String scheduling();

    String squadron();

    String people(String depcode);

    void getFile(HttpServletRequest request, HttpServletResponse response, String key);
}
