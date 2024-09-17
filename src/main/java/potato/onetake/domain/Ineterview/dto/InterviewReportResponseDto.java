package potato.onetake.domain.Ineterview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// 프롬프트 결과보고 일단 보류
public class InterviewReportResponseDto {
	private Long sessionID;
	private String title;
	private String date;
	private Integer score;
	private List<InterviewQNADto> interviewQNAs;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class InterviewQNADto {
		private String question;
		private Long questionIndex;
		private String answer;
		private String aiEvaluation;
		private Integer accuracy;
		private Integer logicalConsistency;
		private Integer technicalDepth;
	}
}
