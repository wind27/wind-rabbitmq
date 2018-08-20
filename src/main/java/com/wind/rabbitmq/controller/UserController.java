package com.wind.rabbitmq.controller;

import com.wind.commons.ErrorCode;
import com.wind.rabbitmq.provider.Provider;
import com.wind.utils.JsonResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 *
 * @author qianchun 2018/8/13
 **/
@RestController
class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private Provider provider;

    @RequestMapping("/user/{id}")
    public String findById(Model model, @PathVariable("id") long id) {
        if(id<=0) {
            return JsonResponseUtil.fail(ErrorCode.PARAM_ERROR);
        }
        provider.send(id);
        return JsonResponseUtil.ok();
    }

}
