package com.ferrientregas.email;

import lombok.Builder;

@Builder
public record EmailRequest (

        String name,
        String to
){
}
