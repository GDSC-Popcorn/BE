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
    public ResponseEntity<?> getAllPopups(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestParam(defaultValue = "1") int page) { //페이지 기본값 1
        Long userId = (userDetails != null) ? userDetails.getUserId() : null; //토큰 옵션
        List<HomeDto> popups = popupService.getAllPopups(page);
        List<HomeDto> topLikedPopups = popupService.getTopLikedPopups(userId);
        Map<String, List<HomeDto>> categoryPopups = popupService.getInterestedPopups(userId);
        List<HomeDto> recommendPopups = popupService.getRecommendedPopups();

        return ResponseEntity.ok(Map.of(
                "todayRecommend", recommendPopups,
                "allPopups" , popups,
                "topLikedPopups" , topLikedPopups,
                "categoryPopups", categoryPopups

        ));
    }

    @GetMapping("/{popupId}")
    public ResponseEntity<PopupDetailDto>getPopupDetail(@PathVariable Long popupId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = (userDetails != null) ? userDetails.getUserId() : null; //userId optional-인증되지 않으면 null
        PopupDetailDto popupDetail = popupService.getPopupDetail(popupId, userId);
        return ResponseEntity.ok(popupDetail);
    }

    @PostMapping("/{popupId}/toggle-like")
    public ResponseEntity<String> toggleLike(@PathVariable Long popupId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
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
