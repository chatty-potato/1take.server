package potato.onetake.global.exception.httpException;

import potato.onetake.global.exception.CustomException;

public class HttpErrorException extends CustomException {
	public HttpErrorException(final HttpErrorCode httpErrorCode) {
		super(httpErrorCode);
	}

	public static class NoApiParamException extends HttpErrorException {
		public NoApiParamException() {
			super(HttpErrorCode.NO_API_PARAMETERS);
		}
	}
}
