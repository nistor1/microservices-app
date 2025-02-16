package ds.microservice.measurement.service;

import ds.microservice.measurement.dto.MeasurementDto;
import ds.microservice.measurement.repository.MeasurementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MeasurementServiceImpl implements MeasurementService{

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementServiceImpl.class);

    private MeasurementRepository measurementRepository;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Override
    public MeasurementDto findMeasurementById(UUID id) {


        Optional<MeasurementDto> measurementOptional = measurementRepository.findById(id);
        if (measurementOptional.isEmpty()) {
            LOGGER.error("Measurement with id {} was not found in db", id);
            throw new EntityNotFoundException(MeasurementDto.class.getSimpleName() + " with id: " + id);
        }
        return measurementOptional.get();
    }

    @Override
    public UUID insertMeasurement(MeasurementDto measurementDto) {

        measurementRepository.save(measurementDto);
        LOGGER.debug("Measurement with id {} was inserted in db", measurementDto.getDeviceId());
        return measurementDto.getDeviceId();
    }

    @Override
    public UUID updateMeasurement(MeasurementDto measurementDto) {

        Optional<MeasurementDto> optionalMeasurement = measurementRepository.findById(measurementDto.getMeasurementId());
        if(optionalMeasurement.isEmpty()) {
            LOGGER.error("Measurement with id {} was not found in db", measurementDto.getMeasurementId());
            throw new EntityNotFoundException(MeasurementDto.class.getSimpleName() + " with id: " + measurementDto.getMeasurementId());
        }
        MeasurementDto measurement = optionalMeasurement.get();
        measurement.setMeasurementId(measurementDto.getMeasurementId());
        measurement.setHourlyConsumption(measurementDto.getHourlyConsumption());
        measurement.setDeviceId(measurementDto.getDeviceId());
        measurement.setTimestamp(measurementDto.getTimestamp());
        measurement = measurementRepository.save(measurement);
        LOGGER.debug("Measurement with id {} was updated in db", measurement.getMeasurementId());

        return measurement.getMeasurementId();
    }

    @Override
    public void deleteMeasurementById(UUID id) {

        Optional<MeasurementDto> measurementOptional = measurementRepository.findById(id);
        if(measurementOptional.isEmpty()) {
            LOGGER.error("Measurement with id {} was not found in db", id);
            throw new EntityNotFoundException(MeasurementDto.class.getSimpleName() + " with id: " + id);
        }

        measurementRepository.deleteById(id);
    }

    @Override
    public List<MeasurementDto> getMeasurementsForDay(LocalDate date, UUID deviceId) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        return measurementRepository.findByDateAndDeviceId(startOfDay, endOfDay, deviceId);
    }

    public MeasurementRepository getMeasurementRepository() {
        return measurementRepository;
    }

    public void setMeasurementRepository(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }
}
