package com.aimu.visiontest.database;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.aimu.visiontest.database.entity.VisionTestRun;

public interface VisionTestRunRepository extends CrudRepository<VisionTestRun, Long> {

	@Query("SELECT count(distinct subjectId) FROM VisionTestRun tr WHERE subjectAge+2 >= :age AND subjectAge-2 <= :age")
    public int findRunCountByAge(@Param("age") int age);
	
	@Query("SELECT count(distinct subjectId) FROM VisionTestRun tr WHERE subjectAge+2 >= :age AND subjectAge-2 <= :age and score < :score")
    public int findRunCountByAgeScore(@Param("score") int score, @Param("age") int age);
}
