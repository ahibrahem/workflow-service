package rmg.workflow.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseExeption extends Exception {
    private final Integer errorCode;
    public BaseExeption(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
