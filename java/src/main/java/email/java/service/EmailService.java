package email.java.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import email.java.model.EmailConfig;
import email.java.model.EmailTemplate;
import email.java.model.EmailHistory;
import email.java.repository.EmailConfigRepository;
import email.java.repository.EmailTemplateRepository;
import email.java.repository.EmailHistoryRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    @Autowired
    private EmailConfigRepository emailConfigRepository;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    public List<EmailConfig> getAllEmailConfigs() {
        logger.info("Recuperando todas as configurações de email.");
        return emailConfigRepository.findAll();
    }

    public List<EmailTemplate> getAllEmailTemplates() {
        logger.info("Recuperando todos os templates de email.");
        return emailTemplateRepository.findAll();
    }

    public void saveEmailTemplate(EmailTemplate emailTemplate) {
        logger.info("Salvando template de email: {}", emailTemplate.getTitle());
        emailTemplateRepository.save(emailTemplate);
    }

    public void saveEmailConfig(EmailConfig emailConfig) {
        logger.info("Salvando configuração de email para servidor: {}", emailConfig.getSmtpServer());
        emailConfigRepository.save(emailConfig);
    }

    public void sendEmail(Long emailConfigId, Long emailTemplateId, String recipientOverride) {
        logger.info("Iniciando envio de e-mail único com configId: {}, templateId: {}", emailConfigId, emailTemplateId);

        EmailConfig config = emailConfigRepository.findById(emailConfigId)
                .orElseThrow(() -> new RuntimeException("Configuração de e-mail não encontrada"));
        EmailTemplate template = emailTemplateRepository.findById(emailTemplateId)
                .orElseThrow(() -> new RuntimeException("Template de e-mail não encontrado"));

        JavaMailSenderImpl mailSender = createMailSender(config);

        String recipient = recipientOverride != null && !recipientOverride.isEmpty() ? recipientOverride : config.getRecipientEmail();
        logger.info("Destinatário do e-mail: {}", recipient);

        EmailHistory emailHistory = new EmailHistory();
        emailHistory.setEmail(recipient);
        emailHistory.setDatadoenvio(LocalDateTime.now());

        try {
            sendIndividualEmail(mailSender, config, template, recipient);
            emailHistory.setStatusenvio("Enviado");
            emailHistory.setErrorMessage(null);
        } catch (Exception e) {
            emailHistory.setStatusenvio("Erro");
            emailHistory.setErrorMessage(e.getMessage());
            logger.error("Erro ao enviar e-mail para {}: {}", recipient, e.getMessage());
        }

        emailHistoryRepository.save(emailHistory);
    }

    @Async
    public void sendBulkEmails(Long emailConfigId, Long emailTemplateId, MultipartFile file, int sendRate) {
        Path tempFilePath = null;
        try {
            tempFilePath = Files.createTempFile("upload_", ".csv");
            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            try (BufferedReader reader = Files.newBufferedReader(tempFilePath, StandardCharsets.UTF_8);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withHeader())) {

                for (CSVRecord record : csvParser) {
                    String carteira = record.get("carteira");
                    String email = record.get("email");

                    if (carteira.isEmpty() || email.isEmpty()) {
                        logger.warn("Registro ignorado: carteira ou email vazio.");
                        continue;
                    }

                    logger.info("Enviando e-mail para: {} com carteira: {}", email, carteira);
                    sendEmail(emailConfigId, emailTemplateId, email);

                    if (sendRate > 0) {
                        try {
                            Thread.sleep(1000 / sendRate);
                        } catch (InterruptedException e) {
                            logger.error("Erro ao pausar o envio: {}", e.getMessage());
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao processar o arquivo CSV temporário: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar arquivo CSV", e);
        } finally {
            if (tempFilePath != null) {
                try {
                    Files.delete(tempFilePath);
                } catch (Exception e) {
                    logger.warn("Não foi possível excluir o arquivo temporário: {}", e.getMessage());
                }
            }
        }
    }

    private JavaMailSenderImpl createMailSender(EmailConfig config) {
        logger.info("Configurando JavaMailSender para servidor: {}, porta: {}", config.getSmtpServer(), config.getPort());

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(config.getSmtpServer());
        mailSender.setPort(config.getPort());
        mailSender.setUsername(config.getUsername());
        mailSender.setPassword(config.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Habilitar STARTTLS
        props.put("mail.smtp.ssl.enable", "true"); // Habilitar SSL se estiver usando a porta 465
        props.put("mail.debug", "true");

        logger.info("JavaMailSender configurado com sucesso.");

        return mailSender;
    }


    private void sendIndividualEmail(JavaMailSenderImpl mailSender, EmailConfig config, EmailTemplate template, String recipient) throws MessagingException {
        logger.info("Enviando e-mail para: {}", recipient);
        logger.info("Assunto: {}", template.getTitle());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(config.getRecipientEmail()); // Remetente agora é o recipient_email do EmailConfig
            helper.setTo(recipient);
            helper.setSubject(template.getTitle());

            Document document = Jsoup.parse(template.getContent());
            Elements imgElements = document.select("img");

            MimeMultipart multipart = new MimeMultipart("related");

            for (Element img : imgElements) {
                String imgSrc = img.attr("src");

                if (!imgSrc.startsWith("cid:")) {
                    String contentId = "image" + System.currentTimeMillis();
                    img.attr("src", "cid:" + contentId);

                    MimeBodyPart imagePart = new MimeBodyPart();
                    DataSource fds = new FileDataSource(imgSrc);
                    imagePart.setDataHandler(new DataHandler(fds));
                    imagePart.setHeader("Content-ID", "<" + contentId + ">");

                    multipart.addBodyPart(imagePart);
                }
            }

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(document.html(), "text/html");
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);

            mailSender.send(message);
            logger.info("E-mail enviado com sucesso para: {}", recipient);

        } catch (Exception e) {
            logger.error("Erro ao enviar e-mail para {}: {}", recipient, e.getMessage());
            throw e;
        }
    }
}
