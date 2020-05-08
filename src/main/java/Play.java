import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.apache.commons.codec.binary.Base64;

import java.util.List;
import java.util.function.Consumer;

public class Play {
    public static void main(String[] args) {
        l("Arrancan");
        AmazonSNS sns = new AmazonSNSClient();
        AmazonSQS sqs = new AmazonSQSClient();
        String topicArn = "arn:aws:sns:us-east-1:222107996444:topicName";
        String queueArn = "https://sqs.us-east-1.amazonaws.com/222107996444/queueName";


        //String myTopicArn = sns.createTopic(new CreateTopicRequest("topicName")).getTopicArn();
        //String myQueueUrl = sqs.createQueue(new CreateQueueRequest("queueName")).getQueueUrl();
        //Topics.subscribeQueue(sns, sqs, myTopicArn, myQueueUrl);

        //sns.listTopics().getTopics().forEach(topic -> l("Este es el topico "+topic.getTopicArn()));
        sqs.listQueues("queuerr").getQueueUrls().forEach(queue->l("Q: "+queue));
        //l(sns.publish(new PublishRequest(topicArn, "Hello world a").withSubject("Hello 1")).getMessageId());
        //l(sns.publish(new PublishRequest(topicArn, "Hello world b").withSubject("Hello 2")).getMessageId());
        //l(sns.publish(new PublishRequest(topicArn, "Hello world c").withSubject("Hello 3")).getMessageId());
//
        //boolean next = true;
        //while (next) {
        //    List<Message> messages =
        //            sqs.receiveMessage(
        //                    new ReceiveMessageRequest(queueArn)
        //                            .withWaitTimeSeconds(20)
        //                            .withMaxNumberOfMessages(10))
        //                    .getMessages();
        //    next = messages.size() > 0;
        //    messages.forEach(message -> l("Message: " + message.getBody()));
        //    l("Next-------");
        //}


        l("Terminan");
    }

    static void l(Object o) {
        System.out.println(o);
    }
}
