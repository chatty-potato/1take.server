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
public class InterviewReportResponseDto {
	private Long sessionID;
	private String title;
	private String date;
	private Integer score;
	private List<InterviewQnaReportDto> interviewQNAs;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class InterviewQnaReportDto {
		private String question;
		private Long questionIndex;
		private String answer;
		private String aiEvaluation;
		private Integer accuracy;
		private Integer logicalConsistency;
		private Integer technicalDepth;

		public InterviewQnaReportDto(String question, Long questionIndex, String answer){
			this.question = question;
			this.questionIndex = questionIndex;
			this.answer = answer;
		}
	}
}
