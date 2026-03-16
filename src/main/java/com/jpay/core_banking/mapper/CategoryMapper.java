package com.jpay.core_banking.mapper;

import com.jpay.core_banking.dto.response.CategoryResponse;
import com.jpay.core_banking.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// componentModel = "spring" -> Giúp Spring hiểu Mapper này là một Bean (có thể @Autowired được)
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    //@Mapping(source = "categoryName", target = "categoryName")
    CategoryResponse toCategoryResponse(Category category);
}
