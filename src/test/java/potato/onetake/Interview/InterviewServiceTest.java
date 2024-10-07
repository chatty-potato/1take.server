package potato.onetake.Interview;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import potato.onetake.domain.Content.dao.CategoryRepository;
import potato.onetake.domain.Content.dao.QuestionCategoryRepository;
import potato.onetake.domain.Content.dao.QuestionRepository;
import potato.onetake.domain.Content.domain.Category;
import potato.onetake.domain.Content.domain.Question;
import potato.onetake.domain.Content.domain.QuestionCategory;
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
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {

	private static int numOfCategory = 5;
	private static int numOfQuestion = 20;

	@Mock private InterviewRepository interviewRepository;
	@Mock private ProfileRepository profileRepository;
	@Mock private CategoryRepository categoryRepository;
	@Mock private QuestionRepository questionRepository;
	@Mock private QuestionCategoryRepository questionCategoryRepository;

	@InjectMocks private InterviewService interviewService;

	@BeforeEach
	public void setup() {
		// Create a mock authentication object
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("testUser");

		// Create a mock SecurityContext and set the authentication object
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);

		// Set the mock SecurityContext in the SecurityContextHolder
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	@DisplayName("인터뷰 생성 테스트")
	public void createInterviewTest() {
		// Given
		InterviewBeginRequestDto interviewBeginRequestDto =
			new InterviewBeginRequestDto(
				"test title",
				Arrays.asList("Test Category 1", "Test Category 2", "Test Category 3"));

		// Expected
		InterviewBeginResponseDto expectedResult = new InterviewBeginResponseDto(1L);
		when(interviewService.createInterview(interviewBeginRequestDto)).thenReturn(expectedResult);

		// When
		InterviewBeginResponseDto interviewBeginResponseDto =
			interviewService.createInterview(interviewBeginRequestDto);

		//Then
		Assertions.assertNotNull(interviewBeginResponseDto.getSessionID());
		Assertions.assertEquals(interviewBeginResponseDto.getSessionID(), expectedResult.getSessionID());
	}

	@Test
	@DisplayName("InterviewCategory 생성 테스트")
	public void createInterviewCategoryTest() {
		// Given
		Profile profile = new Profile("Test Profile");
		when(profileRepository.save(any(Profile.class))).thenReturn(profile);

		Category category = new Category("Test Category");
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		Interview interview = new Interview(profile, "test");
		when(interviewRepository.save(any(Interview.class))).thenReturn(interview);

		// Expected
		InterviewCategory expectedResult = new InterviewCategory(interview, category);
		when(interviewService.createInterviewCategory(interview, category)).thenReturn(expectedResult);

		// When
		InterviewCategory interviewCategory = interviewService.createInterviewCategory(interview, category);

		// Then
		Assertions.assertNotNull(interviewCategory);
		Assertions.assertEquals(expectedResult.getId(), interviewCategory.getId());
		Assertions.assertEquals(expectedResult.getInterview(), interviewCategory.getInterview());
		Assertions.assertEquals(expectedResult.getCategory(), interviewCategory.getCategory());
	}

	@Test
	@DisplayName("인터뷰 전체 조회")
	public void getInterviewTest() {
		// Given
		Profile profile = new Profile("Test Profile");

		// Mock ProfileRepository가 Profile 객체를 반환하도록 설정
		when(profileRepository.findById(anyLong())).thenReturn(Optional.of(profile));

		// 인터뷰 Mock 데이터 설정
		List<Interview> mockInterviews = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Interview interview = new Interview(profile, "test interview " + i);
			mockInterviews.add(interview);
		}

		when(interviewRepository.findAllByProfileId(anyLong())).thenReturn(Optional.of(mockInterviews));

		// Expected
		InterviewsResponseDto expectedResult = new InterviewsResponseDto();
		when(interviewService.getInterviews()).thenReturn(expectedResult);

		// When
		InterviewsResponseDto interviewsResponseDto = interviewService.getInterviews();

		// Then
		Assertions.assertNotNull(interviewsResponseDto);
		List<InterviewsResponseDto.InterviewSessionDto> expectedSessions = expectedResult.getInterviewSessions();
		List<InterviewsResponseDto.InterviewSessionDto> actualSessions = interviewsResponseDto.getInterviewSessions();

		for (int i = 0; i < expectedSessions.size(); i++) {
			InterviewsResponseDto.InterviewSessionDto expectedSession = expectedSessions.get(i);
			InterviewsResponseDto.InterviewSessionDto actualSession = actualSessions.get(i);
			Assertions.assertEquals(expectedSession.getSessionID(), actualSession.getSessionID());
			Assertions.assertEquals(expectedSession.getTitle(), actualSession.getTitle());
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

	@Test
	@DisplayName("질문 생성 테스트")
	void createInterviewQnaTest() {
		// Given
		Interview interview = new Interview(new Profile("test user"), "teset interveiw");
		when(interviewRepository.save(any(Interview.class))).thenReturn(interview);

		Map<Long, Integer> categoryIdAndNumList = new HashMap<>();
		categoryIdAndNumList.put(1L, 3);
		categoryIdAndNumList.put(2L, 4);
		categoryIdAndNumList.put(3L, 1);
		categoryIdAndNumList.put(4L, 2);

		List<QuestionCategory> mockQuestionCategories = new ArrayList<>();
		for (Map.Entry<Long, Integer> entry : categoryIdAndNumList.entrySet()) {
			Long categoryId = entry.getKey();
			Integer numQuestions = entry.getValue();

			// 각 카테고리에 대해 필요한 수만큼 질문 생성
			for (int i = 1; i <= numQuestions; i++) {
				Category category = new Category("Category " + categoryId); // 카테고리 생성
				Question question = new Question("Question " + i + " for Category " + categoryId); // 질문 생성
				QuestionCategory questionCategory = new QuestionCategory(question, category); // QuestionCategory 생성
				mockQuestionCategories.add(questionCategory); // 리스트에 추가
			}
		}
		when(questionCategoryRepository.findRandByCategoryIdList(categoryIdAndNumList))
			.thenReturn(mockQuestionCategories);

		// When
		List<InterviewQna> interviewQnaList = interviewService.createInterviewQna(interview, categoryIdAndNumList);

		// Then
		Assertions.assertNotNull(interviewQnaList);
		Assertions.assertEquals(10, interviewQnaList.size());
		for (InterviewQna interviewQna : interviewQnaList) {
			Assertions.assertNotNull(interviewQna.getInterview());
			Assertions.assertNotNull(interviewQna.getQuestionCategory());
		}
	}

	@Test
	@DisplayName("답변 받기 테스트")
	void getInterviewAnswerTest() {
		// Given
		InterviewAnswerRequestDto interviewAnswerRequestDto =
			new InterviewAnswerRequestDto(3L, "test interview answer");

		long interviewId = 1L;

		// Expected
		InterviewAnswerResponseDto expectedResult = new InterviewAnswerResponseDto(true);
		when(interviewService.getInterviewAnswer(interviewAnswerRequestDto, interviewId))
			.thenReturn(expectedResult);

		// When
		InterviewAnswerResponseDto interviewAnswerResponseDto =
			interviewService.getInterviewAnswer(interviewAnswerRequestDto, interviewId);

		// Then
		Assertions.assertNotNull(interviewAnswerResponseDto);
		Assertions.assertEquals(interviewAnswerResponseDto.getDone(), expectedResult.getDone());
	}

	@Test
	@DisplayName("면접 문답 모두 가져오기")
	void findAllInterviewQnasTest() {
		// Given
		long interviewId = 1L;

		List<InterviewQuestionResponseDto.QuestionDto> questionDtoList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			InterviewQuestionResponseDto.QuestionDto questionDto =
				new InterviewQuestionResponseDto.QuestionDto(
					"test question " + i, 1L + i, "test answer " + i);
			questionDtoList.add(questionDto);
		}

		InterviewQuestionResponseDto expectedResult = new InterviewQuestionResponseDto(questionDtoList);
		when(interviewService.findAllInterviewQnas(interviewId)).thenReturn(expectedResult);

		// When
		InterviewQuestionResponseDto actualResult = interviewService.findAllInterviewQnas(interviewId);


		// Then
		Assertions.assertNotNull(actualResult);
		Assertions.assertEquals(expectedResult.getQuestions().size(), actualResult.getQuestions().size());

		for (int i = 0; i < expectedResult.getQuestions().size(); i++) {
			InterviewQuestionResponseDto.QuestionDto expectedDto = expectedResult.getQuestions().get(i);
			InterviewQuestionResponseDto.QuestionDto actualDto = actualResult.getQuestions().get(i);

			Assertions.assertEquals(expectedDto.getQuestion(), actualDto.getQuestion());
			Assertions.assertEquals(expectedDto.getQuestionIndex(), actualDto.getQuestionIndex());
			Assertions.assertEquals(expectedDto.getAnswer(), actualDto.getAnswer());
		}
	}
}
