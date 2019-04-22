package tech.heron.hdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class HdfsUtil {

	private FileSystem fs;

	public static void main(String[] args) throws IOException {
		// TODO �ϴ��ļ�
		
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop-master:9000/");
		FileSystem fs = FileSystem.get(conf);
		Path src = new Path("hdfs://hadoop-master:9000/jdk-8u191-linux-x64.tar.gz");
		FSDataInputStream in = fs.open(src);
		FileOutputStream os = new FileOutputStream("jdk1.8.tar.gz");
		IOUtils.copy(in, os);
	}
	
	//@Before
	public void init() throws IOException, InterruptedException, URISyntaxException{
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop-master:9000/");
		fs = FileSystem.get(new URI("hdfs://hadoop-master:9000/"), conf, "heron");
	}
	
	@Test
	public void upload() throws IllegalArgumentException, IOException{
		FileInputStream is = new FileInputStream(new File("D:/hdp-test/c.txt"));
		FSDataOutputStream os = fs.create(new Path("hdfs://hadoop-master:9000/test/ii/testdata/c.txt"));
		IOUtils.copy(is, os);
	}
	
	@Test
	public void download() throws IllegalArgumentException, IOException{
		fs.copyToLocalFile(false , new Path("hdfs://hadoop-master:9000/test/flowgroup/result"), new Path("D:/hdp-test/testresult") , true);
	}
	
	@Test
	public void split(){
		
		String line = "13480253104	u_flow = 3 ## d_flow = 180 ## s_flow = 183";
		String[] b = StringUtils.split(line, ' ');
		System.out.print(b);
		
	}
	
	@Test
	public void MilisToDate(){
		long time3 = 45215712;  
        
        Date date2 = new Date();  
        date2.setTime(time3);  
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(date2));

	}
	

}
