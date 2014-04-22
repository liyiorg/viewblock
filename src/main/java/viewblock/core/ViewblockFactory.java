package viewblock.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ClassFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import viewblock.annotation.Viewblock;
import viewblock.annotation.ViewblockCollection;
import viewblock.exception.ViewBlockException;
import viewblock.exception.ViewBlockSpringBeanNotFindException;

public class ViewblockFactory {
	
	private static Logger logger = LoggerFactory.getLogger(ViewblockFactory.class);
	
	//block object map
	private static Map<String, ViewblockObject> map = new HashMap<String, ViewblockObject>();
	
	private static boolean useSpring = false;
	
	
	
	protected static void setUseSpring(boolean use){
		useSpring = use;
	}
	
		
	
	/**
	 * 获取block
	 * @param name
	 * @return
	 */
	protected static ViewblockObject getBlock(String name){
		return map.get(name);
	}
	
	/**
	 * 过滤类
	 * @param pack
	 * @return
	 */
	private static List<Class<?>> scanClasses(String pack){
		ClassFilter  classFilter = new ClassFilter();
		classFilter.packageName(pack);
		classFilter.annotation(ViewblockCollection.class);
		return CPScanner.scanClasses(classFilter);
	}
	
	
	/**
	 * 过滤块
	 * @param list
	 * @throws ViewBlockSpringBeanNotFindException 
	 */
	protected static void scanBlock(String pack) throws ViewBlockSpringBeanNotFindException{
		List<Class<?>> list = scanClasses(pack);
		
		for(Class<?> c : list){
			ViewblockCollection collection = c.getAnnotation(ViewblockCollection.class);
			String pname = collection.name();
			
			//判断是否为spring 的 bean
			boolean inspring = false;
			Object object = null;
			if(useSpring){
				if(!"".equals(collection.spring())){
					object = SpringProxy.getBean(collection.spring());
				}else{
					object = SpringProxy.getBean(c);
				}
				if(object!=null){
					inspring = true;
					break;
				}else{
					logger.error("viewblock con't find spring bean {}",c.getName());
					throw new ViewBlockSpringBeanNotFindException("");
				}
			}
			if(!inspring){
				try {
					object = c.newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(Method method:c.getDeclaredMethods()){
				Viewblock block = method.getAnnotation(Viewblock.class);
				if(block!=null){
					String bname = block.name();
					if(pname!=null&&!"".equals(pname)){
						bname = pname+":"+bname;
					}
					ViewblockObject blockObject = new ViewblockObject();
					blockObject.setName(bname);
					blockObject.setMethod(method);
					blockObject.setObject(object);
					blockObject.setTemplate(block.template());
					blockObject.setClassName(c.getName());
					if(map.containsKey(bname)){
						try {
							throw new ViewBlockException("View block ["+bname+"] existed,can't set same name");
						} catch (ViewBlockException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						map.put(bname,blockObject);
					}
					logger.info("VIEW BLOCK class:{} name:[{}] {}",c.getName(),bname,"".equals(block.template())?"":"template:["+block.template()+"]");
				}
			}
		}
	}
	
}
