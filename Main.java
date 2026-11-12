/**
 * Uygulamanın başlangıç (giriş) sınıfı.
 *
 * SOLID - Tek Sorumluluk İlkesi (Single Responsibility Principle):
 * Bu sınıf yalnızca gerekli nesneleri (Student, HtmlGenerator, WebServer) oluşturup
 * birbirine bağlamaktan ve sunucuyu başlatmaktan sorumludur.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Öğrenci kimlik ve biyografi verilerini içeren model nesnesini oluştur
        Student student = new Student(
                "1250505063",
                "Hüseyin Emre Alemdar",
                "Merhaba, ben Hüseyin Emre Alemdar. Kırklareli Üniversitesi'nde yazılım mühendisliği 2. sınıf öğrencisiyim.",
                "Yazılım dünyasına olan ilgim doğrultusunda eğitim hayatım boyunca kendimi özellikle Web Geliştirme ve Nesne Yönelimli Programlama alanlarında geliştiriyorum. Bugüne kadar Akıllı Lojistik Sistemi gibi çeşitli projeler geliştirerek teorik bilgilerimi pratiğe dökme fırsatı buldum.",
                "Gelecekte teknoloji sektöründe yenilikçi çözümler üreten başarılı bir yazılım mühendisi olmayı hedefliyorum. Kodlama dışında kalan zamanlarımı ise spor yaparak ve arkadaşlarımla buluşarak geçiririm."
        );

        // Geliştirici kontrolü için nesnenin override edilmiş toString() çıktısını konsola yazdır
        System.out.println("Yüklenen profil: " + student);

        // 2. HTML üretici nesnesini oluştur
        HtmlGenerator htmlGenerator = new HtmlGenerator();

        // 3. Web sunucusunu 1989 portu (WebServer.DEFAULT_PORT) ile oluştur ve başlat
        WebServer webServer = new WebServer(WebServer.DEFAULT_PORT, student, htmlGenerator);
        webServer.start();
    }
}
