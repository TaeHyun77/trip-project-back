package com.example.demo0810.controller;

import com.example.demo0810.Entity.etc.Region;
import com.example.demo0810.repository.etc.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegionsController {

    // CRUD를 활용
    private final RegionRepository regionRepository;


    // 조회
    @GetMapping("/regions")
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }
}
