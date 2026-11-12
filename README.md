# Java Soket Tabanlı Basit Web Sunucusu

Bu proje, Kırklareli Üniversitesi Yazılım Mühendisliği Bölümü **Nesneye Yönelimli Programlama** dersi kapsamında geliştirilmiştir. Uygulamanın amacı; Spring Boot veya Tomcat gibi harici bir kütüphane ya da çatı (framework) kullanmadan, yalnızca Java'nın standart `java.net` ve `java.io` paketleriyle çalışan temel bir HTTP web sunucusu oluşturmaktır.

Sunucu `1989` portunu dinler ve tarayıcıdan gelen HTTP isteklerine yanıt olarak öğrenci tanıtım ve biyografi bilgilerini içeren, CSS ile biçimlendirilmiş bir HTML sayfası döndürür.

## Mimari ve Tasarım İlkeleri

Kod yapısı oluşturulurken ders kapsamında işlenen nesneye yönelik programlama kuralları ve **SOLID (özellikle Tek Sorumluluk İlkesi - Single Responsibility Principle)** temel alınmıştır. Ağ iletişimi, veri modeli ve görünüm (HTML) üretimi birbirinden tamamen ayrılmıştır:

* **`Student.java`**: Öğrencinin numara, ad-soyad ve biyografi bilgilerini tutan veri modeli sınıfıdır. Alanlar kapsüllenmiş (`private final`), nesne oluşturulurken geçerlilik kontrolleri yapıcı metot içinde sağlanmış ve `Object` sınıfından gelen `toString`, `equals`, `hashCode` metotları sınıfın kimlik yapısına uygun olarak yeniden tanımlanmıştır (`@Override`).
* **`HtmlGenerator.java`**: Yalnızca `Student` nesnesindeki verileri alarak tarayıcıda görüntülenecek HTML ve CSS çıktısını üretmekten sorumludur. Soket veya ağ işlemleriyle ilgilenmez.
* **`WebServer.java`**: `ServerSocket` kullanarak `1989` portundan gelen bağlantıları kabul eder, tarayıcının gönderdiği HTTP istek başlıklarını okur ve `HtmlGenerator` tarafından hazırlanan içeriği `HTTP/1.1 200 OK` yanıtı olarak istemciye iletir.
* **`Main.java`**: Uygulamanın giriş noktasıdır. Gerekli nesneleri oluşturur, bağımlılıkları birbirine bağlar ve sunucuyu başlatır.

## Gereksinimler

* Java Development Kit (JDK) 8 veya üzeri (JDK 17+ önerilir)
* Herhangi bir modern web tarayıcısı

## Derleme ve Çalıştırma

Projeyi bilgisayarınızda çalıştırmak için aşağıdaki adımları takip edebilirsiniz.

**1. Kaynak kodları derleyin:**

Proje dizininde terminali (veya Komut İstemi / PowerShell) açarak Türkçe karakter sorunlarını önlemek için `-encoding UTF-8` parametresiyle tüm `.java` dosyalarını derleyin:

```bash
javac -encoding UTF-8 *.java
```

**2. Sunucuyu başlatın:**

Derleme işlemi tamamlandıktan sonra `Main` sınıfını çalıştırın:

```bash
java Main
```

Terminalde aşağıdaki çıktıyı gördüğünüzde sunucu dinlemeye başlamış demektir:

```text
Yüklenen profil: Student{studentNumber=1250505063, fullName=Hüseyin Emre Alemdar, paragraphCount=3}
Web sunucusu başlatıldı: http://localhost:1989
Tarayıcınızdan http://localhost:1989 adresine gidebilirsiniz.
Durdurmak için programı kapatın (Ctrl+C).
```

**3. Tarayıcıdan görüntüleyin:**

Web tarayıcınızı açın ve adres çubuğuna şu adresi yazın:

```text
http://localhost:1989
```

Sunucuyu durdurmak için terminal ekranında `Ctrl + C` tuş kombinasyonunu kullanabilirsiniz.

## Geliştirici

* **Ad Soyad:** Hüseyin Emre Alemdar
* **Öğrenci Numarası:** 1250505063
* **Bölüm:** Kırklareli Üniversitesi - Yazılım Mühendisliği (2. Sınıf)
