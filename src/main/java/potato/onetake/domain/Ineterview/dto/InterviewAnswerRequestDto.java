package potato.onetake.domain.Ineterview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterviewAnswerRequestDto {
	public Long questionIndex;
	public String answer;
}
