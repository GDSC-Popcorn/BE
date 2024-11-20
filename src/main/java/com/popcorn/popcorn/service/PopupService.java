package com.popcorn.popcorn.service;

import com.popcorn.popcorn.domain.entity.PopupEntity;
import com.popcorn.popcorn.repository.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PopupService {

    private static PopupRepository popupRepository;

    public static List<PopupEntity> getAllPopups(){
        return popupRepository.findAll();
    }
}
