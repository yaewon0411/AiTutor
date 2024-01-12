package org.example.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MessagesListRequestDto {
    private String limit;
    private String order;
    private String after;
    private String before;

    public MessagesListRequestDto() {
        this(null, null, null, null);
    }

    public MessagesListRequestDto(String limit, String order, String after, String before) {
        this.limit = limit;
        this.order = order;
        this.after = after;
        this.before = before;
    }
}
