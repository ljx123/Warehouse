package tech.heron.kafkas;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class MyKafkaComsumer extends Thread {

	public static void main(String[] args) {

		MyKafkaComsumer consumerThread = new MyKafkaComsumer(
				KafkaProperties.INTOPIC);
		consumerThread.start();

	}

	private final KafkaConsumer<String, String> consumer;

	private final String topic;
	private static final Logger LOG = LoggerFactory
			.getLogger(MyKafkaComsumer.class);

	public MyKafkaComsumer(String topic) {

		Properties props = new Properties();
		// bootstrap.servers 必要
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				KafkaProperties.KAFKA_SERVER_URL0 + ":"
						+ KafkaProperties.KAFKA_SERVER_PORT0 + ","
						+ KafkaProperties.KAFKA_SERVER_URL1 + ":"
						+ KafkaProperties.KAFKA_SERVER_PORT1 + ","
						+ KafkaProperties.KAFKA_SERVER_URL2 + ":"
						+ KafkaProperties.KAFKA_SERVER_PORT2);
		// group id
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "audit");
		// 是否后台自动提交offset 到kafka
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		// 消费者偏移自动提交到Kafka的频率（以毫秒为单位enable.auto.commit）设置为true
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		// 故障检测，心跳检测机制 的间隔时间，，在该值范围内，没有接收到心跳，则会删除该消费者
		// 并启动再平衡（rebanlance）,值必须在group.min.session.timeout 和
		// group.max.session.timeout.ms之间
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		// key - value 的序列化类
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");

		this.consumer = new KafkaConsumer<String, String>(props);
		this.topic = topic;
	}

	public void run() {
		System.out.println("ConsumerThread--run");

		consumer.subscribe(Arrays.asList(KafkaProperties.INTOPIC));

		// consumer.subscribe(Collections.singletonList(this.topic));
		while (true) {

			// consumer.poll()
			ConsumerRecords<String, String> records = consumer.poll(200);

			for (ConsumerRecord<String, String> record : records) {
//				System.out.println("Received message: (" + record.key() + ", "
//						+ record.value() + ") offset " + record.offset()
//						+ " partition " + record.partition() + ")");
				HdfsUtil.appendLine(record.value());
			}
		}
	}

}
