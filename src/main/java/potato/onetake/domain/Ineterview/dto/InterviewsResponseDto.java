package potato.onetake.domain.Ineterview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class InterviewsResponseDto {
	private List<InterviewSessionDto> interviewSessions;

	@Getter
	@Setter
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
