package tech.heron.hqlexec;

public class WorkInfo {

	String workdate;//yyyymmdd-19990101
	boolean work_done_successfully = false;
	boolean load_ods_data_done = false;
	boolean exact_to_dw_fact_page_base_done = false;
	public WorkInfo(String workdate){
		this.workdate = workdate;
	}
	public boolean allMissionDone() {
		// TODO Auto-generated method stub
		boolean result = work_done_successfully && load_ods_data_done && exact_to_dw_fact_page_base_done;
		
		return result;
	}
}
