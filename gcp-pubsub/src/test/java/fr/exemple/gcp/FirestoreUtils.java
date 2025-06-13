package fr.exemple.gcp;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import jakarta.annotation.Nonnull;

public final class FirestoreUtils {

    private FirestoreUtils() {
    }

    /**
     * Récupère tous les documents de la collection Firestore
     *
     * @param firestore      {@link Firestore}
     * @param collectionName {@link Supplier} du nom de la collection
     * @return {@link List} de {@link  DocumentSnapshot}
     */
    public static List<DocumentSnapshot> getAllDocumentsInCollection(@Nonnull Firestore firestore, @Nonnull Supplier<String> collectionName) {
        final var documents = firestore.collection(collectionName.get()).listDocuments();
        return StreamSupport.stream(documents.spliterator(), false)
                .map(DocumentReference::get)
                .map(FirestoreUtils::awaitDocumentSnapshot)
                .toList();
    }

    /**
     * Récupère le nombre de documents dans la collection Firestore
     *
     * @param firestore      {@link Firestore}
     * @param collectionName {@link Supplier} du nom de la collection
     * @return Le nombre de documents
     */
    public static int numberOfDocumentsInCollection(@Nonnull Firestore firestore, @Nonnull Supplier<String> collectionName) {
        return getAllDocumentsInCollection(firestore, collectionName).size();
    }

    public static DocumentSnapshot awaitDocumentSnapshot(@Nonnull ApiFuture<DocumentSnapshot> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
