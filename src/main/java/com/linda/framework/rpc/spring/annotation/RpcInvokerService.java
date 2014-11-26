package com.linda.framework.rpc.spring.annotation;


public @interface RpcInvokerService {
	
	String rpcServer() default "defaultRpcServer";
	
	String name() default "";
}
