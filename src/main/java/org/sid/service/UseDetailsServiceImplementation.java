
package org.sid.service;

import java.util.ArrayList;
import java.util.Collection;

import org.sid.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UseDetailsServiceImplementation implements UserDetailsService {

	@Autowired
	private AccountService accountService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = accountService.findUserByUsername ( username );
		if (user == null)
			throw new UsernameNotFoundException ( username );
		Collection<GrantedAuthority> autorities = new ArrayList<> ( );
		user.getRoles ( ).forEach ( r -> {
			autorities.add ( new SimpleGrantedAuthority ( r.getRoleName ( ) ) );
		} );
		return new User ( user.getUsername ( ) , user.getPassword ( ) , autorities );
	}

}
