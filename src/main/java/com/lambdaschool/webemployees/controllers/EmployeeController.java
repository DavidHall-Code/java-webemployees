package com.lambdaschool.webemployees.controllers;

import com.lambdaschool.webemployees.models.Employee;
import com.lambdaschool.webemployees.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EmployeeController
{
    @Autowired
    EmployeeRepository emprepos;

    private List<Employee> findEmployees(List<Employee> myList, CheckEmployee tester)
    {
        List<Employee> tempList = new ArrayList<>();

        for (Employee e : myList)
        {
            if (tester.test(e))
            {
                tempList.add(e);
            }
        }

        return tempList;
    }

    // http://localhost:2019/employees/all
    @GetMapping(value = "/employees/all", produces = {"application/json"})
    public ResponseEntity<?> listAllEmployees()
    {
        List<Employee> myList = new ArrayList<>();
        emprepos.findAll()
                .iterator()
                .forEachRemaining(myList::add);

        myList.sort((e1, e2) -> e1.getLname().compareToIgnoreCase(e2.getLname()));
        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    // http://localhost:2019/employees/total
    @GetMapping(value = "/employees/total", produces = {"application/json"})
    public ResponseEntity<?> reportTotalSalary()
    {
        List<Employee> myList = new ArrayList<>();
        emprepos.findAll()
                .iterator()
                .forEachRemaining(myList::add);

        double total = 0;
        for (Employee e : myList)
        {
            total = total + e.getSalary();
        }
        System.out.println("Total Salary: " + total);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    // http://localhost:2019/employees/name/{letter}
    @GetMapping(value = "/employees/name/{letter}", produces = {"application/json"})
    public ResponseEntity<?> filterByFirstName(@PathVariable char letter)
    {
        List<Employee> myList = new ArrayList<>();
        emprepos.findAll()
                .iterator()
                .forEachRemaining(myList::add);

        List<Employee> rtnList = findEmployees(myList, e -> e.getFname().charAt(0) == letter);
        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }
}
