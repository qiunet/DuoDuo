package org.qiunet.utils.prometheus;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/***
 *
 * @author qiunet
 * 2022/11/2 16:13
 */
public class PrometheusRegistry {

	private static final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
	/**
	 * 返回给外面. 可能外面单独使用
	 * @return
	 */
	public static PrometheusMeterRegistry registry() {
		return registry;
	}
}
