package java9;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.java9.classes.ExampleSingleton;
import com.java9.interfaces.LambdaPrint;

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
			// callHttpUrl(args[0]);

			collectionImmutable();
			singletonExample();
			lambdaTest();

			playWithTime();
		} catch (IOException e) {
			System.err.println("Errore nella scrittura" + e.getMessage());
		}

	}

	
	
	private static void lambdaTest() {

		// posso anche non specificare il tipo del parametro es: String mex
		LambdaPrint ciao = (mex) -> {
			if (mex != null && mex.length() > 10) {
				System.out.println(mex);
			}
			;
		};
		ciao.scriviQualcosa("oggi scrivo questo con interface");
		ciao.scriviQualcosa("impox");
		ciao.scriviQualcosa(null);
		
		Function<String,String> replaceCWithZ = (testo) -> {
			return testo.toLowerCase().replace("c", "z");
		};
		Function<String,String> replaceZWithR = (testo) -> {
			return testo.toLowerCase().replace("z", "r");
		};
		System.out.println("Function interface C WITH Z " + replaceCWithZ.apply("amici"));
		
		//TODO verificare comportamento con andThen e Compose
		System.out.println("risultato M with Q  " + replaceCWithZ.andThen(replaceZWithR).apply("Milano Area C"));
		replaceCWithZ.compose(replaceZWithR).apply("ciao");
		
	}

	private static void singletonExample() {
		ExampleSingleton.INSTANCE.setStudenti(Arrays.asList("mario", "giacomo", "marco", "stefano"));
		ExampleSingleton.INSTANCE.getStudenti().forEach(s -> {
			if (s.startsWith("m")) {
				System.out.println(s);
			}
		});

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
			// uso il costrutto Collection.of
			List<String> notChangeable = List.of("ciao", "io", "sono");
			notChangeable.add("roberto");
		} catch (UnsupportedOperationException e) {
			System.err.println("Impossibile aggiungere elemento alla lista");
		}
	}

	// TODO non funziona...classi non rilevate
	private static void callHttpUrl(String url) {

		try {

			// Create a HttpClient
			HttpClient httpClient = HttpClient.newHttpClient();
			System.out.println(httpClient.version());

			// Create a GET request
			HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
			Map<String, List<String>> headers = httpRequest.headers().map();

			headers.forEach((k, v) -> System.out.println("k  " + k + "  v " + v));
			HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandler.asString());
			System.out.println("Status code " + httpResponse.statusCode());
		} catch (Exception e) {
			System.out.println("message " + e);
		}

	}

	private static void testStream() {

		String[] languages = { "C#",  "Go", "Java", "VB","XML","C#" };
		
		System.out.println("Elementi originali");
		Stream.of(languages).forEach( s -> {System.out.print(s + " - ");});
		
		System.out.println(System.lineSeparator() + "Elementi originali DISTINTI");
		Stream.of(languages).distinct().forEach( s -> {System.out.print(s + " - ");});
		
		
		// test predicati
		Predicate<String> langGo = s -> s.contains("Go");
		Predicate<String> langJava = s -> s.contains("Java");
		System.out.println(System.lineSeparator() + "Elementi che rispettano il filtro GO or JAVA sono "
				+ Stream.of(languages).filter(langJava.or(langGo)).count()  + "  ");
		Stream.of(languages).filter(langJava.or(langGo)).forEach(System.out::println);
		System.out.println(System.lineSeparator());
		
		System.out.println("Elementi originali ORDINATI");
		Stream.of(languages).sorted().forEach(System.out::println);
		System.out.print("Il primo elemento corrisponde a " );
		
		//ifPresent perchè è di tipo Optional
		Stream.of(languages).findFirst().ifPresent(s -> System.out.println(s));
		
		Optional<String> minimal = Stream.of(languages).min(new Comparatore());
		minimal.ifPresent(s -> System.out.println(" Il valore minore è " + s));
		
		String[] emptyArray = {};
		System.out.println("Array vuoto, allora non stampo nulla " );
		Stream.of(emptyArray).findFirst().ifPresent(s -> System.out.println("primo elemento presente. ERRORE"));
		Optional<String> minimalError = Stream.of(emptyArray).min(new Comparatore());
		System.out.println("Il valore minore dell'array vuoto è " + minimalError.orElse("EMPTY_ARRAY"));
		
		// Stream.of(languages).dropWhile(langJava).forEach(System.out::println);


		//FIXME
		// ELIMINA solo gli elementi SUCCESSIVI a quello che contiene Java
		// System.out.println("Elementi successivi");
		// Stream.of(lang).dropWhile(s ->
		// s.contains("Go")).forEach(System.out::println);

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

	
	private static class Comparatore implements Comparator<String>{

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
		
	}
}
