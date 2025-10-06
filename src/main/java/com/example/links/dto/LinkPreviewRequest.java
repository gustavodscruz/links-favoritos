package com.example.links.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkPreviewRequest {
    private String url;
    private Long userId;
    private LocalDateTime requestedAt;
}
