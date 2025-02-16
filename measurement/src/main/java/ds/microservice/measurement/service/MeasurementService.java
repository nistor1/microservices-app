package ds.microservice.measurement.service;

import ds.microservice.measurement.dto.MeasurementDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MeasurementService {

    MeasurementDto findMeasurementById(UUID id);

    UUID insertMeasurement(MeasurementDto measurementDto);

    UUID updateMeasurement(MeasurementDto measurementDto);

    void deleteMeasurementById(UUID id);
    public List<MeasurementDto> getMeasurementsForDay(LocalDate date, UUID deviceId);

    }
