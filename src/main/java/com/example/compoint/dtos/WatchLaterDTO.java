package com.example.compoint.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatchLaterDTO {
    private UserDTO user;
    private StandupDTO standup;
    private LocalDateTime addedAt;

}
