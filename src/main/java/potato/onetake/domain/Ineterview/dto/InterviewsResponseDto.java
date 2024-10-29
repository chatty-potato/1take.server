package potato.onetake.domain.Ineterview.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewsResponseDto {
	private List<InterviewSessionDto> interviewSessions;

	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class InterviewSessionDto{
		private Long sessionID;
		private String title;
		private String date;
		private Integer score;
		private Boolean done;
	}
}
