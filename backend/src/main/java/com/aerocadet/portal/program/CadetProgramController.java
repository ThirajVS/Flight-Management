package com.aerocadet.portal.program;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/programs")
public class CadetProgramController {

    private final CadetProgramService cadetProgramService;

    public CadetProgramController(CadetProgramService cadetProgramService) {
        this.cadetProgramService = cadetProgramService;
    }

    @GetMapping
    public ResponseEntity<ProgramPageResponse> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) ProgramStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        return ResponseEntity.ok(ProgramPageResponse.from(
                cadetProgramService.search(search, location, status, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(cadetProgramService.get(id));
    }
}

