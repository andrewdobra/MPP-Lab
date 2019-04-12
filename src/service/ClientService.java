package service;

import domain.Client;
import domain.validators.ValidatorException;
import repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author radu.
 */
public class ClientService {
    private Repository<Long, Client> repository;

    public ClientService(Repository<Long, Client> repository) {
        this.repository = repository;
    }

    public void addClient(Client student) throws ValidatorException {
        repository.save(student);
    }

    public void delClient(Client client) throws ValidatorException {
        repository.delete(client.getId());
    }

    public Set<Client> getAllClients() {
        Iterable<Client> students = repository.findAll();
        return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Returns all students whose name contain the given string.
     *
     * @param s
     * @return
     */
    public Set<Client> filterClientsByName(String s) {
        Iterable<Client> students = repository.findAll();
        //version 1
//        Set<Client> filteredClients = StreamSupport.stream(students.spliterator(), false)
//                .filter(student -> student.getName().contains(s)).collect(Collectors.toSet());

        //version 2
        Set<Client> filteredClients= new HashSet<>();
        students.forEach(filteredClients::add);
        filteredClients.removeIf(student -> !student.getName().contains(s));

        return filteredClients;
    }
}
