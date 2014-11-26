package com.linda.framework.rpc.spring.factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import com.linda.framework.rpc.client.AbstractRpcClient;
import com.linda.framework.rpc.spring.annotation.RpcInvokerService;

public class RpcInvokerFactory implements BeanFactory,InitializingBean,ApplicationContextAware,Ordered{

	private Map<String,AbstractRpcClient> clientMap = new HashMap<String,AbstractRpcClient>();
	private RpcInvokerScanner rpcInvokerScanner;
	private ConcurrentHashMap<String, Object> beanCache = new ConcurrentHashMap<String, Object>();
	
	private static final String DEFAULT_RPC_BEAN = "defaultRpcServer";
	
	public Map<String, AbstractRpcClient> getClientMap() {
		return clientMap;
	}

	public void setClientMap(Map<String, AbstractRpcClient> clientMap) {
		this.clientMap = clientMap;
	}

	public RpcInvokerScanner getRpcInvokerScanner() {
		return rpcInvokerScanner;
	}

	public void setRpcInvokerScanner(RpcInvokerScanner rpcInvokerScanner) {
		this.rpcInvokerScanner = rpcInvokerScanner;
	}
	
	private String fixName(String name){
		String newName = name;
		if(!StringUtils.hasText(name)){
			newName = DEFAULT_RPC_BEAN;
		}
		return newName;
	}

	@Override
	public Object getBean(String name) throws BeansException {
		return beanCache.get(this.fixName(name));
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		Object bean = this.getBean(name);
		if(this.isTypeMatch(name, requiredType)){
			return (T)bean;
		}
		return null;
	}

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		Set<String> keys = beanCache.keySet();
		for(String key:keys){
			if(this.isTypeMatch(key, requiredType)){
				return (T)beanCache.get(key);
			}
		}
		return null;
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		return this.getBean(name);
	}

	@Override
	public boolean containsBean(String name) {
		return beanCache.contains(name);
	}

	@Override
	public boolean isSingleton(String name)
			throws NoSuchBeanDefinitionException {
		return beanCache.contains(name);
	}

	@Override
	public boolean isPrototype(String name)
			throws NoSuchBeanDefinitionException {
		return false;
	}

	private boolean isTypeOf(Class<?> sourceType,Class<?> targetType){
		if(sourceType.equals(targetType)){
			return true;
		}
		if(sourceType.equals(Object.class)){
			return false;
		}
		Class<?>[] ifaces = sourceType.getInterfaces();
		if(ifaces!=null){
			for(Class<?> iface:ifaces){
				return isTypeOf(iface,targetType);
			}
		}
		Class<?> superclass = sourceType.getSuperclass();
		return isTypeOf(superclass,targetType);
	}
	
	@Override
	public boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException {
		Object object = this.beanCache.get(name);
		if(object!=null){
			return this.isTypeOf(object.getClass(), targetType);
		}
		return false;
	}

	@Override
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return beanCache.get(name).getClass();
	}
	
	private String getClassSimple(Class<?> clazz){
		String simpleName = clazz.getClass().getSimpleName();
		String suffix = simpleName.substring(1);
		char c = Character.toLowerCase(simpleName.charAt(0));
		return c+suffix;
	}

	@Override
	public String[] getAliases(String name) {
		Object object = beanCache.get(name);
		if(object!=null){
			String simple = this.getClassSimple(object.getClass());
			return new String[]{simple};
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<Class<?>> list = rpcInvokerScanner.scan();
		this.doCreateBeans(list);
	}
	
	private void doCreateBeans(List<Class<?>> remotes){
		if(remotes!=null){
			for(Class<?> remote:remotes){
				RpcInvokerService service = remote.getAnnotation(RpcInvokerService.class);
				String name = service.name();
				String rpcServer = service.rpcServer();
				AbstractRpcClient client = clientMap.get(rpcServer);
				if(client==null){
					throw new BeanCreationException("can't find rpc client of name:"+rpcServer);
				}
				this.doCreateRemoteBean(client, name, remote);
			}
		}
	}
	
	private void doCreateRemoteBean(AbstractRpcClient client,String name,Class<?> remote){
		Object object = client.register(remote);
		if(StringUtils.hasText(name)){
			beanCache.put(name, object);
		}
		String simple = this.getClassSimple(remote);
		beanCache.put(simple, object);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Collection<AbstractRpcClient> clients = clientMap.values();
		for(AbstractRpcClient client:clients){
			client.startService();
		}
	}
	
	public void stopService(){
		Collection<AbstractRpcClient> clients = clientMap.values();
		for(AbstractRpcClient client:clients){
			client.stopService();
		}
	}

	@Override
	public int getOrder() {
		return 200;
	}
}
