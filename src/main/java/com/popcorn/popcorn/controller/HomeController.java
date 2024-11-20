package com.popcorn.popcorn.controller;

import com.popcorn.popcorn.domain.dto.HomeDto;
import com.popcorn.popcorn.service.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/home")
public class HomeController {

    private final PopupService popupService;


    @GetMapping
    public List<HomeDto> getAllPopups(){
        return PopupService.getAllPopups()
                .stream()
                .map(popup -> new HomeDto(
                        popup.getTitle()
                ))
                .collect(Collectors.toList());
    }
}
