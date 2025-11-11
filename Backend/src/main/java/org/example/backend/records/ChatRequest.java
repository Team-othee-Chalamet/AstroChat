package org.example.backend.records;

import java.util.List;

public record ChatRequest(String model, List<ChatMessage> messages) {
}
