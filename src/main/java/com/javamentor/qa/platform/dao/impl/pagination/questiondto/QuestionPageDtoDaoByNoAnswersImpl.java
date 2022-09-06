package com.javamentor.qa.platform.dao.impl.pagination.questiondto;

import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.dao.impl.pagination.transformer.QuestionPageDtoResultTransformer;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("QuestionPageDtoDaoByNoAnswersImpl")
public class QuestionPageDtoDaoByNoAnswersImpl implements PageDtoDao<QuestionViewDto> {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuestionViewDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return (List<QuestionViewDto>) entityManager.createQuery(
                "SELECT DISTINCT" +
                        " q.id as question_id," +
                        " q.title," +
                        " q.user.id as author_id," +
                        " q.user.fullName," +
                        " q.user.imageLink," +
                        " q.description," +
                        " q.persistDateTime," +
                        " q.lastUpdateDateTime," +
                        " (coalesce((select sum(r.count) from Reputation r where r.author.id = q.user.id),0)) as author_reputation," +
                        " (coalesce((select count(a.id) from Answer a where a.question.id = q.id),0)) as answerCounter, " +
                        " (coalesce((select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteQuestion v where v.question.id = q.id), 0) ) as count_valuable," +
                        " ((select count(bm.id) from BookMarks bm where bm.question.id = q.id and bm.user.id = :userId) > 0) as is_user_bookmark, " +
                        " (coalesce((select count(qv.id) from QuestionViewed qv where qv.question.id = q.id), 0)) as view_count" +
                        " from Question q" +
                        " left JOIN q.tags t " +
                        " WHERE q.answers IS EMPTY AND ((:trackedTags) IS NULL OR t.id IN (:trackedTags)) " +
                        " AND ((:ignoredTags) IS NULL OR q.id not IN (select q.id from Question q join q.tags t where t.id in (:ignoredTags)))  " +
                        " and (:dateFilter = 0 or q.persistDateTime >= current_date - :dateFilter) "
                )
                .setParameter("trackedTags", properties.getProps().get("trackedTags"))
                .setParameter("ignoredTags", properties.getProps().get("ignoredTags"))
                .setParameter("dateFilter", properties.getProps().get("dateFilter"))
                .setParameter("userId", properties.getProps().get("userId"))
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionPageDtoResultTransformer())
                .list();
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select distinct count(distinct q.id) from Question q join q.tags t WHERE " +
                "q.answers IS EMPTY AND ((:trackedTags) IS NULL OR t.id IN (:trackedTags)) AND" +
                "((:ignoredTags) IS NULL OR q.id NOT IN (SELECT q.id FROM Question q JOIN q.tags t WHERE t.id IN (:ignoredTags)))")
                .setParameter("trackedTags", properties.get("trackedTags"))
                .setParameter("ignoredTags", properties.get("ignoredTags"))
                .getSingleResult();
    }
}
