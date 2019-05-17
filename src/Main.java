import domain.validators.*;
import domain.*;
import domain.xml.*;
import service.*;
import repository.*;
import ui.*;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        //in-memory repo
//         Validator<Client> studentValidator = new StudentValidator();
//         Repository<Long, Client> studentRepository = new InMemoryRepository<>(studentValidator);
//         StudentService studentService = new StudentService(studentRepository);
//         Console console = new Console(studentService);
//         console.runConsole();

        //file repo
//        try {
//            System.out.println(new File(".").getCanonicalPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //in file repo
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
