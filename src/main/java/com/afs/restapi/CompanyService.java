package com.afs.restapi;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }

    public EmployeeRepository getEmployeeRepository() {
        return employeeRepository;
    }

    public List<Company> findAll() {
        return getCompanyRepository().getCompanies();
    }

    public List<Company> findByPage(Integer page, Integer size) {
        return getCompanyRepository().findByPage(page, size);
    }

    public Company findById(Long id) {
        Company company = getCompanyRepository().findById(id);
        List<Employee> employees = getEmployeeRepository().findByCompanyId(company.getId());
        company.setEmployees(employees);
        return company;
    }

    public void update(Long id, Company company, CompanyController companyController) {
        Company byId = getCompanyRepository().findById(id);
        byId.setName(company.getName());
    }

    public Company create(Company company, CompanyController companyController) {
        return getCompanyRepository().insert(company);
    }

    public List<Employee> findEmployeesByCompanyId(Long id) {
        return getEmployeeRepository().findByCompanyId(id);
    }
}
