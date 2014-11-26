package com.linda.framework.rpc.spring.factory;

public interface RpcInvokerClassFilter {
	
	public boolean accept(Class<?> clazz);

}
