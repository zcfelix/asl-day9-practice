package com.afs.restapi;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {

    private List<Company> companies = new ArrayList<>();

    public List<Company> getCompanies() {
        return companies;
    }

    public Company findById(Long id) {
        return getCompanies().stream()
                .filter(company -> company.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Company insert(Company company) {
        company.setId(nextId());
        getCompanies().add(company);
        return company;
    }

    public void clearAll() {
        companies.clear();
    }

    private Long nextId() {
        long maxId = getCompanies().stream()
                .mapToLong(Company::getId)
                .max()
                .orElse(0L);
        return maxId + 1;
    }

}
