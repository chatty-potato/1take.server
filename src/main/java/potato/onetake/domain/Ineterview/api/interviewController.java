package potato.onetake.domain.Ineterview.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import potato.onetake.domain.Ineterview.dto.*;
import potato.onetake.domain.Ineterview.service.InterviewService;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/interviews")
public class interviewController {

	private final InterviewService interviewService;

	@GetMapping("/")
	public ResponseEntity<InterviewsResponseDto> findAllInterviews() {
		InterviewsResponseDto interviewsResponseDto = interviewService.getInterviews();
		return ResponseEntity.ok(interviewsResponseDto);
	}

	@PostMapping("/")
	public ResponseEntity<InterviewBeginResponseDto> createInterviewSession(InterviewBeginRequestDto requestDto) {
		InterviewBeginResponseDto interviewBeginResponseDto = interviewService.createInterview(requestDto);
		return ResponseEntity.ok(interviewBeginResponseDto);
	}

	@GetMapping("/{sessionID}/questions")
	public ResponseEntity<InterviewQuestionResponseDto> findAllQnaBySessionID(@PathVariable String sessionID) {
		InterviewQuestionResponseDto interviewQuestion = null;
		return ResponseEntity.ok(interviewQuestion);
	}

	@PostMapping("/{sessionID}/answer")
	public ResponseEntity<InterviewAnswerResponseDto> submitAnswer(
		InterviewAnswerRequestDto  interviewAnswerRequestDto, @PathVariable String sessionID) {
		Long interviewId = Long.parseLong(sessionID);
		InterviewAnswerResponseDto interviewAnswer =
			interviewService.getInterviewAnswer(interviewAnswerRequestDto, interviewId);
		return ResponseEntity.ok(interviewAnswer);
	}

	@GetMapping("/reports/{sessionID}")
	public ResponseEntity<InterviewReportResponseDto> findInterviewReport(@PathVariable String sessionID) {
		Long interviewId = Long.parseLong(sessionID);
		InterviewReportResponseDto interviewReport = interviewService.createInterviewReport(interviewId);
		return ResponseEntity.ok(interviewReport);
	}


	@PostMapping("/reports/{sessionID}")
	public ResponseEntity<InterviewReportResponseDto> createInterviewReport(@PathVariable String sessionID) {
		Long interviewId = Long.parseLong(sessionID);
		// 추후 북마크를 추가한 DTO 필요
		InterviewReportResponseDto interviewReport = interviewService.createInterviewReport(interviewId);
		return ResponseEntity.ok(interviewReport);
	}

}
