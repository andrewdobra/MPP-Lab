package socket;

import domain.Book;
import domain.Client;
import domain.Purchase;
import domain.validators.*;
import domain.xml.BookSQL;
import domain.xml.ClientSQL;
import domain.xml.PurchaseSQL;
import repository.DatabaseRepository;
import repository.Repository;
import service.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.*;

public class Server {
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private ClientService clientService;
    private BookService bookService;
    private PurchaseService purchaseService;
    private String signal = "endRead";

    public Server(int port) {
        Validator<Client> clientValidator = new ClientValidator();
        Validator<Book> bookValidator = new BookValidator();
        Validator<Purchase> purchaseValidator = new PurchaseValidator();

        Repository<Long, Client> clientSQLRepository = new DatabaseRepository<Long,Client>(clientValidator,"Clients",new ClientSQL());
        Repository<Long, Book> bookSQLRepository = new DatabaseRepository<Long,Book>(bookValidator,"Books",new BookSQL());
        Repository<Long, Purchase> purchaseSQLRepository = new DatabaseRepository<Long,Purchase>(purchaseValidator,"Purchases",new PurchaseSQL());

        Repository<Long, Book> bookRepository;
        Repository<Long, Client> clientRepository;
        Repository<Long, Purchase> purchaseRepository;

        bookRepository = bookSQLRepository;
        clientRepository = clientSQLRepository;
        purchaseRepository = purchaseSQLRepository;

        this.clientService = new ClientService(clientRepository);
        this.bookService = new BookService(bookRepository);
        this.purchaseService = new PurchaseService(purchaseRepository);

        this.executorService = Executors.newFixedThreadPool(10);

        try {
            System.out.println("Starting Server");
            this.serverSocket = new ServerSocket(port);
        } catch(IOException e) {
            executorService.shutdown();
            System.out.println("Error starting Server on " + port);
            e.printStackTrace();
        }

        while(true) {
            Socket client;
            System.out.println("Waiting for request ...");

            try {
                client = serverSocket.accept();
                System.out.println("Processing request ...");
                executorService.submit(new ServiceRequest(client));
            } catch(IOException e) {
                System.out.println("Request failed!");
                e.printStackTrace();
            }
        }
    }

    //Call the method when you want to stop your server
    private void stopServer() {
        //Stop the executor service.
        executorService.shutdownNow();
        try {
            //Stop accepting requests.
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error during server shutdown!");
            e.printStackTrace();
        }
        System.exit(0);
    }

    private class ServiceRequest implements Runnable {

        private final Socket socket;
        private BufferedReader input = null;
        private PrintWriter output = null;

        public ServiceRequest(Socket connection) {
            this.socket = connection;
        }

        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                String[] commands = {"Choose an action:",
                        "1. Add Clients",
                        "2. Add Books",
                        "3. Add Purchases",
                        "4. Update Clients",
                        "5. Update Books",
                        "6. Update Purchases",
                        "7. Delete Clients",
                        "8. Delete Books",
                        "9. Delete Purchases",
                        "10. Show all Clients",
                        "11. Show all Books",
                        "12. Show all Purchases",
                        "13. Filter Clients",
                        "14. Filter Books",
                        "15. Filter Purchases",
                        "0. Exit",
                        "",
                        "Choice: "};

                output.println("Hello Bookstore!");

                for (int i = 0; i < 19; i++) {
                    output.println(commands[i]);
                }

                output.println(signal);

                String in = input.readLine();
                int choice;

                try {
                    choice = Integer.parseInt(in);
                } catch (Exception e) {
                    output.println("Invalid command!");
                    output.println(signal);
                    return;
                }

                while (choice != 0) {
                    switch (choice) {
                        case 1:
                            addClients(input, output);
                            break;
                        case 2:
                            addBooks(input, output);
                            break;
                        case 3:
                            addPurchases(input, output);
                            break;
                        case 4:
                            updateClients(input, output);
                            break;
                        case 5:
                            updateBooks(input, output);
                            break;
                        case 6:
                            updatePurchases(input, output);
                            break;
                        case 7:
                            delClients(input, output);
                            break;
                        case 8:
                            delBooks(input, output);
                            break;
                        case 9:
                            delPurchases(input, output);
                            break;
                        case 10:
                            printAllClients(input, output);
                            break;
                        case 11:
                            printAllBooks(input, output);
                            break;
                        case 12:
                            printAllPurchases(input, output);
                            break;
                        case 13:
                            filterClients(input, output);
                            break;
                        case 14:
                            filterBooks(input, output);
                            break;
                        case 15:
                            filterPurchases(input, output);
                            break;
                    }

                    for (int i = 0; i < 19; i++)
                        output.println(commands[i]);

                    output.println(signal);

                    in = input.readLine();
                    choice = Integer.parseInt(in);
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }

            //Make sure to close
            try {
                socket.close();
            } catch (IOException ioe) {
                System.out.println("Error closing client connection");
            }
        }
    }

        private void filterClients(BufferedReader input, PrintWriter output) {
            try {
                output.println("Pattern: ");
                output.println(signal);

                String pat = input.readLine();

                output.println("Filtered clients (name containing " + pat + "):");
                Set<domain.Client> clients = this.clientService.filterClientsByName(pat);
                clients.stream().forEach((str) -> output.println(str.toString()));
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
        }

        private void filterBooks(BufferedReader input, PrintWriter output) {
            try {
                output.println("Pattern: ");
                output.println(signal);

                String pat = input.readLine();

                output.println("Filtered books (name containing " + pat + "):");
                Set<Book> books = this.bookService.filterBooksByName(pat);
                books.stream().forEach((str) -> output.println(str.toString()));
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
        }

        private void filterPurchases(BufferedReader input, PrintWriter output) {
            try {
                output.println("1. Client ID or 2.Book ID: ");
                output.println(signal);

                int choice = Integer.parseInt(input.readLine());

                output.println("ID: ");
                output.println(signal);

                Long id = Long.parseLong(input.readLine());
                Set<Purchase> purchases;
                if (choice == 1)
                    purchases = this.purchaseService.filterPurchasesByClient(id);
                else if (choice == 2)
                    purchases = this.purchaseService.filterPurchasesByBook(id);
                else
                    throw new BookstoreException("Invalid choice!");

                output.println("Filtered purchases: ");
                purchases.stream().forEach((str) -> output.println(str.toString()));
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
        }

        private void printAllClients(BufferedReader input, PrintWriter output) {
            Set<domain.Client> clients = this.clientService.getAllClients();
            clients.stream().forEach((str) -> output.println(str.toString()));
            if(clients.isEmpty())
                System.out.println("There are no clients");
        }

        private void printAllBooks(BufferedReader input, PrintWriter output) {
            Set<domain.Book> books = this.bookService.getAllBooks();
            books.stream().forEach((str) -> output.println(str.toString()));
            if(books.isEmpty())
                System.out.println("There are no books");
        }

        private void printAllPurchases(BufferedReader input, PrintWriter output) {
            Set<domain.Purchase> purchases = this.purchaseService.getAllPurchases();
            purchases.stream().forEach((str) -> output.println(str.toString()));
            if(purchases.isEmpty())
                System.out.println("There are no purchases");
        }

        private void addClients(BufferedReader input, PrintWriter output) {
            while (true) {
                domain.Client client = readClient("", input, output);
                if (client == null || client.getId() < 0) {
                    break;
                }
                try {
                    this.clientService.addClient(client);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            }
        }

        private void addBooks(BufferedReader input, PrintWriter output) {
            while (true) {
                Book book = readBook("", input, output);
                if (book == null || book.getId() < 0) {
                    break;
                }
                try {
                    this.bookService.addBook(book);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            }
        }

        private void addPurchases(BufferedReader input, PrintWriter output) {
            while (true) {
                Purchase purchase = readPurchase("", input, output);
                if (purchase == null || purchase.getId() < 0) {
                    break;
                }
                try {
                    this.purchaseService.addPurchase(purchase);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            }
        }

        private void updateBooks(BufferedReader input, PrintWriter output) {
            while (true) {
                Book book1 = readBook("old ", input, output);
                if (book1 == null || book1.getId() < 0) {
                    break;
                }
                try {
                    Book book2 = readBook("new ", input, output);
                    this.bookService.delBook(book1);
                    this.bookService.addBook(book2);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            }
        }

        private void updateClients(BufferedReader input, PrintWriter output) {
            while (true) {
                domain.Client client1 = readClient("old ", input, output);
                if (client1 == null || client1.getId() < 0) {
                    break;
                }
                try {
                    domain.Client client2 = readClient("new ", input, output);
                    this.clientService.delClient(client1);
                    this.clientService.addClient(client2);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            }
        }

        private void updatePurchases(BufferedReader input, PrintWriter output) {
            while (true) {
                Purchase purchase1 = readPurchase("old ", input, output);
                if (purchase1 == null || purchase1.getId() < 0) {
                    break;
                }
                try {
                    Purchase purchase2 = readPurchase("new ", input, output);
                    this.purchaseService.delPurchase(purchase1);
                    this.purchaseService.addPurchase(purchase2);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            }
        }

        private void delClients(BufferedReader input, PrintWriter output) {
            while (true) {
                domain.Client client = readClient("", input, output);
                if (client == null || client.getId() < 0) {
                    break;
                }
                try {
                    this.clientService.delClient(client);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            }
        }

        private void delBooks(BufferedReader input, PrintWriter output) {
            while (true) {
                Book book = readBook("", input, output);
                if (book == null || book.getId() < 0) {
                    break;
                }
                try {
                    this.bookService.delBook(book);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            }
        }

        private void delPurchases(BufferedReader input, PrintWriter output) {
            while (true) {
                Purchase purchase = readPurchase("", input, output);
                if (purchase == null || purchase.getId() < 0) {
                    break;
                }
                try {
                    this.purchaseService.delPurchase(purchase);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            }
        }

        private domain.Client readClient(String s, BufferedReader input, PrintWriter output) {
            output.println("Input " + s + "client ID:");
            output.println(signal);

            try {
                Long id = Long.valueOf(input.readLine());

                output.println("Input " + s + "client name:");
                output.println(signal);

                String name = input.readLine();

                domain.Client client = new Client(id, name);

                return client;
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            return null;
        }

        private Book readBook(String s, BufferedReader input, PrintWriter output) {
            output.println("Input " + s + "book ID:");
            output.println(signal);

            try {
                Long id = Long.valueOf(input.readLine());

                output.println("Input " + s + "book name:");
                output.println(signal);

                String name = input.readLine();

                Book book = new Book(id, name);

                return book;
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            return null;
        }

        private Purchase readPurchase(String s, BufferedReader input, PrintWriter output) {
            output.println("Input " + s + "purchase ID:");
            output.println(signal);

            try {
                Long id = Long.valueOf(input.readLine());

                output.println("Input " + s + "purchase client ID:");
                output.println(signal);
                Long CID = Long.valueOf(input.readLine());

                output.println("Input " + s + "purchase book ID:");
                output.println(signal);

                Long BID = Long.valueOf(input.readLine());

                Purchase purchase = new Purchase(id, CID, BID);

                return purchase;
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            return null;
        }
}

