package com.linda.framework.rpc.spring.factory;

import com.linda.framework.rpc.spring.annotation.RpcInvokerService;

public class RpcAnnotationInvokerClassFilter implements RpcInvokerClassFilter{

	@Override
	public boolean accept(Class<?> clazz) {
		if(clazz.getAnnotation(RpcInvokerService.class)!=null){
			return true;
		}
		return false;
	}

}
