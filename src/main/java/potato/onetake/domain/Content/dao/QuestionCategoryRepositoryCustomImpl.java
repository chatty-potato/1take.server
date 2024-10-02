package potato.onetake.domain.Content.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import potato.onetake.domain.Content.domain.QuestionCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionCategoryRepositoryCustomImpl implements QuestionCategoryRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	@SuppressWarnings("unchecked")
	public List<QuestionCategory> findRandByCategoryIdList(Map<Long, Integer> categoryIdAndNumList) {
		StringBuilder sb = new StringBuilder();

		boolean first = true;
		for (Map.Entry<Long, Integer> entry : categoryIdAndNumList.entrySet()) {
			if (!first) {
				sb.append(" UNION ALL ");
			}
			sb.append("(select * from question_category WHERE category_id =")
				.append(entry.getKey())
				.append(" ORDER BY RANDOM() LIMIT ")
				.append(entry.getValue())
				.append(")");
			first = false;
		}

		Query query = em.createNativeQuery(sb.toString(), QuestionCategory.class);

		return (List<QuestionCategory>) query.getResultList();
	}
}
