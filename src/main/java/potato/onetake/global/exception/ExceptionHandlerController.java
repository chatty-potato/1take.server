package potato.onetake.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import potato.onetake.global.exception.httpException.HttpErrorCode;
import potato.onetake.global.exception.httpException.HttpErrorException;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(HttpErrorException.NoApiParamException.class)
	public ResponseEntity<String> handleNoApiParamException(final CustomException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(e.toString());
	}
}
