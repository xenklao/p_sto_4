package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements AnswerService {

    private final AnswerDao answerDao;

    public AnswerServiceImpl(AnswerDao answerDao) {
        super(answerDao);
        this.answerDao = answerDao;
    }

    @Override
    public Optional<Answer> getAnswerWithAuthor(Long answerId) {
        return answerDao.getAnswerWithAuthor(answerId);
    }

    @Override
    public void deleteById(Long id) {
        answerDao.deleteById(id);
    }

    @Override
    public Optional<Long> getCountOfAnswerVoteByQuestionId(Long id) {
        return answerDao.getCountOfAnswerVoteByQuestionId(id);
    }

    @Override
    public Optional<Long> getCountOfAnswerToQuestionByQuestionId(Long id) {
        return answerDao.getCountOfAnswerToQuestionByQuestionId(id);
    }
}
