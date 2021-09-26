package com.fooqoo56.dev.stub.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class StubDtoErrorResponse implements Serializable {

    private static final long serialVersionUID = 3316176567352799499L;

    @JsonProperty
    private String title;

    @JsonProperty
    private String detail;
}
