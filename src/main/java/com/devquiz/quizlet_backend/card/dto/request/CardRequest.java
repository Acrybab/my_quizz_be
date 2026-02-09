package com.devquiz.quizlet_backend.card.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {
@NotBlank(message = "Term cannot be blank")
private String term;
@NotBlank(message = "Definition cannot be blank")
private String definition;
//private String cardImage;

}
