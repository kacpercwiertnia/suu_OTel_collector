package com.example.backend.common.filtering.infrastructure;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SpecificationHelper {
    public static <TEntity> Specification<TEntity> combine(List<Specification<TEntity>> specifications){
        return specifications.stream().reduce(Specification::and).orElse(null);
    }
}
