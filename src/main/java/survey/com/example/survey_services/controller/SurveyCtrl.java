package survey.com.example.survey_services.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import survey.com.example.survey_services.DTO.SurveyDTO;
import survey.com.example.survey_services.models.Survey;
import survey.com.example.survey_services.repositories.SurveyRepository;
import survey.com.example.survey_services.services.SurveyServices;

@RequestMapping("/api/survey")
@RestController
@RequiredArgsConstructor
public class SurveyCtrl {
    private final SurveyServices surveyServices;
    private final SurveyRepository surveyRepository;

    @PostMapping()
    public ResponseEntity<?> creatSurve(@Valid @RequestBody SurveyDTO surveyDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errMess = result.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();

                return ResponseEntity.badRequest().body(errMess);
            }

            surveyServices.createSurvey(surveyDTO);
            return ResponseEntity.ok("Create success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Create survey faile" + e);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllSurvey() {
        try {
            return ResponseEntity.ok(surveyServices.getAllSurvey());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSurveyWithId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(surveyServices.getSurveyWithId(id));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    // Get survey still exists but the user not joined
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllSurveyWithUserId(@PathVariable Long userId) {
        try {
            List<Survey> res = surveyServices.getAllWithUserId(userId);
           
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSurveyWithId(@PathVariable Long id, @Valid @RequestBody SurveyDTO updateSurveyDTO) {
        try {
            Boolean isSurvey = surveyRepository.existsById(id);
            if (!isSurvey) {
                return ResponseEntity.badRequest().body("SurVey id is not exist");
            }

            Survey isUpdate = surveyServices.updatSurveyWithId(id, updateSurveyDTO);
            if (isUpdate == null) {
                return ResponseEntity.badRequest().body("Update survey is faile");
            }
            return ResponseEntity.ok("Update Survey success with id:" + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update survey is faile");
        }
    }
}
