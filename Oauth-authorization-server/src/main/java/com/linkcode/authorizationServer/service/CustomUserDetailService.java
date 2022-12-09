package com.linkcode.authorizationServer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.linkcode.authorizationServer.model.User;
import com.linkcode.authorizationServer.model.UserRole;
import com.linkcode.authorizationServer.repository.UserRepository;

@Service
@Transactional
//we need this to get user from databse and provide to spring security
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user=userRepo.findByEmail(email);
		if(user==null) {
			System.out.println("User not found\n\n");
			throw new UsernameNotFoundException("No User Found");
		}
		System.out.println("Helllllo1\n\n\n3");
		System.out.println(user);
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				user.isEnable(),
				true,
				true,
				true,
				getAuthorities(List.of(user.getUserrole()))
				);
	}


	private Collection<? extends GrantedAuthority> getAuthorities(List<Set<UserRole>> of) {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(int i=0;i<of.size();i++) {
			for(UserRole u:of.get(i)) {
				authorities.add(new SimpleGrantedAuthority(u.getRole().getName()));
			}
		}
		return authorities;
	}

}
