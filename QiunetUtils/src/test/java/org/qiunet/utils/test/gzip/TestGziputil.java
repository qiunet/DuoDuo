package org.qiunet.utils.test.gzip;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qiunet.utils.gzip.GzipUtil;

import java.io.*;

/**
 * Created by qiunet on 4/6/17.
 */
public class TestGziputil {
	@Test
	public void testGzip() throws IOException {
		String first = "dffsdfasdkffkdfsfkdjnbeorifkdfadfkjfkadfad;gkfakdjfs;dfjasdfhewrijfkasdffgfgvkcngwerufkagjafgahg;alkfgafgadhfkafjewriudfkdns";
		int second = 10;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000);
		DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);
		dos.writeUTF(first);
		dos.writeInt(second);
		dos.close();
		byte [] zipBytes = GzipUtil.zip(byteArrayOutputStream.toByteArray());
		System.out.println(byteArrayOutputStream.toByteArray().length +"压缩后:" + zipBytes.length);
		byte[] unzipBytes = GzipUtil.unzip(zipBytes);

		ByteArrayInputStream bis = new ByteArrayInputStream(unzipBytes);
		DataInputStream dis = new DataInputStream(bis);
		Assertions.assertEquals(first, dis.readUTF());
		Assertions.assertTrue(second == dis.readInt());
	}
}
