package potato.onetake.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final int errorCode;
	private final String errorMessage;

	public <T extends Enum<T> & ErrorCode> CustomException(final T errorType) {
		super(errorType.getErrorMessage());
		errorCode = errorType.getErrorCode();
		errorMessage = errorType.getErrorMessage();
	}
}
