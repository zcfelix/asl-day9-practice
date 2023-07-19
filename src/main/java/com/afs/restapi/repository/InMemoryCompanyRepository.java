package com.afs.restapi.repository;

import com.afs.restapi.entity.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryCompanyRepository {

    private List<Company> companies = new ArrayList<>();

    public List<Company> getCompanies() {
        return companies;
    }

    public Optional<Company> findById(Long id) {
        return getCompanies().stream()
                .filter(company -> company.getId().equals(id))
                .findFirst();
    }

    public Company insert(Company company) {
        company.setId(nextId());
        getCompanies().add(company);
        return company;
    }

    public List<Company> findByPage(Integer page, Integer size) {
        return companies.stream()
                .skip((long) (page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
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

    public void deleteById(Long id) {
        findById(id).ifPresent(company -> companies.remove(company));
    }
}
