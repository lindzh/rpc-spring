package com.linda.framework.rpc.spring.invoker;

public interface RpcInvokerClassFilter {
	
	public boolean accept(Class<?> clazz);

}
