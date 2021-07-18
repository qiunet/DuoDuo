package org.qiunet.utils.secret;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/***
 * Sh1 工具类
 *
 * qiunet
 * 2021/7/18 07:56
 **/
public final class SH1Util {

	private static final ThreadLocal<MessageDigest> SHA1 = ThreadLocal.withInitial(() -> {
		try {
			return MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new InternalError("SHA-1 not supported on this platform - Outdated?");
		}
	});


	/**
	 * Performs a SHA-1 hash on the specified data
	 *
	 * @param data The data to hash
	 * @return The hashed data
	 */
	private static byte[] sha1(byte[] data) {
		MessageDigest digest = SHA1.get();
		digest.reset();
		return digest.digest(data);
	}

	public static String sha1(File file) throws IOException {
		return SecretUtil.byteArrayToHexString(Files.readAllBytes(file.toPath()));
	}

	public static String sha1(String source) {
		return sha1(source, StandardCharsets.UTF_8);
	}

	public static String sha1(String source, Charset charset) {
		byte[] bytes = source.getBytes(charset);
		return SecretUtil.byteArrayToHexString(sha1(bytes));
	}
}
