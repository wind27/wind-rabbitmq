package com.wind.rabbitmq.provider;

import com.alibaba.fastjson.JSONObject;
import com.wind.auth.model.User;
import com.wind.rabbitmq.queue.RabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Provider
 *
 * @author qianchun 2018/8/13
 **/
@Service
@Configuration
public class Provider implements RabbitTemplate.ConfirmCallback {
    private static Logger logger = LoggerFactory.getLogger(Provider.class);

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public Provider(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);

    }

    /**
     * 回调方法
     * 
     * @param correlationData correlationData
     * @param ack ack
     * @param cause cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println(" 回调id:" + correlationData);
        if (ack) {
            System.out.println("消息成功消费");
        } else {
            System.out.println("消息消费失败:" + cause);
        }
    }

    /**
     * send msg
     * 
     * @param id id
     */
    public void send(Long id) {
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setId(id);
            user.setRealname("qc");
            user.setCreateTime(new Date());
            sleep();

            rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE_USER, RabbitMqConfig.ROUTING_USER, "qc" + i);
            logger.info("send msg: {}", JSONObject.toJSON(user));
        }
    }

    /**
     * sleep
     */
    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }
}
