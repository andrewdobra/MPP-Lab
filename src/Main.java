import domain.validators.*;
import domain.*;
import domain.xml.*;
import service.*;
import repository.*;
import socket.Server;
import ui.*;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        System.out.println("Server (0), Client (1) or Local (2): ");

        int start = new Scanner(System.in).nextInt();

        if (start == 0) {
            Server server = new Server();
        } else if (start == 1) {
            socket.Client client = new socket.Client("127.0.0.1", 8085);
        } else if (start == 2) {
            Validator<Client> clientValidator = new ClientValidator();
            Validator<Book> bookValidator = new BookValidator();
            Validator<Purchase> purchaseValidator = new PurchaseValidator();
            Repository<Long,Client> clientFileRepository = new ClientFileRepository(clientValidator, "src\\data\\clients.txt");
            Repository<Long, Book> bookFileRepository = new BookFileRepository(bookValidator, "src\\data\\books.txt");
            Repository<Long, Purchase> purchaseFileRepository = new PurchaseFileRepository(purchaseValidator, "src\\data\\purchases.txt");
            Repository<Long, Client> clientXMLRepository = new XMLRepository<Long,Client>(clientValidator,new ClientXML(),"src\\data\\clients.xml");//
            Repository<Long, Book> bookXMLRepository = new XMLRepository<Long,Book>(bookValidator,new BookXML(), "src\\data\\books.xml");
            Repository<Long, Purchase> purchaseXMLRepository = new XMLRepository<Long,Purchase>(purchaseValidator,new PurchaseXML(), "src\\data\\purchases.xml");
            Repository<Long, Client> clientSQLRepository = new DatabaseRepository<Long,Client>(clientValidator,"Clients",new ClientSQL());
            Repository<Long, Book> bookSQLRepository = new DatabaseRepository<Long,Book>(bookValidator,"Books",new BookSQL());
            Repository<Long, Purchase> purchaseSQLRepository = new DatabaseRepository<Long,Purchase>(purchaseValidator,"Purchases",new PurchaseSQL());

            Repository<Long, Book> bookRepository;
            Repository<Long, Client> clientRepository;
            Repository<Long, Purchase> purchaseRepository;
            System.out.println("Select repository (1 - text, 2 - xml, 3 - database)");

            int choice = new Scanner(System.in).nextInt();
            switch(choice)
            {
                case 1:
                    bookRepository = bookFileRepository;
                    clientRepository = clientFileRepository;
                    purchaseRepository = purchaseFileRepository;
                    break;
                case 2:
                    bookRepository = bookXMLRepository;
                    clientRepository = clientXMLRepository;
                    purchaseRepository = purchaseXMLRepository;
                    break;
                default:
                    bookRepository = bookSQLRepository;
                    clientRepository = clientSQLRepository;
                    purchaseRepository = purchaseSQLRepository;

            }

            ClientService clientService = new ClientService(clientRepository);
            BookService bookService = new BookService(bookRepository);
            PurchaseService purchaseService = new PurchaseService(purchaseRepository);

            Console console = new Console(clientService, bookService, purchaseService);
            console.runConsole();
        }
    }
}
