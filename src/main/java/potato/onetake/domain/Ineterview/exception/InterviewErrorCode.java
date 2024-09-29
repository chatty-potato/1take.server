package potato.onetake.domain.Ineterview.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import potato.onetake.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum InterviewErrorCode implements ErrorCode {

	INTERVIEW_NOT_FOUND(2000,"인터뷰 세션을 찾지 못했습니다."),
	CATEGORY_NOT_FOUND(2001, "카테고리를 찾지 못했습니다."),
	PROFILE_NOT_FOUND(2002, "프로필을 찾지 못했습니다."),
	QUESTION_NOT_FOUND(2003, "질문을 찾지 못했습니다."),
	INVALID_CATEGORY(2005, "질문 생성 중 카테고리에 문제가 발생했습니다.");

	private final int errorCode;
	private final String errorMessage;
}
