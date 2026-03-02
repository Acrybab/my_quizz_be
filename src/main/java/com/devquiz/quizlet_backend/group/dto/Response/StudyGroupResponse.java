package com.devquiz.quizlet_backend.group.dto.Response;

import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroupResponse {
    private Long id;
    private String studyGroupName;
    private String adminName;
//    private int countSets;
    // Nếu bạn muốn trả về danh sách tên các bộ thẻ luôn
    private List<StudySet> studySets;
}
