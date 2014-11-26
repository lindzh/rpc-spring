package com.linda.framework.rpc.spring.factory;

import java.util.List;

public interface RpcInvokerScanner {
	
	static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	
	public List<Class<?>> scan();

}
