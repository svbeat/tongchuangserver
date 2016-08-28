package com.tongchuang.visiondemo.perimetry;

import java.util.List;
import java.util.Map;

import com.tongchuang.visiondemo.job.JobConstants.JobStatus;
import com.tongchuang.visiondemo.job.entity.JobPending;

public interface PerimetryTestService {

	Map<JobStatus, List<Integer>> processTests(List<JobPending> pendingJobs);

}