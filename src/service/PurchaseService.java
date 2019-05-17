package service;

import domain.validators.*;
import domain.Purchase;
import repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PurchaseService {
    private Repository<Long, Purchase> repository;

    public PurchaseService(Repository<Long, Purchase> repository) {
        this.repository = repository;
    }

    public void addPurchase(Purchase student) throws ValidatorException {
        repository.save(student);
    }

    public void delPurchase(Purchase purchase) throws ValidatorException {
        repository.delete(purchase.getId());
    }

    public Set<Purchase> getAllPurchases() {
        Iterable<Purchase> students = repository.findAll();
        return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
    }

    public Set<Purchase> filterPurchasesByClient(Long id) {
        Iterable<Purchase> students = repository.findAll();
        //version 1
//        Set<Purchase> filteredPurchases = StreamSupport.stream(students.spliterator(), false)
//                .filter(student -> student.getName().contains(s)).collect(Collectors.toSet());

        //version 2
        Set<Purchase> filteredPurchases= new HashSet<>();
        students.forEach(filteredPurchases::add);
        filteredPurchases.removeIf(purchase -> !purchase.getClientID().equals(id));

        return filteredPurchases;
    }

    public Set<Purchase> filterPurchasesByBook(Long id) {
        Iterable<Purchase> students = repository.findAll();
        //version 1
//        Set<Purchase> filteredPurchases = StreamSupport.stream(students.spliterator(), false)
//                .filter(student -> student.getName().contains(s)).collect(Collectors.toSet());

        //version 2
        Set<Purchase> filteredPurchases= new HashSet<>();
        students.forEach(filteredPurchases::add);
        filteredPurchases.removeIf(purchase -> !purchase.getBookID().equals(id));

        return filteredPurchases;
    }
}
