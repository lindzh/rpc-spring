package com.linda.framework.rpc.spring.filter;

import org.apache.log4j.Logger;

import com.linda.framework.rpc.RemoteCall;
import com.linda.framework.rpc.RpcObject;
import com.linda.framework.rpc.filter.RpcFilter;
import com.linda.framework.rpc.filter.RpcFilterChain;
import com.linda.framework.rpc.net.RpcSender;
import com.linda.framework.rpc.spring.annotation.RpcProviderFilter;

@RpcProviderFilter(rpcServer="simpleRpcServer")
public class RpcTestFilter implements RpcFilter{
	
	private Logger logger = Logger.getLogger(RpcTestFilter.class);

	@Override
	public void doFilter(RpcObject rpc, RemoteCall call, RpcSender sender,
			RpcFilterChain chain) {
		logger.info(rpc.getHost()+":"+rpc.getPort()+" service:"+call.getService()+"."+call.getVersion());
		chain.nextFilter(rpc, call, sender);
	}
}
