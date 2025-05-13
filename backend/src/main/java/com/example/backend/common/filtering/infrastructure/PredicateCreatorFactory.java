package com.example.backend.common.filtering.infrastructure;

import com.example.backend.common.filtering.ComparisonType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;

public class PredicateCreatorFactory {
    public static <TValue extends Comparable<? super TValue>> PredicateCreator<TValue> create(CriteriaBuilder builder, ComparisonType type){
        return switch (type){
            case Equals ->  (path, value) -> {
                if (value instanceof String){
                    return builder.equal(builder.lower((Path<String>) path), ((String) value).toLowerCase());
                }

                return builder.equal(path, value);
            };
            case GreaterThanOrEqual -> builder::greaterThanOrEqualTo;
            case LesserThanOrEqual -> builder::lessThanOrEqualTo;
            case Contains -> (path, value) -> {
                if (value instanceof String){
                    return builder.like(builder.lower((Path<String>) path), "%" + ((String) value).toLowerCase() + "%");
                }
                throw new IllegalArgumentException("Contains can only be applied to string value");
            };
        };
    }
}
