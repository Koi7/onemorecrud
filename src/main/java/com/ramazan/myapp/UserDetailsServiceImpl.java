package com.ramazan.myapp;

import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetailsServiceImpl() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SqlSession sqlSession = MyBatisUtil.getSession();
        MyAppUserMapper myAppUserMapper = sqlSession.getMapper(MyAppUserMapper.class);
        MyAppUser myAppUser = myAppUserMapper.getUserByUserName(username);
        if (myAppUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(myAppUser.getName(), myAppUser.getPassword(), emptyList());
    }
}
