package co.newmovie.dto;

import com.querydsl.core.annotations.QueryProjection;

import co.newmovie.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private Long userId;
    private String username;

    @Builder
    @QueryProjection
    public UserDto(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public User toEntity() {
        return User.builder()
                // .userId(this.userId)
                .username(this.username)
                .build();
    }

}
