package com.uniquindio.ecommercelibros.aplication.dto.request.usuario;

import com.uniquindio.ecommercelibros.domain.valueObject.RolUsuario;
import jakarta.validation.constraints.*;

/** POST /usuarios */
public record RegistrarUsuarioRequest(
        @NotBlank(message = "El nombre del usuario es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @NotBlank(message = "El correo del usuario es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        @Size(max = 150, message = "El correo no puede superar los 150 caracteres")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
                message = "La contraseña debe tener al menos una mayúscula y un número")
        String contrasenia,

        @NotBlank(message = "El teléfono del usuario es obligatorio")
        @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
        String telefono,

        @NotBlank(message = "La dirección del usuario es obligatoria")
        @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
        String direccion
) {
        @Override
        public String toString() {
                return "RegistrarUsuarioRequest[nombre=" + nombre + ", correo=" + correo
                        + ", contrasenia=****, telefono=" + telefono + ", direccion=" + direccion + "]";
        }
}