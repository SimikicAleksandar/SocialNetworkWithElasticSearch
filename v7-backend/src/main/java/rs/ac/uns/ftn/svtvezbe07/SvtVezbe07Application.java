package rs.ac.uns.ftn.svtvezbe07;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class SvtVezbe07Application {

	public static void main(String[] args) {

		SpringApplication.run(SvtVezbe07Application.class, args);

		// Add shutdown hook to delete all files in the temp directory
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				File tmpDir = new File("C:\\Users\\Simikic\\AppData\\Local\\Temp");

				// Check if the directory exists and is a directory
				if (tmpDir.exists() && tmpDir.isDirectory()) {
					// List all files in the directory
					File[] tempFiles = tmpDir.listFiles();

					if (tempFiles != null) {
						// Loop through each file and delete
						for (File file : tempFiles) {
							if (file.isFile()) {
								boolean deleted = file.delete();
								if (deleted) {
									System.out.println("Deleted file: " + file.getName());
								} else {
									System.out.println("Failed to delete file: " + file.getName());
								}
							}
						}
					} else {
						System.out.println("No files found in the directory.");
					}
				} else {
					System.out.println("Temp directory not found or not a directory.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
	}
}
