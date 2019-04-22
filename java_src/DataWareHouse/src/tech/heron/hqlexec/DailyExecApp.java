package tech.heron.hqlexec;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DailyExecApp {

	
	private static HiveUtils hu;
	private static WorkInfo lastWorkInfo;
	
	private static String columes_ods_log_detail_base = "app_name,device_type,ip,app_ver,userkey,log_type,phone,net,city_id,time_stamp,page_id,refer_page_type";
	private static String columes_dw_page_fact_base = "app_name,device_type,ip,app_ver,userkey,log_type,phone,net,city_id,time_stamp,page_id,refer_page_type";
	
	
	public static void main(String[] args) {
		hu = new HiveUtils();
		//hu.init();
//		try {
//			ResultSet execute = hu.execute("select * from ods.log_detail_base");
//			while(execute.next()){
//				
//				System.out.print(execute.getString(2) + '\n');
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		hu.loadOdsData("20190409");
//		hu.extractPageLog("20190409");
//		hu.extract_fact_user_aggr_daily("20190409");
//		System.out.print(getFormatDateString(""));
		while(true){
			System.out.print("执行一次循环\n");
			boolean dailyWorkResult = dailyWork();
			if(dailyWorkResult){
				try {
					Thread.sleep(86400000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				//程序执行有问题，用某个机制去报告问题，这里选择睡一个小时再执行
				try {
					System.out.print("程序执行出错，休眠一个小时\n");
					Thread.sleep(3600000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//每天凌晨进行一次任务，处理前一天产生的数据
	private static boolean dailyWork(){
		//TODO 对比当前时间和上一次任务的完成情况（执行时间，是否执行完成等），时间没到就return
		String dateString = getFormatDateString("");
		if(lastWorkInfo==null){
			lastWorkInfo = new WorkInfo(dateString);
			lastWorkInfo.work_done_successfully = false;
			lastWorkInfo.workdate = dateString;
		}
		if(Integer.decode(dateString) < Integer.decode(lastWorkInfo.workdate)){
			//TODO 处理当前时间小于lastWorkInfo中的时间的情况
			return true;
		}
		if(Integer.decode(dateString) == Integer.decode(lastWorkInfo.workdate)){
			//如果还是当天，就看前一天的数据有没有处理完成
			if(lastWorkInfo.allMissionDone()){
				return true;
			}
		}
		//如果lastWorkInfo过时了，是前一天的数据了，就更行lastWorkInfo,info中各项任务完成与否的默认值是false
		if(Integer.decode(dateString) > Integer.decode(lastWorkInfo.workdate)){
			
			lastWorkInfo = new WorkInfo(dateString);
		}
		
		// 将前一天的数据load到ods中的对应分区并将执行结果保存到lastWorkInfo中
		if(!lastWorkInfo.load_ods_data_done){
			
			lastWorkInfo.load_ods_data_done = hu.loadOdsData(dateString);
			
		}
		
		if(!lastWorkInfo.load_ods_data_done) {
			System.out.print("load_ods_data failed\n");
			return false;
		}
		//将基础表处理为其它表
		
		//(1) 从log_detail_base表中抽取fact_page_base表
		if(!lastWorkInfo.exact_to_dw_fact_page_base_done){
			lastWorkInfo.exact_to_dw_fact_page_base_done = hu.extractPageLog(dateString);
		}
		
		if(!lastWorkInfo.exact_to_dw_fact_page_base_done){
			System.out.print("exact_to_dw_fact_page_base failed\n");
			return false;
		}
		
		return true;
		
	}
	
	private static String getFormatDateString(String format){
		long currentTimeMillis = System.currentTimeMillis();
		String result = "";
		Date date = new Date(currentTimeMillis - 86400000);
		if(format == ""){
			format = "yyyyMMdd";
		}
		SimpleDateFormat ft = new SimpleDateFormat(format);
		result = ft.format(date);
		return result ;
	}
	
	/**
	 * 
	 */
	@Test
	public void test(){
//		Date date = new Date();
//		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
//		
//		
//		System.out.print(ft.format(date) );//+""+ date.getMonth() + "" + date.getDay());
		
		
	}
}
