package com.linda.framework.rpc.spring.invoker;

import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public abstract class RpcInvokerScanner {
	
	static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	
	protected BeanDefinitionRegistry beanDefinitionRegistry;
	
	public RpcInvokerScanner(BeanDefinitionRegistry registry){
		beanDefinitionRegistry = registry;
	}
	
	public abstract Set<BeanDefinitionHolder> doScan(String basepkg);

}
