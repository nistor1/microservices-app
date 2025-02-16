package ds.microservice.measurement.controller;

import ds.microservice.measurement.dto.MeasurementDto;
import ds.microservice.measurement.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/measurement")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/history/{deviceId}")
    public List<MeasurementDto> getMeasurementsForDay(@PathVariable UUID deviceId,
                                                      @RequestParam("date") String date) {

        LocalDate localDate = LocalDate.parse(date);
        return measurementService.getMeasurementsForDay(localDate, deviceId);
    }
}
