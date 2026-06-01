package andrei.shopservice.dto;

import java.time.Instant;

public record ErrorDto(String message, String code, Instant timestamp) {}
