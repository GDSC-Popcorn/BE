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

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@AllArgsConstructor
@Service
@Transactional
public class PopupService {

    private final PopupRepository popupRepository;
    private final UserLikeRepository userLikeRepository;
    private final UserRepository userRepository;

    public final String imagePath = "var/app/images/pop_up"; //로컬 파일 경로
    public final String imageBaseUrl = "http://localhost:8080/images/popup"; //URL 기본 경로
    private final UserInterestRepository userInterestRepository;


    //홈 화면에 나타낼 팝업 목록 가져오기 메서드
    public List<HomeDto> getAllPopups(int page) {
        int pageSize = 20;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<PopupEntity> popupPage = popupRepository.findAllByOrderByEndedAtAsc(pageable);

        return popupPage.stream()
                .map(this::convertToHomeDTO)
                .collect(Collectors.toList());
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
    public List<HomeDto> getLikedPopups(Long userId) {
        List<LikeEntity> likedPopups = userLikeRepository.findAllByUserId(userId);
        return likedPopups.stream()
                .map(like -> convertToHomeDTO(like.getPopup()))
                .sorted(Comparator.comparing(HomeDto::getEndedAt))
                .collect(Collectors.toList());
    }
    //홈 화면에서의 찜 목록
    public List<HomeDto> getTopLikedPopups(Long userId) {
        List<LikeEntity> likedPopups = userLikeRepository.findAllByUserId(userId);
        return likedPopups.stream()
                .map(like -> convertToHomeDTO(like.getPopup()))
                .sorted(Comparator.comparing(HomeDto::getEndedAt))
                .limit(10)
                .collect(Collectors.toList());
    }

    //관심사
    public Map<String, List<HomeDto>> getInterestedPopups(Long userId) {
        List<InterestType> interests = userInterestRepository.findInterestsByUserId(userId);
        if (interests.isEmpty()) {
            return Map.of();
        }

        // 관심사별 팝업 분류
        Map<String, List<HomeDto>> categoryPopups = new HashMap<>();
        for (InterestType interest : interests) {
            List<PopupEntity> popups = popupRepository.findByCategories(interests);
            List<HomeDto> homeDtos = popups.stream()
                    .map(this::convertToHomeDTO)
                    .limit(10)
                    .collect(Collectors.toList());

            categoryPopups.put(interest.name(), homeDtos);
        }
        return categoryPopups;
    }

    //관심사 별 화면
    public List<HomeDto> getPopupsByCategory(InterestType category) {
        List<PopupEntity> popups = popupRepository.findByCategories(category);
        return popups.stream()
                .map(this::convertToHomeDTO)
                .collect(Collectors.toList());
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
                .build();
    }

    //이미지 URL 생성 메서드
    private String generateImageUrl(Long popupId) {
        return "https://localhost:8080/images/popup/" + popupId + "/01.jpg"; //대표 이미지
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
                .categories(popup.getCategories().stream().map(Enum::name).collect(Collectors.toList()))
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
        File popupDir = new File(imagePath + "/" + popupId);
        if(popupDir.exists() && popupDir.isDirectory()){
            File[] files = popupDir.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
            if(files != null){
                for(File file : files){
                    popupImageUrls.add(imageBaseUrl + "/" +popupId + "/"+ file.getName());
                }
            }
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
