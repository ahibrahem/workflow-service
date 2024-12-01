package rmg.workflow.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoDataFoundException extends BaseExeption {

    private static final Integer ERROR_CODE = 400;

    public NoDataFoundException(String message) {
        super(message, ERROR_CODE);
    }

}
