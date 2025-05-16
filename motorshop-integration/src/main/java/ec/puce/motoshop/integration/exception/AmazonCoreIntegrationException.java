package ec.puce.motoshop.integration.exception;

/**
 * Excepción personalizada para manejar errores de integración con Amazon Core.
 */
public class AmazonCoreIntegrationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor con mensaje de error.
     *
     * @param message Mensaje de error
     */
    public AmazonCoreIntegrationException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     *
     * @param message Mensaje de error
     * @param cause   Causa original de la excepción
     */
    public AmazonCoreIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
