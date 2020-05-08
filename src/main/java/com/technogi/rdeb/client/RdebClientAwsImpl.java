package com.technogi.rdeb.client;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClient;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.technogi.rdeb.client.exceptions.RdebNotConfiguredException;
import com.technogi.rdeb.client.exceptions.RdebNotConnectedException;
import com.technogi.rdeb.client.exceptions.RdepBroadcastException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RdebClientAwsImpl implements RdebClient {
    private final Logger log = LoggerFactory.getLogger(RdebClientAwsImpl.class);

    private static final String QUEUE_PREFIX = "queue.prefix";
    private static final String TOPIC_ARN = "topic.arn";
    private static final String BROADCASTS_TOPIC_ARN = "broadcastsTopic.arn";
    private static final String DEFAULT_QUEUE_PREFIX = "rdeb-queues-";

    private Map<String, List<EventHandler>> eventHandlersMap = Collections.synchronizedMap(new HashMap<>());
    private AmazonSNSAsync sns = null;
    private AmazonSQSAsync sqs = null;
    private boolean connected = false;
    private ScheduledExecutorService executor = null;
    private Config config;
    private String topicArn;
    private String broadcastsTopicArn;
    private int initialDelay = 2;
    private Gson gson = new Gson();

    @Override
    public void connect(Config config) {
        assert config != null;
        if (config.getClientId() == null) {
            log.warn("No ID was givven to the service. A random ID will be assigned");
            config.setClientId(UUID.randomUUID().toString());
        }

        assert config.getProps() != null;
        assert config.getProps().containsKey(TOPIC_ARN);
        topicArn = config.getProps().getProperty(TOPIC_ARN);
        broadcastsTopicArn = config.getProps().getProperty(BROADCASTS_TOPIC_ARN);

        AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
        sns = new AmazonSNSAsyncClient(credentialsProvider);
        sqs = new AmazonSQSAsyncClient(credentialsProvider);

        String queuePrefix = DEFAULT_QUEUE_PREFIX + config.getClientId();
        if (config.getProps().containsKey(QUEUE_PREFIX)) {
            queuePrefix = config.getProps().getProperty(QUEUE_PREFIX) + config.getClientId();
        }

        List<String> queuesUrls = sqs.listQueues(queuePrefix).getQueueUrls();
        String queueArn;
        if (queuesUrls.size() == 0) {
            log.info("Registering queue " + queuePrefix);
            queueArn = sqs.createQueue(new CreateQueueRequest(queuePrefix)).getQueueUrl();
            log.info("Subscribing Queue {} to Topic {}", queueArn, topicArn);
            Topics.subscribeQueue(sns, sqs, topicArn, queueArn);
        } else {
            queueArn = queuesUrls.get(0);
        }
        connected = true;
    }

    @Override
    public void start() {
        if (!connected) throw new RdebNotConnectedException();
        if (executor == null) executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(() -> {

        }, initialDelay, config.getPollingTime(), TimeUnit.SECONDS);
    }

    @Override
    public void stop() {

    }

    @Override
    public void publish(Event event) {

    }

    @Override
    public void broadcast(Event event) {
        assert connected;
        log.debug("Broadcasting event {}", event);
        if (event != null && event.getType() != null) {
            sns.publish(new PublishRequest(broadcastsTopicArn, gson.toJson(event.getProps()), event.getType()));
        }
    }

    @Override
    public void subscribe(String id, EventHandler eventHandler) {

    }
}
