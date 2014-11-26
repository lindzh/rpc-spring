package com.linda.framework.rpc.spring;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RpcProviderTestCase extends AbstractTestCase{

	@Override
	public List<String> getLocations() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("classpath*:rpc-provider-config.xml");
		return list;
	}
	
	@Test
	public void startService(){
		try {
			Thread.currentThread().sleep(300000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
