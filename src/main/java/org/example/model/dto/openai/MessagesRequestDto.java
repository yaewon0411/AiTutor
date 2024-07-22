package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class MessagesRequestDto {
    private String role;
    private String content;

    @JsonProperty("file_ids")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ArrayList<String> file_ids;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object metadata;

    public MessagesRequestDto(String role, String content, ArrayList<String> fileIds) {
        this.role = role;
        this.content = content;
        this.file_ids = fileIds;
    }

    public MessagesRequestDto(String role, String content){
        this.role = role;
        this.content = content;
    }
}
