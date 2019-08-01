package mvc.controller;

import mvc.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: xianlehuang
 * @Description:下拉栏等
 * @date: 2019/06/17
 */

@Controller
@RequestMapping("/common")
public class CommonController {

	@Autowired
	private CommonService commonService;
	//排班
	@RequestMapping("/shift")
	@ResponseBody
	public String shift(){
		return commonService.shift();
	}
	//模板
	@RequestMapping("/template")
	@ResponseBody
	public String template(){
		return commonService.template();
	}
	//勤务
	@RequestMapping("/scheduling")
	@ResponseBody
	public String scheduling(){
		return commonService.scheduling();
	}
	//中队
	@RequestMapping("/squadron")
	@ResponseBody
	public String squadron(){
		return commonService.squadron();
	}
	//警员
	@RequestMapping("/people")
	@ResponseBody
	public String people(String depcode){
		return commonService.people(depcode);
	}

	@RequestMapping(value = "/pic")
	public void pic(HttpServletRequest request, HttpServletResponse response,
					@RequestParam("key") String key) {
		commonService.getFile(request, response, key);
	}
}
