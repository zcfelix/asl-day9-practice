package com.afs.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("companies")
@RestController
public class CompanyController {

    private CompanyRepository companyRepository;
    private EmployeeRepository employeeRepository;

    public CompanyController(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyRepository.getCompanies();
    }

    @GetMapping(params = {"page", "size"})
    public List<Company> getCompaniesByPage(@RequestParam Integer page, @RequestParam Integer size) {
        return companyRepository.findByPage(page, size);
    }

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable Long id) {
        Company company = companyRepository.findById(id);
        List<Employee> employees = employeeRepository.findByCompanyId(company.getId());
        company.setEmployees(employees);
        return company;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCompany(@PathVariable Long id, @RequestBody Company company) {
        Company byId = companyRepository.findById(id);
        byId.setName(company.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompany(@RequestBody Company company) {
        return companyRepository.insert(company);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployeesByCompanyId(@PathVariable Long id) {
        return employeeRepository.findByCompanyId(id);
    }

}
