package com.ferrientregas.email.dto;

import lombok.Builder;

@Builder
public record EmailRequest (

        String to
){
}
