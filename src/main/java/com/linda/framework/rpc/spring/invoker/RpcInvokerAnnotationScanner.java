package com.linda.framework.rpc.spring.invoker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

public class RpcInvokerAnnotationScanner extends ClassPathBeanDefinitionScanner{

	static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	
	public RpcInvokerAnnotationScanner(BeanDefinitionRegistry registry) {
		super(registry,false);
	}

	private Logger logger = Logger.getLogger(RpcInvokerAnnotationScanner.class);
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
	
	private List<String> packages;
	
	private RpcInvokerClassFilter classFilter = new RpcAnnotationInvokerClassFilter();

	public List<String> getPackages() {
		return packages;
	}

	public void setPackages(List<String> packages) {
		this.packages = packages;
	}

	public List<Class<?>> scan() {
		ClassLoader classLoader = this.getClass().getClassLoader();
		LinkedList<Class<?>> list = new LinkedList<Class<?>>();
		if(packages!=null){
			for(String pkg:packages){
				String path = pkg.replace(".", "/");
				String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +path + "/" + DEFAULT_RESOURCE_PATTERN;
				try {
					Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
					if(resources!=null){
						for(Resource resource:resources){
							MetadataReader reader = metadataReaderFactory.getMetadataReader(resource);
							String className = reader.getClassMetadata().getClassName();
							try {
								Class<?> clazz = classLoader.loadClass(className);
								if(classFilter.accept(clazz)){
									list.add(clazz);
								}
							} catch (ClassNotFoundException e) {
								logger.error("class not found:"+e.getMessage());
							}
						}
					}
				} catch (IOException e) {
					logger.error("load resources error:"+packages);
				}
			}
		}
		
		return list;
	}
}
