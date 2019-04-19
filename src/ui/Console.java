package ui;

import domain.*;
import domain.validators.*;
import service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.Scanner;

/**
 * @author radu.
 */
public class Console {
    private ClientService clientService;
    private BookService bookService;
    Scanner keyboard = new Scanner(System.in);

    public Console(ClientService clientService, BookService bookService) {
        this.clientService = clientService;
        this.bookService = bookService;
    }

    public void runConsole() {
        System.out.print("Choose an action:\n" +
                "1. Add Clients\n" +
                "2. Add Books\n" +
                "3. Update Clients\n" +
                "4. Update Books\n" +
                "5. Delete Clients\n" +
                "6. Delete Books\n" +
                "7. Show all clients\n" +
                "8. Show all books\n" +
                "9. Filter clients\n" +
                "10. Filter books\n" +
                "0. Exit\n" +
                "\n" +
                "Choice: ");

        int choice = keyboard.nextInt();

        while (choice != 0) {
            switch(choice) {
                case 1: addClients(); break;
                case 2: addBooks(); break;
                case 3: updateClients(); break;
                case 4: updateBooks(); break;
                case 5: delClients(); break;
                case 6: delBooks(); break;
                case 7: printAllClients(); break;
                case 8: printAllBooks(); break;
                case 9: filterClients(); break;
                case 10: filterBooks(); break;
            }

            System.out.print("Choose an action:\n" +
                    "1. Add Clients\n" +
                    "2. Add Books\n" +
                    "3. Update Clients\n" +
                    "4. Update Books\n" +
                    "5. Delete Clients\n" +
                    "6. Delete Books\n" +
                    "7. Show all clients\n" +
                    "8. Show all books\n" +
                    "9. Filter clients\n" +
                    "10. Filter books\n" +
                    "0. Exit\n" +
                    "\n" +
                    "Choice: ");

            choice = keyboard.nextInt();
        }
    }

    private void filterClients() {
        System.out.print("Pattern: ");
        keyboard.nextLine();
        String pat = keyboard.nextLine();
        System.out.println("Filtered clients (name containing " + pat + "):");
        Set<Client> clients = clientService.filterClientsByName(pat);
        clients.stream().forEach(System.out::println);
    }

    private void filterBooks() {
        System.out.print("Pattern: ");
        keyboard.nextLine();
        String pat = keyboard.nextLine();
        System.out.println("Filtered books (name containing " + pat + "):");
        Set<Book> books = bookService.filterBooksByName(pat);
        books.stream().forEach(System.out::println);
    }

    private void printAllClients() {
        Set<Client> clients = clientService.getAllClients();
        clients.stream().forEach(System.out::println);
        if(clients.isEmpty())
            System.out.println("There are no clients");
    }

    private void printAllBooks() {
        Set<Book> books = bookService.getAllBooks();
        books.stream().forEach(System.out::println);
        if(books.isEmpty())
            System.out.println("There are no books");
    }

    private void addClients() {
        while (true) {
            Client client = readClient("");
            if (client == null || client.getId() < 0) {
                break;
            }
            try {
                clientService.addClient(client);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        }
    }

    private void addBooks() {
        while (true) {
            Book book = readBook("");
            if (book == null || book.getId() < 0) {
                break;
            }
            try {
                bookService.addBook(book);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBooks() {
        while (true) {
            Book book1 = readBook("old ");
            if (book1 == null || book1.getId() < 0) {
                break;
            }
            try {
                Book book2 = readBook("new ");
                bookService.delBook(book1);
                bookService.addBook(book2);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateClients() {
        while (true) {
            Client client1 = readClient("old ");
            if (client1 == null || client1.getId() < 0) {
                break;
            }
            try {
                Client client2 = readClient("new ");
                clientService.delClient(client1);
                clientService.addClient(client2);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        }
    }

    private void delClients() {
        while (true) {
            Client client = readClient("");
            if (client == null || client.getId() < 0) {
                break;
            }
            try {
                clientService.delClient(client);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        }
    }

    private void delBooks() {
        while (true) {
            Book book = readBook("");
            if (book == null || book.getId() < 0) {
                break;
            }
            try {
                bookService.delBook(book);
            } catch (ValidatorException e) {
                e.printStackTrace();
            }
        }
    }

    private Client readClient(String s) {
        System.out.println("Input " + s + "client {id, name}:");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.valueOf(bufferRead.readLine());
            String name = bufferRead.readLine();

            Client client = new Client(id, name);
            client.setId(id);

            return client;
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return null;
    }

    private Book readBook(String s) {
        System.out.println("Input " + s + "book {id, name}:");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.valueOf(bufferRead.readLine());
            String name = bufferRead.readLine();

            Book book = new Book(id, name);
            book.setId(id);

            return book;
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return null;
    }
}
