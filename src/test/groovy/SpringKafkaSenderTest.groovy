import com.example.SpringKafkaApplication
import com.example.producer.Sender
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.test.rule.EmbeddedKafkaRule
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

import static org.assertj.core.api.Assertions.assertThat
import static org.springframework.kafka.test.assertj.KafkaConditions.key
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
class SpringKafkaSenderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringKafkaSenderTest.class)
    private static String SENDER_TOPIC = "sender.t"

    @Autowired
    private Sender sender

    private KafkaMessageListenerContainer<String, String> container;

    private BlockingQueue<ConsumerRecord<String, String>> records;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, "topic")

    @Before
    void setUp() throws Exception {
        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false",
                        embeddedKafka.getEmbeddedKafka())

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<String, String>(consumerProperties)

        // set the topic that needs to be consumed
        ContainerProperties containerProperties = new ContainerProperties(SENDER_TOPIC)

        // create a Kafka MessageListenerContainer
        container = new KafkaMessageListenerContainer<>(consumerFactory,
                containerProperties)

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>()

        // setup a Kafka message listener
        container.setupMessageListener(new MessageListener<String, String>() {
                    @Override
                    void onMessage(
                            ConsumerRecord<String, String> record) {
                        LOGGER.debug("test-listener received message='{}'",
                                record.toString());
                        records.add(record);
                    }
                });

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned partitions
//        ContainerTestUtils.waitForAssignment(container,
//                embeddedKafkaRule.getEmbeddedKafka().getPartitionsPerTopic());
    }

    @After
    void tearDown() {
        // stop the container
        container.stop();
    }

    @Test
    void testSend() throws InterruptedException {
        // send the message
        String greeting = "Hello Spring Kafka Sender!";
        sender.send(greeting);

        // check that the message was received
        ConsumerRecord<String, String> received = records.poll(10, TimeUnit.SECONDS);
        // Hamcrest Matchers to check the value
        assertThat(received, hasValue(greeting));
        // AssertJ Condition to check the key
        assertThat(received).has(key(null));
    }
}
