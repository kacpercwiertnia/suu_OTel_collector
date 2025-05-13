package com.example.backend.domain.drone.filtering;

import com.example.backend.common.filtering.ComparisonType;
import com.example.backend.common.filtering.FilterType;
import com.example.backend.domain.drone.DroneEntity;
import com.example.backend.common.filtering.ComparisonTypeForFilterValidator;
import com.example.backend.common.filtering.infrastructure.PredicateCreator;
import com.example.backend.common.filtering.infrastructure.PredicateCreatorFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Root;

public class DroneTextFilter implements IDroneFilter {
    private static final FilterType FILTER_TYPE = FilterType.Text;
    private final String attributeName;
    private final String value;
    private final ComparisonType comparisonType;

    public DroneTextFilter(String attributeName, String value, ComparisonType comparisonType) {
        validateComparisionType(comparisonType);
        this.attributeName = attributeName;
        this.value = value;
        this.comparisonType = comparisonType;
    }

    //basically untestable
    @Override
    public Specification<DroneEntity> toSpecification() {
        return (Root<DroneEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            PredicateCreator<String> predicateCreator = PredicateCreatorFactory.create(builder, comparisonType);
            return predicateCreator.apply(root.get(attributeName), value);
        };
    }

    private void validateComparisionType(ComparisonType comparisonType) throws IllegalArgumentException{
        if(!ComparisonTypeForFilterValidator.isValid(comparisonType, FILTER_TYPE)){
            throw new IllegalArgumentException(comparisonType + "Comparision is not legal for " + FILTER_TYPE + " filter");
        }
    }
}
