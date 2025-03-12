package com.ferrientregas.email;

import lombok.Builder;

@Builder
public record EmailResponse (
        String message
){
}
