package org.example.project_busticket.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {

    @NotBlank(message = "Full name không được để trống")
    @Size(min = 2, max = 100, message = "Full name phải từ 2-100 ký tự")
    private String fullName;

    @NotBlank(message = "Phone không được để trống")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Address không được để trống")
    @Size(min = 5, max = 255, message = "Address phải từ 5-255 ký tự")
    private String address;
}