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
    private PurchaseService purchaseService;
    Scanner keyboard = new Scanner(System.in);

    public Console(ClientService clientService, BookService bookService, PurchaseService purchaseService) {
        this.clientService = clientService;
        this.bookService = bookService;
        this.purchaseService = purchaseService;
    }

    public void runConsole() {

        System.out.print("Hello Bookstore!\n\n" +
                "Choose an action:\n" +
                "1. Add Clients\n" +
                "2. Add Books\n" +
                "3. Add Purchases\n" +
                "4. Update Clients\n" +
                "5. Update Books\n" +
                "6. Update Purchases\n" +
                "7. Delete Clients\n" +
                "8. Delete Books\n" +
                "9. Delete Purchases\n" +
                "10. Show all Clients\n" +
                "11. Show all Books\n" +
                "12. Show all Purchases\n" +
                "13. Filter Clients\n" +
                "14. Filter Books\n" +
                "15. Filter Purchases\n" +
                "0. Exit\n" +
                "\n" +
                "Choice: ");

        int choice = keyboard.nextInt();

        while (choice != 0) {
            switch(choice) {
                case 1: addClients(); break;
                case 2: addBooks(); break;
                case 3: addPurchases(); break;
                case 4: updateClients(); break;
                case 5: updateBooks(); break;
                case 6: updatePurchases(); break;
                case 7: delClients(); break;
                case 8: delBooks(); break;
                case 9: delPurchases(); break;
                case 10: printAllClients(); break;
                case 11: printAllBooks(); break;
                case 12: printAllPurchases(); break;
                case 13: filterClients(); break;
                case 14: filterBooks(); break;
                case 15: filterPurchases(); break;
            }

            System.out.print("Choose an action:\n" +
                    "1. Add Clients\n" +
                    "2. Add Books\n" +
                    "3. Add Purchases\n" +
                    "4. Update Clients\n" +
                    "5. Update Books\n" +
                    "6. Update Purchases\n" +
                    "7. Delete Clients\n" +
                    "8. Delete Books\n" +
                    "9. Delete Purchases\n" +
                    "10. Show all Clients\n" +
                    "11. Show all Books\n" +
                    "12. Show all Purchases\n" +
                    "13. Filter Clients\n" +
                    "14. Filter Books\n" +
                    "15. Filter Purchases\n" +
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

    private void filterPurchases() {
        System.out.println("1. Client ID or 2.Book ID: ");
        Integer choice = keyboard.nextInt();
        Long id = keyboard.nextLong();
        Set<Purchase> purchases;
        if (choice == 1)
            purchases = purchaseService.filterPurchasesByClient(id);
        else if (choice == 2)
            purchases = purchaseService.filterPurchasesByBook(id);
        else
            throw new BookstoreException("Invalid choice!");

        System.out.println("Filtered purchases: ");
        purchases.stream().forEach(System.out::println);
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

    private void printAllPurchases() {
        Set<Purchase> purchases = purchaseService.getAllPurchases();
        purchases.stream().forEach(System.out::println);
        if(purchases.isEmpty())
            System.out.println("There are no purchases");
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

    private void addPurchases() {
        while (true) {
            Purchase purchase = readPurchase("");
            if (purchase == null || purchase.getId() < 0) {
                break;
            }
            try {
                purchaseService.addPurchase(purchase);
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

    private void updatePurchases() {
        while (true) {
            Purchase purchase1 = readPurchase("old ");
            if (purchase1 == null || purchase1.getId() < 0) {
                break;
            }
            try {
                Purchase purchase2 = readPurchase("new ");
                purchaseService.delPurchase(purchase1);
                purchaseService.addPurchase(purchase2);
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

    private void delPurchases() {
        while (true) {
            Purchase purchase = readPurchase("");
            if (purchase == null || purchase.getId() < 0) {
                break;
            }
            try {
                purchaseService.delPurchase(purchase);
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

            return book;
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return null;
    }

    private Purchase readPurchase(String s) {
        System.out.println("Input " + s + "purchase {ID, Client ID, Book ID}:");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            Long id = Long.valueOf(bufferRead.readLine());
            Long CID = Long.valueOf(bufferRead.readLine());
            Long BID = Long.valueOf(bufferRead.readLine());

            Purchase purchase = new Purchase(id, CID, BID);

            return purchase;
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return null;
    }
}
