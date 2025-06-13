package fr.exemple.gcp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration Spring MVC pour l'application.
 * Cette classe configure les intercepteurs et autres composants MVC.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Intercepteur pour la journalisation des requêtes.
     */
    private final RequestLoggingInterceptor requestLoggingInterceptor;

    /**
     * Constructeur pour l'injection des dépendances.
     *
     * @param requestLoggingInterceptor L'intercepteur de journalisation à configurer
     */
    public WebMvcConfig(RequestLoggingInterceptor requestLoggingInterceptor) {
        this.requestLoggingInterceptor = requestLoggingInterceptor;
    }

    /**
     * Configure les intercepteurs pour l'application.
     * Enregistre l'intercepteur de journalisation pour les chemins correspondant au CommandeController.
     *
     * @param registry Le registre des intercepteurs à configurer
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the interceptor for paths that match the CommandeController
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/api/commande/**");
    }
}
