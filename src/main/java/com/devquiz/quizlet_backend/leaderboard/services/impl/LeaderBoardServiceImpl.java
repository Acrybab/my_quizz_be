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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class LeaderBoardServiceImpl implements LeaderBoardService {
    private LeaderBoardRepository leaderBoardRepository;
    private UserRepository userRepository;
    private StudySetRepository studySetRepository;

    @Override
    public void updateScore(LeaderBoardRequest request) {
        // 1. Tìm bản ghi cũ hoặc khởi tạo mới nếu chưa tồn tại
        LeaderBoard leaderBoard = leaderBoardRepository
                .findByUserIdAndStudySetId(request.getUserId(), request.getStudySetId())
                .orElseGet(() -> {
                    LeaderBoard newEntry = new LeaderBoard();
                    // Thiết lập các quan hệ cơ bản cho bản ghi mới
                    newEntry.setUser(userRepository.getReferenceById(request.getUserId()));
                    newEntry.setStudySet(studySetRepository.getReferenceById(request.getStudySetId()));
                    newEntry.setBestTime(Double.MAX_VALUE); // Mặc định thời gian vô hạn
                    newEntry.setBestScore(0);
                    newEntry.setTotalAttempts(0);
                    return newEntry;
                });

        // 2. Logic cập nhật kỷ lục thời gian (Match Game)
        if (request.getBestTime() != null && request.getBestTime() < leaderBoard.getBestTime()) {
            leaderBoard.setBestTime(request.getBestTime());
        }

        // 3. Cập nhật các thông số khác
        leaderBoard.setBestScore(Math.max(leaderBoard.getBestScore(), request.getCurrentScore()));
        leaderBoard.setTotalAttempts(leaderBoard.getTotalAttempts() + 1); // Tự động tăng 1 lần chơi
        leaderBoard.setLastPlayedAt(request.getLastPlayedAt() != null ? request.getLastPlayedAt() : LocalDateTime.now());

        leaderBoardRepository.save(leaderBoard);
    }
}
