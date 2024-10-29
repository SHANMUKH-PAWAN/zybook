package edu.ncsu.zybook.web.controller;

import edu.ncsu.zybook.DTO.AnswerDTO;
import edu.ncsu.zybook.DTO.QuestionDTO;
import edu.ncsu.zybook.domain.model.Answer;
import edu.ncsu.zybook.domain.model.Question;
import edu.ncsu.zybook.service.IAnswerService;
import edu.ncsu.zybook.service.IQuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/question")
public class QuestionController {
    IQuestionService questionService;
    IAnswerService answerService;

    public QuestionController(IQuestionService questionService, IAnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }
    @PostMapping()
    public String createQuestion(@ModelAttribute QuestionDTO questionDTO) {
        System.out.println("Entered create question post!!");
        Question question = new Question();
        question.setQuestion(questionDTO.getQuestion());
        question.setActivity_id(questionDTO.getActivityId());
        question.setContent_id(questionDTO.getContentId());
        question.setSection_id(questionDTO.getSectionId());
        question.setChapter_id(questionDTO.getChapterId());
        question.setTbook_id(questionDTO.getTbookId());
        question.setIsHidden(questionDTO.getIsHidden());
        question.setAnswer_id(questionDTO.getAnswerId());
        questionService.create(question);

        Answer answer = new Answer();
        for(AnswerDTO answerDTO: questionDTO.getAnswers()){
            answer.setAnswerId(answerDTO.getAnswerId());
            answer.setQuestionId(questionDTO.getQuestionId());
            answer.setActivityId(questionDTO.getActivityId());
            answer.setContentId(questionDTO.getContentId());
            answer.setSectionId(questionDTO.getSectionId());
            answer.setSectionId(questionDTO.getSectionId());
            answer.setChapId(questionDTO.getChapterId());
            answer.setTbookId(questionDTO.getTbookId());
            answer.setAnswerText(answerDTO.getAnswerText());
            answer.setJustification(answerDTO.getJustification());
            answerService.create(answer);
        }
        return "redirect:activity"; //redirected to activity: getAllActivities view
    }
    @GetMapping("/new")
    public String showCreateQuestionForm(@RequestParam("tbookId") int tbookId,
                                         @RequestParam("chapId") int chapId,
                                        @RequestParam("sectionId") int sectionId,
                                        @RequestParam("contentId") int contentId,
                                        @RequestParam("activityId") int activityId,
                                        Model model) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setTbookId(tbookId);
        questionDTO.setChapterId(chapId);
        questionDTO.setSectionId(sectionId);
        questionDTO.setContentId(contentId);
        questionDTO.setActivityId(activityId);
        questionDTO.setAnswers(new AnswerDTO[4]);
        model.addAttribute("question", questionDTO);
        return "question/create";
    }
    @PostMapping("/edit")
    public String updateQuestion(@ModelAttribute QuestionDTO questionDTO) {
        System.out.println("Entered update question!!");
        Question question = new Question();
        question.setQuestion(questionDTO.getQuestion());
        question.setActivity_id(questionDTO.getActivityId());
        question.setContent_id(questionDTO.getContentId());
        question.setSection_id(questionDTO.getSectionId());
        question.setChapter_id(questionDTO.getChapterId());
        question.setTbook_id(questionDTO.getTbookId());
        question.setIsHidden(questionDTO.getIsHidden());
        question.setAnswer_id(questionDTO.getAnswerId());
        questionService.update(question);

        Answer answer = new Answer();
        for(AnswerDTO answerDTO: questionDTO.getAnswers()){
            answer.setAnswerId(answerDTO.getAnswerId());
            answer.setQuestionId(questionDTO.getQuestionId());
            answer.setActivityId(questionDTO.getActivityId());
            answer.setContentId(questionDTO.getContentId());
            answer.setSectionId(questionDTO.getSectionId());
            answer.setSectionId(questionDTO.getSectionId());
            answer.setChapId(questionDTO.getChapterId());
            answer.setTbookId(questionDTO.getTbookId());
            answer.setAnswerText(answerDTO.getAnswerText());
            answer.setJustification(answerDTO.getJustification());
            answerService.update(answer);
        }
        return "redirect:activity";
    }
    @GetMapping("/delete")
    public String deleteQuestion(@RequestParam("questionId") int questionId,
                                 @RequestParam("activityId") int activityId,
                                 @RequestParam("contentId") int contentId,
                                 @RequestParam("sectionId") int sectionId,
                                 @RequestParam("chapterId") int chapterId,
                                 @RequestParam("tbookId") int tbookId,
                                 @ModelAttribute QuestionDTO questionDTO) {
        System.out.println("Entered delete question!!");
        questionService.delete(questionId,activityId,contentId,sectionId,chapterId,tbookId);
        List<Answer> answers = answerService.findAllByQuestion(questionId,activityId,contentId,sectionId,chapterId,tbookId);
        for(Answer answer: answers){
            answerService.delete(answer.getAnswerId(), answer.getQuestionId(),answer.getActivityId(),answer.getContentId(),answer.getSectionId(),answer.getChapId(),answer.getTbookId());
        }
        return "redirect:activity";
    }
}
