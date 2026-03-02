package com.devquiz.quizlet_backend.leaderboard.services.impl;

import com.devquiz.quizlet_backend.leaderboard.dto.request.LeaderBoardRequest;
import com.devquiz.quizlet_backend.leaderboard.entities.LeaderBoard;
import com.devquiz.quizlet_backend.leaderboard.repository.LeaderBoardRepository;
import com.devquiz.quizlet_backend.leaderboard.services.LeaderBoardService;
import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.studySet.repository.StudySetRepository;
import com.devquiz.quizlet_backend.user.entity.User;
import com.devquiz.quizlet_backend.user.respository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class LeaderBoardServiceImpl  implements LeaderBoardService {
    private LeaderBoardRepository leaderBoardRepository;
    private UserRepository userRepository;
    private StudySetRepository studySetRepository;
//    @Override
//    public void updateScore(LeaderBoardRequest leaderBoardRequest) {
//        User user = userRepository.findById(leaderBoardRequest.getUserId()).orElseThrow(
//                () -> new RuntimeException("User not found with id: " + leaderBoardRequest.getUserId())
//        );
//        StudySet studySet = studySetRepository.findById(leaderBoardRequest.getStudySetId()).orElseThrow(
//                () -> new RuntimeException("StudySet not found with id: " + leaderBoardRequest.getStudySetId())
//        );
//
//        LeaderBoard leaderBoard = leaderBoardRepository.findByUserIdAndStudySetId(leaderBoardRequest.getUserId(), leaderBoardRequest.getStudySetId()).orElse(
//                new LeaderBoard(
//                        null,
//                         user,
//                        studySet,
//                        leaderBoardRequest.getCurrentTime(),
//                        leaderBoardRequest.getCurrentScore(),
//                        0
//
//                )
//        );



//    }
}
