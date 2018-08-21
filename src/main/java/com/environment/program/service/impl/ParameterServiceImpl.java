package com.environment.program.service.impl;


import com.environment.program.bean.Parameter;
import com.environment.program.dao.ParameterMapper;
import com.environment.program.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service("ParameterService")
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    @Transactional
    public Integer insert(Parameter parameter) {
        parameter.setCreateTime(new Date());
        return parameterMapper.insert(parameter);
    }

    @Override
    @Transactional
    public Integer update(Parameter parameter) {
        return parameterMapper.update(parameter);
    }

    @Override
    public Parameter selectOne() {
        return parameterMapper.selectOne();
    }
}
