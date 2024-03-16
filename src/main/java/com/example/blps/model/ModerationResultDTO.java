package com.example.blps.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModerationResultDTO {
    private Long id;
    private Boolean moderResult;
    private String moderComment;
}
