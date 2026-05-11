package org.example.project_busticket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name không được để trống")
    @Size(min = 2, max = 100,
            message = "Full name phải từ 2-100 ký tự")
    private String fullName;

    @NotBlank(message = "Phone không được để trống")
    @Pattern(  regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Address không được để trống")
    @Size(min = 5, max = 255, message = "Address phải từ 5-255 ký tự")
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
