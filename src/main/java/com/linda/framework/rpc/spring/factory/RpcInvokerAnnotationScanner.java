package com.linda.framework.rpc.spring.factory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import com.linda.framework.rpc.spring.annotation.RpcInvokerService;

public class RpcInvokerAnnotationScanner implements RpcInvokerScanner{

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

	@Override
	public List<Class<?>> scan() {
		ClassLoader classLoader = this.getClass().getClassLoader();
		LinkedList<Class<?>> list = new LinkedList<Class<?>>();
		if(packages!=null){
			for(String pkg:packages){
				String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +pkg + "/" + DEFAULT_RESOURCE_PATTERN;
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
