package com.gamebuddy.user.controller;

import com.gamebuddy.user.model.LanguagePreference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/languages")
public class LanguageController {

    @GetMapping("/languages")
    public List<String> getLanguages() {
        return Arrays.stream(LanguagePreference.values())
                .map(LanguagePreference::name)
                .collect(Collectors.toList());
    }
}

