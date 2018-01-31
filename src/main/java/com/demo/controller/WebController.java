package com.demo.controller;

import com.alibaba.fastjson.JSON;
import com.demo.bean.ResponseBean;
import com.demo.database.Service;
import com.demo.database.UserBean;
import com.demo.exception.UnauthorizedException;
import com.demo.jwt.JWTUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/jwt")
public class WebController {

    private static final Logger logger = LogManager.getLogger(WebController.class);

    @Autowired
    private Service service;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public void login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletResponse response){
        UserBean userBean = service.getUser(username);
        if(userBean.getPassword().equals(password)){
            ResponseBean responseBean= new ResponseBean(200,"login success", JWTUtil.sign(username,password));
            try {
                response.getWriter().write(JSON.toJSON(responseBean).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            throw new UnauthorizedException();
        }
    }

    @RequestMapping(value = "/article",method = RequestMethod.GET)
    public void article(HttpServletRequest request,HttpServletResponse response){
        Subject subject = SecurityUtils.getSubject();
        ResponseBean responseBean;
        if(subject.isAuthenticated()){
            responseBean = new ResponseBean(200,"you are already logged in",null);

        }else{
            responseBean = new ResponseBean(200,"you are guest",null);
        }
        try {
            response.getWriter().write(JSON.toJSON(responseBean).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/require_auth",method = RequestMethod.GET)
    @RequiresAuthentication
    public void requiredAuth(HttpServletRequest request,HttpServletResponse response){
        ResponseBean responseBean =  new ResponseBean(200,"You are authenticated",null);
        try {
            response.getWriter().write(JSON.toJSON(responseBean).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/require_role",method = RequestMethod.GET)
    @RequiresRoles("admin")
    public void requiredRole(HttpServletRequest request,HttpServletResponse response){
        ResponseBean responseBean =   new ResponseBean(200,"You are visiting require_role",null);
        try {
            response.getWriter().write(JSON.toJSON(responseBean).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/require_permission",method = RequestMethod.GET)
    @RequiresPermissions(logical = Logical.AND,value = {"view","edit"})
    public void requirePermission(HttpServletRequest request,HttpServletResponse response){
        ResponseBean responseBean = new ResponseBean(200,"You are visiting permission require edit,view",null);
        try {
            response.getWriter().write(JSON.toJSON(responseBean).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void unauthorized(HttpServletRequest request,HttpServletResponse response){
        ResponseBean responseBean = new ResponseBean(401,"unauthorized",null);
        try {
            response.getWriter().write(JSON.toJSON(responseBean).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
