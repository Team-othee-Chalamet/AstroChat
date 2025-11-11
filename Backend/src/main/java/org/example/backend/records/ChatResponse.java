package org.example.backend.records;

import java.util.List;

public record ChatResponse(String id, List<Choice> choices) {
}
