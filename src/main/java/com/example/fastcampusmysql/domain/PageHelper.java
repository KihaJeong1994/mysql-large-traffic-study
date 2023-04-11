package com.example.fastcampusmysql.domain;

import org.springframework.data.domain.Sort;

import java.util.List;

/*
* Class to help change Sort instance to String used for query
* */
public class PageHelper {
    public static String orderBy(Sort sort){
        if (sort.isEmpty()) return "id DESC";
        /*
        * "property1 direction1, property2 direction2 ..."
        * */
        List<Sort.Order> orders = sort.toList();
        List<String> orderBys = orders.stream()
                .map(order -> order.getProperty() + " " + order.getDirection())
                .toList();
        return String.join(", ",orderBys);
    }
}
