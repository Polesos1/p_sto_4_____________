package com.javamentor.qa.platform.dao.impl.pagination.chatdto;

import com.javamentor.qa.platform.dao.abstracts.dto.ChatDtoDao;
import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import com.javamentor.qa.platform.dao.abstracts.pagination.PageDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("ChatPageDtoDaoByUserIdImpl")
public class ChatPageDtoDaoByUserIdImpl implements PageDtoDao<ChatDto>{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ChatDto> getPaginationItems(PaginationData properties) {
        int itemsOnPage = properties.getItemsOnPage();
        int offset = (properties.getCurrentPage() - 1) * itemsOnPage;
        return entityManager
                .createQuery("SELECT new com.javamentor.qa.platform.models.dto.ChatDto(   " +
                        " a.id, a.user.id, (SELECT sum(r.count) FROM Reputation r where r.answer.user.id = a.user.id), " +
                        " a.question.id, a.htmlBody, a.persistDateTime, a.isHelpful," +
                        "(select distinct(CASE WHEN  a.user.id = v.user.id THEN true END) from VoteAnswer v " +
                        "where v.answer.id = a.id), " +
                        " a.dateAcceptTime, " +
                        "(select sum(case when v.vote = 'UP_VOTE' then 1 else -1 end) from VoteAnswer v where v.answer.id = a.id)," +
                        " a.user.imageLink, a.user.nickname) " +
                        " FROM Answer as a JOIN VoteAnswer vr ON vr.answer.id = a.id " +
                        " order by a.id", AnswerDTO.class)
                .setFirstResult(offset)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public Long getTotalResultCount(Map<String, Object> properties) {
        return (Long) entityManager.createQuery("select count(a.id) from Answer a")
                .getSingleResult();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
