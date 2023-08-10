package com.my_doctor.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PrescriptionDTO {

    private Long id;

    @NotNull
    private LocalDateTime prescriptionDate;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Integer age;

    @NotNull
    @Size(max = 255)
    private String gender;

    private String diagnosis;

    private String medicines;

    private LocalDateTime nextVisitDate;

}
