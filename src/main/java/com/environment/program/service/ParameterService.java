package com.environment.program.service;

import com.environment.program.bean.Parameter;

public interface ParameterService {

    Integer insert(Parameter parameter);

    Integer update(Parameter parameter);

    Parameter selectOne ();
}
