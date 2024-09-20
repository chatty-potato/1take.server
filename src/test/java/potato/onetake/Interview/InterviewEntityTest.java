package potato.onetake.Interview;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import potato.onetake.domain.Auth.domain.Profile;
import potato.onetake.domain.Ineterview.domain.Interview;
import potato.onetake.domain.Ineterview.dao.InterviewRepository;

import static org.junit.jupiter.api.Assertions.*; // Assertions import
import static org.mockito.Mockito.*; // Mockito methods import

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InterviewEntityTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private InterviewRepository interviewRepository;

	@MockBean
	private Profile mockProfile;

	@Test
	public void InterviewEntityUnitTest() {
		// Given: Mock UserProfile 설정
		when(mockProfile.getId()).thenReturn(1L);

		// Given
		Interview interview = new Interview(mockProfile, "interview title");
		entityManager.persist(interview);

		System.out.printf("interview title: %s\n", interview.getTitle());
		System.out.printf("interview id: %s\n", interview.getId());

		Interview newInterview = interviewRepository.findById(interview.getId()).get();
		assertEquals(interview.getTitle(), newInterview.getTitle());
	}

}
