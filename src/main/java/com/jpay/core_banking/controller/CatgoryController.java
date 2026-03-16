package com.jpay.core_banking.controller;

import com.jpay.core_banking.dto.request.BudgetCreationRequest;
import com.jpay.core_banking.dto.request.CategoryCreationRequest;
import com.jpay.core_banking.dto.response.ApiResponse;
import com.jpay.core_banking.dto.response.CategoryResponse;
import com.jpay.core_banking.service.CategoryService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class CatgoryController {
    CategoryService categoryService;

    @PostMapping("")
    public ApiResponse<CategoryResponse> create(@RequestBody CategoryCreationRequest request){

        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(request))
                .build();
    }

    @PostMapping("/upsertBudget")
    public ApiResponse<CategoryResponse> upsertBudget(@RequestBody BudgetCreationRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.upsertBudget(request))
                .build();
    }
}
