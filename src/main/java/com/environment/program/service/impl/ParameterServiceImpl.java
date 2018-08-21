package com.environment.program.service.impl;


import com.environment.program.bean.Parameter;
import com.environment.program.dao.ParameterDao;
import com.environment.program.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service("ParameterService")
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    private ParameterDao parameterDao;

    @Override
    @Transactional
    public Integer insert(Parameter parameter) {
        parameter.setCreateTime(new Date());
        return parameterDao.insert(parameter);
    }

    @Override
    @Transactional
    public Integer update(Parameter parameter) {
        return parameterDao.update(parameter);
    }
}
