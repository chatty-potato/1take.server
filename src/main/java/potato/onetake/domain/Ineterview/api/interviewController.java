package potato.onetake.domain.Ineterview.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/interview")
public class interviewController {

	@GetMapping("/interviews")
	public ResponseEntity<InterviewListResponseDto> getInterviews() {
		return ResponseEntity.ok(InterviewListResponseDto);
	}

	@GetMapping("/question/{sessionID}")
	public ResponseEntity<InterviewQuestionResponseDto> getInterviewQuestion() {
		return ResponseEntity.ok(InterviewSessionResponseDto);
	}

	@GetMapping("/report/{sessionID}")
	public ResponseEntity<InterviewReportResponseDto> getInterviewReport() {
		return ResponseEntity.ok(InterviewReportResponseDto);
	}

	@PostMapping("/begin")
	public ResponseEntity<InterviewBeginResponseDto> startInterview(InterviewBeginRequestDto requestDto) {
		return ResponseEntity.ok(InterviewBeginResponseDto);
	}

	@PostMapping("/answer/{sessionID}")
	public ResponseEntity<InterviewAnswerResponseDto> submitAnswer(InterviewAnswerRequestDto  requestDto) {
		return ResponseEntity.ok(InterviewAnswerResponseDto);
	}

	@PostMapping("/report/{sessionID}")
	public ResponseEntity<InterviewReportResponseDto> generateReport(InterviewReportCreateRequestDto) {
		return ResponseEntity.ok(InterviewReportResponseDto);
	}

}
