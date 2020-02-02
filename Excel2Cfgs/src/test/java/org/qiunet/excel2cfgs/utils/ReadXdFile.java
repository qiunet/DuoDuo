package org.qiunet.excel2cfgs.utils;

import org.junit.Test;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by qiunet.
 * 17/5/25
 */
public class ReadXdFile {
	@Test
	public void readXdFile (){
		String fileName = "/Users/qiunet/DeskTop/item_data.xd";
		InputStream in=null;
		GZIPInputStream gin=null;
		DataInputStream dis=null;
		try {
			File file = new File(fileName);
			in=new FileInputStream(file);
			gin=new GZIPInputStream(in);
			dis=new DataInputStream(gin);
			int num = dis.readInt();
			System.out.println(num);
			for (int i = 0 ; i < num; i++) {
				System.out.println(dis.readInt());
				System.out.println(dis.readUTF());
				System.out.println(dis.readDouble());
				System.out.println(dis.readInt());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取配置文件:"+fileName+"失败.");
			if(in!=null){
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}
	}
}
