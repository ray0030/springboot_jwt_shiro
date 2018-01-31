package com.demo.service.impl;

import com.demo.dao.TestMapper;
import com.demo.model.Test;
import com.demo.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestServiceImpl implements ITestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public Test queryById(Integer id) {
        return testMapper.selectByPrimaryKey(id);
    }
}
