package org.example.springimplementationboard.Board;

public record BoardRequest(
        Long id,
        String title,
        String body
) {
}
