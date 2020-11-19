package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageCriteria {
    @Min(1)
    private int page;
    @Min(1)
    private int size;
}
