package com.environment.program.controller;

import com.environment.program.bean.Parameter;
import com.environment.program.common.ResponseMessage;
import com.environment.program.service.ParameterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/parameter")
public class ParameterController {

    @Resource
    private ParameterService parameterService;


    @PostMapping("/insert")
    public ResponseMessage authLogin(@RequestBody Parameter parameter) {
        return ResponseMessage.ok(parameterService.insert(parameter));
    }
//
//    /**
//     * 查询当前登录用户的信息
//     *
//     * @return
//     */
//    @PostMapping("/getInfo")
//    public JSONObject getInfo() {
//        return loginService.getInfo();
//    }
//
//    /**
//     * 登出
//     *
//     * @return
//     */
//    @PostMapping("/logout")
//    public JSONObject logout() {
//        return loginService.logout();
//    }
}
