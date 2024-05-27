package com.example.compoint.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WatchLaterDTO {
    private UserDTO user;

    private StandupDTO standup;

    private LocalDateTime addedAt;

}
