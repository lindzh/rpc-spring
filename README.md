#rpc spring bean support

server provide a service impl as a remote service,register it through annotation config.
client register a service as a service which is impl from remote service

##Server provider

>Server provide a service as remote service impl

```java
package com.linda.framework.rpc.spring.impl;

@Service
@RpcProviderService(rpcServer="simpleRpcServer")
public class HelloRpcServiceImpl implements HelloRpcService{
	private Logger logger = Logger.getLogger(HelloRpcServiceImpl.class);
	@Override
	public void sayHello(String message,int tt) {
		logger.info("sayHello:"+message+" intValue:"+tt);
	}

	@Override
	public String getHello() {
		return "this is hello service";
	}

	@Override
	public TestRemoteBean getBean(TestBean bean, int id) {
		logger.info("id:"+id+" bean:"+bean.toString());
		TestRemoteBean remoteBean = new TestRemoteBean();
		remoteBean.setAction("fff-"+id);
		remoteBean.setAge(id*2);
		remoteBean.setName("serviceBean");
		return remoteBean;
	}

	@Override
	public int callException(boolean exception) {
		if(exception){
			throw new RuntimeException("happen");
		}
		return 1;
	}
}

```
>Registe it as remote service impl

```xml
	<context:component-scan base-package="com.linda.framework.rpc.spring.impl"></context:component-scan>
	<context:component-scan base-package="com.linda.framework.rpc.spring.test"></context:component-scan>
	
	<bean id="simpleRpcServer" class="com.linda.framework.rpc.server.SimpleRpcServer">
		<property name="host" value="127.0.0.1"></property>
		<property name="port" value="5432"></property>
	</bean>

	<bean class="com.linda.framework.rpc.spring.provider.RpcProviderProcessor" destroy-method="stopRpcService"/>
```

>Start Server and use it Local

```java
public class RpcProviderTestCase extends AbstractTestCase{

	@Resource
	private CallService callService;
	
	@Override
	public List<String> getLocations() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("classpath*:rpc-provider-config.xml");
		return list;
	}
	
	@Test
	public void startService(){
		callService.callHello("this is provider call", 564356);
		try {
			Thread.currentThread().sleep(1000000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
```

##Client register a remote service throw rpc

>Client register it as a service and import it

```xml
	<bean id="simpleRpcClient" class="com.linda.framework.rpc.client.SimpleRpcClient">
		<property name="host" value="127.0.0.1"></property>
		<property name="port" value="5432"></property>
	</bean>	
				   
	<bean id="clientMap" class="java.util.HashMap">
		<constructor-arg>
			<map>
				<entry key="simpleRpcClient" value-ref="simpleRpcClient"/>
			</map>
		</constructor-arg>
	</bean>
	
	<bean id="rpcPackages" class="java.util.ArrayList">
		<constructor-arg>
			<list>
				<value>com.linda.framework.rpc.spring.test</value>
			</list>
		</constructor-arg>
	</bean>
	
	<bean id="rpcInvokerAnnotationConfigurer" class="com.linda.framework.rpc.spring.invoker.RpcInvokerAnnotationConfigurer" destroy-method="stopRpcService">
		<property name="packages" ref="rpcPackages"/>
		<property name="rpcClients" ref="clientMap"></property>
	</bean>
	
	<context:component-scan base-package="com.linda.framework.rpc.spring.test"></context:component-scan>
```

```java
@Service
public class CallService {
	@Resource
	private HelloRpcService helloService;
	@Resource
	private HelloRpcTestService helloRpcTestService;
	@Resource
	private LoginRpcService loginRpcService;

	public void callHello(String msg,int tt){
		helloService.sayHello(msg, tt);
	}
	
	public void callLogin(String user,String pass){
		loginRpcService.login(user, pass);
	}
	
	public void callHelloTestIndex(int index,String key){
		helloRpcTestService.index(index, key);
	}
	
}
```
