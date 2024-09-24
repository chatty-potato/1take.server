package potato.onetake.domain.Ineterview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class InterviewQuestionResponseDto {
	private boolean done;
	private long currentQuestionIndex;
	private List<QuestionDto> questions;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class QuestionDto {
		private String question;
		private Long questionIndex;
	}
}
