package tech.heron.kafkas;

public class KafkaProperties {
	 	public static final String INTOPIC = "PhoneLogs";
	    //public static final String OUTTOPIC = "topic2";
	    public static final String KAFKA_SERVER_URL0 = "hadoop5";
	    public static final String KAFKA_SERVER_URL1 = "hadoop6";
	    public static final String KAFKA_SERVER_URL2 = "hadoop7";
	    public static final int KAFKA_SERVER_PORT0 = 9092;
	    public static final int KAFKA_SERVER_PORT1 = 9092;
	    public static final int KAFKA_SERVER_PORT2 = 9092;
	    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 65536;
	    public static final int CONNECTION_TIMEOUT = 100000;
	    public static final String CLIENT_ID = "SimpleConsumerDemoClient";
	 
	    private KafkaProperties() {}

}
