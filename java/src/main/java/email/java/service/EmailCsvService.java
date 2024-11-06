package email.java.service;



import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class EmailCsvService {

    private static final Logger logger = LoggerFactory.getLogger(EmailCsvService.class);

    /**
     * Lê um arquivo CSV e conta o número total de emails.
     *
     * @param file O arquivo CSV que contém os emails.
     * @return O total de emails encontrados no arquivo.
     */
    public int countEmailsInCsv(MultipartFile file) {
        int totalEmails = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withHeader())) {

            // Itera sobre cada registro do CSV e conta os emails
            for (CSVRecord record : csvParser) {
                String email = record.get("email"); // Assumindo que a coluna com os emails se chama "email"
                if (!email.isEmpty()) {
                    totalEmails++;
                } else {
                    logger.warn("Registro ignorado: email vazio.");
                }
            }

        } catch (Exception e) {
            logger.error("Erro ao processar o arquivo CSV: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar arquivo CSV", e);
        }

        logger.info("Total de emails encontrados no CSV: {}", totalEmails);
        return totalEmails;
    }
}
