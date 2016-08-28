package com.tongchuang.visiondemo.device;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CalibrationRepository extends CrudRepository<Calibration, String> {

	public List<Calibration> getLatestCalibration(Pageable pageable);
}
