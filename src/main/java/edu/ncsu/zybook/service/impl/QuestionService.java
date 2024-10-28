package edu.ncsu.zybook.service.impl;

import edu.ncsu.zybook.domain.model.Question;
import edu.ncsu.zybook.persistence.repository.QuestionRepository;
import edu.ncsu.zybook.service.IQuestionService;

import java.util.List;
import java.util.Optional;

public class QuestionService implements IQuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question create(Question question) {
        Optional<Question>result = questionRepository.findById(question.getQuestion_id(), question.getActivity_id(),question.getContent_id(),question.getSection_id(),question.getChapter_id(),question.getTbook_id());
        if(result.isEmpty()) {
            return questionRepository.create(question);
        }
        else{
            throw new RuntimeException("Question already exists!");
        }
    }

    @Override
    public Optional<Question> findById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Question> update(Question question) {
        return Optional.empty();
    }

    @Override
    public List<Question> findAll() {
        return List.of();
    }
}
