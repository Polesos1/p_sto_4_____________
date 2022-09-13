package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.AnswerCommentDto;
import com.javamentor.qa.platform.models.dto.AnswerDTO;
import com.javamentor.qa.platform.models.dto.AnswerUserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public class AnswerDtoDaoImpl implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<AnswerDTO> getUndeletedAnswerDtoById(Long id) {

        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.AnswerDTO(" +
                        "a.id, " +
                        "a.user.id, " +
                        "(select coalesce(sum(r.count), 0) from Reputation r where r.author.id = a.user.id), " +
                        "a.question.id, " +
                        "a.htmlBody, " +
                        "a.persistDateTime, " +
                        "a.isHelpful, " +
                        "(select distinct(CASE WHEN  a.user.id = v.user.id THEN true else false END) from VoteAnswer v " +
                        "   where v.answer.id = a.id), " +
                        "a.dateAcceptTime, " +
                        "(select coalesce(sum(case when v.vote = 'UP_VOTE' then 1 else -1 end), 0) from VoteAnswer v where v.answer.id = a.id), " +
                        "a.user.imageLink, " +
                        "a.user.nickname) " +
                        "from Answer as a " +
                               "where a.id = :id and a.isDeleted = false ", AnswerDTO.class)
                .setParameter("id", id));
    }


    @Override
    public List<AnswerDTO> getAllUndeletedAnswerDtoByQuestionId(Long questionId) {

        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.AnswerDTO( " +
                        "a.id," +
                        " a.user.id, " +
                        "(select sum(r.count) from Reputation r where r.author.id = a.user.id), " +
                        "a.question.id," +
                        " a.htmlBody," +
                        " a.persistDateTime," +
                        " a.isHelpful," +
                        "(select distinct(CASE WHEN  a.user.id = v.user.id THEN true END) from VoteAnswer v "  +
                        "where v.answer.id = a.id), " +
                        "a.dateAcceptTime, " +
                        "(select coalesce(sum(case when v.vote = 'UP_VOTE' then 1 else -1 end), 0) from VoteAnswer v " +
                        "where v.answer.id = a.id) as countVotes," +
                        " a.user.imageLink," +
                        " a.user.nickname) " +
                        "from Answer as a "  +
                        "where a.question.id = :id and a.isDeleted = false order by a.isHelpful desc,countVotes desc ", AnswerDTO.class)
                .setParameter("id", questionId)
                .getResultList();
    }


    @Override
    public List<AnswerCommentDto> getAllCommentsDtoByAnswerId(Long answerId) {
        return entityManager.createQuery(
                        "SELECT NEW com.javamentor.qa.platform.models.dto.AnswerCommentDto( " +
                                "c.comment.id, " +
                                "c.answer.id, " +
                                "c.comment.lastUpdateDateTime, " +
                                "c.comment.persistDateTime, " +
                                "c.comment.text, " +
                                "c.comment.user.id, " +
                                "c.comment.user.imageLink, " +
                                "(select sum(r.count) from Reputation r where r.author.id=c.comment.user.id)) " +
                                "FROM CommentAnswer c where c.answer.id=:id ", AnswerCommentDto.class)
                .setParameter("id", answerId)
                .getResultList();

    }


    @Override
    public List<AnswerUserDto> getLastAnswersForWeek(Long userId) {
        return entityManager.createQuery(
                        "SELECT NEW com.javamentor.qa.platform.models.dto.AnswerUserDto(" +
                                "a.id," +
                                "a.question.id," +
                                "(select coalesce(sum(case when v.vote = 'UP_VOTE' then 1 else -1 end), 0) " +
                                "from VoteAnswer v where v.answer.id = a.id) as countVotes," +
                                "a.persistDateTime," +
                                "a.htmlBody)" +
                                "FROM Answer AS a WHERE a.user.id=:id AND a.persistDateTime >= :date",
                        AnswerUserDto.class)
                .setParameter("date", LocalDateTime.now().minusDays(7))
                .setParameter("id",userId)
                .getResultList();
    }
}
