package org.qiunet.utils.secret;

import com.google.common.base.Preconditions;

import java.io.File;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.qiunet.utils.secret.SecretUtil.byteArrayToHexString;
import static org.qiunet.utils.secret.SecretUtil.hexDigits;

public class MD5Util {

	private static final ThreadLocal<MessageDigest> MD5 = ThreadLocal.withInitial(() -> {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new InternalError("MD5 not supported on this platform - Outdated?");
		}
	});


	public static String encrypt(String source){
		return encrypt(source,"utf8");
	}


	private static MessageDigest getMd5Digest(){
		MessageDigest digest = MD5.get();
		digest.reset();
		return digest;
	}

	public static String encrypt(String source,String charset){
		String rt="";
		try {
			MessageDigest digest = getMd5Digest();
			digest.update(source.getBytes(charset));
			byte[] md = digest.digest();
			char[] str = new char[md.length * 2];
			for(int i=0,k=0;i<md.length;i++){
				byte b = md[i];
				str[k++] = hexDigits[b>>>4&0xf];
				str[k++] = hexDigits[b&0xf];
			}
			rt = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rt;
	}

	private MD5Util(){}
	/**
	 * encryptAndDecrypt 加密
	 * @param bytes
	 * @return
	 */
	public static String encrypt(byte [] bytes){
		String resultString=null;
		try {
			MessageDigest digest = getMd5Digest();
			resultString = byteArrayToHexString(digest.digest(bytes));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

	/***
	 * 对文件进行md5
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(File file) throws Exception {
		Preconditions.checkNotNull(file);
		return encrypt(Files.readAllBytes(file.toPath()));
	}

}
