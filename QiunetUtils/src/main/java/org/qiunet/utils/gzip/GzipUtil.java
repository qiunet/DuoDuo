package org.qiunet.utils.gzip;

import java.io.*;
import java.util.zip.*;

/**
 * 压缩 解压缩的工具类
 * Created by qiunet on 4/6/17.
 */
public class GzipUtil {
	/****
	 * 对字节流进行压缩
	 * @param bytes 要压缩的字节码数组
	 * @return
	 */
	public static byte [] zip(byte [] bytes) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
		GZIPOutputStream zos = null;
		try {
			zos = new GZIPOutputStream(bos);
			zos.write(bytes);

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (zos != null) zos.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bos.toByteArray();
	}

	/***
	 * 解压缩
	 * @param bytes
	 * @return
	 */
	public static byte [] unzip(byte [] bytes) {
		byte [] rbytes = new byte[1024];
		int num = 0;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		GZIPInputStream zis = null;
		try {
			zis = new GZIPInputStream(bis);
			while ((num = zis.read(rbytes, 0 , rbytes.length)) != -1){
				bos.write(rbytes, 0, num);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				bos.close();
				bis.close();
				if (zis != null) zis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bos.toByteArray();
	}

	public static byte[] validAndDecryptBytes(byte[] bytes) {
//        logger.info("------------decode----------before:" + bytes.length + "===" + Arrays.toString(bytes));
		InputStream inputStream = null;
		DataInputStream dataInputStream = null;
		try {
			inputStream = new ByteArrayInputStream(bytes);
			dataInputStream = new DataInputStream(inputStream);
			//先读出来一个加密前的数据长度
//            int length = dataInputStream.readInt();
			bytes = decompress(inputStream);
//            logger.info("------------decode----------end:" + bytes.length + "===" + Arrays.toString(bytes) + "\n" + new String(bytes));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dataInputStream.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}

	/**
	 * 压缩
	 *
	 * @param data 待压缩数据
	 * @return byte[] 压缩后的数据
	 */
	public static byte[] compress(byte[] data) {
		byte[] output = new byte[0];

		Deflater compresser = new Deflater();

		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		compresser.end();
		return output;
	}

	/**
	 * 解压缩
	 *
	 * @param data 压缩的数据
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] decompress(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}

	/**
	 * 解压缩
	 *
	 * @param is 输入流
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] decompress(InputStream is) {
		InflaterInputStream iis = new InflaterInputStream(is);
		ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
		try {
			int i = 1024;
			byte[] buf = new byte[i];

			while ((i = iis.read(buf, 0, i)) > 0) {
				o.write(buf, 0, i);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return o.toByteArray();
	}
}
