package ds.microservice.measurement.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.queue.json.name}")
    private String jsonQueue;

    @Value("${rabbitmq.queue.simulator.name}")
    private String simulatorQueue;

    @Value("${rabbitmq.queue.device.delete.name}")
    private String deviceDeleteQueue;

    @Value("${rabbitmq.queue.simulator.update.name}")
    private String deviceUpdateQueue;

    @Value("${rabbitmq.queue.simulator.insert.name}")
    private String deviceInsertQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.measurement.exchange.name}")
    private String measurementExchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    @Value("${rabbitmq.routing.simulator.key}")
    private String simulatorRoutingKey;

    @Value("${rabbitmq.routing.device.delete.key}")
    private String deviceDeleteKey;

    @Value("${rabbitmq.routing.device.update.key}")
    private String deviceUpdateKey;

    @Value("${rabbitmq.routing.device.insert.key}")
    private String deviceInsertKey;


    // spring bean for rabbitmq queue
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    // spring bean for rabbitmq queue (for json messages)
    @Bean
    public Queue jsonQueue() {
        return new Queue(jsonQueue);
    }

    @Bean
    public Queue simulatorQueue(){
        return new Queue(simulatorQueue);
    }

    @Bean
    public Queue deviceDeleteQueue(){
        return new Queue(deviceDeleteQueue);
    }
    @Bean
    public Queue deviceInsertQueue(){
        return new Queue(deviceInsertQueue);
    }
    @Bean
    public Queue deviceUpdateQueue(){
        return new Queue(deviceUpdateQueue);
    }

    //spring bean for rabbitmq exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    //spring bean for rabbitmq exchange
    @Bean
    public TopicExchange measurementExchange(){
        return new TopicExchange(measurementExchange);
    }

    //simulator biding queue
    @Bean
    public Binding deviceInsertBinding() {
        return BindingBuilder.bind(deviceInsertQueue())
                .to(measurementExchange())
                .with(deviceInsertKey);
    }
    //simulator biding queue
    @Bean
    public Binding deviceUpdateBinding() {
        return BindingBuilder.bind(deviceUpdateQueue())
                .to(measurementExchange())
                .with(deviceUpdateKey);
    }
    //simulator biding queue
    @Bean
    public Binding deviceDeleteBinding() {
        return BindingBuilder.bind(deviceDeleteQueue())
                .to(measurementExchange())
                .with(deviceDeleteKey);
    }

    //simulator biding queue
    @Bean
    public Binding simulatorBinding() {
        return BindingBuilder.bind(simulatorQueue())
                .to(measurementExchange())
                .with(simulatorRoutingKey);
    }

    // binding between queue and exchange using routing_key
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(simulatorQueue())
                .to(exchange())
                .with(routingKey);
    }

    // binding between jsonQueue and exchange using routing_json_key
    @Bean
    public Binding jsonBinding() {
        return BindingBuilder.bind(jsonQueue())
                .to(exchange())
                .with(routingJsonKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate  rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());

        return rabbitTemplate;
    }

    //ConnectionFactory
    //RabbitTemplate
    //RabbitAdmin
}
