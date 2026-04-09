# Project Folder Structure (Spring Boot MVC Standard)

```text
src/
  main/
    java/com/example/demo/
      config/           # App and framework configuration classes
      controller/       # REST controllers (API layer)
      service/          # Service interfaces
      service/impl/     # Service implementations (business logic)
      repository/       # Spring Data repositories (DB access)
      entity/           # JPA entities (database models)
      dto/
        request/        # Request DTOs
        response/       # Response DTOs
      enums/            # Application enums
      exception/        # Custom exceptions
      advice/           # Global exception handlers (@ControllerAdvice)
      mapper/           # Entity <-> DTO mappers
      specification/    # Dynamic query specifications
      constant/         # Application constants
      util/             # Utility/helper classes
      validation/       # Custom validators and constraints
      security/         # Security config and auth-related classes
```

## Suggested Flow

`controller -> service -> repository -> entity`

Use `dto` + `mapper` between `controller` and `service` to keep APIs decoupled from persistence models.
