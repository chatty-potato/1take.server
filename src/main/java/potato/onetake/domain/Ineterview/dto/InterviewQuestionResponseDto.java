package potato.onetake.domain.Ineterview.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class InterviewQuestionResponseDto {
	private List<QuestionDto> questions = new ArrayList<>();

	public void addQuestion(QuestionDto question) {
		questions.add(question);
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class QuestionDto {
		private String question;
		private Long questionIndex;
		private String answer;
	}
}
