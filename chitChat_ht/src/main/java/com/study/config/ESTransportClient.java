package com.study.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetSocketAddress;

public class ESTransportClient {
	private static TransportClient client;

	public static TransportClient getInstance() {
		return client;
	}

	private ESTransportClient() {
	}

	static {

		// 通过setting对象指定集群配置信息, 配置的集群名
		Settings settings = Settings.settingsBuilder().put("cluster.name", "es-cluster") // 设置集群名
				.put("client.transport.ignore_cluster_name", true) // 忽略集群名字验证,
				.put("client.transport.sniff", true) // 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
				.build();
		client = TransportClient.builder().settings(settings).build();
		client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("10.10.63.35", 9300)));

	}

}
