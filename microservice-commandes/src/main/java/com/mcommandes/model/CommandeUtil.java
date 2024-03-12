import com.mcommandes.model.Commande;
import com.mcommandes.model.Panier;

import java.util.Date;

public class CommandeUtil {

    public static Commande createCommandeFromPanier(Panier panier) {
        Commande commande = new Commande();
        commande.setDateCommande(new Date()); // Set the current date as the command date
        commande.setQuantite(panier.getQuantite()); // Set the quantity from the panier
        commande.setProducts(panier.getProducts()); // Set the products from the panier to the commande

        // You may need to set other fields based on your application logic

        return commande;
    }
}
