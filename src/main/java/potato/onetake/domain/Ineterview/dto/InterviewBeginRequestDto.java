package potato.onetake.domain.Ineterview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterviewBeginRequestDto {
	private String title;
	private List<String> categories;
}
