viewblock
---------
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.liyiorg/viewblock/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.liyiorg/viewblock/)
[![GitHub release](https://img.shields.io/github/release/liyiorg/viewblock.svg)](https://github.com/liyiorg/viewblock/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Viewblock is a JSP block loading tool, which is used to handle complex business request and page block loading, effectively reduce the coupling degree between the page blocks, and reduce the Ajax request of the conventional page.

#### demo 
https://github.com/liyiorg/viewblock-example

#### features
---------
* annotation
* support request parameters, type conversion
* support Freemark, JSP template
* support IOC spring
* support asynchronous loading block (servlet3.0, web tomcat-7.0.50, jetty 9.2.19.v20160908, container: other web containers to be loaded asynchronously without HTML), occupying JS (embedded integrity, will not damage the page to the search engines.) , asynchronous and synchronous loading can be used simultaneously.

#### use scene
Viewblock is applicable to large pages, a single request multi page block logic processing, page code block reuse, asynchronous page loading project, can be used together with the MVC architecture, such as Spring MVC, Struts1 ,Struts2.

#### Full use example:
---------
##### 1. maven
```xml
<dependency>
  <groupId>com.github.liyiorg</groupId>
  <artifactId>viewblock</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>

##### 2. web.xml
```xml
<filter>   
	<filter-name>viewblock</filter-name>   
	<filter-class>com.github.liyiorg.viewblock.core.ViewblockFilter</filter-class>  
	<init-param>   
		<param-name>config_properties</param-name>   
		<param-value>  	  		
		pack_scan=example.*    
		spring=false    
		jsp_template=/WEB-INF/block    
		freemarker=false    
		freemarker_template=/WEB-INF/block    
		freemarker_delay=0    
		freemarker_encode=UTF-8    
		</param-value>    
	</init-param>   
</filter>

<!-- jetty config -->
<servlet id="jsp">
	<servlet-name>jsp</servlet-name>
	<servlet-class>org.eclipse.jetty.jsp.JettyJspServlet</servlet-class>
	<async-supported>true</async-supported>
	<init-param>
		<param-name>logVerbosityLevel</param-name>
		<param-value>DEBUG</param-value>
	</init-param>
	<init-param>
		<param-name>fork</param-name>
		<param-value>false</param-value>
	</init-param>
	<init-param>
		<param-name>xpoweredBy</param-name>
		<param-value>false</param-value>
	</init-param>
	<init-param>
		<param-name>compilerTargetVM</param-name>
		<param-value>1.7</param-value>
	</init-param>
	<init-param>
		<param-name>compilerSourceVM</param-name>
		<param-value>1.7</param-value>
	</init-param>
	<load-on-startup>0</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>jsp</servlet-name>
	<url-pattern>*.jsp</url-pattern>
	<url-pattern>*.jspf</url-pattern>
	<url-pattern>*.jspx</url-pattern>
	<url-pattern>*.xsp</url-pattern>
	<url-pattern>*.JSP</url-pattern>
	<url-pattern>*.JSPF</url-pattern>
	<url-pattern>*.JSPX</url-pattern>
	<url-pattern>*.XSP</url-pattern>
</servlet-mapping>  
```
##### 3. create viewblock  
```java
@ViewblockCollection
public class ExampleBlock {

	@Viewblock(name = "header", template = "header.jsp")
	public void header() {
		try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Viewblock(name = "content", template = "content.jsp")
	public void content(@BRequestParam(required=false) String name,BModelMap bModelMap){
		try {
			Thread.currentThread().sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bModelMap.addAttribute("name", name);
	}

	@Viewblock(name = "footer", template = "footer.ftl")
	public String footer() {
		try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "footer.ftl";
	}
}
```

##### 3. JSP

###### sync load

```jsp
<%@taglib uri="/viewblock" prefix="viewblock"%>  

<viewblock:block name="header"/>  
<div>other html...</div>   
<viewblock:block name="content"/>
<viewblock:block name="footer"/>  
```

###### async load

```jsp
<%@taglib uri="/viewblock" prefix="viewblock"%>
<!-- Load asynchronous execution in order to load the page faster, and reduce the wait for the asynchronous output tag, the asynchronous execution block should be placed at the top of the JSP page.-->  
<viewblock:block name="content" async="true"/>

<viewblock:block name="header"/>
<div>other html...</div>
<!-- Output asynchronous execution content-->
<viewblock:output name="content"/>
  
<viewblock:block name="footer"/>  
<!-- Release asynchronous link -->
<viewblock:asyncFinish/>
```
