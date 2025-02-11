package com.popcorn.popcorn.controller;

import com.popcorn.popcorn.domain.InterestType;
import com.popcorn.popcorn.domain.dto.CustomUserDetails;
import com.popcorn.popcorn.domain.dto.HomeDto;
import com.popcorn.popcorn.domain.dto.PopupDetailDto;
import com.popcorn.popcorn.service.PopupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/popups")
public class HomeController {

    private final PopupService popupService;

    @GetMapping("/home")
    public ResponseEntity<?> getAllPopups(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<HomeDto> popups = popupService.getAllPopups();
        List<HomeDto> topLikedPopups = popupService.getTopLikedPopups(userId);
        Map<String, List<HomeDto>> categoryPopups = popupService.getInterestedPopups(userId);

        return ResponseEntity.ok(Map.of(
                "allPopups" , popups,
                "topLikedPopups" , topLikedPopups,
                "categoryPopups", categoryPopups
        ));
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

    @GetMapping("/likes")
    public ResponseEntity<List<HomeDto>> getLikesPopups(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<HomeDto> likedPopups = popupService.getLikedPopups(userId);
        return ResponseEntity.ok(likedPopups);
    }

    @GetMapping("/interests/{category}")
    public ResponseEntity<List<HomeDto>> getPopupsByCategory(@PathVariable String category) {
        InterestType interestType;
        try{
            interestType = InterestType.valueOf(category.toUpperCase());
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
        List<HomeDto> popups = popupService.getPopupsByCategory(interestType);
        return ResponseEntity.ok(popups);
    }
}
