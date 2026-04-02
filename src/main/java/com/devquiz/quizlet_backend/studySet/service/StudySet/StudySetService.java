package com.devquiz.quizlet_backend.studySet.service.StudySet;

import com.devquiz.quizlet_backend.card.dto.response.CardResponse;
import com.devquiz.quizlet_backend.learn.dto.request.LearningRequest;
import com.devquiz.quizlet_backend.learn.dto.response.MatchQuizzResponse;
import com.devquiz.quizlet_backend.learn.dto.response.QuizQuestionResponse;
import com.devquiz.quizlet_backend.learn.dto.response.QuizzDataReponse;
import com.devquiz.quizlet_backend.studySet.dto.request.SavedStudySetRequest;
import com.devquiz.quizlet_backend.studySet.dto.request.StudySetRequest;
import com.devquiz.quizlet_backend.studySet.dto.request.TestSubmissionDto;
import com.devquiz.quizlet_backend.studySet.dto.response.StudySetResponse;
import com.devquiz.quizlet_backend.studySet.dto.response.TestQuestionDto;
import com.devquiz.quizlet_backend.studySet.dto.response.TestType;
import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.studySet.entity.TestResult;
import com.devquiz.quizlet_backend.user.entity.User;

import java.util.List;

public interface StudySetService {
StudySetResponse createStudySet(StudySetRequest studySetRequest);
List<StudySetResponse> getAllStudySets(String userEmail);
StudySetResponse getStudySetById(Long id);
StudySetResponse updateStudySet(Long id, StudySetRequest studySetRequest);
void deleteStudySet(Long id);
String toogleSaveSet(SavedStudySetRequest savedStudySetRequest, Long studySetId);
List<StudySetResponse> getMySavedSets(String userEmail);
List<CardResponse> getCardsWithStatus(String userEmail, Long cardId);
QuizzDataReponse generateQuiz(Long studySetId , Long userId);
List<CardResponse> getMasteredCards(String userEmail);
List<MatchQuizzResponse> generateMatchQuiz(Long studySetId, Long userId);
List <TestQuestionDto> generateTest(Long studySetId, int limit , TestType testType);
TestResult submitTest(TestSubmissionDto testSubmissionDto, User user);
List<TestResult> getTestResults(Long studySetId , String userEmail);
}
