package com.my_doctor.restController;

import com.my_doctor.model.PrescriptionDTO;
import com.my_doctor.service.PrescriptionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/prescriptions", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrescriptionResource {

    private final PrescriptionService prescriptionService;

    public PrescriptionResource(final PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescriptions(@RequestParam(value = "strDate", required = false) LocalDateTime startDate,
                                                                     @RequestParam(value = "endDate", required = false) LocalDateTime endDate) {

        if (startDate!=null & endDate != null){
            return ResponseEntity.ok(prescriptionService.findAll(startDate,endDate));
        }else {
            return ResponseEntity.ok(prescriptionService.getCurrentMonthData());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescription(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(prescriptionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPrescription(
            @RequestBody @Valid final PrescriptionDTO prescriptionDTO) {
        final Long createdId = prescriptionService.create(prescriptionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePrescription(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PrescriptionDTO prescriptionDTO) {
        prescriptionService.update(id, prescriptionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePrescription(@PathVariable(name = "id") final Long id) {
        prescriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
