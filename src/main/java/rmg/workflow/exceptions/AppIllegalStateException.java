package rmg.workflow.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppIllegalStateException extends BaseExeption{

    private static final Integer ERROR_CODE = 1001;

    public AppIllegalStateException(String message) {
        super(message, ERROR_CODE);
    }
}
