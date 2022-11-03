package org.qiunet.function.prometheus;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpConnectionPoolMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.qiunet.cross.node.ServerNodeManager;
import org.qiunet.utils.http.HttpRequest;
import org.qiunet.utils.prometheus.PrometheusRegistry;

import java.util.Arrays;

/***
 * Grafana import DuoDuo.Dashboard.
 * <a href="https://grafana.com/grafana/dashboards/?search=DuoDuo">获取模板</a>
 * 可以兼容DuoDuo的监控数据
 * 之后可以自行定制修改.
 *
 * @author qiunet
 * 2022/11/2 16:13
 */
public enum RootRegistry {
	instance;

	private final PrometheusMeterRegistry registry = PrometheusRegistry.registry();

	RootRegistry() {
		if (ServerNodeManager.getCurrServerInfo() != null) {
			registry.config().commonTags("serverId", String.valueOf(ServerNodeManager.getCurrServerId()));
		}
		new OkHttpConnectionPoolMetrics(HttpRequest._client().connectionPool()).bindTo(registry);
		new FileDescriptorMetrics().bindTo(registry);
		new JvmCompilationMetrics().bindTo(registry);
		new JvmHeapPressureMetrics().bindTo(registry);
		new ClassLoaderMetrics().bindTo(registry);
		new ClassLoaderMetrics().bindTo(registry);
		new ProcessorMetrics().bindTo(registry);
		new JvmMemoryMetrics().bindTo(registry);
		new ProcessorMetrics().bindTo(registry);
		new JvmThreadMetrics().bindTo(registry);
		new LogbackMetrics().bindTo(registry);
		new JvmInfoMetrics().bindTo(registry);
		new UptimeMetrics().bindTo(registry);
		new JvmGcMetrics().bindTo(registry);
	}

	/**
	 * 返回给外面. 可能外面单独使用
	 * @return
	 */
	public MeterRegistry registry() {
		return registry;
	}

	/**
	 * 注册新的Meter
	 * @param meter
	 */
	public void registry(MeterBinder meter) {
		meter.bindTo(registry);
	}

	/**
	 * 生成新的summary
	 * @param name
	 * @param tags
	 * @return
	 */
	public DistributionSummary summary(String name, Tag ... tags) {
		return registry.summary(name, Arrays.asList(tags));
	}

	/**
	 * 生成gange
	 * @param name
	 * @param number
	 * @param tags
	 * @return
	 */
	public Double gauge(String name, Double number, Tag... tags) {
		return registry.gauge(name, Arrays.asList(tags), number);
	}
	/**
	 * 生成Counter
	 * @param name counter 的名称
	 * @param tags 必须双数
	 * @return
	 */
	public Counter counter(String name, Tag ... tags) {
		return registry.counter(name, Arrays.asList(tags));
	}

	/**
	 * timer
	 * @param name
	 * @param tags
	 * @return
	 */
	public Timer timer(String name, Tag... tags) {
		return registry.timer(name, Arrays.asList(tags));
	}
	/**
	 * 生成对应的数据
	 * @return 数据
	 */
	public String scrape() {
		return registry.scrape();
	}
}
