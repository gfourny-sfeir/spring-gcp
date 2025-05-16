package fr.exemple.gcp;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = CommandeController.class)
class CommandeControllerTest {

    @Captor
    ArgumentCaptor<Commande> commandeCaptor;
    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private CommandeSaver commandeSaver;

    @Test
    @DisplayName("Doit sauvegarder une commande et retourner la commande sauvegardée")
    void testSaveCommande() {
        // Given
        var commande = """
                {
                    "id": "123",
                    "nom": "Test Commande",
                    "prix": 99.99
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/commande")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commande))
                .assertThat()
                .hasStatus2xxSuccessful()
                .bodyJson()
                .isEqualTo(commande);

        then(commandeSaver).should().save(commandeCaptor.capture());

        assertThat(commandeCaptor.getValue()).isNotNull()
                .extracting(Commande::id, Commande::nom, Commande::prix)
                .containsExactly(
                        "123",
                        "Test Commande",
                        BigDecimal.valueOf(99.99)
                );

    }
}
