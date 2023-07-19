package com.afs.restapi.service;

import com.afs.restapi.entity.Company;
import com.afs.restapi.exception.CompanyNotFoundException;
import com.afs.restapi.repository.InMemoryCompanyRepository;
import com.afs.restapi.entity.Employee;
import com.afs.restapi.repository.InMemoryEmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final InMemoryCompanyRepository inMemoryCompanyRepository;
    private final InMemoryEmployeeRepository inMemoryEmployeeRepository;

    public CompanyService(InMemoryCompanyRepository inMemoryCompanyRepository, InMemoryEmployeeRepository inMemoryEmployeeRepository) {
        this.inMemoryCompanyRepository = inMemoryCompanyRepository;
        this.inMemoryEmployeeRepository = inMemoryEmployeeRepository;
    }

    public InMemoryCompanyRepository getCompanyRepository() {
        return inMemoryCompanyRepository;
    }

    public InMemoryEmployeeRepository getEmployeeRepository() {
        return inMemoryEmployeeRepository;
    }

    public List<Company> findAll() {
        return getCompanyRepository().getCompanies();
    }

    public List<Company> findByPage(Integer page, Integer size) {
        return getCompanyRepository().findByPage(page, size);
    }

    public Company findById(Long id) {
        Company company = getCompanyRepository().findById(id).orElseThrow(CompanyNotFoundException::new);
        List<Employee> employees = getEmployeeRepository().findByCompanyId(company.getId());
        company.setEmployees(employees);
        return company;
    }

    public void update(Long id, Company company) {
        Optional<Company> optionalCompany = getCompanyRepository().findById(id);
        optionalCompany.ifPresent(previousCompany -> previousCompany.setName(company.getName()));
    }

    public Company create(Company company) {
        return getCompanyRepository().insert(company);
    }

    public List<Employee> findEmployeesByCompanyId(Long id) {
        return getEmployeeRepository().findByCompanyId(id);
    }

    public void delete(Long id) {
        inMemoryCompanyRepository.deleteById(id);
    }
}
