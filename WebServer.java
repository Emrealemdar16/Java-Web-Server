import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Belirtilen port üzerinden gelen HTTP bağlantılarını dinleyen ve tarayıcıya yanıt dönen sınıf.
 *
 * SOLID - Tek Sorumluluk İlkesi (Single Responsibility Principle):
 * Bu sınıf yalnızca ağ soketini dinlemek ve HTTP protokolü üzerinden yanıt iletmekten sorumludur.
 * Öğrenci verilerini tutmaz ve HTML/CSS tasarımının nasıl üretildiğini bilmez.
 */
public class WebServer {

    // Sihirli sayı (magic number) kullanmamak için sabit tanımı (Bölüm 5.3)
    public static final int DEFAULT_PORT = 1989;
    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65535;
    private static final String UTF_8 = "UTF-8";

    private final int port;
    private final Student student;
    private final HtmlGenerator htmlGenerator;

    /**
     * Varsayılan port (1989) ile sunucu oluşturan kısa yapıcı.
     * Yapıcı zincirleme (this(...)) kullanarak işi ana yapıcıya devreder.
     *
     * @param student       Sayfada gösterilecek öğrenci bilgisi
     * @param htmlGenerator HTML metnini üretecek nesne
     */
    public WebServer(Student student, HtmlGenerator htmlGenerator) {
        this(DEFAULT_PORT, student, htmlGenerator);
    }

    /**
     * Ana (tam) yapıcı: tüm doğrulamalar burada, tek yerde yapılır.
     *
     * @param port          Sunucunun dinleyeceği port numarası
     * @param student       Sayfada gösterilecek öğrenci bilgisi
     * @param htmlGenerator HTML metnini üretecek nesne
     */
    public WebServer(int port, Student student, HtmlGenerator htmlGenerator) {
        if (port < MIN_PORT || port > MAX_PORT) {
            throw new IllegalArgumentException("Geçersiz port numarası: " + port);
        }
        if (student == null) {
            throw new IllegalArgumentException("Öğrenci nesnesi null olamaz");
        }
        if (htmlGenerator == null) {
            throw new IllegalArgumentException("HtmlGenerator nesnesi null olamaz");
        }
        this.port = port;
        this.student = student;
        this.htmlGenerator = htmlGenerator;
    }

    public int getPort() {
        return port;
    }

    /**
     * Sunucuyu başlatır ve gelen tarayıcı isteklerini sırasıyla yanıtlar.
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Web sunucusu başlatıldı: http://localhost:" + port);
            System.out.println("Tarayıcınızdan http://localhost:" + port + " adresine gidebilirsiniz.");
            System.out.println("Durdurmak için programı kapatın (Ctrl+C).");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClientConnection(clientSocket);
                } catch (IOException e) {
                    System.err.println("İstemci bağlantısı işlenirken hata oluştu: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Sunucu " + port + " portunda başlatılamadı: " + e.getMessage());
        }
    }

    /**
     * Tek bir istemci (tarayıcı) bağlantısını okur ve üretilen HTML çıktısını HTTP yanıtı olarak gönderir.
     *
     * @param clientSocket Bağlanan istemcinin soketi
     * @throws IOException Giriş/çıkış hatası durumunda fırlatılır
     */
    private void handleClientConnection(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), UTF_8)
        );

        // Tarayıcının gönderdiği HTTP istek başlıklarını boş satıra kadar oku
        String requestLine = reader.readLine();
        if (requestLine == null) {
            return;
        }
        System.out.println("Gelen istek: " + requestLine);

        String headerLine;
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            // İstek başlıkları (headers) tüketilir; böylece bağlantı sıfırlama (RST) hatası önlenir
        }

        // HTML üretim sorumluluğu HtmlGenerator sınıfına aittir
        String htmlContent = htmlGenerator.generateHtml(student);
        byte[] responseBody = htmlContent.getBytes(UTF_8);

        // HTTP/1.1 200 OK yanıt başlıklarını hazırla
        String httpHeaders = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html; charset=UTF-8\r\n"
                + "Content-Length: " + responseBody.length + "\r\n"
                + "Connection: close\r\n"
                + "\r\n";

        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(httpHeaders.getBytes(UTF_8));
        outputStream.write(responseBody);
        outputStream.flush();
    }

    @Override
    public String toString() {
        return "WebServer{port=" + port + ", student=" + student + "}";
    }
}
