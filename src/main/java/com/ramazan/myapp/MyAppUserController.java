package com.ramazan.myapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.ramazan.myapp.SecurityConstants.HEADER_STRING;
import static com.ramazan.myapp.SecurityConstants.SECRET;
import static com.ramazan.myapp.SecurityConstants.TOKEN_PREFIX;

@RestController
public class MyAppUserController {


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(){}

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public List list(@RequestParam(value = "id", defaultValue = "0") String  strId) {
        int id;
        try {
            id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            id = 0;
        }
        SqlSession sqlSession = MyBatisUtil.getSession();
        MyAppUserMapper myAppUserMapper = sqlSession.getMapper(MyAppUserMapper.class);
        List list = myAppUserMapper.getAllUsersExcept(id);
        return list;
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST, consumes="application/json")
    public Boolean create(@RequestBody String json) {
        boolean isSuccess = false;
        SqlSession sqlSession = MyBatisUtil.getSession();
        try {
            MyAppUser myAppUser = new ObjectMapper().readValue(json, MyAppUser.class);
            myAppUser.encodePassword();
            MyAppUserMapper myAppUserMapper = sqlSession.getMapper(MyAppUserMapper.class);
            myAppUserMapper.createUser(myAppUser);
            sqlSession.commit();
            isSuccess = true;
        }catch (Exception e){
            isSuccess = false;
            e.printStackTrace();
        }finally {
            sqlSession.close();
            return isSuccess;
        }
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST, consumes="application/json")
    public Boolean update(@RequestBody String json) {
        boolean isSuccess = false;
        SqlSession sqlSession = MyBatisUtil.getSession();
        MyAppUserMapper myAppUserMapper = sqlSession.getMapper(MyAppUserMapper.class);
        try {
            MyAppUser myAppUser = new ObjectMapper().readValue(json, MyAppUser.class);
            MyAppUser myAppUserToUpdate = myAppUserMapper.getUserById(myAppUser.getId());
            if (myAppUser.getName().length() == 0) myAppUser.setName(myAppUserToUpdate.getName());
            if (myAppUser.getPassword().length() == 0) myAppUser.setPassword(myAppUserToUpdate.getPassword());
            else myAppUser.encodePassword();
            if (myAppUser.getEmail().length() == 0) myAppUser.setEmail(myAppUserToUpdate.getEmail());
            myAppUserMapper.updateUser(myAppUser);
            sqlSession.commit();
            isSuccess = true;
        }catch (Exception e){
            isSuccess = false;
            //e.printStackTrace();
        }finally {
            sqlSession.close();
            return isSuccess;
        }
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.GET)
    public Boolean delete(@RequestParam(value = "id", defaultValue = "0") String  strId) {
        boolean isSuccess = false;
        int id;
        try {
            id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            isSuccess = false;
            return isSuccess;
        }
        if (id < 1) return false;
        SqlSession sqlSession = MyBatisUtil.getSession();
        MyAppUserMapper myAppUserMapper = sqlSession.getMapper(MyAppUserMapper.class);
        MyAppUser myAppUser = myAppUserMapper.getUserById(id);
        if (myAppUser == null){
            isSuccess = false;
        }else {
            isSuccess = true;
            myAppUserMapper.deleteUser(myAppUser);
        }
        sqlSession.commit();
        sqlSession.close();
        return isSuccess;
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest req) {
        String token = req.getHeader(HEADER_STRING);
        if (token != null) {
            SqlSession sqlSession = MyBatisUtil.getSession();
            MyAppTokenMapper myAppTokenMapper = sqlSession.getMapper(MyAppTokenMapper.class);
            myAppTokenMapper.addToken(token);
            sqlSession.commit();
            sqlSession.close();
        }

    }


}
