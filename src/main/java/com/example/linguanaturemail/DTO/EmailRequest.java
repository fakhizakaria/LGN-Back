package com.example.linguanaturemail.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    private String senderEmail;
    private String senderName;
    private String receiverEmail;
    private String receiverName;
}
