package com.example.backend.domain.drone.filtering;

import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.common.filtering.FilterType;
import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.common.filtering.ComparisonTypeForFilterValidator;
import com.example.backend.common.filtering.infrastructure.PredicateCreator;
import com.example.backend.common.filtering.infrastructure.PredicateCreatorFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Root;

public class DroneNumberFilter implements IDroneFilter {
    private static final FilterType FILTER_TYPE = FilterType.Number;
    private final String attributeName;
    private final double value;
    private final ComparisonType comparisonType;

    public DroneNumberFilter(String attributeName, double value, ComparisonType comparisonType) {
        validateComparisionType(comparisonType);
        this.attributeName = attributeName;
        this.value = value;
        this.comparisonType = comparisonType;
    }

    //WARNING
    //There is silent agreement that number fields are applied ONLY to flying drones
    @Override
    public Specification<DroneEntity> toSpecification() {
        return (Root<DroneEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Predicate isAirbornePredicate = builder.isTrue(root.get("isAirborne"));

            PredicateCreator<Double> predicateCreator = PredicateCreatorFactory.create(builder, comparisonType);

            return builder.and(
                    isAirbornePredicate,
                    predicateCreator.apply(root.get(attributeName), value)
            );
        };
    }

    private void validateComparisionType(ComparisonType comparisonType) throws IllegalArgumentException{
        if(!ComparisonTypeForFilterValidator.isValid(comparisonType, FILTER_TYPE)){
            throw new IllegalArgumentException(comparisonType + "Comparision is not legal for " + FILTER_TYPE + " filter");
        }
    }
}