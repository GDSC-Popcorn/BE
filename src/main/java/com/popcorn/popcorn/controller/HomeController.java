package com.popcorn.popcorn.controller;

import com.popcorn.popcorn.domain.dto.HomeDto;
import com.popcorn.popcorn.domain.dto.PopupDetailDto;
import com.popcorn.popcorn.service.PopupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/popups")
public class HomeController {

    private final PopupService popupService;

    @GetMapping("/home")
    public ResponseEntity<List<HomeDto>> getAllPopups() {
        List<HomeDto> popups = popupService.getAllPopups();
        return ResponseEntity.ok(popups);
    }

    @GetMapping("/{popupId}")
    public ResponseEntity<PopupDetailDto>getPopupDetail(@PathVariable Long popupId,
                                                        @RequestParam(required = false) Long userId) { //userId는 선택적
        PopupDetailDto popupDetail = popupService.getPopupDetail(popupId, userId);
        return ResponseEntity.ok(popupDetail);
    }

    @PostMapping("/{popupId}/toggle-like")
    public ResponseEntity<String> toggleLike(@PathVariable Long popupId, @RequestParam Long userId) {
        boolean isLiked = popupService.toggleLike(userId, popupId);
        return ResponseEntity.ok(isLiked ? "Yes" : "No");
    }
}
