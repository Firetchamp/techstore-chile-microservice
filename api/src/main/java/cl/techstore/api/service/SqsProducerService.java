package cl.techstore.api.service; // Ajusta el subpaquete si lo dejas dentro de una carpeta "service"

import cl.techstore.api.dto.AuditMessage;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsProducerService {

    private final SqsTemplate sqsTemplate;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    // Inyección por constructor de SqsTemplate (provisto por el starter de AWS SQS)
    public SqsProducerService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    /**
     * Envía un mensaje de auditoría estructurado de forma asíncrona a Amazon SQS
     */
    public void sendAuditMessage(AuditMessage message) {
        try {
            sqsTemplate.send(queueUrl, message);
            System.out.println("--> [SQS Success] Mensaje enviado a la cola para el producto: " + message.getName());
        } catch (Exception e) {
            System.err.println("--> [SQS Error] Falló el envío a la cola: " + e.getMessage());
        }
    }
}