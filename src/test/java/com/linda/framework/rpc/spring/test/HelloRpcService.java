package com.linda.framework.rpc.spring.test;

import com.linda.framework.rpc.spring.annotation.RpcInvokerService;
import com.linda.framework.rpc.spring.annotation.RpcProviderService;

@RpcInvokerService(rpcServer="simpleRpcClient")
public interface HelloRpcService {
	
	public void sayHello(String message,int tt);
	
	public String getHello();
	
	public TestRemoteBean getBean(TestBean bean,int id);
	
	public int callException(boolean exception);

}
