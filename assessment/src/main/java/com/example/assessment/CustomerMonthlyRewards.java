package com.example.assessment;

import com.example.assessment.entities.Customer;
import lombok.Getter;
import lombok.Setter;
import java.util.*;


@Getter
@Setter
public class CustomerMonthlyRewards {
    private Customer customer;

    private List<MonthlyRewards> rewardsList;

}
