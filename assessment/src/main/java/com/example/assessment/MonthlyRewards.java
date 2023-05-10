package com.example.assessment;

import lombok.Getter;
import lombok.Setter;

import java.time.Month;
import java.util.Comparator;

@Getter
@Setter
public class MonthlyRewards implements Comparator<MonthlyRewards> {

    private Month month;

    private Integer rewards;
    @Override
    public int compare(MonthlyRewards o1, MonthlyRewards o2) {
        return o1.month.getValue() - o2.month.getValue();
    }
}
