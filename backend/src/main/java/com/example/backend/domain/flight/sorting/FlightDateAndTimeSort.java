package com.example.backend.domain.flight.sorting;

import com.example.backend.common.sorting.OrderType;
import com.example.backend.domain.flight.FlightEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FlightDateAndTimeSort implements IFlightSort{
    private final String attributeName;
    private final OrderType orderType;

    public FlightDateAndTimeSort(String attributeName, OrderType orderType) {
        this.attributeName = attributeName;
        this.orderType = orderType;
    }

    @Override
    public Specification<FlightEntity> toSpecification() {
        return (Root<FlightEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Order> orderList = new ArrayList<>();

            if (orderType.equals(OrderType.ASC)) {
                orderList.add(builder.asc(root.get(prependAttributeWithDate())));
                orderList.add(builder.asc(root.get(prependAttributeWithTime())));
            } else if (orderType.equals(OrderType.DESC)) {
                orderList.add(builder.desc(root.get(prependAttributeWithDate())));
                orderList.add(builder.desc(root.get(prependAttributeWithTime())));
            }

            query.orderBy(orderList);

            return null;
        };
    }

    private String prependAttributeWithDate(){
        return attributeName+"Date";
    }

    private String prependAttributeWithTime(){
        return attributeName+"Time";
    }
}
