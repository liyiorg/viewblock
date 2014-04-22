package viewblock.resolve;

import java.util.Map;

/**
 * 
* <p>Title: ViewResolve.java</p>
* <p>Description: 模板解析接口 </p>
* @author liuemc
* @date 2014年1月10日
* @since 1.0
 */
public interface ViewResolve {
	
	/**
	 * 解析模板
	 * @param model
	 * @param template
	 * @return
	 */
	String resolve(Map<String, Object> model,String template);
}
