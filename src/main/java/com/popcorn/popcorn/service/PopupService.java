package com.popcorn.popcorn.service;

import com.popcorn.popcorn.domain.InterestType;
import com.popcorn.popcorn.domain.dto.HomeDto;
import com.popcorn.popcorn.domain.dto.PopupDetailDto;
import com.popcorn.popcorn.domain.entity.LikeEntity;
import com.popcorn.popcorn.domain.entity.PopupEntity;
import com.popcorn.popcorn.domain.entity.UserEntity;
import com.popcorn.popcorn.repository.PopupRepository;
import com.popcorn.popcorn.repository.UserInterestRepository;
import com.popcorn.popcorn.repository.UserLikeRepository;
import com.popcorn.popcorn.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class PopupService {

    private final PopupRepository popupRepository;
    private final UserLikeRepository userLikeRepository;
    private final UserRepository userRepository;

    public final String imageBaseUrl = "http://localhost:8080/images"; //URL 기본 경로
    private final UserInterestRepository userInterestRepository;


    //홈 화면에 나타낼 팝업 목록 가져오기 메서드
    public Map<String, Object> getAllPopups(int page) {
        int pageSize = 20;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<PopupEntity> popupPage = popupRepository.findAllByOrderByEndedAtAsc(pageable);

        List<HomeDto> popups = popupPage.stream()
                .map(this::convertToHomeDTO)
                .collect(Collectors.toList());

        Map<String,Object> response = new HashMap<>();
        response.put("popups",popups);
        response.put("currentPage",page);
        response.put("totalPages",popupPage.getTotalPages());

        return response;
    }

    //랜덤 추천 팝업
    public List<HomeDto> getRecommendedPopups() {
        List<PopupEntity> popups = popupRepository.findAll();

        if(popups.size() <= 5){ // 이미 5개이하면 그대로 반환
            return popups.stream()
                    .map(this::convertToHomeDTO)
                    .collect(Collectors.toList());
        }

        Collections.shuffle(popups); //팝업 리스트 섞기
        return popups.stream() //앞에서 5개 선택 후 반환
                .limit(5)
                .map(this::convertToHomeDTO)
                .collect(Collectors.toList());
    }

    //찜 화면
    public Map<String, Object> getLikedPopups(Long userId, int page) {
        int pageSize = 10; // 한 페이지당 10개씩 반환
        Pageable pageable = PageRequest.of(page - 1, pageSize); // JPA는 0부터 시작하므로 page - 1

        List<LikeEntity> likedPopupsPage = userLikeRepository.findAllByUserId(userId, pageable);

        List<HomeDto> likedPopups = likedPopupsPage.stream()
                .map(like -> convertToHomeDTO(like.getPopup()))
                .toList();

        return Map.of(
                "popups", likedPopups,
                "totalPages", likedPopupsPage,
                "currentPage", page
        );
    }
    //홈 화면에서의 찜 목록
    public List<HomeDto> getTopLikedPopups(Long userId) {
        List<LikeEntity> likedPopups = userLikeRepository.findTop10ByUserIdOrderByIdDesc(userId);
        return likedPopups.stream()
                .map(like -> convertToHomeDTO(like.getPopup()))
                .sorted(Comparator.comparing(HomeDto::getEndedAt))
                .limit(10)
                .collect(Collectors.toList());
    }

    //관심사
    public Map<InterestType, List<HomeDto>> getInterestedPopups(Long userId) {
        List<InterestType> interests = userInterestRepository.findInterestsByUserId(userId);
        if (interests.isEmpty()) {
            return Map.of();
        }
        Pageable pageable = PageRequest.of(0, 10); //카테고리 별 최대 10개 표시
        List<PopupEntity> popups = popupRepository.findByInterestIn(interests, pageable);

        // 관심사별 팝업 분류 DTO 변환
        return popups.stream()
                .map(this::convertToHomeDTO)
                .collect(Collectors.groupingBy(HomeDto::getInterest));
    }

    //관심사 별 화면
    public Map<String, Object> getPopupsByInterest(InterestType interest, int page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<PopupEntity> interestedPopupsPage = popupRepository.findByInterest(interest, pageable);

        List<HomeDto> interestedPopups = interestedPopupsPage.stream()
                .map(this::convertToHomeDTO)
                .collect(Collectors.toList());

        return Map.of(
                "popups", interestedPopups,
                "totalPages", interestedPopupsPage.getTotalPages(),
                "currentPage", page
        );
    }

    private HomeDto convertToHomeDTO(PopupEntity popup) {
        
        String popupImgUrl = generateImageUrl(popup.getId());

        // DTO 반환
        return HomeDto.builder()
                .popupId(popup.getId())
                .title(popup.getTitle())
                .popupImage(popupImgUrl)
                .startedAt(popup.getStartedAt())
                .endedAt(popup.getEndedAt())
                .location(popup.getLocation())
                .interest(popup.getInterest())
                .build();
    }

    //이미지 URL 생성 메서드
    private String generateImageUrl(Long popupId) {
        return "http://localhost:8080/images/" + popupId + "/01.jpg"; //대표 이미지
    }

    //팝업 상세 정보 메서드
    public PopupDetailDto getPopupDetail(Long popupId,Long userId) {
        //팝업 데이터베이스 조회
        PopupEntity popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new RuntimeException("Popup not found ID: " + popupId));

        //이미지 목록
        List<String> popupImages = getPopupImages(popupId);

        //예약 URL 유무
        String reservationUrl = (popup.getReservationUrl() != null && !popup.getReservationUrl().isEmpty())
                ? popup.getReservationUrl() : null;

        //찜 여부 확인
        boolean isLiked = false;
        if(userId != null){ //userId가 있는 경우에만 찜 여부 확인
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(()->new RuntimeException("User not found ID:" + userId));
            isLiked = userLikeRepository.existsByUserIdAndPopupId(userId,popupId);
        }

        return PopupDetailDto.builder()
                .popupId(popup.getId())
                .title(popup.getTitle())
                .interest(popup.getInterest())
                .popupImage(popupImages)
                .location(popup.getLocation())
                .startedAt(popup.getStartedAt())
                .endedAt(popup.getEndedAt())
                .organizerUrl(popup.getOrganizerUrl())
                .contents(popup.getContents())
                .hours(popup.getHours())
                .reservationUrl(reservationUrl)
                .isLiked(isLiked)
                .build();
    }

    //팝업 ID의 이미지 URL 리스트 생성
    private List<String> getPopupImages(Long popupId) {
        List<String> popupImageUrls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String filename = String.format("%02d.jpg",i);
            popupImageUrls.add(imageBaseUrl + "/" + popupId + "/" + filename);
        }
        return popupImageUrls;
    }

    //찜 상태 변경
    public boolean toggleLike(Long popupId,Long userId) {
        UserEntity user = userRepository.findById(userId) //user 조회
                .orElseThrow(()->new RuntimeException("User not found ID:" + userId));

        PopupEntity popup = popupRepository.findById(popupId) //popup 조회
                .orElseThrow(()->new RuntimeException("Popup not found ID:" + popupId));

        //현재 찜 여부 확인
        if(userLikeRepository.existsByUserIdAndPopupId(userId,popupId)){ //이미 찜한 경우 찜 삭제
            LikeEntity like = userLikeRepository.findById(userId)
                    .orElseThrow(()->new RuntimeException("Like not found"));
            userLikeRepository.delete(like);
            return false; //unliked 반환
        } else{ //찜한 상태가 아닌 경우 찜 추가
            LikeEntity like = new LikeEntity(user,popup);
            userLikeRepository.save(like);
            return true; //liked 반환
        }

    }


}
