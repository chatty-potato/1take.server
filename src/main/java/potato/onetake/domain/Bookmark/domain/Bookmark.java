package potato.onetake.domain.Bookmark.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import potato.onetake.domain.Member.domain.Profile;
import potato.onetake.domain.Interview.domain.InterviewQna;
import potato.onetake.global.BaseEntity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bookmark")
public class Bookmark extends BaseEntity {

	@ManyToOne()
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Profile profile;

	@ManyToOne()
	@JoinColumn(name = "qna_id", referencedColumnName = "id")
	private InterviewQna qna;

	public Bookmark(final Profile profile, final InterviewQna qna){
		this.profile = profile;
		this.qna = qna;
	}
}
