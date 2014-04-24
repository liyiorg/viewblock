viewblock
---------

viewblock 是一个JSP块加载工具,用于处理复杂的业务请求与页面块加载.

示例  
https://github.com/liyiorg/viewblock-example

特点
---------
注解编写  
支持请求参数,类型转换  
支持JSP,Freemark 块模板  
支持异步加载块(servlet3.0,目前只支持apache-tomcat-7.0.50 以上版本)  
支持spring IOC

使用场景
---------
viewblock 适用于大页面,单一请求多逻辑处理,页面代码块重复使用,异步页面加载的项目,可以结合MVC架构一起使用,如Spring MVC,struts

使用方式:
---------
1. 创建 viewblock  

@ViewblockCollection   
public class ExampleBlock {

	@Viewblock(name = "header", template = "header.jsp")
	public void header() {
		
	}
	
	@Viewblock(name = "footer", template = "footer.ftl")
	public void footer() {
		
	}

	@Viewblock(name = "content", template = "content.jsp")
	public void content(@BRequestParam(required=false) String name,BModelMap bModelMap){
		bModelMap.addAttribute("name", name);
	}
}

2. web.xml 配置  
&lt;filter&gt;   
	&lt;filter-name&gt;viewblock&lt;/filter-name&gt;   
	&lt;filter-class&gt;viewblock.core.ViewblockFilter&lt;/filter-class&gt;   
	&lt;async-supported&gt;true&lt;/async-supported&gt;   
	
	&lt;init-param&gt;   
		&lt;param-name&gt;config_properties&lt;/param-name&gt;   
		&lt;param-value&gt;  	  		
		pack_scan=example.*    
		spring=false    
		jsp_template=/WEB-INF/block    
		freemarker=true    
		freemarker_template=/WEB-INF/block    
		freemarker_delay=0    
		freemarker_encode=UTF-8    
		&lt;/param-value&gt;    
	&lt;/init-param&gt;    
&lt;/filter&gt;  

3. JSP 标签申明  
&lt;%@taglib uri="/viewblock" prefix="viewblock"%&gt;  

4. 引入块  
&lt;viewblock:block name="header"/&gt;  
&lt;viewblock:block name="content"/&gt;   
&lt;viewblock:block name="footer"/&gt;  

