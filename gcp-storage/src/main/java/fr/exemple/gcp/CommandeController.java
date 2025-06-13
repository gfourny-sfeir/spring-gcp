package fr.exemple.gcp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour gérer les opérations liées aux commandes.
 * Expose les endpoints pour créer et manipuler des commandes.
 */
@RestController
@RequestMapping("/api/commande")
class CommandeController {

    /**
     * Service responsable de la sauvegarde des commandes.
     */
    private final CommandeSaver commandeSaver;

    /**
     * Constructeur pour l'injection des dépendances.
     *
     * @param commandeSaver Service de sauvegarde des commandes
     */
    CommandeController(CommandeSaver commandeSaver) {
        this.commandeSaver = commandeSaver;
    }

    /**
     * Endpoint pour sauvegarder une nouvelle commande.
     *
     * @param commande La commande à sauvegarder
     * @return Une réponse HTTP contenant la commande sauvegardée
     */
    @PostMapping
    ResponseEntity<Commande> saveCommande(@RequestBody final Commande commande) {
        commandeSaver.save(commande);
        return ResponseEntity.ok(commande);
    }
}
