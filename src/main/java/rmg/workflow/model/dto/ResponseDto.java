package rmg.workflow.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto {

    private boolean success;
    private String exceptionMessage;
    private Object data;
    private Integer errorCode;

    public ResponseDto(boolean success, String exceptionMessage, Object data, Integer errorCode) {
        this.success = success;
        this.exceptionMessage = exceptionMessage;
        this.data = data;
        this.errorCode = errorCode;
    }
}
