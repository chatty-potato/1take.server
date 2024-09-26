package potato.onetake.domain.Ineterview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import potato.onetake.domain.Content.dao.CategoryRepository;
import potato.onetake.domain.Content.dao.QuestionCategoryRepository;
import potato.onetake.domain.Content.domain.Category;
import potato.onetake.domain.Content.domain.Question;
import potato.onetake.domain.Content.domain.QuestionCategory;
import potato.onetake.domain.Ineterview.dao.InterviewCategoryRepository;
import potato.onetake.domain.Ineterview.dao.InterviewQnaRepository;
import potato.onetake.domain.Ineterview.dao.InterviewRepository;
import potato.onetake.domain.Ineterview.domain.Interview;
import potato.onetake.domain.Ineterview.domain.InterviewCategory;
import potato.onetake.domain.Ineterview.domain.InterviewQna;
import potato.onetake.domain.Ineterview.dto.*;
import potato.onetake.domain.Position.dao.ProfileRepository;
import potato.onetake.domain.Position.domain.Profile;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterviewService {

	private final InterviewRepository interviewRepository;
	private final ProfileRepository profileRepository;
	private final CategoryRepository categoryRepository;
	private final InterviewQnaRepository interviewQnaRepository;
	private final InterviewCategoryRepository interviewCategoryRepository;
	private final QuestionCategoryRepository questionCategoryRepository;

	/**
	 *
	 * @param interviewBeginRequestDto
	 * 시큐리티 컨텍스트에서 로그인 정보 가져온뒤 프로필 정보 가져옴
	 * 만들어진 카테고리와 request로 받은 카테고리 정보를 기반으로 interview-category 다대다 관계 설정
	 * 이후, 아직은 미구현이지만, 카테고리를 기반으로 qna 테이블
	 * 인터뷰 <- 카테고리 기반으로 질문을 가져와야함
	 * 1. 퀘스천 자체에 카테고리 추가
	 * @return interview
	 */
	@Transactional
	public InterviewBeginResponseDto createInterview(final InterviewBeginRequestDto interviewBeginRequestDto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = Long.parseLong(authentication.getName()); // 추후 시큐리티 완성 후 맞게 수정 필요

		final Profile profile = profileRepository.getReferenceById(userId);

		Interview interview = new Interview(profile, interviewBeginRequestDto.getTitle());

		interviewRepository.save(interview);

		List<Long> categoryIdList = new ArrayList<Long>();

		interviewBeginRequestDto.getCategories().stream()
			.map(categoryName -> categoryRepository.findByContent(categoryName)
				.orElseThrow()) // exception 구현 필요
			.forEach(category -> {
				createInterviewCategory(interview, category);
				categoryIdList.add(category.getId());
			});
		createInterviewQna(interview, categoryIdList);
		return new InterviewBeginResponseDto(interview.getId());
	}

	@Transactional
	public void createInterviewQna(final Interview interview, final List<Long> categoryIdList) {
		final Map<Long, Integer> categoryIdAndNumList = distributeQuestions(10, categoryIdList);
		final List<QuestionCategory> questionCategoryList =
			questionCategoryRepository.findRandByCategoryIdList(categoryIdAndNumList);

		List<InterviewQna> interviewQnaList = new ArrayList<>();

		for (QuestionCategory questionCategory : questionCategoryList) {
			interviewQnaList.add(new InterviewQna(interview, questionCategory));
		}

		interviewQnaRepository.saveAll(interviewQnaList);
	}

	@Transactional
	public void createInterviewCategory(final Interview interview, final Category category) {
		InterviewCategory interviewCategory = new InterviewCategory(interview, category);
		interviewCategoryRepository.save(interviewCategory);
	}

	@Transactional
	public InterviewsResponseDto getInterviews() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = Long.parseLong(authentication.getName());

		Optional<List<Interview>> interviews = interviewRepository.findAllByProfileId(userId);

		InterviewsResponseDto interviewsResponseDto = new InterviewsResponseDto();

		if (interviews.isPresent()) {
			List<Interview> interviewList = interviews.get();
			List<InterviewsResponseDto.InterviewSessionDto> sessionDtos = interviewList.stream()
				.map(interview -> new InterviewsResponseDto.InterviewSessionDto(
					interview.getId(),
					interview.getTitle(),
					interview.getCreatedAt().toString(),
					0,
					interview.isDone()
				)).toList();

			interviewsResponseDto.setInterviewSessions(sessionDtos); // TODO : 리팩토링
		}

		return interviewsResponseDto;
	}

	// 추후 answer set하는 부분을 setter가 아닌 서비스 로직을 통하여 변경 가능하도록 변경 예정
	// answer
	@Transactional
	public InterviewAnswerResponseDto getInterviewAnswer(InterviewAnswerRequestDto interviewAnswerRequestDto, Long interviewId){
		Optional<Interview> interview = interviewRepository.findById(interviewId);
		boolean allAnswered = false;

		if (interview.isPresent()) {
			Interview interviewEntity = interview.get();
			Optional<InterviewQna> interviewQna = interviewQnaRepository.findByInterviewIdAndQuestionId(
				interviewId, interviewAnswerRequestDto.getQuestionIndex());
			interviewQna.ifPresent(qna -> {
				qna.setAnswer(interviewAnswerRequestDto.getAnswer());
				interviewQnaRepository.save(qna);
			}); // TODO : setter 리팩토링
			List<InterviewQna> interviewQnaList = interviewQnaRepository.findAllByInterviewId(interviewId);
			allAnswered = interviewQnaList.stream()
				.allMatch(qna -> qna.getAnswer() != null);
			interviewEntity.setDone(allAnswered);
			interviewRepository.save(interviewEntity);
		}
		return new InterviewAnswerResponseDto(allAnswered);
	}

	@Transactional
	public InterviewReportResponseDto createInterviewReport(InterviewReportCreateRequestDto interviewReportCreateRequestDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = Long.parseLong(authentication.getName());
		Long interviewId = interviewReportCreateRequestDto.getSessionID();
		Optional<Interview> interview = interviewRepository.findById(interviewId);
		InterviewReportResponseDto interviewReportResponseDto = new InterviewReportResponseDto();
		if (interview.isPresent()) {

			Interview interviewEntity = interview.get();
			List<InterviewQna> qnaList = interviewQnaRepository.findAllByInterviewId(interviewId);

			InterviewReportResponseDto reportDto = new InterviewReportResponseDto();
			reportDto.setSessionID(interviewId);
			reportDto.setDate(interviewEntity.getCreatedAt().toString());
			reportDto.setTitle(interviewEntity.getTitle());
			List<InterviewReportResponseDto.InterviewQnaReportDto> qnaReport = qnaList.stream().map(
				interviewQna -> {
					Question question = interviewQna.getQuestion();
					String questionContent = question.getContent();
					String answer = interviewQna.getAnswer(); // TODO: answer이 없을 경우, 예외 필요
					// TODO: 레포트 생성하는 부분 처리 필요
					return new InterviewReportResponseDto.InterviewQnaReportDto(
						questionContent, question.getId(), answer
					);
				}
			).toList();
			reportDto.setInterviewQNAs(qnaReport);

			return reportDto;
		}
		return new InterviewReportResponseDto();
	}

	/**
	 *
	 * @param totalQuestionsCount
	 * @param categories
	 * 세션 질문 수 n / 카테고리 수 m = 카테고리 별로 최소 들어가야할 질문 수
	 * (전체 질문 수 현재 10개지만 추후 질문 수 확장 염두에 뒀으니까 그냥 n으로 둠)
	 *
	 * 세션 질문 수 n % 카테고리 수 m = 카테고리 별로 들어가야할 남은 질문 수
	 *
	 * 남은 질문은 자바 Rand사용해서 랜덤한 카테고리 하나씩 for문 돌리면서 카운트 하나 씩 감소하면서 0이 될 때까지 돌림
	 *
	 * HashMap 형태로, <String, num>으로 카테고리, 개수 형태로 query 요청
	 * @return
	 */
	public Map<Long, Integer> distributeQuestions(int totalQuestionsCount, List<Long> categories){
		// redis에서 totalQuestionsCount를 보관하면 성능도 좋을듯
		int categoriesCount = categories.size();

		// TODO: if (num of question > num of category) 일 경우 exception 필요

		Map<Long, Integer> questionsMap = new HashMap<>();

		int remainQuestionsPerCategory = totalQuestionsCount - categoriesCount;

		for (Long category : categories) {
			questionsMap.put(category, 1);
		}

		for (int i = 0; i < remainQuestionsPerCategory; i++) {
			Long randomCategory = categories.get(ThreadLocalRandom.current().nextInt());
			questionsMap.put(randomCategory, questionsMap.get(randomCategory) + 1);
		}

		return questionsMap;
	}
}
