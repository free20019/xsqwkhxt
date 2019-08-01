package filter;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Router implements Filter {
	Logger logger=Logger.getLogger(this.getClass());
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
    	request.setCharacterEncoding("UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	String user =(String) request.getSession().getAttribute("username");
    	String contextPath = request.getRequestURL().toString();
    	String origin = request.getHeader("Origin");
    	response.setHeader("Access-Control-Allow-Origin", origin==null?"*":origin);
    	response.setHeader("Access-Control-Allow-Credentials", "true");
    	String method = request.getMethod();
//    	if(null == user && !method.toUpperCase().equals("OPTIONS")){
//    		if(		contextPath.indexOf("/app/") >= 0
//					|| contextPath.indexOf("/resources/") >= 0
//					|| contextPath.indexOf("/login") >= 0
//					|| contextPath.indexOf("/index.jsp") >= 0
//				){
//
//			}else{
//				response.sendRedirect("/xsqwkhxt/index.jsp");
//				return;
//			}
//		}
		try {
			arg2.doFilter(arg0, arg1);
		} catch (Exception e) {
			String exceptionString = ExceptionUtils.getFullStackTrace(e);
			logger.error(exceptionString);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
