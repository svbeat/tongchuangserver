package com.aimu.visiontest.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aimu.visiontest.database.entity.VisionTestOutcome;
import com.aimu.visiontest.database.entity.VisionTestRun;

public interface VisionTestOutcomeRepository extends CrudRepository<VisionTestOutcome, Integer> {

}
