package fr.exemple.gcp.config;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Intercepteur pour journaliser les requêtes HTTP entrantes et sortantes.
 * Cette classe enregistre les détails des requêtes avant leur traitement et les réponses après leur traitement.
 */
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    /**
     * Logger pour les messages de journalisation.
     */
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    /**
     * Méthode exécutée avant le traitement de la requête par le contrôleur.
     * Journalise les informations de la requête entrante, y compris l'URI, la méthode HTTP et les en-têtes.
     *
     * @param request  La requête HTTP entrante
     * @param response La réponse HTTP qui sera envoyée
     * @param handler  Le gestionnaire qui traitera la requête
     * @return true pour continuer le traitement de la requête, false pour l'arrêter
     */
    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {

            logger.info("Request to CommandeController - Method: {} - URI: {} - HTTP Method: {}",
                    handlerMethod.getMethod().getName(),
                    request.getRequestURI(),
                    request.getMethod());

            // Log headers
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                logger.debug("Header: {} = {}", headerName, request.getHeader(headerName));
            }
        }
        return true;
    }

    /**
     * Méthode exécutée après le traitement de la requête par le contrôleur.
     * Journalise les informations de la réponse sortante, y compris le code de statut HTTP.
     *
     * @param request      La requête HTTP qui a été traitée
     * @param response     La réponse HTTP qui sera envoyée
     * @param handler      Le gestionnaire qui a traité la requête
     * @param modelAndView Le modèle et la vue résultants (peut être null)
     */
    @Override
    public void postHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, ModelAndView modelAndView) {
        if (handler instanceof HandlerMethod handlerMethod) {

            logger.info("Response from CommandeController - Method: {} - URI: {} - Status: {}",
                    handlerMethod.getMethod().getName(),
                    request.getRequestURI(),
                    response.getStatus());

        }
    }
}
