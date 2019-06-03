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

    /**
     * This executor service has 6 threads.
     * So it means your server can process max 6 concurrent requests.
     */

    private ExecutorService executorService = Executors.newFixedThreadPool(6);

    public Server() {
        runServer();
    }

    private void runServer() {
        int serverPort = 8085;
        try {
            System.out.println("Starting Server");
            serverSocket = new ServerSocket(serverPort);

            for(;;) {
                System.out.println("Waiting for request ...");
                try {
                    Socket s = serverSocket.accept();
                    System.out.println("Processing request ...");
                    executorService.submit(new ServiceRequest(s));
                } catch(IOException ioe) {
                    System.out.println("Error accepting connection!");
                    ioe.printStackTrace();
                }
            }
        } catch(IOException e) {
            executorService.shutdown();
            System.out.println("Error starting Server on " + serverPort);
            e.printStackTrace();
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

    class ServiceRequest implements Runnable {

        private Socket socket;
        private BufferedReader input = null;
        private PrintWriter output = null;
        private ClientService clientService;
        private BookService bookService;
        private PurchaseService purchaseService;

        public ServiceRequest(Socket connection) {
            this.socket = connection;

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
            
            ClientService clientService = new ClientService(clientRepository);
            BookService bookService = new BookService(bookRepository);
            PurchaseService purchaseService = new PurchaseService(purchaseRepository);

            run();
        }

        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                String signal = "endRead";
                String[] commands = { "Choose an action:",
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
                                    "Choice: " };

                output.println("Hello Bookstore!");

                for (int i = 0; i < 19; i++) {
                    output.println(commands[i]);
                }

                output.println(signal);

                String in = input.readLine();
                int choice = Integer.parseInt(in);

                while (choice != 0) {
                    switch (choice) {
                        case 1:
                            addClients();
                            break;
                        case 2:
                            addBooks();
                            break;
                        case 3:
                            addPurchases();
                            break;
                        case 4:
                            updateClients();
                            break;
                        case 5:
                            updateBooks();
                            break;
                        case 6:
                            updatePurchases();
                            break;
                        case 7:
                            delClients();
                            break;
                        case 8:
                            delBooks();
                            break;
                        case 9:
                            delPurchases();
                            break;
                        case 10:
                            printAllClients();
                            break;
                        case 11:
                            printAllBooks();
                            break;
                        case 12:
                            printAllPurchases();
                            break;
                        case 13:
                            filterClients();
                            break;
                        case 14:
                            filterBooks();
                            break;
                        case 15:
                            filterPurchases();
                            break;
                    }

                    for (int i = 0; i < 19; i++)
                        output.println(commands[i]);

                    in = input.readLine();
                    choice = Integer.parseInt(in);
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }

            //Make sure to close
            try {
                socket.close();
            }catch(IOException ioe) {
                System.out.println("Error closing client connection");
            }
        }

        private void filterClients() {
            try {
                output.println("Pattern: ");
                
                input.readLine();
                String pat = input.readLine();
                output.println("Filtered clients (name containing " + pat + "):");

                Set<domain.Client> clients = clientService.filterClientsByName(pat);
                clients.stream().forEach((str) -> output.println(str.toString()));
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
        }

        private void filterBooks() {
            try {
                output.println("Pattern: ");
                
                input.readLine();
                String pat = input.readLine();
                output.println("Filtered books (name containing " + pat + "):");
                
                Set<Book> books = bookService.filterBooksByName(pat);
                books.stream().forEach((str) -> output.println(str.toString()));
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
        }

        private void filterPurchases() {
            try {
                output.println("1. Client ID or 2.Book ID: ");
                
                int choice = Integer.parseInt(input.readLine());
                Long id = Long.parseLong(input.readLine());
                Set<Purchase> purchases;
                if (choice == 1)
                    purchases = purchaseService.filterPurchasesByClient(id);
                else if (choice == 2)
                    purchases = purchaseService.filterPurchasesByBook(id);
                else
                    throw new BookstoreException("Invalid choice!");

                output.println("Filtered purchases: ");
                
                purchases.stream().forEach((str) -> output.println(str.toString()));
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
        }

        private void printAllClients() {
            Set<domain.Client> clients = clientService.getAllClients();
            clients.stream().forEach((str) -> output.println(str.toString()));
            if(clients.isEmpty())
                    System.out.println("There are no clients");
        }

        private void printAllBooks() {
            Set<domain.Book> books = bookService.getAllBooks();
            books.stream().forEach((str) -> output.println(str.toString()));
            if(books.isEmpty())
                System.out.println("There are no books");
        }

        private void printAllPurchases() {
            Set<domain.Purchase> purchases = purchaseService.getAllPurchases();
            purchases.stream().forEach((str) -> output.println(str.toString()));
            if(purchases.isEmpty())
                System.out.println("There are no purchases");
        }

        private void addClients() {
            while (true) {
                domain.Client client = readClient("");
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
                domain.Client client1 = readClient("old ");
                if (client1 == null || client1.getId() < 0) {
                    break;
                }
                try {
                    domain.Client client2 = readClient("new ");
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
                domain.Client client = readClient("");
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

        private domain.Client readClient(String s) {
            output.println("Input " + s + "client {id, name}:");

            try {
                Long id = Long.valueOf(input.readLine());
                String name = input.readLine();

                domain.Client client = new Client(id, name);

                return client;
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            return null;
        }

        private Book readBook(String s) {
            output.println("Input " + s + "book {id, name}:");

            try {
                Long id = Long.valueOf(input.readLine());
                String name = input.readLine();

                Book book = new Book(id, name);

                return book;
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            return null;
        }

        private Purchase readPurchase(String s) {
            output.println("Input " + s + "purchase {ID, Client ID, Book ID}:");

            try {
                Long id = Long.valueOf(input.readLine());
                Long CID = Long.valueOf(input.readLine());
                Long BID = Long.valueOf(input.readLine());

                Purchase purchase = new Purchase(id, CID, BID);

                return purchase;
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            return null;
        }
    }
}
