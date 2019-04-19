import domain.validators.*;
import domain.*;
import domain.xml.BookSQL;
import domain.xml.BookXML;
import domain.xml.ClientXML;
import service.*;
import repository.*;
import ui.*;

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
        Repository<Long, Client> clientRepository = new XMLRepository<Long,Client>(clientValidator,new ClientXML(),"src\\data\\clients.xml");//new ClientFileRepository(clientValidator, "src\\data\\clients.txt");
        Repository<Long, Book> bookRepository = new DatabaseRepository<Long,Book>(bookValidator,"Books",new BookSQL());
        //XMLRepository<Long,Book>(bookValidator,new BookXML(), "src\\data\\books.xml");//BookFileRepository(bookValidator, "src\\data\\books.txt");
        ClientService clientService = new ClientService(clientRepository);
        BookService bookService = new BookService(bookRepository);
        Console console = new Console(clientService, bookService);
        console.runConsole();

        System.out.println("Hello Bookstore!");
    }
}
