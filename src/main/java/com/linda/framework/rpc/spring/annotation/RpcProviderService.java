package com.linda.framework.rpc.spring.annotation;


public @interface RpcProviderService {
	
	String bean() default "defaultRpcServer";
	
}
