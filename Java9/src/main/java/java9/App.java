package java9;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;


/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		try {
			System.out.println("Hello World!");
			
			collectionTest();
			writeFile(new BufferedWriter(new FileWriter("Easy")), "Sample");
			detailAllProcess();
			testStream();
			//callHttpUrl(args[0]);
			
			collectionImmutable();
			
			playWithTime();
		} catch (IOException e) {
			System.err.println("Errore nella scrittura" + e.getMessage());
		}

	}

	
	private static void collectionTest() {
		
		
	}


	private static void playWithTime() {
		ZonedDateTime ora = java.time.Instant.now().atZone(ZoneId.of("Europe/Rome"));
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		System.out.println("Oggi è il giorno " + ora.format(dateFormatter));
		System.out.println("sono le ore " + ora.format(hourFormatter));
		System.out.println("ZoneId default" + ZoneId.systemDefault().toString());
		
		System.out.println("30 giorni fa era " + ora.minusDays(30).format(dateFormatter));
	}

	private static void collectionImmutable() {
		
		try {
			
			//uso il costrutto Collection.of
			List<String> notChangeable = List.of("ciao","io","sono");
			notChangeable.add("roberto");
		} catch (UnsupportedOperationException e) {
			System.err.println("Impossibile aggiungere elemento alla lista");
		}
	}

	//TODO non funziona...classi non rilevate
	private static void callHttpUrl(String url) {

		try {

			// Create a HttpClient
			HttpClient httpClient = HttpClient.newHttpClient();
			System.out.println(httpClient.version());

			// Create a GET request
			HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
			Map<String, List<String>> headers = httpRequest.headers().map();

			headers.forEach((k,v) -> System.out.println("k  " + k + "  v " + v));
			HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandler.asString());
			System.out.println("Status code " + httpResponse.statusCode());
		} catch (Exception e) {
			System.out.println("message " + e);
		}

	}

	// TODO non funziona come dovrebbe
	private static void testStream() {

		List<String> lang = Arrays.asList("  C#  ", "  Go  ", "  VB  ", "  Java   ", "   XML  ");

		// mantieni solo gli elementi PRECEDENTI a quello che contiene Java
		System.out.println("Elementi precedenti");
		Stream.of(lang).takeWhile(s -> s.contains("Go")).forEach(System.out::println);

		// ELIMINA solo gli elementi SUCCESSIVI a quello che contiene Java
		System.out.println("Elementi successivi");
		Stream.of(lang).dropWhile(s -> s.contains("Go")).forEach(System.out::println);
		;

	}

	// TODO da verificare. in teoria dovrebbe funzionare con try (bufferedWriter)
	// senza assegnazione
	public static void writeFile(BufferedWriter bufferedWriter, String string) {
		try (BufferedWriter b = bufferedWriter) {
			b.write(string);
		} catch (IOException e) {
			System.err.println("Errore nella scrittura" + e.getMessage());
		}

	}

	public static void detailAllProcess() {
		ProcessHandle.allProcesses().forEach(p -> System.out.println(leggiQualcheDato(p)));
	}

	private static String leggiQualcheDato(ProcessHandle p) {
		long pid = p.pid();
		Optional<String> command = p.info().command();
		return (command.isPresent() ? new StringBuilder().append(command).append("-").append(pid).toString() : "");
	}

}
