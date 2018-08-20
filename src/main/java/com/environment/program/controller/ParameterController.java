package com.environment.program.controller;

import com.environment.program.bean.Parameter;
import com.environment.program.common.ResponseMessage;
import com.environment.program.service.ParameterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/parameter")
public class ParameterController {

    @Resource
    private ParameterService parameterService;


    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody Parameter parameter) {
        return ResponseMessage.ok(parameterService.insert(parameter));
    }

    /**
     * 更新表数据
     * @param parameter
     * @return
     */
    @PutMapping("/update")
    public ResponseMessage update(@RequestBody Parameter parameter) {
        return ResponseMessage.ok(parameterService.update(parameter));
    }
}
