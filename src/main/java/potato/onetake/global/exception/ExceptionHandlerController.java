package potato.onetake.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import potato.onetake.domain.Ineterview.exception.InterviewException;
import potato.onetake.global.exception.httpException.HttpErrorException;

/**
 * INTERVIEW_NOT_FOUND(2000,"인터뷰 세션을 찾지 못했습니다."),
 * CATEGORY_NOT_FOUND(2001, "카테고리를 찾지 못했습니다."),
 * PROFILE_NOT_FOUND(2002, "프로필을 찾지 못했습니다."),
 * QUESTION_NOT_FOUND(2003, "질문을 찾지 못했습니다."),
 * BOOKMARK_NOT_FOUND(2004, "북마크를 찾지 못했습니다.");
 */
@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(HttpErrorException.NoApiParamException.class)
	public ResponseEntity<String> handleNoApiParamException(final CustomException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(e.toString());
	}

	@ExceptionHandler({
		InterviewException.ProfileNotFoundException.class,
		InterviewException.InterviewNotFoundException.class,
		InterviewException.CategoryNotFoundException.class,
		InterviewException.QuestionNotFoundException.class,
	})
	public ResponseEntity<String> handleInterviewFactorNotFoundException(final CustomException e) {
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(e.toString());
	}

	@ExceptionHandler(InterviewException.InvalidCategoryException.class)
	public ResponseEntity<String> handleInvalidCategoryException(final CustomException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(e.toString());
	}
}
