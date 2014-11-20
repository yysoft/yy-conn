/**
 * 
 */
package net.caiban.db.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author parox
 *
 */
public class ConnHelper {

	@SuppressWarnings("rawtypes")
	public static Map<String, String> readPropertyFile(String file,String charsetName) throws IOException {
		if (charsetName==null || charsetName.trim().length()==0){
			charsetName="gbk";
		}
		Map<String, String> map = Maps.newHashMap();
		InputStream is =null;
		if(file.startsWith("file:"))
			is=new FileInputStream(new File(file.substring(5)));
		else
			is=ConnHelper.class.getClassLoader().getResourceAsStream(file);
		Properties properties = new Properties();
		properties.load(is);
		Enumeration en = properties.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String code = new String(properties.getProperty(key).getBytes(
					"ISO-8859-1"), charsetName);
			map.put(key, code);
		}
		return map;
	}
	
	public static List<String> splitToString(String args){
		if(args==null){
			return Lists.newArrayList();
		}
		Splitter spliter = Splitter.on(",").omitEmptyStrings().trimResults();
		List<String> labels = Lists.newArrayList(spliter.split(args));
		return labels;
	}
}
