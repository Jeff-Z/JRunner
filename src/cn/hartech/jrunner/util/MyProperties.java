package cn.hartech.jrunner.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.util.Log;

/**
 * ��ֵ�Ե������ļ���ʹ�÷�ʽ��

		// ���ָ�������ļ������ڣ��򴴽�һ�����ļ�
		MyProperties property = new MyProperties();

		property.getProperty("abc", "cc"); // "cc"Ϊ�Ҳ���ʱ��Ĭ��ֵ

		property.put("abc", "abcd001�й�");

		// ����ļ������ڣ��ڴ����������ʱ����ɴ���
		property.saveMyPropertyFile();
		
 * 
 * �κ��쳣�����ڱ��౻�̵�������ӡ��־���������׳��쳣���ж��ϲ���������
 * 
 * @author jin.zheng
 * @date 2013-5-5
 *
 */
public class MyProperties extends Properties {

	private static final long serialVersionUID = 4314921863390475269L;

	private String propertyFilePath;

	/**
	 * �½�������ʱ����ȡָ�������õ��ļ������û�У��򴴽�һ���ļ�
	 */
	public MyProperties() {

		super();

		propertyFilePath = MyUtility.getAppFolder()
				+ Constant.PROPERTY_FILE_NAME;

		FileInputStream in = null;

		try {

			in = new FileInputStream(propertyFilePath);

		} catch (FileNotFoundException e) {

			// ����ļ������ڣ��򴴽�һ��

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
	 * ��������������Ϣ
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
