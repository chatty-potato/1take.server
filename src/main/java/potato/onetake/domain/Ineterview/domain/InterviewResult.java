package potato.onetake.domain.Ineterview.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import potato.onetake.domain.Auth.domain.Profile;
import potato.onetake.global.BaseEntity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="interview_result")
public class InterviewResult extends BaseEntity {

	@ManyToOne()
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Profile profile;

	@ManyToOne()
	@JoinColumn(name = "qna_id", referencedColumnName = "id")
	private InterviewQna qna;

	@Column(name = "aiEvaluation")
	private String aiEvaluation;

	@Column(name = "accuracyCorrect")
	private Integer accuracyCorrect;

	@Column(name = "logicalCorrect")
	private Integer logicalCorrect;

	@Column(name = "technicalCorrect")
	private Integer technicalCorrect;

	public InterviewResult(Profile profile, InterviewQna qna) {
		this.profile = profile;
		this.qna = qna;
	}

	public InterviewResult(Profile profile, InterviewQna qna,
						   String aiEvaluation, Integer accuracyCorrect,
						   Integer logicalCorrect, Integer technicalCorrect) {
		this.profile = profile;
		this.qna = qna;
		this.aiEvaluation = aiEvaluation;
		this.accuracyCorrect = accuracyCorrect;
		this.logicalCorrect = logicalCorrect;
		this.technicalCorrect = technicalCorrect;
	}
}
