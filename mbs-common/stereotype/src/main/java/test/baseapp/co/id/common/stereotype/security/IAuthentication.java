package test.baseapp.co.id.common.stereotype.security;

import org.springframework.security.core.Authentication;

public interface IAuthentication<A extends Authentication, U extends User>{

	public A getAuthentication();
	
	public U getUser();
	
}
