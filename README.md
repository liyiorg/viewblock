viewblock
---------

viewblock 是一个JSP块加载工具,用于处理复杂的业务请求与页面块加载.有效降低页面块之间的偶合度，并减少常规页面中子业务块的ajax请求。



示例  
https://github.com/liyiorg/viewblock-example

#### 特点
---------
注解编写  
支持请求参数,类型转换  
支持JSP,Freemark 模板  
支持spring IOC
支持异步加载块(servlet3.0,web 容器：tomcat-7.0.50 ，jetty 9.1.2 ,glassfish4，其它web容器有待测试 )，异步加载无html占位JS嵌入（不会破坏页面的完整性，有利于搜索引擎抓取。），异步加载与同步加载可同时使用。  

#### 使用场景
---------
viewblock 适用于大页面,单一请求多页面块逻辑处理,页面代码块重复使用,异步页面加载的项目,可以结合MVC架构一起使用,如Spring MVC,struts

#### 使用方式:
---------
##### 1. 创建 viewblock  
```java
@ViewblockCollection   
public class ExampleBlock {

	@Viewblock(name = "header", template = "header.jsp")
	public void header() {
		
	}
	
	@Viewblock(name = "footer")
	public void footer() {
		return "footer.ftl";
	}

	@Viewblock(name = "content", template = "content.jsp")
	public void content(@BRequestParam(required=false) String name,BModelMap bModelMap){
		bModelMap.addAttribute("name", name);
	}
}
```
##### 2. web.xml 配置
```xml
<filter>   
	<filter-name>viewblock</filter-name>   
	<filter-class>com.github.liyiorg.viewblock.core.ViewblockFilter</filter-class>  
	<async-supported>true</async-supported>   
	
	<init-param>   
		<param-name>config_properties</param-name>   
		<param-value>  	  		
		pack_scan=example.*    
		spring=false    
		jsp_template=/WEB-INF/block    
		freemarker=true    
		freemarker_template=/WEB-INF/block    
		freemarker_delay=0    
		freemarker_encode=UTF-8    
		</param-value>    
	</init-param>   
</filter>  
```
##### 3. JSP 加载
###### 同步加载

```jsp
<%@taglib uri="/viewblock" prefix="viewblock"%>  

<viewblock:block name="header"/>  
<div>other html...</div>   
<viewblock:block name="content"/>
<viewblock:block name="footer"/>  
```

###### 异步加载

```jsp
<%@taglib uri="/viewblock" prefix="viewblock"%>
<!-- 加载异步执行     为了更快的加载页面，并减少异步输出标签的等待，异步执行块应放在JSP页面的顶部。-->  
<viewblock:block name="content" async="true"/>

<viewblock:block name="header"/>
<div>other html...</div>
<!-- 输出异步执行内容-->
<viewblock:output name="content"/>
  
<viewblock:block name="footer"/>  
<!-- 释放异步链接 -->
<viewblock:asyncFinish/>
```
