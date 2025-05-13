package com.example.backend.domain.drone.sorting;

import com.example.backend.common.sorting.OrderType;
import com.example.backend.domain.drone.DroneEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class DroneSort implements IDroneSort{
    private final String attributeName;
    private final OrderType orderType;

    public DroneSort(String attributeName, OrderType orderType) {
        this.attributeName = attributeName;
        this.orderType = orderType;
    }

    @Override
    public Specification<DroneEntity> toSpecification() {
        return (Root<DroneEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if(orderType.equals(OrderType.ASC)){
               query.orderBy(builder.asc(root.get(attributeName)));
            }
            else if(orderType.equals(OrderType.DESC)){
                query.orderBy(builder.desc(root.get(attributeName)));
            }

            return null;
        };
    }

}
