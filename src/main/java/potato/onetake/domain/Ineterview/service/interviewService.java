package potato.onetake.domain.Ineterview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import potato.onetake.domain.Content.dao.CategoryRepository;
import potato.onetake.domain.Content.domain.Category;
import potato.onetake.domain.Content.service.CategoryService;
import potato.onetake.domain.Ineterview.dao.InterviewCategoryRepository;
import potato.onetake.domain.Ineterview.dao.InterviewRepository;
import potato.onetake.domain.Ineterview.domain.Interview;
import potato.onetake.domain.Ineterview.domain.InterviewCategory;
import potato.onetake.domain.Ineterview.dto.InterviewBeginRequestDto;
import potato.onetake.domain.Member.dao.ProfileRepository;
import potato.onetake.domain.Member.domain.Profile;
import potato.onetake.domain.Member.service.ProfileService;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterviewService {
	final InterviewRepository interviewRepository;
	final ProfileRepository profileRepository; // 추후 profileService 로 수정 필요
	final CategoryService categoryService;
	final InterviewCategoryRepository interviewCategoryRepository;
	private final CategoryRepository categoryRepository;

	@Transactional
	public Interview createInterview(final InterviewBeginRequestDto interviewBeginRequestDto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = Long.parseLong(authentication.getName()); // 추후 시큐리티 완성 후 맞게 수정 필요

		final Profile profile = profileRepository.getReferenceById(userId);

		Interview interview = new Interview(profile, interviewBeginRequestDto.getTitle());

		interviewBeginRequestDto.getCategories().stream()
			.map(categoryName -> categoryRepository.findByContent(categoryName)
				.orElseThrow()) // exception 구현 필요
				.forEach(category -> createInterviewCategory(interview, category));

		return interview;
	}

	@Transactional
	public void createInterviewCategory(final Interview interview, final Category category) {
		InterviewCategory interviewCategory = new InterviewCategory(interview, category);
		interviewCategoryRepository.save(interviewCategory);
	}
}
