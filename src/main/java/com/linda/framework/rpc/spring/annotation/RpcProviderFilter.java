package com.linda.framework.rpc.spring.annotation;

import org.springframework.stereotype.Component;

@Component
public @interface RpcProviderFilter {
	
	String bean() default "defaultRpcServer";

}
