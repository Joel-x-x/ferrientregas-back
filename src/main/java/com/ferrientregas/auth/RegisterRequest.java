package com.ferrientregas.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

   private String firstNames;
   private String lastNames;
   private String email;
   private String password;
}
