package com.linda.framework.rpc.spring.invoker;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RpcInvokerScanConfigurer implements BeanDefinitionRegistryPostProcessor,InitializingBean,ApplicationContextAware,BeanNameAware{
	
	private List<String> packages;

	public List<String> getPackages() {
		return packages;
	}

	public void setPackages(List<String> packages) {
		this.packages = packages;
	}
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		//do nothing
	}

	@Override
	public void setBeanName(String name) {
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		
	}

}
