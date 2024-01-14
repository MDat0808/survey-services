package survey.com.example.survey_services.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import survey.com.example.survey_services.DTO.JoinSurveyDTO;
import survey.com.example.survey_services.models.JoinSurvey;
import survey.com.example.survey_services.models.Score;
import survey.com.example.survey_services.models.Survey;
import survey.com.example.survey_services.repositories.JoinSurveyRepository;
import survey.com.example.survey_services.repositories.ScoreRepository;
import survey.com.example.survey_services.repositories.SurveyRepository;

@RequiredArgsConstructor
@Service
public class JoinSurveyServices {
    private final JoinSurveyRepository joinSurveyRepository;
    private final ScoreRepository scoreRepository;
    private final SurveyRepository surveyRepository;

    public JoinSurvey createJoinSurveyandAddPoint(JoinSurveyDTO joinSurveyDTO) {
        Survey userCreateSurvey = surveyRepository.findById(joinSurveyDTO.getSurveyId()).orElse(null);
        if (userCreateSurvey.getUserId() == joinSurveyDTO.getUserID()) {
            return null;
        }
        JoinSurvey newJoinSurvey = JoinSurvey.builder().surveyId(joinSurveyDTO.getSurveyId())
                .userId(joinSurveyDTO.getUserID())
                .JoinedAt(LocalDate.now())
                .build();

        Score userJoin = scoreRepository.findByUserId(newJoinSurvey.getUserId());
        userJoin.setPoint(userJoin.getPoint() + 1); // add poitn when join survey

        scoreRepository.save(userJoin);
        return joinSurveyRepository.save(newJoinSurvey);
    }

    public List<JoinSurvey> getAllJoinSurvey() {
        try {
            return joinSurveyRepository.findAll();
        } catch (Exception e) {
            throw new EntityNotFoundException(e);

        }
    }

    public List<JoinSurvey> getAllJoinSurveyWithUserId(Long UserId) {
        try {
            return joinSurveyRepository.findByUserId(UserId);
        } catch (Exception e) {
            throw new EntityNotFoundException(e);

        }
    }

}
