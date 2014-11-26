package com.linda.framework.rpc.spring.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.linda.framework.rpc.spring.annotation.RpcProviderService;
import com.linda.framework.rpc.spring.test.LoginRpcService;

@Service
@RpcProviderService(rpcServer="simpleRpcServer")
public class LoginRpcServiceImpl implements LoginRpcService{
	
	private Logger logger = Logger.getLogger(LoginRpcServiceImpl.class);
	
	private Map<String,String> cache = new HashMap<String,String>();

	@Override
	public boolean login(String username, String password) {
		logger.info("login user:"+username+" password:"+password);
		String pass = cache.get(username);
		return pass!=null&&pass.equals(password);
	}
	
	public LoginRpcServiceImpl(){
		cache.put("linda", "123456");
		cache.put("test", "123456");
		cache.put("admin", "123456");
	}
}
