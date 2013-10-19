package cn.hartech.jrunner.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.util.Log;

/**
 * 键值对的配置文件，使用方式：

		// 如果指定配置文件不存在，则创建一个新文件
		MyProperties property = new MyProperties();

		property.getProperty("abc", "cc"); // "cc"为找不到时的默认值

		property.put("abc", "abcd001中国");

		// 如果文件不存在，在创建本类对象时已完成创建
		property.saveMyPropertyFile();
		
 * 
 * 任何异常将会在本类被吞掉，并打印日志，但不会抛出异常，中断上层代码的运行
 * 
 * @author jin.zheng
 * @date 2013-5-5
 *
 */
public class MyProperties extends Properties {

	private static final long serialVersionUID = 4314921863390475269L;

	private String propertyFilePath;

	/**
	 * 新建本对象时，读取指定的配置的文件，如果没有，则创建一新文件
	 */
	public MyProperties() {

		super();

		propertyFilePath = MyUtility.getAppFolder()
				+ Constant.PROPERTY_FILE_NAME;

		FileInputStream in = null;

		try {

			in = new FileInputStream(propertyFilePath);

		} catch (FileNotFoundException e) {

			// 如果文件不存在，则创建一个

			Log.w("JRunner-MyProperties",
					"Not property file found. will create one!");

			File file = new File(propertyFilePath);

			try {

				file.createNewFile();

				in = new FileInputStream(file);

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		try {

			load(in);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 保存所有配置信息
	 */
	public void saveMyPropertyFile() {

		FileOutputStream out;

		try {

			out = new FileOutputStream(propertyFilePath, false);

			store(out, "MyProperties");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
