package CDiesel72;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class App {
    private static Scanner sc;
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void main(String[] args) {
        try {
            sc = new Scanner(System.in);
            emf = Persistence.createEntityManagerFactory("JPABank");
            em = emf.createEntityManager();
        } catch (Exception ex) {
            ex.printStackTrace();
            closeAll();
            return;
        }

        Menu menu = new Menu(em, sc);
        while (true) {
            if (menu.menuClient()) {
                menu.menuAccount();
            } else {
                closeAll();
                return;
            }
        }
    }

    private static void closeAll() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
        if (sc != null) {
            sc.close();
        }
    }
}
