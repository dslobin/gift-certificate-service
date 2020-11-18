package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CertificateSearchCriteria {
    private Set<String> tags = new HashSet<>();
    private String name;
    private String description;
    private String sortByName;
    private String sortByCreateDate;
}
