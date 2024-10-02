package potato.onetake.Interview;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import potato.onetake.domain.Content.dao.CategoryRepository;
import potato.onetake.domain.Content.dao.QuestionCategoryRepository;
import potato.onetake.domain.Content.dao.QuestionRepository;
import potato.onetake.domain.Content.domain.Category;
import potato.onetake.domain.Content.domain.Question;
import potato.onetake.domain.Content.domain.QuestionCategory;
import potato.onetake.domain.Ineterview.dao.InterviewCategoryRepository;
import potato.onetake.domain.Ineterview.dao.InterviewQnaRepository;
import potato.onetake.domain.Ineterview.dao.InterviewRepository;
import potato.onetake.domain.Ineterview.dto.InterviewBeginRequestDto;
import potato.onetake.domain.Ineterview.dto.InterviewBeginResponseDto;
import potato.onetake.domain.Ineterview.dto.InterviewsResponseDto;
import potato.onetake.domain.Ineterview.service.InterviewService;
import potato.onetake.domain.Position.dao.ProfileRepository;
import potato.onetake.domain.Position.domain.Profile;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback
public class InterviewServiceTest {

	private final InterviewRepository interviewRepository;
	private final ProfileRepository profileRepository;
	private final CategoryRepository categoryRepository;
	private final InterviewQnaRepository interviewQnaRepository;
	private final InterviewCategoryRepository interviewCategoryRepository;
	private final QuestionCategoryRepository questionCategoryRepository;

	@Autowired
	private InterviewService interviewService;
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	public InterviewServiceTest(InterviewRepository interviewRepository, ProfileRepository profileRepository, CategoryRepository categoryRepository, InterviewQnaRepository interviewQnaRepository, InterviewCategoryRepository interviewCategoryRepository, QuestionCategoryRepository questionCategoryRepository) {
		this.interviewRepository = interviewRepository;
		this.profileRepository = profileRepository;
		this.categoryRepository = categoryRepository;
		this.interviewQnaRepository = interviewQnaRepository;
		this.interviewCategoryRepository = interviewCategoryRepository;
		this.questionCategoryRepository = questionCategoryRepository;
	}

	// profile 모킹, test category, question 설정
	// Given
	@BeforeEach
	public void setUp() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			"testUser", null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Profile profile = new Profile("tester");
		profileRepository.save(profile);

		for (int i = 1; i <= 5; i++) {
			String categoryContent = "Test Category " + i;
			Category category = new Category(categoryContent);
			categoryRepository.save(category);
		}

		for (int i = 1; i <= 100; i++) {
			String questionContent = "Test Question " + i;
			Question question = new Question(questionContent);
			questionRepository.save(question);
		}

		List<Category> categories = categoryRepository.findAll();
		List<Question> questions = questionRepository.findAll();

		int questionIndex = 0;
		for (Category category : categories) {
			for (int j = 0; j < 20; j++) {
				Question question = questions.get(questionIndex);
				QuestionCategory questionCategory = new QuestionCategory(question, category);
				questionCategoryRepository.save(questionCategory);
				questionIndex++;
			}
		}
	}

	@Test
	public void createInterviewTest() {
		//Given
		InterviewBeginRequestDto interviewBeginRequestDto =
			new InterviewBeginRequestDto("test title", Arrays.asList("Test Category 1", "Test Category 2", "Test Category 3"));
		//When
		InterviewBeginResponseDto interviewBeginResponseDto =
			interviewService.createInterview(interviewBeginRequestDto);
		//Then
		Assertions.assertNotNull(interviewBeginResponseDto.getSessionID());

		Assertions.assertTrue(interviewRepository.findById(interviewBeginResponseDto.getSessionID()).isPresent());
	}

	@Test
	public void getInterviewTest() {
		//When
		InterviewsResponseDto interviewsResponseDto = interviewService.getInterviews();
		//Then
		Assertions.assertNotNull(interviewsResponseDto.getInterviewSessions());

		Assertions.assertTrue(interviewsResponseDto.getInterviewSessions().isEmpty());
	}
}
