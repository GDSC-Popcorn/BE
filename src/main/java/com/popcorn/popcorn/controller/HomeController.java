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
import java.util.Objects;


@RequiredArgsConstructor
@RestController
@RequestMapping("/popups")
public class HomeController {

    private final PopupService popupService;

    @GetMapping("/home")
    public ResponseEntity<Map<String, Object>> getAllPopups(@RequestHeader(value = "Authorization", required = false)
                                                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestParam(defaultValue = "1") int page) { //페이지 기본값 1
        Long userId = (userDetails != null) ? userDetails.getUserId() : null; //토큰 옵션
        Map<String, Object> paginatedPopups = popupService.getAllPopups(page);
        List<HomeDto> topLikedPopups = popupService.getTopLikedPopups(userId);
        Map<InterestType, List<HomeDto>> interestedPopups = popupService.getInterestedPopups(userId);
        List<HomeDto> recommendPopups = popupService.getRecommendedPopups();

        return ResponseEntity.ok(Map.of(
                "todayRecommend", recommendPopups,
                "allPopups", paginatedPopups.get("popups"),
                "topLikedPopups", topLikedPopups,
                "categoryPopups", interestedPopups,
                "totalPages", paginatedPopups.get("totalPages"),
                "currentPages", paginatedPopups.get("currentPage")
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
    public ResponseEntity<?> getLikesPopups(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestParam(defaultValue = "1") int page) {
        Long userId = userDetails.getUserId();
        Map<String, Object> paginatedLikedPopups = popupService.getLikedPopups(userId, page);
        return ResponseEntity.ok(Map.of(
                "likedPopups", paginatedLikedPopups.get("popups"),
                "totalPages", paginatedLikedPopups.get("totalPages"),
                "currentPage", paginatedLikedPopups.get("currentPage")
        ));
    }

    @GetMapping("/interests/")
    public ResponseEntity<?> getInterestsPopups(@PathVariable String interest,
                                                 @RequestParam(defaultValue = "1") int page) {
        InterestType interestType;
        try{
            interestType = InterestType.valueOf(interest.toUpperCase());
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("잘못된 카테고리가 입력되었습니다 : " + interest);
        }
        Map<String, Object> popups = popupService.getPopupsByInterest(interestType, page);
        return ResponseEntity.ok(popups);
    }
}
