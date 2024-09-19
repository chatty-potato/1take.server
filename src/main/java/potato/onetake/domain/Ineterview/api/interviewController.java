package potato.onetake.domain.Ineterview.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import potato.onetake.domain.Ineterview.dto.*;

@RestController
@RequestMapping(value = "/api/interview")
public class interviewController {

	@GetMapping("/interviews")
	public ResponseEntity<InterviewsResponseDto> getInterviews() {
		InterviewsResponseDto interviews = null;
		return ResponseEntity.ok(interviews);
	}

	@GetMapping("/question/{sessionID}")
	public ResponseEntity<InterviewQuestionResponseDto> getInterviewQuestion() {
		InterviewQuestionResponseDto interviewQuestion = null;
		return ResponseEntity.ok(interviewQuestion);
	}

	@GetMapping("/report/{sessionID}")
	public ResponseEntity<InterviewReportResponseDto> getInterviewReport() {
		InterviewReportResponseDto interviewReport = null;
		return ResponseEntity.ok(interviewReport);
	}

	@PostMapping("/begin")
	public ResponseEntity<InterviewBeginResponseDto> startInterview(InterviewBeginRequestDto requestDto) {
		InterviewBeginResponseDto interviewBegin = null;
		return ResponseEntity.ok(interviewBegin);
	}

	@PostMapping("/answer/{sessionID}")
	public ResponseEntity<InterviewAnswerResponseDto> submitAnswer(InterviewAnswerRequestDto  requestDto) {
		InterviewAnswerResponseDto interviewAnswer = null;
		return ResponseEntity.ok(interviewAnswer);
	}

	@PostMapping("/report/{sessionID}")
	public ResponseEntity<InterviewReportResponseDto> generateReport(InterviewReportCreateRequestDto requestDto) {
		InterviewReportResponseDto interviewReport = null;
		return ResponseEntity.ok(interviewReport);
	}

}
