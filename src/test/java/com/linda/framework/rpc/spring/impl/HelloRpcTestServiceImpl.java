package com.linda.framework.rpc.spring.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.linda.framework.rpc.spring.test.HelloRpcTestService;

@Service
public class HelloRpcTestServiceImpl implements HelloRpcTestService{

	private Logger logger = Logger.getLogger(HelloRpcTestServiceImpl.class);
	
	@Override
	public String index(int index, String key) {
		logger.info("index:"+index+" key:"+key);
		return "HelloRpcTestServiceImpl-"+index;
	}

}
