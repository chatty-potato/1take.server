package potato.onetake.domain.Ineterview.exception;

import potato.onetake.global.exception.CustomException;

/**
 * INTERVIEW_NOT_FOUND(2000,"인터뷰 세션을 찾지 못했습니다."),
 * CATEGORY_NOT_FOUND(2001, "카테고리를 찾지 못했습니다."),
 * INVALID_CATEGORY(2005, "질문 생성 중 카테고리에 문제가 발생했습니다.");
 * PROFILE_NOT_FOUND(2002, "프로필을 찾지 못했습니다."),
 * QUESTION_NOT_FOUND(2003, "질문을 찾지 못했습니다."),
 */
public class InterviewException extends CustomException {
	public InterviewException(InterviewErrorCode interviewErrorCode) {
		super(interviewErrorCode);
	}

	public static class InterviewNotFoundException extends InterviewException {
		public InterviewNotFoundException() { super(InterviewErrorCode.INTERVIEW_NOT_FOUND); }
	}

	public static class CategoryNotFoundException extends InterviewException {
		public CategoryNotFoundException() { super(InterviewErrorCode.CATEGORY_NOT_FOUND); }
	}

	public static class InvalidCategoryException extends InterviewException {
		public InvalidCategoryException() { super(InterviewErrorCode.CATEGORY_NOT_FOUND); }
	}

	public static class ProfileNotFoundException extends InterviewException {
		public ProfileNotFoundException() { super(InterviewErrorCode.PROFILE_NOT_FOUND); }
	}

	public static class QuestionNotFoundException extends InterviewException {
		public QuestionNotFoundException() { super(InterviewErrorCode.QUESTION_NOT_FOUND); }
	}

	public static class AnswerNotFoundException extends InterviewException {
		public AnswerNotFoundException() { super(InterviewErrorCode.ANSWER_NOT_FOUND); }
	}

}
