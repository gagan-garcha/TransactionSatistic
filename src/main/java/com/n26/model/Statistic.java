package com.n26.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.n26.serialize.MoneySerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Statistic {

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal sum = BigDecimal.ZERO;


    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal avg = BigDecimal.ZERO;


    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal max = BigDecimal.ZERO;


    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal min = BigDecimal.ZERO;

    private long count = 0;
}
