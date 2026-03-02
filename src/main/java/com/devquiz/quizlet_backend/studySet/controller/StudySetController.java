package com.devquiz.quizlet_backend.studySet.controller;

import com.devquiz.quizlet_backend.card.dto.response.CardResponse;
import com.devquiz.quizlet_backend.card.entity.Card;
import com.devquiz.quizlet_backend.learn.dto.request.LearningRequest;
import com.devquiz.quizlet_backend.learn.dto.response.MatchQuizzResponse;
import com.devquiz.quizlet_backend.learn.dto.response.QuizQuestionResponse;
import com.devquiz.quizlet_backend.learn.dto.response.QuizzDataReponse;
import com.devquiz.quizlet_backend.studySet.dto.request.StudySetRequest;

import com.devquiz.quizlet_backend.studySet.dto.response.StudySetResponse;
import com.devquiz.quizlet_backend.studySet.service.AIService.AIService;
import com.devquiz.quizlet_backend.studySet.service.StudySet.StudySetService;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import com.devquiz.quizlet_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/study-sets")
@RequiredArgsConstructor
public class StudySetController {
    private final StudySetService studySetService;
    private final AIService aiService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/create")
    public ResponseEntity<ApiResponse<StudySetResponse>> create(@ModelAttribute StudySetRequest request) {
        StudySetResponse data = studySetService.createStudySet(request);

        // Bọc dữ liệu vào ApiResponse
        ApiResponse<StudySetResponse> response = new ApiResponse<>();
        response.setData(data);

        return ResponseEntity.ok(response);
    }
//    @GetMapping("/status-cards/{userId}/{cardId}")
//    public ResponseEntity<ApiResponse<List<CardResponse>>> getCardsWithStatus(@PathVariable("userId") Long userId, @PathVariable("cardId") Long cardId) {
//        List<CardResponse> data = studySetService.getCardsWithStatus(
//                    userId, cardId
//        ); // Thay null bằng LearningRequest nếu cần
//
//        ApiResponse<List<CardResponse>> response = new ApiResponse<>();
//        response.setData(data);
//
//        return ResponseEntity.ok(
//                ApiResponse.<List<CardResponse>>builder()
//                        .code(200)
//                        .message("Lấy danh sách Study Sets thành công")
//                        .data(studySetService.getCardsWithStatus(userId, cardId))
//                        .build()
//        );
//    }

    @PostMapping("/generate-description")
    public ResponseEntity<?> generateDescription(@RequestBody Map <String, String> body) {
        String title = body.get("title");
        String description = aiService.generateDescription(title);
        return ResponseEntity.ok(Map.of("description", description));
    }


     @GetMapping("")
    public ResponseEntity<ApiResponse<List<StudySetResponse>>> getAllStudySets() {
         ApiResponse<List<StudySetResponse>> response = ApiResponse.<List<StudySetResponse>>builder()
                 .code(200)
                 .message("Lấy danh sách Study Sets thành công")
                 .data(studySetService.getAllStudySets())
                 .build();
            return ResponseEntity.ok(response);
     }

     @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudySetResponse>> getStudySetById(@PathVariable Long id) {
         return ResponseEntity.ok(
                 ApiResponse.<StudySetResponse>builder()
                         .code(200)
                         .message("Lấy Study Set thành công")
                         .data(studySetService.getStudySetById(id))
                         .build()
         );
     }
     @PutMapping ("/{id}")
    public StudySetResponse updateStudySet(@PathVariable Long id, @RequestBody StudySetRequest studySetRequest) {
         return studySetService.updateStudySet(id, studySetRequest);
     }

     @DeleteMapping("/{id}")
    public void deleteStudySet(@PathVariable Long id) {
         studySetService.deleteStudySet(id);
     }

    @GetMapping("/saved")
    public ResponseEntity<?> getSavedStudySets(Principal principal) {
        return ResponseEntity.ok(studySetService.getMySavedSets(principal.getName()));
    }

    @GetMapping("/status-cards/{cardId}")
    public ResponseEntity<ApiResponse<List<CardResponse>>> getCardsWithStatus(@PathVariable("cardId") Long cardId, Principal principal) {
        List<CardResponse> data = studySetService.getCardsWithStatus(
                principal.getName(), cardId
        ); // Thay null bằng LearningRequest nếu cần

        ApiResponse<List<CardResponse>> response = ApiResponse.<List<CardResponse>>builder()
                .code(200)
                .message("Lấy danh sách Study Sets thành công")
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/mastered-cards")
    public ResponseEntity<ApiResponse<List<CardResponse>>> getMasteredCards(Principal principal) {
        List<CardResponse> data = studySetService.getMasteredCards(principal.getName());

        ApiResponse<List<CardResponse>> response = ApiResponse.<List<CardResponse>>builder()
                .code(200)
                .message("Lấy danh sách thẻ đã thành thạo thành công")
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }


     @PostMapping("/{studySetId}/toggle-save")
        public ResponseEntity<ApiResponse<String>> toggleSaveStudySet(
                @PathVariable Long studySetId,
                @RequestBody com.devquiz.quizlet_backend.studySet.dto.request.SavedStudySetRequest savedStudySetRequest) {
            String resultMessage = studySetService.toogleSaveSet(savedStudySetRequest, studySetId);

            ApiResponse<String> response = ApiResponse.<String>builder()
                    .code(200)
                    .message(resultMessage)
                    .data(resultMessage)
                    .build();

            return ResponseEntity.ok(response);
        }

    @GetMapping("/{id}/generate-quiz")
    public ResponseEntity<ApiResponse<QuizzDataReponse>> generateQuiz(
            @PathVariable Long id,
            Authentication authentication) {

        // Giả sử bạn lưu UserId trong CustomUserDetails hoặc lấy qua Email
        // Ở đây mình ví dụ lấy từ Principal (tùy cách bạn config Security)
        Long userId = ((User) authentication.getPrincipal()).getUserId();

        QuizzDataReponse quizData = studySetService.generateQuiz(id, userId);

        return ResponseEntity.ok(
                ApiResponse.<QuizzDataReponse>builder()
                        .code(200)
                        .message("Tạo Quiz thành công")
                        .data(quizData)
                        .build()
        );
    }
    @GetMapping("/{id}/generate-match-quiz")
    public ResponseEntity<ApiResponse<List<MatchQuizzResponse>>> generateMatchQuiz(
            @PathVariable Long id,
            Authentication authentication) {

        // Giả sử bạn lưu UserId trong CustomUserDetails hoặc lấy qua Email
        // Ở đây mình ví dụ lấy từ Principal (tùy cách bạn config Security)
        Long userId = ((User) authentication.getPrincipal()).getUserId();

        List<MatchQuizzResponse> quizData = studySetService.generateMatchQuiz(id, userId);

        return ResponseEntity.ok(
                ApiResponse.<List<MatchQuizzResponse>>builder()
                        .code(200)
                        .message("Tạo Match Quiz thành công")
                        .data(quizData)
                        .build()
        );
     }


}
