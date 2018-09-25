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

    /**
     * insert data
     * @param parameter
     * @return
     */
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody Parameter parameter) {
        return ResponseMessage.ok(parameterService.insert(parameter));
    }

    /**
     * update data
     * @param parameter
     * @return
     */
    @PutMapping("/update")
    public ResponseMessage update(@RequestBody Parameter parameter) {
        return ResponseMessage.ok(parameterService.update(parameter));
    }

    /**
     * get one data according to data insert database time desc
     * @return
     */
    @GetMapping ("/selectOne")
    public ResponseMessage selectOne() {
        return ResponseMessage.ok(parameterService.selectOne());
    }

    @GetMapping ("/{deviceId}")
    public ResponseMessage select(@PathVariable("deviceId") String deviceId) {
        return ResponseMessage.ok(parameterService.select(deviceId));
    }
}
