package ds.microservice.device.controllers;

import ds.microservice.device.dtos.DeviceDto;
import ds.microservice.device.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDto>> getPersons() {
        List<DeviceDto> dtos = deviceService.findAllDevices();
        for (DeviceDto dto : dtos) {
            Link personLink = linkTo(methodOn(DeviceController.class)
                    .getPerson(dto.getDeviceId())).withRel("deviceDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertPerson(@Valid @RequestBody DeviceDto deviceDto) {
        UUID personID = deviceService.insertDevice(deviceDto);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDto> getPerson(@PathVariable("id") UUID deviceId) {
        DeviceDto dto = deviceService.findDeviceById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<UUID> updatePerson(@Valid @RequestBody DeviceDto deviceDto) {
        UUID deviceId = deviceService.updateDevice(deviceDto);
        return new ResponseEntity<>(deviceId, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deletePersonById(@PathVariable("id") UUID deviceId) {
        deviceService.deleteDeviceById(deviceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<UUID> deletePersonById() {
        deviceService.deleteAllDevices();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("client/{id}")
    public ResponseEntity<List<DeviceDto>> getPersons(@PathVariable("id") UUID personId) {
        List<DeviceDto> dtos = deviceService.findDevicesByPersonId(personId);
        for (DeviceDto dto : dtos) {
            Link personLink = linkTo(methodOn(DeviceController.class)
                    .getPerson(dto.getDeviceId())).withRel("deviceDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}

