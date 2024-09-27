package potato.onetake.domain.Content.dao;

import potato.onetake.domain.Content.domain.QuestionCategory;

import java.util.List;
import java.util.Map;

public interface QuestionCategoryRepositoryCustom {
	List<QuestionCategory> findRandByCategoryIdList(Map<Long, Integer> categoryIdAndNumList);
}
