package pjcbm9.quicket;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
public class MailSender {
    private class MailAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(usermail, userpass);
        }
    }
    private String usermail = null;
    private String userpass = null;
    private Properties emailProperties = null;
    private MimeMultipart mailBody = null;
    private String mailText = null;
    private String mailSubject = null;
    private String[] toAdresses;
    public MailSender() {
        mailText = "";
        mailBody = new MimeMultipart();
    }
    public MailSender attachFile(String filename, String path) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(path);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        mailBody.addBodyPart(messageBodyPart);
        return this;
    }
    public MailSender useMailPropertiesGMail(){
// ####################################################################################################
// Data from https://support.google.com/mail/troubleshooter/1668960?rd=1&hl=en
// ####################################################################################################
// Standard configuration instructions:
// Outgoing Mail (SMTP) Server - requires TLS or SSL: smtp.gmail.com
// Use Authentication: Yes
// Port for TLS/STARTTLS: 587
// Port for SSL: 465
// Server timeouts Greater than 1 minute, we recommend 5
// Account Name or User Name: your full email address (including @gmail.com or @your_domain.com)
// Email Address: your email address (username@gmail.com or username@your_domain.com)
// Password: your Gmail password
// ####################################################################################################
// ####################################################################################################
// SUMMING UP
// ####################################################################################################
// SMTP server: smtp.gmail.com
// Port: 465 -> if trouble (587)
// Security type: SSL (always)
// ####################################################################################################
        emailProperties = new Properties();
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.host", "smtp.gmail.com");
        emailProperties.put("mail.smtp.port", "465");
        emailProperties.put("mail.smtp.socketFactory.port", "465");
        emailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        emailProperties.put("mail.smtp.socketFactory.fallback", "false");
        return this;
    }
    public void useMailPropertiesSNMP(String serverHost, int serverPort, int socketFactoryPort, boolean autenticationEnabled){
        emailProperties = new Properties();
        emailProperties.put("mail.smtp.auth", Boolean.toString(autenticationEnabled));
        emailProperties.put("mail.smtp.host", serverHost);
        emailProperties.put("mail.smtp.port", Integer.toString(serverPort));
        emailProperties.put("mail.smtp.socketFactory.port", Integer.toString(socketFactoryPort));
        emailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        emailProperties.put("mail.smtp.socketFactory.fallback", "false");
    }
    public void send() throws Exception {
        MimeMessage emailMsg = new MimeMessage(Session.getInstance(emailProperties, new MailAuthenticator()));
        emailMsg.setFrom(new InternetAddress(usermail));
        emailProperties.put("mail.debug", "true");
        int toAddressesLenght = toAdresses.length;
        InternetAddress[] addressTo = new InternetAddress[toAddressesLenght];
        for (int i = 0; i < toAddressesLenght; ++i)
            addressTo[i] = new InternetAddress(toAdresses[i]);
        emailMsg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
        emailMsg.setSentDate(new Date());
        emailMsg.setSubject(mailSubject);
        BodyPart mailBodyPart = new MimeBodyPart();
        mailBodyPart.setText(mailText);
        mailBody.addBodyPart(mailBodyPart);
        emailMsg.setContent(mailBody);
        Transport.send(emailMsg);
    }
    public MailSender setCredentials(String usr, String pwd) {
        this.usermail = usr;
        this.userpass = pwd;
        return this;
    }
    public MailSender setToAddresses(String[] addresses) {
        this.toAdresses = addresses;
        return this;
    }
    public MailSender setSubject(String subject){
        this.mailSubject = subject;
        return this;
    }
    public MailSender setMailText(String text){
        this.mailText = text;
        return this;
    }
}