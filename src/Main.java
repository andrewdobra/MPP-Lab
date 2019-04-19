import domain.validators.*;
import domain.*;
import domain.xml.BookSQL;
import domain.xml.BookXML;
import domain.xml.ClientSQL;
import domain.xml.ClientXML;
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
        Repository<Long,Client> clientFileRepository = new ClientFileRepository(clientValidator, "src\\data\\clients.txt");
        Repository<Long, Book> bookFileRepository = new BookFileRepository(bookValidator, "src\\data\\books.txt");
        Repository<Long, Client> clientXMLRepository = new XMLRepository<Long,Client>(clientValidator,new ClientXML(),"src\\data\\clients.xml");//
        Repository<Long, Book> bookXMLRepository = new XMLRepository<Long,Book>(bookValidator,new BookXML(), "src\\data\\books.xml");
        Repository<Long, Client> clientSQLRepository = new DatabaseRepository<Long,Client>(clientValidator,"Clients",new ClientSQL());
        Repository<Long, Book> bookSQLRepository = new DatabaseRepository<Long,Book>(bookValidator,"Books",new BookSQL());

        Repository<Long, Book> bookRepository;
        Repository<Long, Client> clientRepository;
        System.out.println("Select repository (1 - text, 2 - xml, 3 - database");

        int choice = new Scanner(System.in).nextInt();
        switch(choice)
        {
            case 1:
                bookRepository = bookFileRepository;
                clientRepository=clientFileRepository;
                break;
            case 2:
                bookRepository = bookXMLRepository;
                clientRepository = clientXMLRepository;
                break;
            default:
                bookRepository = bookSQLRepository;
                clientRepository = clientSQLRepository;

        }

        ClientService clientService = new ClientService(clientRepository);
        BookService bookService = new BookService(bookRepository);
        Console console = new Console(clientService, bookService);
        console.runConsole();

        System.out.println("Hello Bookstore!");
    }
}
