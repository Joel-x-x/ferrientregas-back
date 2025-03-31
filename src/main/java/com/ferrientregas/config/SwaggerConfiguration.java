package com.ferrientregas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Configuration
public class SwaggerConfiguration {
  @Bean
  public OpenAPI customOpenAPI(){
    return new OpenAPI()
      .components(new Components()
          .addSecuritySchemes("bearerAuth",
            new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")))
      .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
      .info(new Info()
          .title("API GESTION DE ENTREGAS EN FERRIDESCUENTOS")
          .version("1.0")
          .description("Documentación interactiva de la API para gestionar " +
                  "los modulos del Software FerriFlete."));
  }

  @Bean
  public OperationCustomizer customizeParameters() {
    return (operation, handlerMethod) -> {
      // Verificar si uno de los parámetros del método es de tipo Pageable
      boolean hasPageable = Arrays.stream(handlerMethod.getMethodParameters())
              .anyMatch(param -> Pageable.class.isAssignableFrom(param.getParameterType()));

      // Solo agregar los parámetros page y size si no hay Pageable en el método
      if (!hasPageable) {
        operation.addParametersItem(
                new Parameter()
                        .name("page")
                        .in("query")
                        .schema(new Schema<Integer>().example(0)) // Página por defecto 0
                        .description("Número de la página (por defecto: 0)")
        );
        operation.addParametersItem(
                new Parameter()
                        .name("size")
                        .in("query")
                        .schema(new Schema<Integer>().example(10)) // Tamaño por defecto 10
                        .description("Cantidad de elementos por página (por defecto: 10)")
        );
      }
      return operation;
    };
  }

  @Bean
  public OpenApiCustomizer removePageableParameter() {
    return openApi -> openApi.getPaths().forEach((path, pathItem) ->
            pathItem.readOperations().forEach(operation -> {
              List<Parameter> parameters = operation.getParameters();
              if (parameters != null) {
                parameters.removeIf(param -> Objects.equals(param.getName(), "pageable"));
              }
            })
    );
  }

}
