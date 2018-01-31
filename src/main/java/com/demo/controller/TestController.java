package com.demo.controller;

import com.alibaba.fastjson.JSON;
import com.demo.model.Test;
import com.demo.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ITestService testService;

    @RequestMapping("/query")
    public void queryById(HttpServletRequest request, HttpServletResponse response){
        Integer id = Integer.valueOf(request.getParameter("id"));
        Test test =  testService.queryById(id);
        String result = JSON.toJSONString(test);
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
