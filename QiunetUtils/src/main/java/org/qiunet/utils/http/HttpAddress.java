package org.qiunet.utils.http;

/***
 * Http 的地址
 *
 * @author qiunet
 * 2024/1/15 14:28
 **
 * @param host
host
 * @param ssl
是否是ssl
 * @param port
端口*/
public record HttpAddress(String host, boolean ssl, int port) {

	@Override
	public String toString() {
		return "HttpAddress{" +
			"host='" + host + '\'' +
			", ssl=" + ssl +
			", port=" + port +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		HttpAddress that = (HttpAddress) o;

		if (ssl != that.ssl) return false;
		if (port != that.port) return false;
		return host.equals(that.host);
	}

}
