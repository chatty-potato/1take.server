package potato.onetake.Interview;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
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
import potato.onetake.domain.Ineterview.service.InterviewService;
import potato.onetake.domain.Position.dao.ProfileRepository;
import potato.onetake.domain.Position.domain.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {

	@Mock private InterviewRepository interviewRepository;
	@Mock private ProfileRepository profileRepository;
	@Mock private CategoryRepository categoryRepository;
	@Mock private InterviewCategoryRepository interviewCategoryRepository;
	@Mock private QuestionCategoryRepository questionCategoryRepository;
	@Mock private InterviewQnaRepository interviewQnaRepository;

	@InjectMocks private InterviewService interviewService;

	@Test
	@DisplayName("인터뷰 엔티티 생성 테스트")
	public void createInterviewTest() {
		// Given
		// Create a mock authentication object
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("testUser");

		// Create a mock SecurityContext and set the authentication object
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);

		// Set the mock SecurityContext in the SecurityContextHolder
		SecurityContextHolder.setContext(securityContext);

		// Profile mocking
		Profile testProfile = new Profile("testUser");
		when(profileRepository.findByAlias("testUser")).thenReturn(Optional.of(testProfile));

		String interviewTitle = "Test Interview";

		// When
		Interview actualInterview =
			interviewService.createInterview(interviewTitle);

		//Then
		Assertions.assertNotNull(actualInterview.getTitle());
		Assertions.assertEquals(interviewTitle, actualInterview.getTitle());
	}

	@Test
	@DisplayName("InterviewCategory 생성 테스트")
	public void createInterviewCategoryTest() {
		// Given
		Interview interview = Interview.builder()
			.id(1L)
			.title("test Interview")
			.done(false)
			.build();

		Category category = Category.builder()
			.id(1L)
			.content("test Category")
			.build();

		// When
		InterviewCategory interviewCategory = interviewService.createInterviewCategory(interview, category);

		// Then (기대하는 결과 확인)
		Assertions.assertNotNull(interviewCategory, "생성된 InterviewCategory는 null이어서는 안 됩니다.");
		Assertions.assertEquals(interview, interviewCategory.getInterview(), "Interview가 일치해야 합니다.");
		Assertions.assertEquals(category, interviewCategory.getCategory(), "Category가 일치해야 합니다.");
	}

	@Test
	@DisplayName("카테고리 별 질문 리스트 생성 테스트")
	void createQuestionCategoryListTest() {
		// refer : List<QuestionCategory> createQuestionCategoryList(final Map<Long, Integer> categoryIdAndNumList)
		// Given
		Map<Long, Integer> mockedCategoryIdAndNumList = Map.of(
			1L, 3,
			2L, 4,
			3L, 3
		);

		List<QuestionCategory> mockedQuestionCategoryList = new ArrayList<>(List.of());
		mockedCategoryIdAndNumList.forEach(
			(id, num) -> {
				for (int i = 0; i < num; i++) {
					mockedQuestionCategoryList.add(
						QuestionCategory.builder()
							.category(Category.builder().content("test category " + id).build())
							.question(Question.builder().content("test question " + i).build())
							.build()
					);
				}
			});

		// When
		when(questionCategoryRepository.findRandByCategoryIdList(mockedCategoryIdAndNumList))
			.thenReturn(mockedQuestionCategoryList);

		// When
		List<QuestionCategory> actualQuestionCategoryList =
			interviewService.createQuestionCategoryList(mockedCategoryIdAndNumList);

		// Then
		Assertions.assertNotNull(actualQuestionCategoryList);
		Assertions.assertEquals(mockedQuestionCategoryList.size(), actualQuestionCategoryList.size());
	}

	// 이거 보니까 pk 문제 때문에 그런 것 같은데 최대한 pk배제해서 테스트 할 수 있는 방법 찾아봐야 할 듯
	@Test
	@DisplayName("질문 생성 테스트")
	void createInterviewQnaTest() {
		// Given
		// Mocked interview
		Interview interview = Interview.builder()
			.title("test Interview")
			.done(false)
			.build();
		// Mocked QuestionCategory
		Map<Long, Integer> mockedCategoryIdAndNumList = Map.of(
			1L, 3,
			2L, 4,
			3L, 3
		);
		List<InterviewQna> expectedInterviewQnas = new ArrayList<>(List.of());
		List<QuestionCategory> mockedQuestionCategoryList = new ArrayList<>(List.of());
		mockedCategoryIdAndNumList.forEach(
			(id, num) -> {
				for (int i = 0; i < num; i++) {
					QuestionCategory mockedQuestionCategory = QuestionCategory.builder()
						.category(Category.builder().content("test category " + id).build())
						.question(Question.builder().content("test question " + i).build())
						.build();
					mockedQuestionCategoryList.add(mockedQuestionCategory);
					InterviewQna mockedInterviewQna = InterviewQna.builder()
						.interview(interview)
						.questionCategory(mockedQuestionCategory)
						.build();
					expectedInterviewQnas.add(mockedInterviewQna);
				}
			});

		when(interviewQnaRepository.saveAll(anyList())).thenReturn(expectedInterviewQnas);

		// When
		List<InterviewQna> actualInterviewQnas =
			interviewService.createInterviewQna(interview, mockedQuestionCategoryList);

		// Then
		Assertions.assertNotNull(actualInterviewQnas);
		Assertions.assertEquals(mockedQuestionCategoryList.size(), actualInterviewQnas.size());
		for (int i = 0; i < expectedInterviewQnas.size(); i++) {
			InterviewQna expectedInterviewQna = expectedInterviewQnas.get(i);
			InterviewQna actualInterviewQna = actualInterviewQnas.get(i);
			Assertions.assertEquals(
				expectedInterviewQna.getInterview().getTitle(),
				actualInterviewQna.getInterview().getTitle());
			Assertions.assertEquals(
				expectedInterviewQna.getQuestionCategory().getCategory().getContent(),
				actualInterviewQna.getQuestionCategory().getCategory().getContent());
			Assertions.assertEquals(
				expectedInterviewQna.getQuestionCategory().getQuestion().getContent(),
				actualInterviewQna.getQuestionCategory().getQuestion().getContent()
			);
		}
	}

	@Test
	@DisplayName("인터뷰 전체 조회")
	public void findAllInterviewsTest() {
		// Given
		// Create a mock authentication object
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("testUser");

		// Create a mock SecurityContext and set the authentication object
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);

		// Set the mock SecurityContext in the SecurityContextHolder
		SecurityContextHolder.setContext(securityContext);

		// Profile mocking
		Profile testProfile = new Profile("testUser");
		when(profileRepository.findByAlias("testUser")).thenReturn(Optional.of(testProfile));

		// 인터뷰 리스트 mocking
		Optional<List<Interview>> mockedInterviewList = Optional.of(new ArrayList<>());
		List<InterviewsResponseDto.InterviewSessionDto> mockedSessionDtos = new ArrayList<>();

		long numOfInterviews = 10;
		for (long i = 0; i < numOfInterviews; i++) {
			mockedInterviewList.get().add(
				Interview.builder()
					.id(i)
					.title("test title " + i)
					.createdAt(LocalDateTime.of(2021, 10, 20, 10, 20))
					.done(true)
					.build()
			);
			mockedSessionDtos.add(InterviewsResponseDto.InterviewSessionDto.builder()
				.sessionID(i)
				.title("test title " + i)
				.date("2021-10-20T10:20")
				.score(0)
				.done(true)
				.build());
		}

		when(interviewRepository.findAllByProfileId(testProfile.getId()))
			.thenReturn(mockedInterviewList);

		InterviewsResponseDto mockedInterviewsResponseDto =
			InterviewsResponseDto.builder().interviewSessions(mockedSessionDtos).build();

		// When
		InterviewsResponseDto actualInterviewsResponseDto =
			interviewService.findAllInterviews();

		//Then
		Assertions.assertNotNull(actualInterviewsResponseDto);
		Assertions.assertEquals(actualInterviewsResponseDto.getInterviewSessions().size(), mockedInterviewList.get().size());
		for (int i = 0; i < actualInterviewsResponseDto.getInterviewSessions().size(); i++) {
			Assertions.assertEquals(actualInterviewsResponseDto.getInterviewSessions().get(i).getSessionID(), mockedSessionDtos.get(i).getSessionID());
			Assertions.assertEquals(actualInterviewsResponseDto.getInterviewSessions().get(i).getTitle(), mockedSessionDtos.get(i).getTitle());
			Assertions.assertEquals(actualInterviewsResponseDto.getInterviewSessions().get(i).getDate(), mockedSessionDtos.get(i).getDate());
			Assertions.assertEquals(actualInterviewsResponseDto.getInterviewSessions().get(i).getScore(), mockedSessionDtos.get(i).getScore());
			Assertions.assertEquals(actualInterviewsResponseDto.getInterviewSessions().get(i).getDone(), mockedSessionDtos.get(i).getDone());
		}
	}

	@Test
	@DisplayName("카테고리 별 질문 분배 테스트")
	void distributeInterviewTest() {

		// Given
		List<Long> categoryIds = List.of(1L, 2L, 3L);
		int totalNumOfQuestions = 10;

		// When
		Map<Long, Integer> distributedQuestions =
			interviewService.distributeQuestions(totalNumOfQuestions, categoryIds);

		// Then
		Assertions.assertNotNull(distributedQuestions);
		Assertions.assertEquals(categoryIds.size(), distributedQuestions.size());
		int resultOfNumOfTotalQuestions =
			distributedQuestions.values().stream()
				.mapToInt(Integer::intValue)
				.sum();
		Assertions.assertEquals(totalNumOfQuestions, resultOfNumOfTotalQuestions);
	}

	/**
	 * public InterviewQna findInterviewQnaById(Long qnaId) {
	 * 		return interviewQnaRepository
	 * 			.findById(qnaId)
	 * 			.orElseThrow(InterviewException.QuestionNotFoundException::new);
	 * 	    }
	 */

	/**
	 * @Transactional
	 * 	    public void updateInterviewQnaByAnswer (InterviewAnswerRequestDto interviewAnswerRequestDto) {
	 * 		Long qnaId = interviewAnswerRequestDto.getQuestionIndex();
	 * 		String requestedAnswer = interviewAnswerRequestDto.getAnswer();
	 *
	 * 		InterviewQna interviewQna = findInterviewQnaById(qnaId);
	 *
	 * 		interviewQna.setAnswer(requestedAnswer);
	 * 		interviewQnaRepository.save(interviewQna);
	 *    }
	 *    여기서 저장이 잘 되는지 확인하기.
	 */
	@Test
	@DisplayName("인터뷰 qna 테이블 업데이트 테스트")
	void updateInterviewQnaByAnswerTest() {
		// Given
		InterviewAnswerRequestDto mockedInterviewAnswerResponseDto =
			InterviewAnswerRequestDto.builder()
				.questionIndex(1L)
				.answer("test answer")
				.build();

		InterviewQna mockedInterviewQna = InterviewQna.builder()
			.id(1L)
			.questionCategory(
				QuestionCategory.builder()
					.id(1L)
					.question(Question.builder().id(1L).content("test question").build())
					.category(Category.builder().id(1L).content("test category").build())
					.build()
			)
			.answer(null)
			.build();

		InterviewQna expectedResult = InterviewQna.builder()
			.id(1L)
			.questionCategory(
				QuestionCategory.builder()
					.id(1L)
					.question(Question.builder().id(1L).content("test question").build())
					.category(Category.builder().id(1L).content("test category").build())
					.build()
			)
			.answer("test answer")
			.build();

		when(interviewQnaRepository.findById(anyLong())).thenReturn(Optional.of(mockedInterviewQna));

		// When
		InterviewQna actualInterviewQna = interviewService.updateInterviewQnaByAnswer(mockedInterviewAnswerResponseDto);

		// Then
		Assertions.assertNotNull(actualInterviewQna);
		Assertions.assertEquals(
			actualInterviewQna.getQuestionCategory().getCategory().getContent(),
			mockedInterviewQna.getQuestionCategory().getCategory().getContent());
		Assertions.assertEquals(
			actualInterviewQna.getQuestionCategory().getQuestion().getContent(),
			mockedInterviewQna.getQuestionCategory().getQuestion().getContent());
		Assertions.assertEquals(actualInterviewQna.getAnswer(), mockedInterviewAnswerResponseDto.getAnswer());
	}

	@Test
	@DisplayName("인터뷰 카테고리 생성 테스트")
	void createInterviewCategoriesTest() {
		// Given
		Long interviewId = 1L;
		List<String> categoryNames = List.of("Category1", "Category2", "Category3");

		// Mock Interview
		Interview mockInterview = Interview.builder()
			.id(interviewId)
			.title("Test Interview")
			.done(false)
			.build();

		// Mock Categories
		Map<String, Category> mockCategories = new HashMap<>();
		List<Long> expectedCategoryIds = new ArrayList<>();

		for (int i = 0; i < categoryNames.size(); i++) {
			Long categoryId = (long) (i + 1);
			Category category = Category.builder()
				.id(categoryId)
				.content(categoryNames.get(i))
				.build();
			mockCategories.put(categoryNames.get(i), category);
			expectedCategoryIds.add(categoryId);
		}

		// Mock Repository responses
		when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(mockInterview));
		mockCategories.forEach((name, category) ->
			when(categoryRepository.findByContent(name)).thenReturn(Optional.of(category)));

		// When
		List<Long> actualCategoryIds = interviewService.createInterviewCategories(interviewId, categoryNames);

		// Then
		Assertions.assertNotNull(actualCategoryIds);
		Assertions.assertEquals(expectedCategoryIds.size(), actualCategoryIds.size());
		for (int i = 0; i < expectedCategoryIds.size(); i++) {
			Assertions.assertEquals(expectedCategoryIds.get(i), actualCategoryIds.get(i));
		}
	}

	@Test
	@DisplayName("인터뷰 리포트 생성 테스트")
	void createInterviewReportTest() {
		// Given
		Long interviewId = 1L;
		LocalDateTime createdAt = LocalDateTime.of(2024, 3, 28, 10, 0);
		String interviewTitle = "Test Interview";
		int numOfQuestions = 10;

		// Mock Authentication
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("1"); // userId를 String으로 "1" 반환

		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		// Mock Interview
		Interview mockInterview = Interview.builder()
			.id(interviewId)
			.title(interviewTitle)
			.createdAt(createdAt)
			.done(true)
			.build();

		// Mock InterviewQna List
		List<InterviewQna> mockInterviewQnas = new ArrayList<>();
		for (int i = 0; i < numOfQuestions; i++) {
			Question question = Question.builder()
				.id((long) i)
				.content("Test Question " + i)
				.build();

			Category category = Category.builder()
				.id((long) i)
				.content("Test Category " + i)
				.build();

			QuestionCategory questionCategory = QuestionCategory.builder()
				.id((long) i)
				.question(question)
				.category(category)
				.build();

			InterviewQna qna = InterviewQna.builder()
				.id((long) i)
				.interview(mockInterview)
				.questionCategory(questionCategory)
				.answer("Test Answer " + i)
				.build();

			mockInterviewQnas.add(qna);
		}

		// Mock Repository responses
		when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(mockInterview));
		when(interviewQnaRepository.findAllByInterviewId(interviewId)).thenReturn(mockInterviewQnas);

		// When
		InterviewReportResponseDto reportDto = interviewService.createInterviewReport(interviewId);

		// Then
		Assertions.assertNotNull(reportDto);
		Assertions.assertEquals(interviewId, reportDto.getSessionID());
		Assertions.assertEquals(interviewTitle, reportDto.getTitle());
		Assertions.assertEquals(createdAt.toString(), reportDto.getDate());

		// QnA 검증
		List<InterviewReportResponseDto.InterviewQnaReportDto> qnaReports = reportDto.getInterviewQNAs();
		Assertions.assertEquals(numOfQuestions, qnaReports.size());

		for (int i = 0; i < numOfQuestions; i++) {
			InterviewReportResponseDto.InterviewQnaReportDto qnaReport = qnaReports.get(i);
			Assertions.assertEquals("Test Question " + i, qnaReport.getQuestion());
			Assertions.assertEquals("Test Answer " + i, qnaReport.getAnswer());
		}
	}

	@Test
	@DisplayName("인터뷰 질문 생성 테스트")
	void createInterviewQuestionsTest() {
		// Given
		Long interviewId = 1L;
		List<Long> categoryIds = List.of(1L, 2L, 3L);

		// Mock Interview
		Interview mockInterview = Interview.builder()
			.id(interviewId)
			.title("Test Interview")
			.done(false)
			.build();

		// Mock Questions and Categories
		List<QuestionCategory> mockQuestionCategories = new ArrayList<>();
		for (int i = 0; i < 10; i++) { // 총 10개의 질문 생성
			Question question = Question.builder()
				.id((long) i)
				.content("Test Question " + i)
				.build();

			Category category = Category.builder()
				.id(categoryIds.get(i % categoryIds.size()))
				.content("Test Category " + (i % categoryIds.size()))
				.build();

			QuestionCategory questionCategory = QuestionCategory.builder()
				.id((long) i)
				.question(question)
				.category(category)
				.build();

			mockQuestionCategories.add(questionCategory);
		}

		// Mock Repository responses
		when(interviewRepository.findById(interviewId)).thenReturn(Optional.of(mockInterview));
		when(questionCategoryRepository.findRandByCategoryIdList(any())).thenReturn(mockQuestionCategories);

		// Mock InterviewQna saves
		List<InterviewQna> mockInterviewQnas = mockQuestionCategories.stream()
			.map(qc -> InterviewQna.builder()
				.interview(mockInterview)
				.questionCategory(qc)
				.build())
			.collect(Collectors.toList());
		when(interviewQnaRepository.saveAll(any())).thenReturn(mockInterviewQnas);

		// When
		interviewService.createInterviewQuestions(interviewId, categoryIds);

		// Then
		// 1. Interview가 존재하는지 확인
		verify(interviewRepository).findById(interviewId);

		// 2. 질문이 분배되었는지 확인
		verify(questionCategoryRepository).findRandByCategoryIdList(any());

		// 3. InterviewQna가 저장되었는지 확인
		verify(interviewQnaRepository).saveAll(any());
	}

	@Test
	@DisplayName("답변 받기 테스트")
	void updateInterviewAnswerTest() {
		// Given
		InterviewAnswerRequestDto mockedInterviewAnswerResponseDto =
			InterviewAnswerRequestDto.builder()
				.questionIndex(1L)
				.answer("test answer")
				.build();

		when(interviewQnaRepository.findById(anyLong())).thenReturn(
			Optional.ofNullable(InterviewQna.builder()
				.id(1L)
				.questionCategory(
					QuestionCategory.builder()
						.id(1L)
						.question(Question.builder().id(1L).content("test question").build())
						.category(Category.builder().id(1L).content("test category").build())
						.build()
				)
				.answer(null)
				.build())
		);

		// Expected
		InterviewQna expectedResult = InterviewQna.builder()
			.id(1L)
			.questionCategory(
				QuestionCategory.builder()
					.id(1L)
					.question(Question.builder().id(1L).content("test question").build())
					.category(Category.builder().id(1L).content("test category").build())
					.build()
			)
			.answer("test answer")
			.build();

		// When
		InterviewQna actualResult = interviewService.updateInterviewQnaByAnswer(mockedInterviewAnswerResponseDto);

		// Then
		Assertions.assertNotNull(actualResult);
	}

	@Test
	@DisplayName("모두 답변 완료 체크")
	void isAllAnsweredTest() {
		// Given
		List<InterviewQna> interviewQnaList = new ArrayList<>();
		int numOfQnas = 10;
		for (int i = 0; i < numOfQnas; i++) {
			interviewQnaList.add(InterviewQna.builder()
				.interview(Interview.builder()
					.id(1L)
					.title("test title")
					.build())
				.questionCategory(QuestionCategory.builder()
					.id(1L)
					.question(Question.builder().id(1L).content("test question" + i).build())
					.category(Category.builder().id(1L).content("test category" + i).build())
					.build())
				.answer("test answer" + i)
				.build());
		}

		// When
		when(interviewQnaRepository.findAllByInterviewId(anyLong())).thenReturn(interviewQnaList);
		boolean actualResult = interviewService.isAllAnswered(1L);

		// Then
		Assertions.assertTrue(actualResult, "모든 질문에 답변이 있을 경우 true를 반환해야 합니다");
	}

	@Test
	@DisplayName("면접 문답 모두 가져오기")
	void findAllInterviewQnasTest() {
		// Given
		Long interviewId = 1L;
		int numOfQuestions = 10;

		// Mock InterviewQna List 생성
		List<InterviewQna> mockInterviewQnas = new ArrayList<>();
		for (int i = 0; i < numOfQuestions; i++) {
			Question question = Question.builder()
				.id((long) i)
				.content("Test Question " + i)
				.build();

			Category category = Category.builder()
				.id((long) i)
				.content("Test Category " + i)
				.build();

			QuestionCategory questionCategory = QuestionCategory.builder()
				.id((long) i)
				.question(question)
				.category(category)
				.build();

			InterviewQna qna = InterviewQna.builder()
				.id((long) i)
				.interview(Interview.builder()
					.id(interviewId)
					.title("Test Interview")
					.build())
				.questionCategory(questionCategory)
				.answer("Test Answer " + i)
				.build();

			mockInterviewQnas.add(qna);
		}

		// Mock Repository Response
		when(interviewQnaRepository.findAllByInterviewId(interviewId)).thenReturn(mockInterviewQnas);

		// When
		InterviewQuestionResponseDto responseDto = interviewService.findAllInterviewQnas(interviewId);

		// Then
		Assertions.assertNotNull(responseDto, "응답 DTO가 null이 아니어야 합니다");
		Assertions.assertNotNull(responseDto.getQuestions(), "질문 리스트가 null이 아니어야 합니다");
		Assertions.assertEquals(numOfQuestions, responseDto.getQuestions().size(), "질문 수가 일치해야 합니다");

		// 각 질문의 세부 내용 검증
		List<InterviewQuestionResponseDto.QuestionDto> questions = responseDto.getQuestions();
		for (int i = 0; i < numOfQuestions; i++) {
			InterviewQuestionResponseDto.QuestionDto questionDto = questions.get(i);
			Assertions.assertEquals((long) i, questionDto.getQuestionIndex(), "질문 인덱스가 일치해야 합니다");
			Assertions.assertEquals("Test Question " + i, questionDto.getQuestion(), "질문 내용이 일치해야 합니다");
			Assertions.assertEquals("Test Answer " + i, questionDto.getAnswer(), "답변 내용이 일치해야 합니다");
		}
	}
}
