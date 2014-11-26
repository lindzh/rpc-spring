package com.linda.framework.rpc.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcInvokerService {
	
	String rpcServer() default "defaultRpcServer";
	
	String name() default "";
}
