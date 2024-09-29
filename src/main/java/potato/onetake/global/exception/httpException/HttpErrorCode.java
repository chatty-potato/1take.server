package potato.onetake.global.exception.httpException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import potato.onetake.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum HttpErrorCode implements ErrorCode {

	NO_API_PARAMETERS(1000, "파라미터가 없는 Api 요청입니다.");

	private final int errorCode;
	private final String errorMessage;
}
