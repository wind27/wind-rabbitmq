package com.wind.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.wind.rabbitmq.queue.RabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * Consumer
 *
 * @author qianchun 2018/8/13
 **/
@Component
@RabbitListener(queues = RabbitMqConfig.QUEUE_USER, containerFactory = "rabbitListenerContainerFactory")
public class Consumer {
    private static Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private Queue userQueue;


    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(userQueue);
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                byte[] body = message.getBody();
                logger.info("receive msg : " + new String(body));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
            }
        });
        return container;
    }
}
