package potato.onetake.domain.Ineterview.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import potato.onetake.domain.Ineterview.exception.InterviewException;
import potato.onetake.domain.Position.dao.ProfileRepository;
import potato.onetake.domain.Position.domain.Profile;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InterviewService {

	private final InterviewRepository interviewRepository;
	private final ProfileRepository profileRepository;
	private final CategoryRepository categoryRepository;
	private final InterviewQnaRepository interviewQnaRepository;
	private final InterviewCategoryRepository interviewCategoryRepository;
	private final QuestionCategoryRepository questionCategoryRepository;

	/**
	 * createInterview 메서드
	 * 인터뷰 세션을 생성하고, 사용자의 프로필과 선택된 카테고리 기반으로 인터뷰를 구성합니다.
	 *
	 * @param interviewBeginRequestDto 사용자로부터 받은 인터뷰 시작 요청 정보
	 *  - 사용자 프로필을 조회하고, 선택된 카테고리로 인터뷰를 생성
	 *  - 각 카테고리에 대해 InterviewCategory 및 InterviewQna를 생성
	 *
	 * 처리 단계:
	 * 1. 시큐리티 컨텍스트에서 로그인된 사용자 ID를 가져옵니다.
	 * 2. 사용자 프로필을 조회하고, 프로필이 없으면 예외를 던집니다.
	 * 3. 주어진 제목으로 인터뷰 세션을 생성하고 저장합니다.
	 * 4. 사용자가 선택한 카테고리로 InterviewCategory를 생성하여 저장합니다.
	 * 5. 각 카테고리에 속한 질문들로 InterviewQna를 생성하여 인터뷰와 연결합니다.
	 *
	 * @return InterviewBeginResponseDto - 생성된 인터뷰 세션 ID를 포함한 응답 DTO를 반환
	 */
	@Transactional
	public InterviewBeginResponseDto createInterview(final InterviewBeginRequestDto interviewBeginRequestDto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long userId;
		try {
			userId = Long.parseLong(authentication.getName()); // 추후 시큐리티 완성 후 맞게 수정 필요
		} catch (NumberFormatException e) {
			throw new InterviewException.ProfileNotFoundException(); // userId를 찾지 못했을 때 예외 발생
		}

		final Profile profile = profileRepository.findById(userId)
			.orElseThrow(InterviewException.ProfileNotFoundException::new);

		Interview interview = new Interview(profile, interviewBeginRequestDto.getTitle());
		interviewRepository.save(interview);

		List<Long> categoryIdList = new ArrayList<Long>();
		interviewBeginRequestDto.getCategories().stream()
			.map(categoryName -> categoryRepository.findByContent(categoryName)
				.orElseThrow(InterviewException.CategoryNotFoundException::new)) // 카테고리를 찾지 못했을 때 예외 발생
			.forEach(category -> {
				createInterviewCategory(interview, category);
				categoryIdList.add(category.getId());
			});

		createInterviewQna(interview, categoryIdList);
		return new InterviewBeginResponseDto(interview.getId());
	}

	/**
	 * createInterviewQna 메서드
	 * 인터뷰에 질문을 분배하고, 각 질문에 대한 InterviewQna를 생성합니다.
	 *
	 * @param interview 생성된 인터뷰 객체
	 * @param categoryIdList 선택된 카테고리의 ID 리스트
	 *
	 * 처리 단계:
	 * 1. 각 카테고리에 대해 분배할 질문 수를 계산합니다.
	 * 2. 각 카테고리에 대해 랜덤으로 질문을 선택하여 InterviewQna를 생성합니다.
	 * 3. 생성된 InterviewQna를 저장합니다.
	 *
	 * 예외 처리:
	 *  - 질문 목록이 비어있거나, 질문 개수가 부족할 경우 InvalidCategoryException 예외 발생
	 */
	@Transactional
	public void createInterviewQna(final Interview interview, final List<Long> categoryIdList) {
		final Map<Long, Integer> categoryIdAndNumList = distributeQuestions(10, categoryIdList);
		final List<QuestionCategory> questionCategoryResultList =
			questionCategoryRepository.findRandByCategoryIdList(categoryIdAndNumList);

		if (questionCategoryResultList.isEmpty() || questionCategoryResultList.size() < 10) {
			throw new InterviewException.InvalidCategoryException();
		} // 완성된 qna 수가 10개 미만이거나 비었을 경우 exception 던짐

		List<InterviewQna> interviewQnaList = new ArrayList<>();

		questionCategoryResultList.forEach(questionCategory ->
			interviewQnaList.add(new InterviewQna(interview, questionCategory)));

		interviewQnaRepository.saveAll(interviewQnaList);
	}

	@Transactional
	public void createInterviewCategory(final Interview interview, final Category category) {
		InterviewCategory interviewCategory = new InterviewCategory(interview, category);
		interviewCategoryRepository.save(interviewCategory);
	}

	/**
	 * getInterviews 메서드
	 * 현재 로그인된 사용자의 모든 인터뷰 세션을 조회하고 반환합니다.
	 *
	 * 처리 단계:
	 * 1. 사용자 인증 정보를 가져옵니다.
	 * 2. 사용자의 프로필을 조회하고, 존재하지 않으면 예외를 던집니다.
	 * 3. 사용자의 모든 인터뷰 세션을 조회합니다.
	 * 4. 인터뷰 세션 리스트를 InterviewsResponseDto 형식으로 반환합니다.
	 *
	 * @return InterviewsResponseDto - 조회된 인터뷰 세션 정보를 포함한 응답 DTO
	 */
	@Transactional
	public InterviewsResponseDto getInterviews() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long userId;

		try {
			userId = Long.parseLong(authentication.getName()); // 추후 시큐리티 완성 후 맞게 수정 필요
		} catch (NumberFormatException e) {
			throw new InterviewException.ProfileNotFoundException(); // userId를 찾지 못했을 때 예외 발생
		}

		final Profile profile = profileRepository.findById(userId)
			.orElseThrow(InterviewException.ProfileNotFoundException::new);

		Optional<List<Interview>> interviews = interviewRepository.findAllByProfileId(profile.getId());

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

	/**
	 * getInterviewAnswer 메서드
	 * 사용자가 인터뷰 질문에 답변을 제공한 후 답변을 저장하고, 모든 질문이 완료되었는지 확인합니다.
	 *
	 * @param interviewAnswerRequestDto 사용자가 제공한 답변 정보
	 * @param interviewId 인터뷰 세션 ID
	 *
	 * 처리 단계:
	 * 1. 인터뷰 세션을 조회하고, 없으면 예외를 던집니다.
	 * 2. 사용자가 제공한 답변을 InterviewQna에 저장합니다.
	 * 3. 모든 질문에 답변이 완료되었는지 확인하고, 완료되었으면 인터뷰 상태를 완료로 표시합니다.
	 *
	 * @return InterviewAnswerResponseDto - 모든 질문이 완료되었는지 여부를 포함한 응답 DTO
	 */
	@Transactional
	public InterviewAnswerResponseDto getInterviewAnswer(InterviewAnswerRequestDto interviewAnswerRequestDto, Long interviewId){
		Optional<Interview> interview = interviewRepository.findById(interviewId);
		boolean allAnswered = false;

		if (interview.isPresent()) {
			Interview interviewEntity = interview.get();
			Optional<InterviewQna> interviewQna = Optional.ofNullable(interviewQnaRepository
				.findByInterviewIdAndQuestionCategoryId(
					interviewId, interviewAnswerRequestDto.getQuestionIndex())
				.orElseThrow(InterviewException.QuestionNotFoundException::new));
			interviewQna.ifPresent(qna -> {
				qna.setAnswer(interviewAnswerRequestDto.getAnswer());
				interviewQnaRepository.save(qna);
			});

			// 모든 질문이 저장되었는지 홧인
			List<InterviewQna> interviewQnaList = interviewQnaRepository.findAllByInterviewId(interviewId);
			allAnswered = interviewQnaList.stream().allMatch(qna -> qna.getAnswer() != null);
			interviewEntity.setDone(allAnswered);

			interviewRepository.save(interviewEntity);
		}
		return new InterviewAnswerResponseDto(allAnswered);
	}

	@Transactional
	public InterviewQuestionResponseDto findAllInterviewQnas(Long interviewId) {
		List<InterviewQna> interviewQnaList = interviewQnaRepository.findAllByInterviewId(interviewId);
		InterviewQuestionResponseDto interviewQuestionResponseDto = new InterviewQuestionResponseDto();
		interviewQnaList.forEach(qna -> {
			InterviewQuestionResponseDto.QuestionDto questionDto =
				InterviewQuestionResponseDto.QuestionDto.builder()
					.questionIndex(qna.getId())
					.question(qna.getQuestionCategory().getQuestion().getContent())
					.answer(qna.getAnswer())
					.build();
			interviewQuestionResponseDto.addQuestion(questionDto);
		});
		return interviewQuestionResponseDto;
	}


	@Transactional
	public InterviewReportResponseDto createInterviewReport(Long interviewId) {


		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long userId;

		try {
			userId = Long.parseLong(authentication.getName()); // 추후 시큐리티 완성 후 맞게 수정 필요
		} catch (NumberFormatException e) {
			throw new InterviewException.ProfileNotFoundException(); // userId를 찾지 못했을 때 예외 발생
		}

		Optional<Interview> interview = Optional.ofNullable(interviewRepository.findById(interviewId)
			.orElseThrow(InterviewException.InterviewNotFoundException::new));
		if (interview.isPresent()) {

			Interview interviewEntity = interview.get();
			List<InterviewQna> qnaList = interviewQnaRepository.findAllByInterviewId(interviewId);

			if (qnaList.isEmpty() || qnaList.size() < 10) {
				throw new InterviewException.QuestionNotFoundException();
			}

			InterviewReportResponseDto reportDto = new InterviewReportResponseDto();
			reportDto.setSessionID(interviewId);
			reportDto.setDate(interviewEntity.getCreatedAt().toString());
			reportDto.setTitle(interviewEntity.getTitle());
			List<InterviewReportResponseDto.InterviewQnaReportDto> qnaReport = qnaList.stream().map(
				interviewQna -> {
					Question question = interviewQna.getQuestionCategory().getQuestion();
					String questionContent = question.getContent();
					if (questionContent == null || questionContent.isEmpty()) {
						throw new InterviewException.QuestionNotFoundException();
					}
					String answer = interviewQna.getAnswer();
					if (answer == null) {
						throw new InterviewException.AnswerNotFoundException();
					}
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
	 * distributeQuestions 메서드
	 * 주어진 총 질문 수와 카테고리 목록을 기반으로, 각 카테고리에 분배할 질문 수를 계산합니다.
	 *
	 * @param totalQuestionsCount 세션에서 사용할 전체 질문 수 (현재 10개)
	 * @param categories 사용할 카테고리 목록 (각 카테고리마다 최소 1개 이상의 질문이 할당됨)
	 *
	 * 분배 방식:
	 * 1. 카테고리 개수(m)만큼 최소 질문 수를 각 카테고리에 1개씩 할당합니다.
	 * 2. 나머지 질문 수는 자바의 Random 함수를 사용하여 랜덤하게 카테고리들에 분배합니다.
	 *    - 남은 질문 수 = totalQuestionsCount - categories.size()
	 *    - 각 카테고리에 1개씩 할당된 이후 남은 질문을 무작위로 카테고리에 추가 분배합니다.
	 *
	 * @return HashMap<Long, Integer> - 카테고리 ID와 해당 카테고리에 할당된 질문 수를 매핑하여 반환합니다.
	 */
	public Map<Long, Integer> distributeQuestions(int totalQuestionsCount, List<Long> categories){
		int categoriesCount = categories.size();

		// 만약 전체 질문 수가 카테고리 수보다 적으면 예외
		if (totalQuestionsCount < categoriesCount) {
			throw new InterviewException.InvalidCategoryException();
		}

		Map<Long, Integer> questionsMap = new HashMap<>();

		int remainQuestionsPerCategory = totalQuestionsCount - categoriesCount;

		// 각 카테고리에 최소 1개씩 할당합니다.
		for (Long category : categories) {
			questionsMap.put(category, 1);
		}

		// 남은 질문을 랜덤하게 카테고리에 할당합니다.
		for (int i = 0; i < remainQuestionsPerCategory; i++) {
			Long randomCategory = categories.get(ThreadLocalRandom.current().nextInt(categories.size()));
			questionsMap.put(randomCategory, questionsMap.get(randomCategory) + 1);
		}

		return questionsMap;
	}
}
