package com.rahul.learning.ems.backend.dtos;

/*
 * What is a Record?
 * A record is a special type of class introduced in Java 16 (preview in Java 14).
 * It is designed to hold immutable data.
 * Instead of writing lots of boilerplate code like constructors, getters, equals(), hashCode(), and toString(),
 * Java generates them automatically.
 */

public record SuccessResponseDTO(String message) {
}