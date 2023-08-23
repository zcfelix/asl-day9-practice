package com.afs.restapi.service;

import com.afs.restapi.entity.Company;
import com.afs.restapi.entity.Employee;
import com.afs.restapi.exception.CompanyNotFoundException;
import com.afs.restapi.repository.CompanyJpaRepository;
import com.afs.restapi.repository.EmployeeJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {


    private final CompanyJpaRepository companyJpaRepository;

    private final EmployeeJpaRepository employeeJpaRepository;

    public CompanyService(CompanyJpaRepository companyJpaRepository, EmployeeJpaRepository employeeJpaRepository) {
        this.companyJpaRepository = companyJpaRepository;
        this.employeeJpaRepository = employeeJpaRepository;
    }

    public List<Company> findAll() {
        return companyJpaRepository.findAll();
    }

    public List<Company> findByPage(Integer page, Integer size) {
        return companyJpaRepository.findAll(PageRequest.of(page, size)).toList();
    }

    public Company findById(Long id) {
        return companyJpaRepository.findById(id).orElseThrow(CompanyNotFoundException::new);
    }

    public void update(Long id, Company company) {
        Company toBeUpdateCompany = findById(id);
        toBeUpdateCompany.setName(company.getName());
        companyJpaRepository.save(toBeUpdateCompany);
    }

    public Company create(Company company) {
        return companyJpaRepository.save(company);
    }

    public List<Employee> findEmployeesByCompanyId(Long id) {
        return employeeJpaRepository.findByCompanyId(id);
    }

    public void delete(Long id) {
        companyJpaRepository.deleteById(id);
    }
}
