package potato.onetake.domain.Ineterview;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import potato.onetake.domain.Member.domain.Profile;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="interview")
@EntityListeners(AuditingEntityListener.class)
public class Interview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Profile profile;

	@Column(name = "title", length = 30, nullable = false)
	private String title;

	@Column(name = "done", nullable = false)
	private boolean done;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "deleted_at")
	private Date deletedAt;

	public Interview(Profile profile, String title) {
		this.profile = profile;
		this.title = title;
		this.done = false;
	}

	public Interview(Profile profile, String title, boolean done) {
		this.profile = profile;
		this.title = title;
		this.done = done;
	}

	public Interview(Profile profile, String title, Date createdAt, Date updatedAt) {
		this.profile = profile;
		this.title = title;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.done = false;
	}
}
