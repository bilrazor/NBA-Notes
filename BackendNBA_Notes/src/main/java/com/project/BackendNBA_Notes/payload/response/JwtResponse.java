package com.project.BackendNBA_Notes.payload.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String username;
    private String email;
    private String jwtToken;


    public JwtResponse(Long id, String username, String email) {
    }
}
