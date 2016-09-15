package com.tongchuang.visiondemo.job;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tongchuang.visiondemo.job.JobConstants.JobCode;
import com.tongchuang.visiondemo.job.JobConstants.JobStatus;
import com.tongchuang.visiondemo.job.entity.JobPending;
import com.tongchuang.visiondemo.perimetry.PerimetryTestService;

@Service
public class JobServiceImpl {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private NamedParameterJdbcTemplate 	visionDBTemplate;
	private JobPendingRepository		jobPendingRepository;
	private PerimetryTestService			perimetryTestService;
	
    @Autowired
    public JobServiceImpl(NamedParameterJdbcTemplate visionDBTemplate, JobPendingRepository	jobPendingRepository,
    							PerimetryTestService perimetryTestService) {
        this.visionDBTemplate = visionDBTemplate;
        this.jobPendingRepository = jobPendingRepository;
        this.perimetryTestService = perimetryTestService;
    }
    
    @Scheduled(fixedDelay = 60000)
    public void processPerimetryPendingJobs() {
    	logger.info("processPerimetryPendingJobs called.");
    	String batchId = UUID.randomUUID().toString();
    	if (markPendingJobs(batchId, JobCode.PERIMETRY_POSTPROCESS) > 0) {
    		List<JobPending> pendingJobs = jobPendingRepository.getPendingJobs(batchId);
    		Map<JobStatus, List<Integer>> processedJob = perimetryTestService.processTests(pendingJobs);
    		for (JobStatus s : processedJob.keySet()) {
    			markCompleteJobs(s, processedJob.get(s));
    		}
    	}
    	logger.info("processPerimetryPendingJobs done.");
    }


	private int markPendingJobs(String batchId, JobCode jobCode) {
		int rowCount = visionDBTemplate.update("update job_pending set batch_id = :batch_id, status='"+JobStatus.RUNNING+"'"+
								" where status='"+JobStatus.PENDING+"' and job_code=:job_code",
							new MapSqlParameterSource("batch_id", batchId).addValue("job_code", jobCode.name()));
		return rowCount;
	}
	
	private int markCompleteJobs(JobStatus s, List<Integer> ids) {
		if (ids == null || ids.isEmpty()) {
			return 0;
		}
		int rowCount = visionDBTemplate.update("update job_pending set status='"+s.name() +"'"+
				" where job_pending_id in (:ids)",
			new MapSqlParameterSource("ids", ids));
		return rowCount;
		
	}
}
