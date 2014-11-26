package com.linda.framework.rpc.spring.test;

import com.linda.framework.rpc.spring.annotation.RpcInvokerService;
import com.linda.framework.rpc.spring.annotation.RpcProviderService;

@RpcInvokerService
@RpcProviderService(bean="simpleRpcServer")
public interface HelloRpcTestService {
	
	public String index(int index,String key);

}
