package rmg.workflow.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import rmg.workflow.model.dto.ResponseDto;

@ControllerAdvice
public class CustomGlobalExceptionHandler {


    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ResponseDto> handle(NoDataFoundException ex) {
        ResponseDto responseDto = new ResponseDto(false, ex.getMessage(), null, ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
    }

    @ExceptionHandler(AppIllegalStateException.class)
    public ResponseEntity<ResponseDto> handle(AppIllegalStateException ex) {
        ResponseDto responseDto = new ResponseDto(false, ex.getMessage(), null, ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseDto);
    }



}
