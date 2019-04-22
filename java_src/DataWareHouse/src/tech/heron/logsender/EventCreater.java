package tech.heron.logsender;

import java.awt.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.api.SecureRpcClientFactory;
import org.apache.flume.event.EventBuilder;

public class EventCreater {

	private static RpcClient client;
	private static Properties props;
	private static MessageContainer mc;
	
	static{
		try {
			mc = new MessageContainer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		props = new Properties();
		props.put("client.type", "default_loadbalance");

		// List of hosts (space-separated list of user-chosen host aliases)
		props.put("hosts", "h1 h2 h3");

		// host/port pair for each host alias
		String host1 = "hadoop5:41414";
		String host2 = "hadoop6:41414";
		String host3 = "hadoop7:41414";
		props.put("hosts.h1", host1);
		props.put("hosts.h2", host2);
		props.put("hosts.h3", host3);

		props.put("host-selector", "random"); // For random host selection
		// props.put("host-selector", "round_robin"); // For round-robin host
		// // selection
		props.put("backoff", "true"); // Disabled by default.

		props.put("maxBackoff", "10000"); // Defaults 0, which effectively
											// becomes 30000 ms

		client = RpcClientFactory.getInstance(props);
		
		
		int count = 0;
		String str = "";

		while (true) {
			Thread.sleep(60000);
			str = mc.getNextStr();
			sendDataToFlume(str);
			System.out.print(str);
		}

	}

	public static void sendDataToFlume(String data) {
		if (client == null) {
			return;
		}

		Event event = EventBuilder.withBody(data, Charset.forName("UTF-8"));

		// Send the event
		try {
			client.append(event);
			System.out.print("正常發送一條event\n");
		} catch (EventDeliveryException e) {
			// clean up and recreate the client
			client.close();
			client = null;
			client = RpcClientFactory.getInstance(props);
		}

	}
	
	public static class MessageContainer{
		
		private ArrayList<String> list;
		int index = 0;
		private int size;

		public MessageContainer() throws IOException{
			FileReader fr = new FileReader("log_raw.txt");
			list = new ArrayList<String>();
			BufferedReader br = new BufferedReader(fr);
			String str = "";
			while((str = br.readLine()) != null){
				list.add(str);
//				System.out.print(str+'\n');
			}
		}
		
		public String getNextStr() throws Exception{
			if (list == null) {
				throw new Exception("list为null");
			}
			size = list.size();
			if (index >= size) {
				index = 0;
			}
			return list.get(index++);
			
		}
		
		
	}

}
