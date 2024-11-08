package fr.exemple.gcp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/commande")
@RequiredArgsConstructor
class CommandeController {

    private final CommandeSaver commandeSaver;

    @PostMapping
    ResponseEntity<Commande> saveCommande(@RequestBody final Commande commande) {
        commandeSaver.save(commande);
        return ResponseEntity.ok(commande);
    }
}
